public class Main {
    private static final int REQUIRED_ARGUMENTS = 1;

    public static void main(String[] args) {

        if (args.length != REQUIRED_ARGUMENTS) {
            System.out.println("Usage: java Main <filename>");
            return;
        }

        try {
            WBS wbs = WBSFileManager.load(args[0]);
            runApplication(wbs);
        } catch (WBSException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    private static void runApplication(WBS wbs) {

        System.out.println("WBS:");
        wbs.display();

        System.out.println();
        System.out.println("Total known effort = " + wbs.getTotalEffort());
        System.out.println("Unknown tasks = " + wbs.getUnknownTaskCount());
    }
}