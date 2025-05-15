import java.io.File;
import java.util.HashMap;

public class ArgumentHandler {
    static void handleArguments(String[] args, OreRegistry oreRegistry) {
        HashMap<Integer, Integer> runParameter = getRunParameters(args);
        if (runParameter == null) return;
        HashMap<Integer, ?> oreData;
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
                case 2 -> {
                    if (argumentSplit.length < 2) { System.out.println("Missing argument for '" + argumentSplit[0] + "'"); return; }
                    addAll(oreRegistry, argumentSplit[1]);
                }
                case 3 -> {
                    if (argumentSplit.length < 2) { System.out.println("Missing argument for '" + argumentSplit[0] + "'"); return; }
                    if (!oreRegistry.add(argumentSplit[1])) { System.out.println("File not found"); return; }
                }
                case 4 -> {
                    if (argumentSplit.length < 2) { System.out.println("Missing argument for '" + argumentSplit[0] + "'"); return; }
                    if (!argumentSplit[1].equals("percent") && !argumentSplit[1].equals("absolute")) System.out.println("Unrecognised mode '" + argumentSplit[1] + "'. Using default value 'percent'");
                    else mode = argumentSplit[1];
                }
                case 5 -> {
                    if (argumentSplit.length < 2) { System.out.println("Missing argument for '" + argumentSplit[0] + "'"); return; }
                    Ore ore = oreRegistry.get(argumentSplit[1]);
                    if (ore == null) { System.out.println("Ore not found in ore registry"); return; }
                    if (mode.equals("percent")) oreData = ore.getGenPercentages();
                    else oreData = ore.getGenData();
                    ConsoleUI.printMap(oreData);
                }
                case 6 -> {}
                case 7 -> {}
            }
        }
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
    static void addAll(OreRegistry oreRegistry, String path) {
        File dir = new File(path);
        File[] filesArray = dir.listFiles(File::isFile); /* Add only files, no subdirectories */
        if (filesArray == null) {
            return;
        }
        for (File file : filesArray) {
            String filePath = file.getAbsolutePath();
            if (!oreRegistry.add(filePath)) {
                System.out.println("Path '" + filePath + "' not valid.");
            }
        }

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
}
