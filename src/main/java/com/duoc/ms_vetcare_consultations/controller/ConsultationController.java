package com.duoc.ms_vetcare_consultations.controller;

import com.duoc.ms_vetcare_consultations.dto.ConsultationResponse;
import com.duoc.ms_vetcare_consultations.dto.CreateConsultationRequest;
import com.duoc.ms_vetcare_consultations.dto.UpdateStatusRequest;
import com.duoc.ms_vetcare_consultations.entity.ConsultationStatus;
import com.duoc.ms_vetcare_consultations.service.ConsultationService;

import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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
            LocalDate to,

            @AuthenticationPrincipal
            Jwt jwt

    ) {

        String userOid =
                jwt.getClaimAsString("oid");

        boolean isClient =
                hasRole(jwt, "Cliente");

        return service.findAll(
                status,
                from,
                to,
                userOid,
                isClient
        );
    }

    @GetMapping("/{id}")
    public ConsultationResponse getConsultation(

            @PathVariable Long id,

            @AuthenticationPrincipal
            Jwt jwt

    ) {

        String userOid =
                jwt.getClaimAsString("oid");

        boolean isClient =
                hasRole(jwt, "Cliente");

        return service.findById(
                id,
                userOid,
                isClient
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConsultationResponse createConsultation(

            @Valid
            @RequestBody
            CreateConsultationRequest request,

            @AuthenticationPrincipal
            Jwt jwt

    ) {

        String userOid =
                jwt.getClaimAsString("oid");

        boolean isClient =
                hasRole(jwt, "Cliente");

        return service.create(
                request,
                userOid,
                isClient
        );
    }

    @PutMapping("/{id}/status")
    public ConsultationResponse updateStatus(

            @PathVariable Long id,

            @Valid
            @RequestBody
            UpdateStatusRequest request

    ) {

        return service.updateStatus(
                id,
                request
        );
    }

    /*
     * Comprueba directamente los App Roles
     * recibidos desde Microsoft Entra.
     */
    private boolean hasRole(
            Jwt jwt,
            String role
    ) {

        List<String> roles =
                jwt.getClaimAsStringList("roles");

        return roles != null &&
                roles.contains(role);
    }
}