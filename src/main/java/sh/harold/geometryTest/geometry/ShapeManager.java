package sh.harold.geometryTest.geometry;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import org.bukkit.scheduler.BukkitRunnable;

public class ShapeManager {
    private static final Map<String, ShapeInstance> instances = new ConcurrentHashMap<>();
    private static JavaPlugin plugin;

    private static final String[] CHEESE_WORDS = {
        "cheddar", "brie", "gouda", "swiss", "feta", "mozzarella", "parmesan", "camembert", "stilton", "provolone", "havarti", "colby", "gruyere", "edam", "roquefort", "pecorino", "manchego", "asiago", "ricotta", "emmental"
    };
    private static final java.util.Random RANDOM = new java.util.Random();
    private static String generateCheeseId() {
        String w1 = CHEESE_WORDS[RANDOM.nextInt(CHEESE_WORDS.length)];
        String w2 = CHEESE_WORDS[RANDOM.nextInt(CHEESE_WORDS.length)];
        int num = RANDOM.nextInt(100);
        return w1 + w2 + num;
    }

    public static void initialize(JavaPlugin pl) {
        plugin = pl;
        new BukkitRunnable() {
            @Override
            public void run() {
                for (ShapeInstance instance : instances.values()) {
                    instance.tick();
                }
            }
        }.runTaskTimer(plugin, 1L, 1L);
    }

    public static String createInstance(String type, double scale, double rotation, Vector position, int resolution, org.bukkit.World world) {
        String id = generateCheeseId();
        while (instances.containsKey(id)) {
            id = generateCheeseId();
        }
        ShapeInstance instance = new ShapeInstance(id, type, position, scale, rotation, resolution, plugin, world);
        instances.put(id, instance);
        return id;
    }

    public static ShapeInstance getInstance(String id) {
        return instances.get(id);
    }

    public static void removeInstance(String id) {
        instances.remove(id);
    }

    public static List<String> getInstanceIds() {
        return List.copyOf(instances.keySet());
    }

    public static boolean morphInstance(String id, String targetType, double scale, int resolution) {
        ShapeInstance instance = instances.get(id);
        if (instance == null) return false;
        List<Vector> targetPoints = GeometryFactory.createShape(targetType, scale, instance.getRotation(), resolution);
        instance.morphTo(targetPoints);
        return true;
    }
}
