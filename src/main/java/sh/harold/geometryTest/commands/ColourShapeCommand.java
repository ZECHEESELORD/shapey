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

public class ColourShapeCommand implements TabExecutor {
    public static void register(JavaPlugin plugin) {
        PluginCommand command = plugin.getCommand("colourmyshape");
        if (command != null) {
            command.setExecutor(new ColourShapeCommand());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§eUsage: §a/colourmyshape <id> <colour|#hex|rainbow>");
            return false;
        }
        String id = args[0];
        String colour = args[1];
        ShapeInstance instance = ShapeManager.getInstance(id);
        if (instance == null) {
            sender.sendMessage("§eShape with ID §a" + id + " §enot found.");
            return false;
        }
        if (colour.equalsIgnoreCase("rainbow")) {
            if (!instance.canColour()) {
                sender.sendMessage("§eThis shape's particle type cannot be coloured.");
                return true;
            }
            instance.setRainbow(true);
            sender.sendMessage("§eShape §a" + id + " §eis now in §arainbow §emode.");
        } else {
            if (!instance.canColour()) {
                sender.sendMessage("§eThis shape's particle type cannot be coloured.");
                return true;
            }
            instance.setRainbow(false);
            instance.setColour(colour);
            sender.sendMessage("§eShape §a" + id + " §ecolour set to §a" + colour + "§e.");
        }
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
