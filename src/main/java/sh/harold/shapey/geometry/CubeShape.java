package sh.harold.shapey.geometry;

import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CubeShape {

    public static List<Vector> generate(double scale, double rotation, int resolution) {
        List<Vector> points = new ArrayList<>();

        // Generate cube edges
        Vector[] vertices = getVectors(scale);

        int[][] edges = {
                {0, 1}, {1, 2}, {2, 3}, {3, 0},
                {4, 5}, {5, 6}, {6, 7}, {7, 4},
                {0, 4}, {1, 5}, {2, 6}, {3, 7}
        };

        for (int[] edge : edges) {
            Vector start = vertices[edge[0]];
            Vector end = vertices[edge[1]];

            for (int i = 0; i < resolution; i++) {
                double t = (double) i / (resolution - 1);
                points.add(start.clone().multiply(1 - t).add(end.clone().multiply(t)));
            }
        }

        return points;
    }

    private static Vector @NotNull [] getVectors(double scale) {
        double halfScale = scale / 2;
        return new Vector[]{
                new Vector(-halfScale, -halfScale, -halfScale),
                new Vector(halfScale, -halfScale, -halfScale),
                new Vector(halfScale, halfScale, -halfScale),
                new Vector(-halfScale, halfScale, -halfScale),
                new Vector(-halfScale, -halfScale, halfScale),
                new Vector(halfScale, -halfScale, halfScale),
                new Vector(halfScale, halfScale, halfScale),
                new Vector(-halfScale, halfScale, halfScale)
        };
    }
}
