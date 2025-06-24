package sh.harold.shapey.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import sh.harold.shapey.geometry.ShapeManager;

import java.util.Collections;
import java.util.List;

public class DeleteShapeCommand implements TabExecutor {
    public static void register(JavaPlugin plugin) {
        PluginCommand command = plugin.getCommand("deleteshape");
        if (command != null) {
            command.setExecutor(new DeleteShapeCommand());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§eUsage: §a/deleteshape <id>");
            return false;
        }
        String id = args[0];
        String fullId = ShapeManager.getInstanceIds().stream().filter(i -> i.startsWith(id)).findFirst().orElse(null);
        if (fullId == null) {
            sender.sendMessage("§eShape with ID §a" + id + " §enot found.");
            return false;
        }
        ShapeManager.removeInstance(fullId);
        sender.sendMessage("§eShape §a" + id + " §edeleted.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return ShapeManager.getInstanceIds().stream().map(i -> i.substring(0, 4)).toList();
        }
        return Collections.emptyList();
    }
}
