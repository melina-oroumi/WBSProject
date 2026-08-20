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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WBSFileManager {

    private static final int MIN_FIELDS = 3;
    private static final int MAX_FIELDS = 4;

    private static final Logger LOGGER = LoggerFactory.getLogger(WBSFileManager.class);

    public static WBS load(String filename) throws WBSException {
        LOGGER.info("Loading WBS file: {}", filename);

        List<TaskRecord> records = readRecords(filename);
        return buildWBS(records);
    }

    private static List<TaskRecord> readRecords(String filename) throws WBSException {
        List<TaskRecord> records = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {

            while (true) {
                String line = reader.readLine();

                if (line == null) {
                    break;
                }

                addRecord(records,line);
            }
            return records;

        } catch (IOException e) {
            LOGGER.error("Unable to read WBS file: {}",filename, e);

            throw new WBSException("Unable to read WBS file: " + filename, e);
        }
    }

    private static void addRecord(List<TaskRecord> records, String line) throws WBSException {
        String[] fields = line.split(";", -1);

        if (fields.length != MIN_FIELDS && fields.length != MAX_FIELDS) {
            throw new WBSException("Invalid WBS line: " + line);
        }

        String parentID = fields[0].trim();
        String taskID = fields[1].trim();
        String description = fields[2].trim();
        Integer effort = parseEffort(fields, taskID);

        records.add(new TaskRecord(parentID, taskID, description, effort));
    }

    private static Integer parseEffort(String[] fields, String taskID) throws WBSException {
        if (fields.length != MAX_FIELDS) {
            return null;
        }

        String text = fields[3].trim();

        if (text.isEmpty()) {
            return null;
        }

        try {
            int effort = Integer.parseInt(text);

            if (effort <= 0) {
                throw new WBSException("Effort must be positive for task: " + taskID);
            }

            return effort;
        } catch (NumberFormatException e) {
            throw new WBSException("Invalid effort for task: " + taskID, e);
        }
    }

    private static WBS buildWBS(List<TaskRecord> records) throws WBSException {

        WBS wbs = new WBS();

        validateUniqueIDs(records);
        
        Set<String> parentIDs = findParentIDs(records);
        Map<String, WBSComponent> tasks = createTasks(records, parentIDs);

        connectTasks(wbs, records, tasks);

        return wbs;
    }

    private static void validateUniqueIDs(List<TaskRecord> records) throws WBSException {
        Set<String> ids = new HashSet<>();

        for (TaskRecord record : records) {
            if (!ids.add(record.id())) {
                throw new WBSException(
                    "Duplicate task ID: " + record.id()
                );
            }
        }
    }

    private static void checkDuplicate(Map<String, TaskRecord> records, TaskRecord record) throws WBSException {
        if (records.containsKey(record.id())) {
            throw new WBSException("Duplicate task ID: " + record.id());
        }
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
        LOGGER.debug("Creating task: {}", record.id());

        if (parentIDs.contains(record.id())) {
            return new CompositeTask(record.id(), record.description());
        }

        return new LeafTask(record.id(), record.description(), record.effort());
    }

    private static void connectTasks(WBS wbs, List<TaskRecord> records, Map<String, WBSComponent> tasks) throws WBSException{
        for (TaskRecord record : records) {
            WBSComponent task = tasks.get(record.id());

            if (record.parentID().isEmpty()) {
                wbs.addRootTask(task);
            } else {
                addToParent(record, tasks);
            }
        }
    }

    private static void addToParent(TaskRecord record, Map<String, WBSComponent> tasks) throws WBSException {
        WBSComponent parent = tasks.get(record.parentID());

        if (parent == null) {
            throw new WBSException("Parent task not found: " + record.parentID());
        }

        addChild(parent, tasks.get(record.id()));
    }

    private static void addChild(WBSComponent parent, WBSComponent child) throws WBSException {

        if (parent instanceof CompositeTask composite) {
            composite.addSubtask(child);
            return;
        }

        throw new WBSException("Task cannot contain subtasks: " + parent.getId());
    }

    public static void save(WBS wbs, String filename) throws WBSException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (WBSComponent task : wbs.getRootTasks()) {
                saveTask(writer, task, "");
            }

            LOGGER.info("WBS saved successfully: {}", filename);
        } catch (IOException e) {
            LOGGER.error("Unable to save WBS file: {}", filename, e);

            throw new WBSException("Unable to save WBS file: " + filename, e);
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