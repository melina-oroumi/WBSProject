// Common interface for the Composite pattern

public interface WBSComponent {
    String getId();
    String getDescription();
    Integer getEffort();
    void setEffort(Integer effort);
    boolean hasSubtasks();
    void display(int level);
}