import java.util.*;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static HashSet<String> optionsMain = new HashSet<>(List.of("1", "2", "3", "4"));
    static HashSet<String> optionsGet = new HashSet<>(List.of("1", "2", "3"));

    static void printData(HashMap<Integer, Integer> data) {
        for (int i = 0; i <256; i++) {
            int iData = data.getOrDefault(i, 0);
            if (iData == 0) {
                continue;
            }
            System.out.println("y = " + i + ": " + iData);
        }
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
    static void printMenu() {
        System.out.print("""
                1) add ore
                2) get ore
                3) quit
                4) help
                selection:\s""");
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
    static void getOre(OreRegistry oreRegistry) {
        String id;
        Ore ore;
        do {
            System.out.print("id of the ore (or quit): ");
            id = sc.nextLine();
            ore = oreRegistry.get(id);
            if (id.equals("quit")) {
                return;
            }
        } while (ore == null);
        while (true) {
            String input;
            do {
                System.out.print("""
                        1) print block counts
                        2) print generation percentage
                        3) quit
                        selection:\s""");
                input = sc.nextLine();
            } while (!optionsGet.contains(input));
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
    public static void main(String[] args) {
        OreRegistry oreReg = new OreRegistry();
        String input;
        while(true) {
            do {
                printMenu();
                input = sc.nextLine();
            } while (!optionsMain.contains(input));
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