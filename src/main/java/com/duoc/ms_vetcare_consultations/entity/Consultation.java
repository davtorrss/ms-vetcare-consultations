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

    public Consultation() {
    }

    public Consultation(
            String petName,
            Long serviceId,
            LocalDateTime requestedAt,
            String observations,
            ConsultationStatus status
    ) {
        this.petName = petName;
        this.serviceId = serviceId;
        this.requestedAt = requestedAt;
        this.observations = observations;
        this.status = status;
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