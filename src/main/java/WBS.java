//
// WBS.java: Manages the overall WBS
//

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class WBS {
    private final List<WBSComponent> rootTasks;
    private final Map<String, WBSComponent> tasks;

    public WBS() {
        rootTasks = new ArrayList<>();
        tasks = new HashMap<>();
    }

    public void addRootTask(WBSComponent task) {
        rootTasks.add(task);
        tasks.put(task.getId(),task);
    }

    public void addTask(WBSComponent task) {
        tasks.put(task.getId(), task);
    }

    public WBSComponent findTask(String id) {
        return tasks.get(id);
    }

    public List<WBSComponent> getRootTasks() {
        return rootTasks;
    }

    public void display() {

        for (WBSComponent task : rootTasks) {
            task.display(0);
        }
    }

    public int getTotalEffort() {
        int total = 0;
        for (WBSComponent task : rootTasks) {
            total += calculateEffort(task);
        }
        return total;
    }

    private int calculateEffort(WBSComponent task) {
        if (!task.hasSubtasks()) {
            return getEffortValue(task);
        }
        return calculateChildrenEffort(task);
    }

    private int getEffortValue(WBSComponent task) {
        return task.getEffort() == null ? 0 : task.getEffort();
    }

    private int calculateChildrenEffort(WBSComponent task) {
        int total = 0;
        CompositeTask composite = (CompositeTask) task;

        for (WBSComponent child : composite.getSubtasks()) {
            total += calculateEffort(child);
        }
        return total;
    }

    public int getUnknownTaskCount() {
        int count = 0;
        for (WBSComponent task : rootTasks) {
            count += countUnknown(task);
        }
        return count;
    }

    private int countUnknown(WBSComponent task) {
        if (!task.hasSubtasks()) {
            return task.getEffort() == null ? 1 : 0;
        }
        return countUnknownChildren(task);
    }

    private int countUnknownChildren(WBSComponent task) {
        int count = 0;
        CompositeTask composite = (CompositeTask) task;

        for (WBSComponent child : composite.getSubtasks()) {
            count += countUnknown(child);
        }
        return count;
    }
}