import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class JsonParser {
    static public void generateJson(OreRegistry oreRegistry) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonArray oreArray = new JsonArray();
        for (String id : oreRegistry.getOreIds()) {
            JsonObject oreEntry = new JsonObject();
            oreRegistry.get(id).setMinMaxHeight();
            oreEntry.addProperty("type", "jeresources:ore");
            oreEntry.addProperty("block", id);
            oreEntry.addProperty("minY", oreRegistry.get(id).getMinHeight());
            oreEntry.addProperty("maxY", oreRegistry.get(id).getMaxHeight());
            //oreEntry.addProperty("count", 10);
            oreEntry.addProperty("scrapeClusters", true);
            oreArray.add(oreEntry);
        }
        System.out.println(gson.toJson(oreArray));
    }

}
