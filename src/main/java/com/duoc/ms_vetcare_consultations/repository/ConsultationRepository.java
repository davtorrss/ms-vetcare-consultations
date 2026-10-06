package com.duoc.ms_vetcare_consultations.repository;

import com.duoc.ms_vetcare_consultations.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultationRepository
        extends JpaRepository<Consultation, Long> {
}