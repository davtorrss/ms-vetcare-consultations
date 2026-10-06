package com.duoc.ms_vetcare_consultations.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class CreateConsultationRequest {

    @NotBlank(message = "El nombre de la mascota es obligatorio")
    private String petName;

    @NotNull(message = "El servicio es obligatorio")
    private Long serviceId;

    @NotNull(message = "La fecha de la consulta es obligatoria")
    private LocalDateTime requestedAt;

    private String observations;

    public CreateConsultationRequest() {
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
}