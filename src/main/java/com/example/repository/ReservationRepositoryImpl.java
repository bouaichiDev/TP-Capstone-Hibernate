package com.example.repository;

import com.example.model.Reservation;
import javax.persistence.EntityManager;
import java.util.List;

public class ReservationRepositoryImpl implements ReservationRepository {
    private final EntityManager em;

    public ReservationRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    public Reservation findById(Long id) {
        return em.find(Reservation.class, id);
    }

    public List<Reservation> findAll() {
        return em.createQuery("SELECT r FROM Reservation r", Reservation.class).getResultList();
    }

    public void save(Reservation reservation) {
        em.persist(reservation);
    }

    public void update(Reservation reservation) {
        em.merge(reservation);
    }

    public void delete(Long id) {
        Reservation reservation = findById(id);
        if (reservation != null) {
            em.remove(reservation);
        }
    }
}
