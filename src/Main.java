import java.util.*;

public class Main {
    static Scanner sc = new Scanner(System.in);

    static void printData(HashMap<Integer, Integer> data) {
        for (int i = 0; i <256; i++) {
            int iData = data.getOrDefault(i, 0);
            if (iData == 0) {
                continue;
            }
            System.out.println("y = " + i + ": " + iData);
        }
    }
    static String printMenu(String[] options) {
        String input;
        String prompt = "";
        for (int i = 0; i < options.length; i++) {
            prompt += i+1 + ") " + options[i] + "\n";
        }
        prompt += "selection: ";
        while (true) {
            System.out.print(prompt);
            input = sc.nextLine();
            if (input.matches("\\d+")) { /*check is String is digit*/
                if (Integer.parseInt(input) <= options.length && Integer.parseInt(input) > 0) {
                    break;
                }
            }
            System.out.println("Input not allowed");
        }
        return input;
    }
    static void printPerc(HashMap<Integer, Double> data) {
        for (int i = 0; i <256; i++) {
            Double iData = data.getOrDefault(i, 0.0);
            if (iData == 0.0) {
                continue;
            }
            System.out.println("y = " + i + ": " + iData);
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
    static Ore enterId(OreRegistry oreRegistry) {
        String id;
        Ore ore;
        while (true) {
            System.out.print("id of the ore (or quit): ");
            id = sc.nextLine();
            ore = oreRegistry.get(id);
            if (id.equals("quit")) {
                return null;
            }
            if (!(ore == null)) {
                break;
            }
            System.out.println("Input not allowed");
        }
        return ore;
    }
    static void getOre(OreRegistry oreRegistry) {
        String input;
        Ore ore = new Ore();
        do {
            input = printMenu(new String[] {"enter id", "list available ids"});
            switch (input) {
                case "1":
                    ore = enterId(oreRegistry);
                    break;
                case "2":
                    listIds(oreRegistry);
                    break;
            }
        } while (!input.equals("1"));
        if (ore == null) {
            return;
        }
        while (true) {
            input = printMenu(new String[] {"print block counts", "print generation percentage", "quit"});
            switch (input) {
                case "1":
                    printData(ore.getGenData());
                    break;
                case "2":
                    printPerc(ore.getGenPercentages());
                    break;
                case "3":
                    return;
            }
        }

    }

    private static void listIds(OreRegistry oreRegistry) {
        for (String id : oreRegistry.getOreIds()) {
            System.out.println(id);
        }
    }

    public static void main(String[] args) {
        OreRegistry oreReg = new OreRegistry();
        String input;
        while(true) {
            input = printMenu(new String[] {"add ore", "get ore", "quit", "help"});
            switch (input) {
                case "1":
                    addOre(oreReg);
                    break;
                case "2":
                    getOre(oreReg);
                    break;
                case "3":
                    return;
                case "4":
                    printHelp();
                    break;
                default:
                    break;
            }
        }
    }
}