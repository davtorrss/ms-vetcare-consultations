package com.duoc.ms_vetcare_consultations.service;

import com.duoc.ms_vetcare_consultations.dto.ConsultationResponse;
import com.duoc.ms_vetcare_consultations.dto.CreateConsultationRequest;
import com.duoc.ms_vetcare_consultations.dto.UpdateStatusRequest;
import com.duoc.ms_vetcare_consultations.entity.Consultation;
import com.duoc.ms_vetcare_consultations.entity.ConsultationStatus;
import com.duoc.ms_vetcare_consultations.exception.InvalidStatusTransitionException;
import com.duoc.ms_vetcare_consultations.exception.ResourceNotFoundException;
import com.duoc.ms_vetcare_consultations.messaging.ConsultationEventPublisher;
import com.duoc.ms_vetcare_consultations.repository.ConsultationRepository;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ConsultationService {

    private final ConsultationRepository repository;
    private final ConsultationEventPublisher eventPublisher;

    public ConsultationService(
            ConsultationRepository repository,
            ConsultationEventPublisher eventPublisher
    ) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    public List<ConsultationResponse> findAll(
            ConsultationStatus status,
            LocalDate from,
            LocalDate to,
            String userOid,
            boolean isClient
    ) {

        return repository.findAll()
                .stream()

                /*
                 * Si es Cliente, solamente puede visualizar
                 * consultas cuyo ownerOid coincida con su
                 * identidad real de Microsoft Entra.
                 */
                .filter(consultation ->
                        !isClient ||
                        (
                                consultation.getOwnerOid() != null &&
                                consultation.getOwnerOid().equals(userOid)
                        )
                )

                .filter(consultation ->
                        status == null ||
                        consultation.getStatus() == status
                )

                .filter(consultation ->
                        from == null ||
                        !consultation
                                .getRequestedAt()
                                .toLocalDate()
                                .isBefore(from)
                )

                .filter(consultation ->
                        to == null ||
                        !consultation
                                .getRequestedAt()
                                .toLocalDate()
                                .isAfter(to)
                )

                .map(ConsultationResponse::fromEntity)
                .toList();
    }

    public ConsultationResponse findById(
            Long id,
            String userOid,
            boolean isClient
    ) {

        Consultation consultation = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Consulta no encontrada con id: " + id
                        )
                );

        /*
         * Un Cliente no puede consultar una atención
         * perteneciente a otro usuario.
         */
        if (isClient) {

            if (consultation.getOwnerOid() == null ||
                    !consultation.getOwnerOid().equals(userOid)) {

                throw new AccessDeniedException(
                        "No tiene permiso para acceder a esta consulta"
                );
            }
        }

        return ConsultationResponse.fromEntity(consultation);
    }

    public ConsultationResponse create(
            CreateConsultationRequest request,
            String userOid,
            boolean isClient
    ) {

        /*
         * Si quien crea es Cliente:
         *
         * ownerOid = su propio OID.
         *
         * Nadie puede mandar ese valor desde el frontend.
         *
         * Si crea un Operador, por ahora ownerOid queda null,
         * porque el modelo actual no tiene una relación
         * confiable para determinar el Cliente propietario.
         */
        String ownerOid =
                isClient
                        ? userOid
                        : null;

        Consultation consultation =
                new Consultation(
                        request.getPetName(),
                        request.getServiceId(),
                        request.getRequestedAt(),
                        request.getObservations(),
                        ConsultationStatus.SOLICITADA,
                        ownerOid,
                        userOid
                );

        Consultation saved =
                repository.save(consultation);

        ConsultationResponse response =
                ConsultationResponse.fromEntity(saved);

        /*
         * Publica evento RabbitMQ después de persistir
         * correctamente la consulta.
         */
        eventPublisher.publishCreated(response);

        return response;
    }

    public ConsultationResponse updateStatus(
            Long id,
            UpdateStatusRequest request
    ) {

        Consultation consultation = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Consulta no encontrada con id: " + id
                        )
                );

        ConsultationStatus currentStatus =
                consultation.getStatus();

        ConsultationStatus newStatus =
                request.getStatus();

        if (!isValidTransition(currentStatus, newStatus)) {

            throw new InvalidStatusTransitionException(
                    "No se puede cambiar el estado de "
                            + currentStatus
                            + " a "
                            + newStatus
            );
        }

        consultation.setStatus(newStatus);

        Consultation updated =
                repository.save(consultation);

        ConsultationResponse response =
                ConsultationResponse.fromEntity(updated);

        /*
         * Publica evento RabbitMQ cuando el cambio de estado
         * fue válido y quedó guardado en PostgreSQL.
         */
        eventPublisher.publishStatusChanged(response);

        return response;
    }

    private boolean isValidTransition(
            ConsultationStatus current,
            ConsultationStatus next
    ) {

        return switch (current) {

            case SOLICITADA ->
                    next == ConsultationStatus.CONFIRMADA ||
                    next == ConsultationStatus.CANCELADA;

            case CONFIRMADA ->
                    next == ConsultationStatus.EN_ESPERA ||
                    next == ConsultationStatus.CANCELADA;

            case EN_ESPERA ->
                    next == ConsultationStatus.EN_CONSULTA ||
                    next == ConsultationStatus.CANCELADA;

            case EN_CONSULTA ->
                    next == ConsultationStatus.CERRADA;

            case CERRADA, CANCELADA -> false;
        };
    }
}