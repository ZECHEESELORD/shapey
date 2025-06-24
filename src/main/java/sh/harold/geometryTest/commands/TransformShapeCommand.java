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

public class TransformShapeCommand implements TabExecutor {
    public static void register(JavaPlugin plugin) {
        PluginCommand command = plugin.getCommand("transformtheshape");
        if (command != null) {
            command.setExecutor(new TransformShapeCommand());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§eUsage: §a/transformtheshape <id> <targetShape>");
            return true;
        }
        String id = args[0];
        String targetShape = args[1];
        ShapeInstance instance = ShapeManager.getInstance(id);
        if (instance == null) {
            sender.sendMessage("§eShape with ID §a" + id + " §enot found.");
            return true;
        }
        boolean ok = ShapeManager.morphInstance(id, targetShape, instance.getScale(), instance.getBasePoints().size());
        if (!ok) {
            sender.sendMessage("§eMorph failed.");
            return true;
        }
        sender.sendMessage("§eShape §a" + id + " §emorphing to §a" + targetShape + "§e.");
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
