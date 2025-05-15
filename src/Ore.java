import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ore {
    public HashMap<Integer, Integer> getGenData() {
        return this.genData;
    }
    public HashMap<Integer, Double> getGenPercentages() {
        if (genData != null) {
            this.genPercentages = new HashMap<>();
            this.calculatePercentages(this.genData);
        }
        return this.genPercentages;
    }
    public String getId() {
        return this.id;
    }
    /* set the path of the file containing the ore data */
    public boolean setPath(String path) {
        String default_path = System.getProperty("user.home") + "/.minecraft/config/tellme/";
        File file = new File(path);
        if (!file.exists()) {
            path = default_path + path;
            file = new File(path);
            if (!file.exists()) {
                return false;
            }
        }
        this.rawDataPath = path;
        return true;
    }
    /* creates the hashmap, key is the y-coordinate and value is the amount of block on that height */
    public void read() {
        if (this.rawDataPath == null) {
            return;
        }
        HashMap<Integer, Integer> yCoordinatesMap = new HashMap<>(); /* creates the hashmap for saving the key and value pairs */
        List<String> lines;
        try {
        lines = Files.readAllLines(Paths.get(rawDataPath));
        } catch (IOException e) {
            System.out.println("Error reading file");
            return;
        } /* for saving the lines */
        if (this.id == null) {
            if (lines.size() > 3) {
                this.id = extractId(lines.get(3));
            }
        }
        for (String line : lines) {
            int yCoordinate = extractY(line); /* saves y coordinate from the current line */
            if (yCoordinate == -1) {
                continue;
            }
            int currentCount = yCoordinatesMap.getOrDefault(yCoordinate, 0); /* gets existing count from the hash map at current y coordinate */
            yCoordinatesMap.put(yCoordinate, currentCount+1); /* increments y coordinate in the hash map at current coordinate (as another block at this coordinate has been found) */
        }
        this.genData = yCoordinatesMap;
    }
    private void calculatePercentages(HashMap<Integer, Integer> genDataL) {
        double percentage;
        int total_amount = 0;
        for (int j = 0; j < 256; j++) {
            total_amount = total_amount + genDataL.getOrDefault(j, 0);
        }
        if (total_amount == 0) {
            return;
        }
        for (int i = 0; i < 256; i++) {
            percentage = ((double) genDataL.getOrDefault(i, 0) /total_amount) * 100;
            this.genPercentages.put(i, percentage);
        }
    }
    /* extract the block id from a single line (add later) */
    private String extractId(String line) {
        Pattern p = Pattern.compile("\\|\\s*([^|]+?)\\s*\\|");
        Matcher m = p.matcher(line);
        if (m.find()) {
            return m.group(1);
        }
        return null;

    }
    /* extract the y coordinate from a single line */
    private int extractY(String line) {
        Pattern p = Pattern.compile("y\\s*=\\s*(\\d+)"); /* pattern for getting the y coordinate */
        Matcher m = p.matcher(line);
        if (m.find()) {
            return Integer.parseInt(m.group(1)); /* returns the y coordinate if the pattern is found */
        }
        return -1; /* if the pattern isn't found, returns -1 (real y coordinate will never be negative) */
    }
    private String rawDataPath = null;
    private String id = null;
    private HashMap<Integer, Integer> genData = null;
    private HashMap<Integer, Double> genPercentages = null;
}
