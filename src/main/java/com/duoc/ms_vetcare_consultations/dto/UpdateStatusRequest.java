package com.duoc.ms_vetcare_consultations.dto;
import com.duoc.ms_vetcare_consultations.entity.ConsultationStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateStatusRequest {

    @NotNull(message = "El estado es obligatorio")
    private ConsultationStatus status;

    public UpdateStatusRequest() {
    }

    public ConsultationStatus getStatus() {
        return status;
    }

    public void setStatus(ConsultationStatus status) {
        this.status = status;
    }
}