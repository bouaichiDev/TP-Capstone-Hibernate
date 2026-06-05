package com.example.service;

import com.example.model.Reservation;
import com.example.repository.ReservationRepository;
import javax.persistence.EntityManager;
import java.util.List;

public class ReservationServiceImpl implements ReservationService {
    private final EntityManager em;
    private final ReservationRepository reservationRepository;

    public ReservationServiceImpl(EntityManager em, ReservationRepository reservationRepository) {
        this.em = em;
        this.reservationRepository = reservationRepository;
    }

    public Reservation findById(Long id) {
        return reservationRepository.findById(id);
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public void createReservation(Reservation reservation) {
        boolean newTx = !em.getTransaction().isActive();
        if (newTx) em.getTransaction().begin();
        try {
            reservationRepository.save(reservation);
            if (newTx) em.getTransaction().commit();
        } catch (Exception e) {
            if (newTx && em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }

    public void updateReservation(Reservation reservation) {
        boolean newTx = !em.getTransaction().isActive();
        if (newTx) em.getTransaction().begin();
        try {
            reservationRepository.update(reservation);
            if (newTx) em.getTransaction().commit();
        } catch (Exception e) {
            if (newTx && em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }

    public void cancelReservation(Long id) {
        boolean newTx = !em.getTransaction().isActive();
        if (newTx) em.getTransaction().begin();
        try {
            Reservation reservation = reservationRepository.findById(id);
            if (reservation != null) {
                reservation.setStatut(Reservation.StatutReservation.ANNULEE);
                reservationRepository.update(reservation);
            }
            if (newTx) em.getTransaction().commit();
        } catch (Exception e) {
            if (newTx && em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        }
    }
}
