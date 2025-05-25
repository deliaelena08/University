package org.example.examen.validators;

import org.example.examen.models.SeatReservations;

public class SeatReservationValidator implements Validator<SeatReservations> {
    @Override
    public void validate(SeatReservations reservation) {
        if (reservation.getUserEmail() == null || reservation.getUserEmail().isEmpty()) {
            throw new IllegalArgumentException("Email-ul utilizatorului nu poate fi gol.");
        }
        if (reservation.getShowId() == null) {
            throw new IllegalArgumentException("ID-ul spectacolului nu poate fi null.");
        }
    }
}
