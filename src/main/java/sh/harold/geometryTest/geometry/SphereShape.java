package sh.harold.geometryTest.geometry;

import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class SphereShape {

    public static List<Vector> generate(double scale, double rotation, int resolution) {
        List<Vector> points = new ArrayList<>();

        for (int i = 0; i < resolution; i++) {
            double theta = Math.PI * i / resolution;
            for (int j = 0; j < resolution; j++) {
                double phi = 2 * Math.PI * j / resolution;

                double x = scale * Math.sin(theta) * Math.cos(phi);
                double y = scale * Math.cos(theta);
                double z = scale * Math.sin(theta) * Math.sin(phi);

                points.add(new Vector(x, y, z));
            }
        }

        return points;
    }
}
