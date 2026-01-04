package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessageTask extends Task {

    private String message;
    private String from;
    private String to;
    private LocalDateTime time;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public MessageTask(String taskId, String description, String message, String from, String to, LocalDateTime time) {
        super(taskId, description);
        this.message = message;
        this.from = from;
        this.to = to;
        this.time = time;
    }

    @Override
    public void execute() {
        System.out.println("Message: " + message + "  From: " + from + "  To: " + to + "  Date: " + time.toLocalDate());
    }

    @Override
    public String toString() {
        return "id=" + this.getTaskId() + " description=" + this.getDescription() + " message=" + this.message + " date=" + this.time.format(formatter);
    }
}

