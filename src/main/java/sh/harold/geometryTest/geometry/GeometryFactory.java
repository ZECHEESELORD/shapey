package sh.harold.geometryTest.geometry;

import org.bukkit.util.Vector;

import java.util.List;

public class GeometryFactory {

    public static List<Vector> createShape(String shape, double scale, double rotation, int resolution) {
        return switch (shape.toLowerCase()) {
            case "cube" -> CubeShape.generate(scale, rotation, resolution);
            case "sphere" -> SphereShape.generate(scale, rotation, resolution);
            case "torus" -> TorusShape.generate(scale, rotation, resolution);
            // Add other shapes like "custom" here
            default -> throw new IllegalArgumentException("Unknown shape: " + shape);
        };
    }
}
