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

        wbs.display();

        WBSFileManager.save(wbs, filename);
    }
}