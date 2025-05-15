import java.util.*;

public class Main {
    static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        OreRegistry oreRegistry = new OreRegistry();
        String input;
        while(true) {
            input = printMenu("add ore", "get ore", "quit", "help");
            switch (input) {
                case "1" -> addOre(oreRegistry);
                case "2" -> getOre(oreRegistry);
                case "3" -> { return; }
                case "4" -> printHelp();
            }
        }
    }
    static String printMenu(String ... options) {
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
    static void printHelp() {
        System.out.println("""
                How do get a tell me file (1.12.2)?
                -----------------------------------
                1) Install the tell me mod
                2) Run these two command in Minecraft:
                /tellme blockstats count all-loaded-chunks
                /tellme blockstats dump hbm:ore_niter
                3) Get the path of the file or just the filename if using default path (~/.minecraft/tellme/)
                
                Important:
                You have to add every with 'tell me' analysed ore separately and everytime you run this program. Saving the Ore Data is not supported yet.
                Using '~' instead of /home/{user} does not work, you have to type the full path (/home/{user}/path/to/tellme)
                Source code available at:
                
                Type enter to continue.""");
        sc.nextLine();
    }

    static void addOre(OreRegistry oreRegistry) {
        String path;
        do {
            System.out.print("path of the tell me file (or quit): ");
            path = sc.nextLine();
            if (Objects.equals(path, "quit")) {
                return;
            }
        } while (!oreRegistry.add(path));
        System.out.println("Ore successfully added.");
    }
    static Ore selectOre(OreRegistry oreRegistry) {
        while (true) {
            System.out.print("id of the ore (or quit): ");
            String id = sc.nextLine();
            if (id.equals("quit")) return null;
            Ore ore = oreRegistry.get(id);
            if (ore != null) return ore;
            System.out.println("Ore not found");
        }
    }
    static void getOre(OreRegistry oreRegistry) {
        Ore ore = null;
        while (ore == null) {
            switch (printMenu("enter id", "list available ids", "quit")) {
                case "1" -> ore = selectOre(oreRegistry);
                case "2" -> listIds(oreRegistry);
                case "3" -> { return; }
            }
        }
        while (true) {
            switch (printMenu("print block counts", "print generation percentage", "quit")) {
                case "1" -> printMap(ore.getGenData());
                case "2" -> printMap(ore.getGenPercentages());
                case "3" -> { return; }
            }
        }
    }
    private static void listIds(OreRegistry oreRegistry) {
        for (String id : oreRegistry.getOreIds()) {
            System.out.println(id);
        }
    }
}