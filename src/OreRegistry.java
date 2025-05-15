import java.util.HashMap;

public class OreRegistry {
    public boolean add(String path) {
        Ore ore = new Ore();
        if (!ore.setPath(path)) {
            return false;
        }
        ore.read();
        if (ore.getId() == null) {
            System.out.println("Error parsing file '" + path + "'.");
            return false;
        }
        ores.put(ore.getId(), ore);
        return true;
    }
    public Ore get(String id) {
        return ores.get(id);
    }
    public String[] getOreIds() {
        return ores.keySet().toArray(new String[0]);
    }
    private final HashMap<String, Ore> ores = new HashMap<>();

}
