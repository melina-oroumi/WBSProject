public class Main {
    public static void main(String[] args) {

        WBS wbs = new WBS();

        Task research =
                new Task("A", "Market Research", 10);

        Task design =
                new Task("B", "Game Design", null);

        Task concepts =
                new Task("C", "Core Concepts", 15);

        Task story =
                new Task("D", "Storyline", 25);

        Task world =
                new Task("E", "Game World", 40);

        design.addSubtask(concepts);
        design.addSubtask(story);
        story.addSubtask(world);

        wbs.addRootTask(research);
        wbs.addRootTask(design);

        wbs.display();
    }
}