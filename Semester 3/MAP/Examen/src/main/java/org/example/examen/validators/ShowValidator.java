package org.example.examen.validators;
import org.example.examen.models.Show;
import java.time.LocalDateTime;

public class ShowValidator implements Validator<Show> {

    @Override
    public void validate(Show show) {
        if (show.getName() == null || show.getName().isEmpty()) {
            throw new IllegalArgumentException("Numele spectacolului nu poate fi gol.");
        }
        if (show.getDurationMinutes() <= 0) {
            throw new IllegalArgumentException("Durata trebuie să fie un număr pozitiv.");
        }
        if (show.getNumberOfSeats() <= 0) {
            throw new IllegalArgumentException("Spectacolul trebuie să aibă cel puțin un loc.");
        }
        if (show.getStartDate() == null || show.getCreationDate() == null) {
            throw new IllegalArgumentException("Datele spectacolului nu pot fi nule.");
        }
        if (show.getStartDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Data de start trebuie să fie în viitor.");
        }
    }
}

