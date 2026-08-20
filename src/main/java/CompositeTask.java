// 
// CompositeTask.java: Represents a task containing other tasks
//

import java.util.ArrayList;
import java.util.List;

public class CompositeTask implements WBSComponent {
    private final String id;
    private final String description;
    private final List<WBSComponent> subtasks;

    public CompositeTask(String id, String description) {
        this.id = id;
        this.description = description;
        this.subtasks = new ArrayList<>();
    }

    public void addSubtask(WBSComponent task) {
        subtasks.add(task);
    }

    public List<WBSComponent> getSubtasks() {
        return subtasks;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Integer getEffort() {
        return null;
    }

    @Override
    public void setEffort(Integer effort) {
        throw new UnsupportedOperationException("Composite tasks cannot have effort estimates.");
    }
    
    @Override
    public boolean hasSubtasks() {
        return !subtasks.isEmpty();
    }

    @Override
    public void display(int level) {
        printIndent(level);
        System.out.println(id + ": " + description);
        displayChildren(level);
    }

    private void printIndent(int level) {
        System.out.print("  ".repeat(level));
    }

    private void displayChildren(int level) {
        for (WBSComponent task : subtasks) {
            task.display(level + 1);
        }
    }
}