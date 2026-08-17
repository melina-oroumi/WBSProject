public class Main {
    public static void main(String[] args) {

        // Create a parent task
        Task gameDesign = new Task(
            "C",
            "Game Design",
            null
        );

        // Create two subtasks
        Task concepts = new Task(
            "D",
            "Identify core game concepts",
            15
        );

        Task storyline = new Task(
            "E",
            "Develop basic storyline",
            25
        );

        // Add the subtasks
        gameDesign.addSubtask(concepts);
        gameDesign.addSubtask(storyline);

        // Test the parent task
        System.out.println("ID: " + gameDesign.getId());
        System.out.println("Description: " + gameDesign.getDescription());
        System.out.println("Has subtasks: " + gameDesign.hasSubtasks());

        // Test the subtasks
        System.out.println("\nSubtasks:");

        for (Task task : gameDesign.getSubtasks()) {
            System.out.println(
                task.getId() + ": " +
                task.getDescription() +
                ", effort = " +
                task.getEffort()
            );
        }
    }   
}