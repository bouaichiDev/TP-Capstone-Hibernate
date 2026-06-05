package com.example.repository;

import com.example.model.Salle;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SalleRepositoryImpl implements SalleRepository {
    private final EntityManager em;

    public SalleRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    public List<Salle> findAvailableRooms(LocalDateTime start, LocalDateTime end) {
        return em.createQuery(
                        "SELECT DISTINCT s FROM Salle s WHERE s.id NOT IN " +
                        "(SELECT r.salle.id FROM Reservation r " +
                        "WHERE r.statut = com.example.model.Reservation.StatutReservation.CONFIRMEE " +
                        "AND (r.dateDebut <= :end AND r.dateFin >= :start))", Salle.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }

    public List<Salle> searchRooms(Map<String, Object> criteria) {
        StringBuilder jpql = new StringBuilder("SELECT DISTINCT s FROM Salle s");
        List<String> conditions = new ArrayList<>();

        if (criteria.containsKey("equipement")) {
            jpql.append(" JOIN s.equipements e");
        }

        jpql.append(" WHERE 1=1");

        if (criteria.containsKey("capaciteMin")) {
            jpql.append(" AND s.capacite >= :capaciteMin");
        }
        if (criteria.containsKey("capaciteMax")) {
            jpql.append(" AND s.capacite <= :capaciteMax");
        }
        if (criteria.containsKey("batiment")) {
            jpql.append(" AND s.batiment = :batiment");
        }
        if (criteria.containsKey("etage")) {
            jpql.append(" AND s.etage = :etage");
        }
        if (criteria.containsKey("equipement")) {
            jpql.append(" AND e.id = :equipementId");
        }

        TypedQuery<Salle> query = em.createQuery(jpql.toString(), Salle.class);

        if (criteria.containsKey("capaciteMin")) {
            query.setParameter("capaciteMin", criteria.get("capaciteMin"));
        }
        if (criteria.containsKey("capaciteMax")) {
            query.setParameter("capaciteMax", criteria.get("capaciteMax"));
        }
        if (criteria.containsKey("batiment")) {
            query.setParameter("batiment", criteria.get("batiment"));
        }
        if (criteria.containsKey("etage")) {
            query.setParameter("etage", criteria.get("etage"));
        }
        if (criteria.containsKey("equipement")) {
            query.setParameter("equipementId", criteria.get("equipement"));
        }

        return query.getResultList();
    }

    public List<Salle> getPaginatedRooms(int page, int pageSize) {
        return em.createQuery("SELECT s FROM Salle s ORDER BY s.id", Salle.class)
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public long countRooms() {
        return em.createQuery("SELECT COUNT(s) FROM Salle s", Long.class).getSingleResult();
    }

    public int getTotalPages(int pageSize) {
        long count = countRooms();
        return (int) Math.ceil((double) count / pageSize);
    }
}
