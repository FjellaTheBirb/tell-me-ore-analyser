import java.io.File;
import java.util.*;

//TODO
// automatically parse files in specific dir
// list files in tell me dir (or path)
// rework ui
// save and parse registry in/from file

public class Main {
    static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        OreRegistry oreRegistry = new OreRegistry();

        if (args.length != 0) {
            handleArguments(args);
            return;
        }

        /* ui version if no arguments (args is empty) */
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
    static void handleArguments(String[] args) {
        OreRegistry oreRegistry = new OreRegistry();
        HashMap<Integer, Integer> runParameter = getRunParameters(args);
        if (runParameter == null) return;
        HashMap<Integer, ?> oreData = new HashMap<>();
        String mode = "percent"; /* mode for option show (percent, absolute) */
        for (int i = 0; i < 8; i++) {
            int runId = runParameter.getOrDefault(i, -2);
            if (runId == -2) {
                continue;
            }
            String[] argumentSplit = args[runId].split("=");
            switch (i) {
                case 0 -> { printArgumentsHelp(); return; }
                case 1 -> {}
                case 2 -> addAll(oreRegistry, argumentSplit[1]);
                case 3 -> { if (!oreRegistry.add(argumentSplit[1])) return; }
                case 4 -> { if (!argumentSplit[1].equals("percent") && !argumentSplit[1].equals("absolute")) System.out.println("Unrecognised mode '" + argumentSplit[1] + "'. Using default value 'percent'");
                            else mode = argumentSplit[1];
                }
                case 5 -> {
                    Ore ore = oreRegistry.get(argumentSplit[1]);
                    if (ore == null) { System.out.println("Ore not found in ore registry"); return; }
                    if (mode.equals("percent")) oreData = ore.getGenPercentages();
                    else oreData = ore.getGenData();
                }
                case 6 -> {}
                case 7 -> {}
            }
        }
        printMap(oreData);
    }

    private static HashMap<Integer, Integer> getRunParameters(String[] args) {
        HashMap<Integer, Integer> runParameter = new HashMap<>();
        for (int i = 0; i< args.length; i++) {
            String argument = args[i].split("=")[0];
            switch (argument) {
                case "help" -> runParameter.put(0, i);
                case "registry" -> runParameter.put(1, i);
                case "search" -> runParameter.put(2, i);
                case "add" -> runParameter.put(3, i);
                case "mode" -> runParameter.put(4, i);
                case "show" -> runParameter.put(5, i);
                case "save" -> runParameter.put(6, i);
                case "generate" -> runParameter.put(7, i);
                default -> { System.out.println("unrecognised option '" + argument + "'.\nUse 'help' for more information."); return null; }
            }
        }
        return runParameter;
    }

    private static void printArgumentsHelp() {
        System.out.println("""
                This program parses tell me files and calculates ore distribution.
                
                Usage: java -jar ore-analyser.jar [options]
                
                Options:
                help                                show this message
                add=</path/to/tellme>               adds an ore entry from the specified tell me file
                registry=</path/to/registry>        uses an ore registry at the specified path
                search=</path/to/dir>               tries to parse all files in the specified directory
                show=<id>                           shows the data from the specified ore (block id)
                mode=<percent,absolute>             specify the mode for show (default is percent)
                generate=</path/to/target/file>     generates json file for jre at the specified path
                save=</path/to/target/file>         saves the ore registry at the specified path
                
                Important: use /home/{user} instead of '~' when specifying path""");
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
    static void addAll(OreRegistry oreRegistry, String path) {
        File dir = new File(path);
        File[] filesArray = dir.listFiles(File::isFile); /* Add only files, no subdirectories */
        if (filesArray == null) {
            return;
        }
        for (File file : filesArray) {
            String filePath = file.getAbsolutePath();
            if (!oreRegistry.add(filePath)) {
                System.out.println("Path" + filePath + "not valid.");
            }
        }

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