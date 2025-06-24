package sh.harold.geometryTest.geometry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a live, transformable, and animatable shape instance.
 */
public class ShapeInstance {
    private final String id;
    private String type;
    private Vector position;
    private double scale;
    private Vector rotationVec = new Vector(0, 0, 0); // current rotation (yaw, pitch, roll)
    private Vector spinVelocity = new Vector(0, 0, 0); // radians per tick (yaw, pitch, roll)
    private List<Vector> basePoints;
    private List<Vector> morphTargetPoints;
    private double morphProgress; // 0.0 to 1.0
    private final JavaPlugin plugin;
    private String colour = "RED";
    private boolean rainbow = false;
    private float rainbowHue = 0f;
    private org.bukkit.Particle particle = org.bukkit.Particle.DUST;
    private World world;

    public ShapeInstance(String id, String type, Vector position, double scale, double rotation, int resolution, JavaPlugin plugin, World world) {
        this.id = id;
        this.type = type;
        this.position = position;
        this.scale = scale;
        this.rotationVec = new Vector(0, rotation, 0);
        this.plugin = plugin;
        this.basePoints = GeometryFactory.createShape(type, scale, rotation, resolution);
        this.morphTargetPoints = null;
        this.morphProgress = 0.0;
        this.world = world;
    }

    public void tick() {
        // Apply spinning
        if (!spinVelocity.equals(new Vector(0, 0, 0))) {
            rotationVec.add(spinVelocity);
        }
        // Morphing
        if (morphTargetPoints != null && morphProgress < 1.0) {
            morphProgress = Math.min(1.0, morphProgress + 0.02); // 50 ticks
        }
        // Update points
        List<Vector> displayPoints = getCurrentPoints();
        // Render on main thread
        Bukkit.getScheduler().runTask(plugin, () -> render(displayPoints));
    }

    private List<Vector> getCurrentPoints() {
        List<Vector> from = basePoints;
        if (morphTargetPoints == null || morphProgress == 0.0) {
            return transformPoints(from);
        }
        List<Vector> to = morphTargetPoints;
        List<Vector> morphed = new ArrayList<>(from.size());
        for (int i = 0; i < from.size(); i++) {
            Vector a = from.get(i);
            Vector b = to.get(i);
            Vector interp = a.clone().multiply(1 - morphProgress).add(b.clone().multiply(morphProgress));
            morphed.add(interp);
        }
        return transformPoints(morphed);
    }

    private List<Vector> transformPoints(List<Vector> points) {
        List<Vector> out = new ArrayList<>(points.size());
        for (Vector v : points) {
            Vector scaled = v.clone().multiply(scale);
            Vector rotated = sh.harold.geometryTest.utils.Transform3D.rotate(scaled, rotationVec.getY(), rotationVec.getX(), rotationVec.getZ());
            Vector translated = rotated.clone().add(position);
            out.add(translated);
        }
        return out;
    }

    private void render(List<Vector> points) {
        if (world == null) return;
        Location loc = new Location(world, 0, 0, 0);
        org.bukkit.Color dustColor = getCurrentColor();
        Particle.DustOptions dust = new Particle.DustOptions(dustColor, 1);
        for (Vector point : points) {
            loc.setX(point.getX());
            loc.setY(point.getY());
            loc.setZ(point.getZ());
            if (particle == Particle.DUST) {
                loc.getWorld().spawnParticle(Particle.DUST, loc, 1, dust);
            } else {
                loc.getWorld().spawnParticle(particle, loc, 1);
            }
        }
        if (rainbow) {
            rainbowHue += 0.01f;
            if (rainbowHue > 1f) rainbowHue = 0f;
        }
    }

    private org.bukkit.Color getCurrentColor() {
        if (rainbow) {
            java.awt.Color awtColor = java.awt.Color.getHSBColor(rainbowHue, 1f, 1f);
            return org.bukkit.Color.fromRGB(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue());
        }
        String c = colour.toUpperCase();
        try {
            if (c.startsWith("#")) {
                int rgb = Integer.parseInt(c.substring(1), 16);
                return org.bukkit.Color.fromRGB((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
            }
            // Enum support
            return switch (c) {
                case "RED" -> org.bukkit.Color.RED;
                case "DARK_RED" -> org.bukkit.Color.fromRGB(170, 0, 0);
                case "GREEN" -> org.bukkit.Color.LIME;
                case "DARK_GREEN" -> org.bukkit.Color.fromRGB(0, 85, 0);
                case "BLUE" -> org.bukkit.Color.BLUE;
                case "DARK_BLUE" -> org.bukkit.Color.fromRGB(0, 0, 170);
                case "YELLOW" -> org.bukkit.Color.YELLOW;
                case "ORANGE" -> org.bukkit.Color.fromRGB(255, 140, 0);
                case "PURPLE" -> org.bukkit.Color.fromRGB(128, 0, 128);
                case "CYAN" -> org.bukkit.Color.AQUA;
                case "WHITE" -> org.bukkit.Color.WHITE;
                case "BLACK" -> org.bukkit.Color.BLACK;
                case "GRAY" -> org.bukkit.Color.GRAY;
                case "DARK_GRAY" -> org.bukkit.Color.fromRGB(64, 64, 64);
                default -> org.bukkit.Color.RED;
            };
        } catch (Exception e) {
            return org.bukkit.Color.RED;
        }
    }

    // --- API ---
    public String getId() { return id; }
    public void setSpinVelocity(Vector velocity) {
        this.spinVelocity = velocity;
    }
    public Vector getSpinVelocity() {
        return spinVelocity;
    }
    public void setRotationVec(Vector rotation) {
        this.rotationVec = rotation;
    }
    public Vector getRotationVec() {
        return rotationVec;
    }
    public void morphTo(List<Vector> targetPoints) {
        this.morphTargetPoints = targetPoints;
        this.morphProgress = 0.0;
    }
    public void setPosition(Vector pos) { this.position = pos; }
    public void setScale(double scale) { this.scale = scale; }
    public double getRotation() { return rotationVec.getY(); }
    public Vector getPosition() { return position; }
    public double getScale() { return scale; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public void setBasePoints(List<Vector> points) { this.basePoints = points; }
    public List<Vector> getBasePoints() { return basePoints; }
    public void setColour(String colour) {
        this.colour = colour;
    }
    public String getColour() {
        return colour;
    }
    public void setRainbow(boolean rainbow) {
        this.rainbow = rainbow;
        this.rainbowHue = 0f;
    }
    public boolean isRainbow() {
        return rainbow;
    }
    public void setParticle(org.bukkit.Particle particle) {
        this.particle = particle;
    }
    public org.bukkit.Particle getParticle() {
        return particle;
    }
    public boolean canColour() {
        return particle == org.bukkit.Particle.DUST;
    }
    public World getWorld() { return world; }
    public void setWorld(World world) { this.world = world; }
}
