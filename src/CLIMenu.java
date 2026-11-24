import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Command-Line Interface (CLI) menu for the Turing Tape Simulator.
 * Prompts the user to provide a plain-text program file, validates the file path,
 * and provides options to read and load the program contents.
 */
public class CLIMenu {

    private final Scanner scanner;
    private List<String> lines;

    public CLIMenu() {
        this.scanner = new Scanner(System.in);
    }

    public CLIMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Displays the main menu and handles user selection.
     */
    public void start() {
        boolean running = true;

        while (running) {
            printDivider();
            System.out.println("   TURING TAPE SIMULATOR - MAIN MENU");
            printDivider();
            System.out.println("1. Load Plain-Text Program File");
            System.out.println("2. Exit");
            printDivider();
            System.out.print("Select an option (1-2): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    File programFile = promptForProgramFile();
                    if (programFile != null) {
                        this.lines = loadProgramLines(programFile);
                        if (lines != null) {
                            System.out.println("\n[SUCCESS] Successfully loaded: " + programFile.getName());
                            System.out.println("Total lines read: " + lines.size());
                        }
                    }
                    break;

                case "2":
                    System.out.println("\nExiting simulator. Goodbye!");
                    running = false;
                    break;

                default:
                    System.out.println("\n[ERROR] Invalid option. Please enter 1 or 2.");
                    break;
            }
        }
    }

    /**
     * Prompts the user to enter the path to a plain-text program file.
     * Continues to prompt until a valid readable file is provided or the user cancels.
     *
     * @return Validated {@link File} object, or null if the user cancels.
     */
    public File promptForProgramFile() {
        while (true) {
            System.out.println();
            printDivider();
            System.out.println("Enter the path to your plain-text program file");
            System.out.println("(e.g., program.txt, C:/path/to/program.txt, or type 'cancel' to return)");
            printDivider();
            System.out.print("File path: ");

            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("cancel")) {
                System.out.println("File loading cancelled.");
                return null;
            }

            if (input.isEmpty()) {
                System.out.println("[ERROR] File path cannot be empty. Please try again.");
                continue;
            }

            // Remove surrounding single or double quotes if present (common when copying paths on Windows)
            if ((input.startsWith("\"") && input.endsWith("\"")) ||
                (input.startsWith("'") && input.endsWith("'"))) {
                input = input.substring(1, input.length() - 1).trim();
            }

            File file = new File(input);

            if (!file.exists()) {
                System.out.println("[ERROR] File not found: \"" + input + "\". Please check the path and try again.");
                continue;
            }

            if (!file.isFile()) {
                System.out.println("[ERROR] The path \"" + input + "\" is a directory, not a file.");
                continue;
            }

            if (!file.canRead()) {
                System.out.println("[ERROR] The file \"" + input + "\" cannot be read. Check file permissions.");
                continue;
            }

            return file;
        }
    }

    /**
     * Reads all lines from the specified plain-text file.
     * Leading and trailing whitespaces are removed from each line,
     * and blank lines are ignored before adding them to the list,
     * adhering to requirement P2.
     *
     * @param file The validated program file to read.
     * @return List of non-blank, trimmed line strings, or null on I/O failure.
     */
    public List<String> loadProgramLines(File file) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (!trimmed.isEmpty()) {
                    lines.add(trimmed);
                }
            }
            return lines;
        } catch (IOException e) {
            System.out.println("[ERROR] Failed to read file: " + e.getMessage());
            return null;
        }
    }

    /**
     * Displays a preview of the loaded program lines.
     *
     * @param lines The list of program lines.
     */
    public void displayProgramPreview(List<String> lines) {
        printDivider();
        System.out.println("PROGRAM PREVIEW:");
        printDivider();
        if (lines.isEmpty()) {
            System.out.println("(File is empty)");
        } else {
            for (int i = 0; i < lines.size(); i++) {
                System.out.printf("%4d | %s%n", i + 1, lines.get(i));
            }
        }
        printDivider();
    }

    public List<String> getProgramLines() {
        return lines;
    }

    private void printDivider() {
        System.out.println("==================================================");
    }

    /**
     * Direct entry point for testing the CLI menu standalone.
     */
    public static void main(String[] args) {
        CLIMenu menu = new CLIMenu();
        menu.start();
    }
}
