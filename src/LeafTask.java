// 
// LeafTask.java: Represents a task with no subtasks
//

public class LeafTask implements WBSComponent {

    private final String id;
    private final String description;
    private Integer effort;

    public LeafTask(String id, String description, Integer effort) {
        this.id = id;
        this.description = description;
        this.effort = effort;
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
        return effort;
    }

    @Override
    public void setEffort(Integer effort) {
        this.effort = effort;
    }
    
    @Override
    public boolean hasSubtasks() {
        return false;
    }

    @Override
    public void display(int level) {
        printIndent(level);
        System.out.print(id + ": " + description);
        printEffort();
        System.out.println();
    }

    private void printIndent(int level) {
        System.out.print(" ".repeat(level));
    }

    private void printEffort() {
        if (effort != null) {
            System.out.print(", effort = " + effort);
        }
    }
}