package sh.harold.geometryTest.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import sh.harold.geometryTest.geometry.ShapeManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RenderShapeCommand implements TabExecutor {
    public static void register(JavaPlugin plugin) {
        PluginCommand command = plugin.getCommand("rendershape");
        if (command != null) {
            command.setExecutor(new RenderShapeCommand());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§eOnly players can use this command.");
            return true;
        }
        if (args.length < 7) {
            sender.sendMessage("§eUsage: §a/rendershape <shape> <x> <y> <z> <scale> <rotation> <resolution>");
            return false;
        }
        String shape = args[0];
        Location base = player.getLocation();
        Double x = parseRelative(args[1], base.getX());
        Double y = parseRelative(args[2], base.getY());
        Double z = parseRelative(args[3], base.getZ());
        if (x == null || y == null || z == null) {
            sender.sendMessage("§eInvalid coordinates. Use numbers or §a~§e for relative.");
            return false;
        }
        double scale, rotation;
        int resolution;
        try {
            scale = Double.parseDouble(args[4]);
            rotation = Double.parseDouble(args[5]);
            resolution = Integer.parseInt(args[6]);
        } catch (Exception e) {
            sender.sendMessage("§eInvalid arguments. Usage: §a/rendershape <shape> <x> <y> <z> <scale> <rotation> <resolution>");
            return false;
        }
        // Interpret scale as side length for cube, radius for sphere/torus
        if (shape.equalsIgnoreCase("cube")) {
            // scale is side length, convert to half-extent for geometry
            scale = scale / 2.0;
        }
        Vector position = new Vector(x, y, z);
        String id = ShapeManager.createInstance(shape, scale, rotation, position, resolution, player.getWorld());
        sender.sendMessage("§eShape created with ID: §a" + id);
        return true;
    }

    private Double parseRelative(String arg, double base) {
        if (arg.startsWith("~")) {
            if (arg.length() == 1) return base;
            try {
                return base + Double.parseDouble(arg.substring(1));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        try {
            return Double.parseDouble(arg);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return List.of("cube", "sphere", "torus", "custom");
        }
        if (args.length >= 2 && args.length <= 4) {
            List<String> completions = new ArrayList<>();
            completions.add("~");
            completions.add("~1");
            completions.add("~0.5");
            return completions;
        }
        if (args.length == 5) return List.of("1");
        if (args.length == 6) return List.of("0");
        if (args.length == 7) return List.of("32", "64", "128");
        return Collections.emptyList();
    }
}
