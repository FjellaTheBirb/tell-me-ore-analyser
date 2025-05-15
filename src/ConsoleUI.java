import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class ConsoleUI {
    static final Scanner sc = new Scanner(System.in);

    static void startUI(OreRegistry oreRegistry) {
        /* ui version if no arguments (args is empty) */
        String input;
        while(true) {
            input = ConsoleUI.printMenu("add ore", "get ore", "quit", "help", "generate json");
            switch (input) {
                case "1" -> ConsoleUI.addOre(oreRegistry);
                case "2" -> ConsoleUI.getOre(oreRegistry);
                case "3" -> { return; }
                case "4" -> ConsoleUI.printHelp();
                case "5" -> JsonParser.generateJson(oreRegistry);
            }
        }
    }
    private static void addOre(OreRegistry oreRegistry) {
        while (true) {
            switch (printMenu("add specific ore from tell me file", "add all tell me files in a specific directory", "quit")) {
                case "1" -> {
                    selectPath(oreRegistry);
                    return;
                }
                case "2" -> {
                    System.out.print("Path of the directory or quit (leave blank for default path): ");
                    String path = sc.nextLine();
                    if (path.equals("quit")) return;
                    if (path.isEmpty()) path = System.getProperty("user.home") + "/.minecraft/config/tellme/";
                    ArgumentHandler.addAll(oreRegistry, path);
                    return;
                }
                case "3" -> { return; }
            }
        }
    }
    private static void selectPath(OreRegistry oreRegistry) {
        String path;
        while (true) {
            System.out.print("Path of the tell me file or quit: ");
            path = sc.nextLine();
            if (Objects.equals(path, "quit")) {
                return;
            }
            if (oreRegistry.add(path)) return;
            System.out.println("File not found");
        }
    }
    private static Ore selectOre(OreRegistry oreRegistry) {
        while (true) {
            System.out.print("id of the ore or quit: ");
            String id = sc.nextLine();
            if (id.equals("quit")) return null;
            Ore ore = oreRegistry.get(id);
            if (ore != null) return ore;
            System.out.println("Ore not found");
        }
    }
    private static void getOre(OreRegistry oreRegistry) {
        while (true) {
            Ore ore = null;
            while (ore == null) {
                switch (printMenu("enter id", "list available ids", "quit")) {
                    case "1" -> ore = selectOre(oreRegistry);
                    case "2" -> listIds(oreRegistry);
                    case "3" -> {
                        return;
                    }
                }
            }
            String input = "";
            while (!input.equals("3")) {
                input = printMenu("print block counts", "print generation percentage", "quit");
                switch(input) {
                    case "1" -> printMap(ore.getGenData());
                    case "2" -> printMap(ore.getGenPercentages());
                }
            }
        }
    }
    private static void listIds(OreRegistry oreRegistry) {
        for (String id : oreRegistry.getOreIds()) {
            System.out.println(id);
        }
    }


    private static String printMenu(String ... options) {
        String input;
        for (int i = 0; i < options.length; i++) {
            System.out.println(i + 1 + ") "  + options[i]);
        }
        while (true) {
            System.out.print("selection: ");
            input = sc.nextLine();
            if (input.matches("\\d+")) { /*check is String is digit*/
                if (Integer.parseInt(input) <= options.length && Integer.parseInt(input) > 0) return input;                }
            System.out.println("Input not allowed");
        }
    }
    static void printMap(Map<Integer, ?> data) {
        for (var entry : data.entrySet()) {
            Object value = entry.getValue();
            if (!(value instanceof Number number) || number.doubleValue() == 0) continue;
            System.out.println("y = " + entry.getKey() + ": " + value);
        }
    }
    private static void printHelp() {
        System.out.println("""
                How do get a tell me file (1.12.2)?
                -----------------------------------
                1) Install the tell me mod
                2) Run this command in Minecraft:
                /tellme locate block dump all-loaded-chunks {ore}
                3) Get the path of the file or just the filename if using default path (~/.minecraft/tellme/)
                
                Important:
                You have to add every with 'tell me' analysed ore separately and everytime you run this program. Saving the Ore Data is not supported yet.
                Using '~' instead of /home/{user} does not work, you have to type the full path (/home/{user}/path/to/tellme)
                Source code available at:
                
                Type enter to continue.""");
        sc.nextLine();
    }
}
