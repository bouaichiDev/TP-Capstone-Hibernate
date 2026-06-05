package com.example.service;

import com.example.model.Salle;
import com.example.repository.SalleRepository;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class SalleServiceImpl implements SalleService {
    private final EntityManager em;
    private final SalleRepository salleRepository;

    public SalleServiceImpl(EntityManager em, SalleRepository salleRepository) {
        this.em = em;
        this.salleRepository = salleRepository;
    }

    public List<Salle> findAvailableRooms(LocalDateTime start, LocalDateTime end) {
        return salleRepository.findAvailableRooms(start, end);
    }

    public List<Salle> searchRooms(Map<String, Object> criteria) {
        return salleRepository.searchRooms(criteria);
    }

    public List<Salle> getPaginatedRooms(int page, int pageSize) {
        return salleRepository.getPaginatedRooms(page, pageSize);
    }

    public long countRooms() {
        return salleRepository.countRooms();
    }

    public int getTotalPages(int pageSize) {
        return salleRepository.getTotalPages(pageSize);
    }
}
