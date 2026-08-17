import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WBSFileManager {
    public static WBS load(String filename) {

        WBS wbs = new WBS();

        // Stores tasks using their ID
        Map<String, Task> tasks = new HashMap<>();

        // Stores each task's parents ID
        Map<String, String> parentIDs = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(
                new FileReader(filename))) {
            
            String line;

            // CREATE ALL TASKS

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] fields = line.split(";", -1);

                if (fields.length != 3 && fields.length != 4) {
                    System.out.println("Invalid line: " + line);
                    continue;
                }

                String parentID = fields[0].trim();
                String taskID = fields[1].trim();
                String description = fields[2].trim();

                Integer effort = null;

                if (fields.length == 4) {
                    String effortText = fields[3].trim();

                    if (!effortText.isEmpty()) {

                        try {
                            effort = Integer.parseInt(effortText);
                        } catch (NumberFormatException e) {
                            System.out.println(
                                "Invalid effort for task: " + taskID
                            );

                            continue;
                        }
                    }
                }

                // Create Task
                Task task = new Task(
                    taskID,
                    description,
                    effort
                );

                // Store task by ID
                tasks.put(taskID, task);

                // Store parent relationship
                parentIDs.put(taskID, parentID);
            }

            // BUILD TREE

            for (Task task : tasks.values()) {

                String parentID = parentIDs.get(task.getId());

                if (parentID == null || parentID.isEmpty()) {
                    wbs.addRootTask(task);
                } else {
                    Task parent = tasks.get(parentID);
                    
                    if (parent != null) {
                        parent.addSubtask(task);
                    } else {
                        System.out.println(
                            "Parent not found for task: "
                            + task.getId()
                        );
                    }
                }
            }

        } catch (IOException e) {

            System.out.println("Error reading file: " + e.getMessage());
        }

        return wbs;

    }
}