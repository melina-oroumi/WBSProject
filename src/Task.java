import java.util.ArrayList;
import java.util.List;

public class Task {

    // Task information
    private String id;
    private String description;
    private Integer effort;

    // Tasks underneath this task
    private List<Task> subtasks;

    // Constructor
    public Task(String id, String description, Integer effort) {
        this.id = id;
        this.description = description;
        this.effort= effort;
        this.subtasks = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Integer getEffort() {
        return effort;
    }

    public void setEffort(Integer effort) {
        this.effort = effort;
    }

    public List<Task> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(Task task) {
        subtasks.add(task);
    }

    public boolean hasSubtasks() {
        return !subtasks.isEmpty();
    }
}