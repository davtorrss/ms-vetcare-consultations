package com.duoc.ms_vetcare_consultations.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "consultations")
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String petName;

    @Column(nullable = false)
    private Long serviceId;

    @Column(nullable = false)
    private LocalDateTime requestedAt;

    @Column(length = 1000)
    private String observations;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConsultationStatus status;

    /*
     * OID del usuario Cliente dueño de la consulta.
     *
     * Se obtiene desde Microsoft Entra.
     * Nunca se recibe libremente desde Angular/Postman.
     *
     * Puede ser null para consultas creadas por un Operador,
     * ya que el modelo actual todavía no tiene una relación
     * confiable Operador -> Cliente.
     */
    @Column(name = "owner_oid")
    private String ownerOid;

    /*
     * Identidad de quien creó la consulta.
     * También se obtiene desde el JWT.
     */
    @Column(name = "created_by_oid")
    private String createdByOid;

    public Consultation() {
    }

    public Consultation(
            String petName,
            Long serviceId,
            LocalDateTime requestedAt,
            String observations,
            ConsultationStatus status,
            String ownerOid,
            String createdByOid
    ) {
        this.petName = petName;
        this.serviceId = serviceId;
        this.requestedAt = requestedAt;
        this.observations = observations;
        this.status = status;
        this.ownerOid = ownerOid;
        this.createdByOid = createdByOid;
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

    public String getOwnerOid() {
        return ownerOid;
    }

    public String getCreatedByOid() {
        return createdByOid;
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

    public void setOwnerOid(String ownerOid) {
        this.ownerOid = ownerOid;
    }

    public void setCreatedByOid(String createdByOid) {
        this.createdByOid = createdByOid;
    }
}