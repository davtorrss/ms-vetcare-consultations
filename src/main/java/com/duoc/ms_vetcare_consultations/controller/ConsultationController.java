package com.duoc.ms_vetcare_consultations.controller;

import com.duoc.ms_vetcare_consultations.dto.ConsultationResponse;
import com.duoc.ms_vetcare_consultations.dto.CreateConsultationRequest;
import com.duoc.ms_vetcare_consultations.dto.UpdateStatusRequest;
import com.duoc.ms_vetcare_consultations.entity.ConsultationStatus;
import com.duoc.ms_vetcare_consultations.service.ConsultationService;

import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/consultations")
public class ConsultationController {

    private final ConsultationService service;

    public ConsultationController(
            ConsultationService service
    ) {
        this.service = service;
    }

    @GetMapping
    public List<ConsultationResponse> getConsultations(

            @RequestParam(required = false)
            ConsultationStatus status,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate to

    ) {

        return service.findAll(
                status,
                from,
                to
        );
    }

    @GetMapping("/{id}")
    public ConsultationResponse getConsultation(
            @PathVariable Long id
    ) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConsultationResponse createConsultation(
            @Valid
            @RequestBody
            CreateConsultationRequest request
    ) {
        return service.create(request);
    }

    @PutMapping("/{id}/status")
    public ConsultationResponse updateStatus(
            @PathVariable Long id,
            @Valid
            @RequestBody
            UpdateStatusRequest request
    ) {
        return service.updateStatus(id, request);
    }
}