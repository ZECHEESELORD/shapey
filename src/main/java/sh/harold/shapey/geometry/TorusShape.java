package sh.harold.shapey.geometry;

import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class TorusShape {

    public static List<Vector> generate(double scale, double rotation, int resolution) {
        List<Vector> points = new ArrayList<>();

        double R = scale; // Major radius
        double r = scale / 2; // Minor radius

        for (int i = 0; i < resolution; i++) {
            double theta = 2 * Math.PI * i / resolution;
            for (int j = 0; j < resolution; j++) {
                double phi = 2 * Math.PI * j / resolution;

                double x = (R + r * Math.cos(theta)) * Math.cos(phi);
                double y = r * Math.sin(theta);
                double z = (R + r * Math.cos(theta)) * Math.sin(phi);

                points.add(new Vector(x, y, z));
            }
        }

        return points;
    }
}
