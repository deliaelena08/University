import Domain.Angajat;
import Domain.Pontaj;
import Domain.Sarcina;
import Repository.AngajatInFileRepository;
import Repository.PontajInFileRepostiory;
import Repository.SarcinaInFileRepository;

import java.util.Enumeration;

public class Main {
    public static void main(String[] args) {
        AngajatInFileRepository repoAngajati = new AngajatInFileRepository("D:\\facultate\\Semestrul 3\\MAP\\Seminar13_nou\\Seminar12\\src\\Assets\\angajati.txt");
        SarcinaInFileRepository repoSarcini = new SarcinaInFileRepository("D:\\facultate\\Semestrul 3\\MAP\\Seminar13_nou\\Seminar12\\src\\Assets\\sarcini.txt");
        PontajInFileRepostiory repoPontaj = new PontajInFileRepostiory("D:\\facultate\\Semestrul 3\\MAP\\Seminar13_nou\\Seminar12\\src\\Assets\\pontaj.txt","D:\\facultate\\Semestrul 3\\MAP\\Seminar13_nou\\Seminar12\\src\\Assets\\angajati.txt","D:\\facultate\\Semestrul 3\\MAP\\Seminar13_nou\\Seminar12\\src\\Assets\\sarcini.txt");

        Enumeration<Angajat> angajati = repoAngajati.findAll();
        Enumeration<Sarcina> sarcini = repoSarcini.findAll();
        Enumeration<Pontaj> pontaje = repoPontaj.findAll();

        while (angajati.hasMoreElements()) {
            Angajat angajat = angajati.nextElement();
            System.out.println(angajat);
        }

        while (sarcini.hasMoreElements()) {
            Sarcina sarcina = sarcini.nextElement();
            System.out.println(sarcina);
        }

        while (pontaje.hasMoreElements()) {
            Pontaj pontaj = pontaje.nextElement();
            System.out.println(pontaj);
        }
    }
}