import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<String> lines;
        ArrayList<Object> tape = new ArrayList<>();



        CLIMenu menu = new CLIMenu();
        menu.start();
        lines = menu.getProgramLines();

    }
}
