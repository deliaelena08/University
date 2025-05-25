package model;

import java.util.Objects;


public abstract class Task {

    private String taskId;
    private String description;


    public String getTaskId() {
        return taskId;
    }

    public String getDescription() {
        return description;
    }

    Task(String taskId, String description) { this.taskId = taskId; this.description = description; }

    public abstract void execute();

    @Override
    //IN java toate clasele au ca parinte Object

    public String toString() {
        return "Task [taskId=" + taskId + ", description=" + description + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;

        if(obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        Task task = (Task) obj;
        return Objects.equals(taskId, task.taskId);
    }
    @Override
    public int hashCode() {
        return Objects.hash(taskId);
    }
}
