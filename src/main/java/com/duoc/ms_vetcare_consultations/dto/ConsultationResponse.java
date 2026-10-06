package com.duoc.ms_vetcare_consultations.dto;
import com.duoc.ms_vetcare_consultations.entity.Consultation;
import com.duoc.ms_vetcare_consultations.entity.ConsultationStatus;

import java.time.LocalDateTime;

public class ConsultationResponse {

    private Long id;
    private String petName;
    private Long serviceId;
    private LocalDateTime requestedAt;
    private String observations;
    private ConsultationStatus status;

    public ConsultationResponse() {
    }

    public ConsultationResponse(
            Long id,
            String petName,
            Long serviceId,
            LocalDateTime requestedAt,
            String observations,
            ConsultationStatus status
    ) {
        this.id = id;
        this.petName = petName;
        this.serviceId = serviceId;
        this.requestedAt = requestedAt;
        this.observations = observations;
        this.status = status;
    }

    public static ConsultationResponse fromEntity(
            Consultation consultation
    ) {
        return new ConsultationResponse(
                consultation.getId(),
                consultation.getPetName(),
                consultation.getServiceId(),
                consultation.getRequestedAt(),
                consultation.getObservations(),
                consultation.getStatus()
        );
    }

    public Long getId() {
        return id;
    }

    public String getPetName() {
        return petName;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public String getObservations() {
        return observations;
    }

    public ConsultationStatus getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public void setStatus(ConsultationStatus status) {
        this.status = status;
    }
}