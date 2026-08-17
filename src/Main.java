public class Main {
    public static void main(String[] args) {

        WBS wbs = WBSFileManager.load(
            "../testdata/example.txt"
        );

        wbs.display();
    }
}