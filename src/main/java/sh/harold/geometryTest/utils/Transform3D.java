package sh.harold.geometryTest.utils;

import org.bukkit.util.Vector;

public class Transform3D {

    public static Vector scale(Vector vector, double scale) {
        return vector.clone().multiply(scale);
    }

    public static Vector rotate(Vector vector, double yaw, double pitch, double roll) {
        // Yaw (Y), Pitch (X), Roll (Z) in radians
        double x = vector.getX();
        double y = vector.getY();
        double z = vector.getZ();
        // Roll (Z)
        double cosR = Math.cos(roll), sinR = Math.sin(roll);
        double x1 = x * cosR - y * sinR;
        double y1 = x * sinR + y * cosR;
        double z1 = z;
        // Pitch (X)
        double cosP = Math.cos(pitch), sinP = Math.sin(pitch);
        double x2 = x1;
        double y2 = y1 * cosP - z1 * sinP;
        double z2 = y1 * sinP + z1 * cosP;
        // Yaw (Y)
        double cosY = Math.cos(yaw), sinY = Math.sin(yaw);
        double x3 = x2 * cosY + z2 * sinY;
        double y3 = y2;
        double z3 = -x2 * sinY + z2 * cosY;
        return new Vector(x3, y3, z3);
    }

    public static Vector translate(Vector vector, Vector translation) {
        return vector.clone().add(translation);
    }
}
