import java.util.HashMap;

public class OreRegistry {
    public boolean add(String path) {
        Ore ore = new Ore();
        if (!ore.setPath(path)) {
            return false;
        }
        ore.read();
        ores.put(ore.getId(), ore);
        return true;
    }
    public Ore get(String id) {
        return ores.get(id);
    }
    private final HashMap<String, Ore> ores = new HashMap<>();
}
