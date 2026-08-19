//
// WBSFileManager: Loads and saves files
//

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WBSFileManager {

    public static WBS load(String filename) {
        List<TaskRecord> records = readRecords(filename);

        if (records == null) {
            return null;
        }

        return buildWBS(records);
    }

    private static List<TaskRecord> readRecords(String filename) {
        List<TaskRecord> records = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    addRecord(records, line);
                }
            }
            return records;
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return null;
        }
    }

    private static void addRecord(List<TaskRecord> records, String line) {
        String[] fields = line.split(";", -1);

        if (fields.length != 3 && fields.length != 4) {
            System.out.println("Invalid line: " + line);
            return;
        }

        String parentID = fields[0].trim();
        String taskID = fields[1].trim();
        String description = fields[2].trim();
        Integer effort = parseEffort(fields, taskID);

        records.add(new TaskRecord(parentID, taskID, description, effort));
    }

    private static Integer parseEffort(String[] fields, String taskID) {
        if (fields.length != 4) {
            return null;
        }

        String text = fields[3].trim();

        if (text.isEmpty()) {
            return null;
        }

        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            System.out.println("Invalid effort for task: " + taskID);
            return null;
        }
    }

    private static WBS buildWBS(List<TaskRecord> records) {
        WBS wbs = new WBS();

        Map<String, TaskRecord> recordMap = createRecordMap(records);
        Set<String> parentIDs = findParentIDs(records);
        Map<String, WBSComponent> tasks = createTasks(records, parentIDs);

        connectTasks(wbs, records, tasks);

        return wbs;
    }

    private static Map<String, TaskRecord> createRecordMap(List<TaskRecord> records) {
        Map<String, TaskRecord> result = new HashMap<>();

        for (TaskRecord record : records) {
            result.put(record.id(), record);
        }
        return result;
    }

    private static Set<String> findParentIDs(List<TaskRecord> records) {
        Set<String> parentIDs = new HashSet<>();

        for (TaskRecord record : records) {
            if (!record.parentID().isEmpty()) {
                parentIDs.add(record.parentID());
            }
        }
        return parentIDs;
    }

    private static Map<String, WBSComponent> createTasks(List<TaskRecord> records, Set<String> parentIDs) {
        Map<String, WBSComponent> tasks = new HashMap<>();

        for (TaskRecord record : records) {
            WBSComponent task = createTask(record, parentIDs);
            tasks.put(record.id(), task);
        }
        return tasks;
    }

    private static WBSComponent createTask(TaskRecord record, Set<String> parentIDs) {
        if (parentIDs.contains(record.id())) {
            return new CompositeTask(record.id(), record.description());
        }

        return new LeafTask(record.id(), record.description(), record.effort());
    }

    private static void connectTasks(WBS wbs, List<TaskRecord> records, Map<String, WBSComponent> tasks) {
        for (TaskRecord record : records) {
            WBSComponent task = tasks.get(record.id());

            if (record.parentID().isEmpty()) {
                wbs.addRootTask(task);
            } else {
                addToParent(record, tasks);
            }
        }
    }

    private static void addToParent(TaskRecord record, Map<String, WBSComponent> tasks) {
        WBSComponent parent = tasks.get(record.parentID());

        if (parent instanceof CompositeTask composite) {
            composite.addSubtask(tasks.get(record.id()));
        } else {
            System.out.println("Invalid parent for task: " + record.id());
        }
    }

    public static void save(WBS wbs, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (WBSComponent task : wbs.getRootTasks()) {
                saveTask(writer, task, "");
            }

            System.out.println("WBS saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving fdile: " + e.getMessage());
        }
    }

    private static void saveTask(BufferedWriter writer, WBSComponent task, String parentID) throws IOException {
        writer.write(parentID + " ; ");
        writer.write(task.getId() + " ; ");
        writer.write(task.getDescription());

        writeEffort(writer, task);
        writer.newLine();

        if (task instanceof CompositeTask composite) {
            for (WBSComponent child : composite.getSubtasks()) {
                saveTask(writer, child, task.getId());
            }
        }
    }

    private static void writeEffort(BufferedWriter writer, WBSComponent task) throws IOException {
        if (task.getEffort() != null) {
            writer.write(" ; ");
            writer.write(String.valueOf(task.getEffort()));
        }
    }

    private record TaskRecord(String parentID, String id, String description, Integer effort) {}
}