package sh.harold.geometryTest.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import sh.harold.geometryTest.geometry.ShapeManager;
import sh.harold.geometryTest.geometry.ShapeInstance;

import java.util.Collections;
import java.util.List;

public class SpinShapeCommand implements TabExecutor {
    public static void register(JavaPlugin plugin) {
        PluginCommand command = plugin.getCommand("spinmyshape");
        if (command != null) {
            command.setExecutor(new SpinShapeCommand());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 4) {
            sender.sendMessage("§eUsage: §a/spinmyshape <id> <yaw> <pitch> <roll>");
            return false;
        }
        String id = args[0];
        double yaw, pitch, roll;
        try {
            yaw = Double.parseDouble(args[1]) / 100.0;
            pitch = Double.parseDouble(args[2]) / 100.0;
            roll = Double.parseDouble(args[3]) / 100.0;
        } catch (NumberFormatException e) {
            sender.sendMessage("§eAll velocities must be numbers.");
            return false;
        }
        ShapeInstance instance = ShapeManager.getInstance(id);
        if (instance == null) {
            sender.sendMessage("§eShape with ID §a" + id + " §enot found.");
            return false;
        }
        instance.setSpinVelocity(new org.bukkit.util.Vector(yaw, pitch, roll));
        sender.sendMessage("§eShape §a" + id + " §eis now spinning at velocity §a" + (yaw*100) + ", " + (pitch*100) + ", " + (roll*100) + "§e.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return ShapeManager.getInstanceIds();
        }
        return Collections.emptyList();
    }
}
