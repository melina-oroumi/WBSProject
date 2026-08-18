public class Main {
    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Usage: java Main <filename>");
            return;
        }

        String filename = args[0];

        WBS wbs = WBSFileManager.load(filename);

        if (wbs == null) {
            System.out.println("Could not load WBS.");
            return;
        }

        System.out.println("WBS:");
        wbs.display();

        System.out.println();

        System.out.println("Total known effort = " + wbs.getTotalEffort());
        System.out.println("Unknown tasks = " + wbs.getUnknownTaskCount());

        WBSFileManager.save(wbs, filename);
    }
}