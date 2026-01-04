import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        List<Nota> note = getNte(getStudents(), getTeme());
        report1(note,"");
        System.out.println();
        report2(note,"");
        System.out.println();
        report4(note,2);
    }


    private static List<Student> getStudents() {
        Student s1 = new Student("andrei", 221);
        s1.setId(2l);
        Student s2 = new Student("dan", 222);
        s2.setId(2l);
        Student s3 = new Student("gigi", 221);
        s3.setId(2l);
        Student s4 = new Student("costel", 222);
        s4.setId(2l);
        return List.of(s1, s2, s3, s4);
    }

    private static List<Tema> getTeme() {
        return List.of(
                new Tema("t1", "desc1"),
                new Tema("t2", "desc2"),
                new Tema("t3", "desc3"),
                new Tema("t4", "desc4")
        );
    }

    private static List<Nota> getNote(List<Student> stud, List<Tema> teme) {
        return List.of(
                new Nota(stud.get(0), teme.get(0), 10d, LocalDate.of(2019, 11, 2), "profesor1"),
                new Nota(stud.get(1), teme.get(0), 9d, LocalDate.of(2019, 11, 2).minusWeeks(1), "profesor1"),
                new Nota(stud.get(1), teme.get(1), 10d, LocalDate.of(2019, 10, 20), "profesor2"),
                new Nota(stud.get(1), teme.get(2), 10d, LocalDate.of(2019, 10, 20), "profesor2"),
                new Nota(stud.get(2), teme.get(1), 7d, LocalDate.of(2019, 10, 28), "profesor1"),
                new Nota(stud.get(2), teme.get(3), 9d, LocalDate.of(2019, 10, 27), "profesor2"),
                new Nota(stud.get(1), teme.get(3), 10d, LocalDate.of(2019, 10, 29), "profesor2")
        );
    }
    private static void report1(List<Nota> note, String s) {
        Map<Student, List<Nota>> studentsGrades = note
                .stream()
                .collect(Collectors.groupingBy(Nota::getStudent));
        studentsGrades.entrySet()
                .stream()
                .filter(x -> x.getKey().getName().contains(s))
                .sorted((o1, o2) -> {
                    double avg1 = average(o1.getValue());
                    double avg2 = average(o2.getValue());
                    if (avg1 > avg2) return -1;
                    else return 0;
                })
                .forEach(x -> System.out.println(x.getKey().getName() + "; media: " + average(x.getValue())));
    }
    public static double average(List<Nota> notaList) {
        double sum = notaList .stream()
                .map(Nota::getValue)
                .reduce(0d, Double::sum);
        return sum/notaList.size();
    }

    private static void report2(List<Nota> note, String s) {
        Map<String, List<Nota>> profesorsGrades = note
                .stream()
                .collect(Collectors.groupingBy(Nota::getProfesor));
        profesorsGrades.entrySet()
                .stream()
                .filter(x -> x.getKey().contains(s))
                .sorted((o1, o2) -> {
                    double avg1 = average(o1.getValue());
                    double avg2 = average(o2.getValue());
                    if (avg1 > avg2) return -1;
                    else return 0;
                })
                .forEach(x -> System.out.println(x.getKey() + "; media: " + average(x.getValue())));
    }

    public static void report3(List<Nota> note,int group){

    }

    public static void report4 (List<Nota> note,int nr){
        Map<Student, List<Nota>> studentsGroups = note
                .stream()
                .collect(Collectors.groupingBy(Nota::getStudent));

        studentsGroups.entrySet()
                .stream()
                .collect(Collectors.groupingBy(
                        entry -> entry.getKey().getGroup(),
                        Collectors.mapping(
                                entry -> average(entry.getValue()),
                                Collectors.toList()
                        )
                ))
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey() / 100 == nr)
                .sorted((entry1, entry2) -> Double.compare(
                        entry2.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0),
                        entry1.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0)
                ))
                .forEach(entry -> {
                    double groupAvg = entry.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0);
                    System.out.println("Grupa: " + entry.getKey() + "; media grupei: " + groupAvg);
                });
    }
}