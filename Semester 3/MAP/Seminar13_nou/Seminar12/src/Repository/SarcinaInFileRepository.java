package Repository;

import Domain.Angajat;
import Domain.Dificultate;
import Domain.Nivel;
import Domain.Sarcina;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SarcinaInFileRepository extends InFileRepository<String, Sarcina> {
    @Override
    void loadFromFile() {
        {
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    Sarcina angajat = new Sarcina(
                            parts[0],
                            Dificultate.valueOf(parts[1]),
                            Integer.parseInt(parts[2])
                    );
                    elemente.put(parts[0], angajat);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public SarcinaInFileRepository(String fileName) {
        super(fileName);
        loadFromFile();
    }
}