package com.example.service;

import com.example.model.Reservation;
import java.util.List;

public interface ReservationService {
    Reservation findById(Long id);
    List<Reservation> findAll();
    void createReservation(Reservation reservation);
    void updateReservation(Reservation reservation);
    void cancelReservation(Long id);
}
