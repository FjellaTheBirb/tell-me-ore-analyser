//TODO
// save registry as json(?) file
// list files in tell me dir (or path)
// error handling
// rework ui

public class Main {

    public static void main(String[] args) {
        OreRegistry oreRegistry = new OreRegistry();

        if (args.length != 0) {
            ArgumentHandler.handleArguments(args, oreRegistry);
            return;
        }
        ConsoleUI.startUI(oreRegistry);
    }
}