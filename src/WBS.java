import java.util.ArrayList;
import java.util.List;

public class WBS {
    private List<Task> rootTasks;

    public WBS() {
        rootTasks = new ArrayList<>();
    }

    public void addRootTask(Task task) {
        rootTasks.add(task);
    }

    public List<Task> getRootTasks() {
        return rootTasks;
    }

    public void display() {

        for (Task task : rootTasks) {
            displayRecursive(task, 0);
        }
    }

    private void displayRecursive(Task task, int level) {
        
        for (int i = 0; i < level; i++) {
            System.out.print(" ");
        }

        System.out.print(task.getId() + ": " + task.getDescription());

        if(task.getEffort() != null) {
            System.out.print(", effort = " + task.getEffort());
        }

        System.out.println();

        for (Task child : task.getSubtasks()) {
            displayRecursive(child, level + 1);
        }
    }
}