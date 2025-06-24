package sh.harold.shapey.commands;

import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import sh.harold.shapey.geometry.ShapeManager;
import sh.harold.shapey.geometry.ShapeInstance;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ParticleShapeCommand implements TabExecutor {
    public static void register(JavaPlugin plugin) {
        PluginCommand command = plugin.getCommand("particleshapes");
        if (command != null) {
            command.setExecutor(new ParticleShapeCommand());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§eUsage: §a/particleshapes <id> <particle>");
            return false;
        }
        String id = args[0];
        String particleName = args[1].toUpperCase(Locale.ROOT);
        ShapeInstance instance = ShapeManager.getInstance(id);
        if (instance == null) {
            sender.sendMessage("§eShape with ID §a" + id + " §enot found.");
            return false;
        }
        Particle particle;
        try {
            particle = Particle.valueOf(particleName);
        } catch (IllegalArgumentException e) {
            sender.sendMessage("§eInvalid particle type. Use tab completion for valid options.");
            return false;
        }
        instance.setParticle(particle);
        sender.sendMessage("§eShape §a" + id + " §eparticle set to §a" + particle.name() + "§e.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return ShapeManager.getInstanceIds();
        }
        if (args.length == 2) {
            String prefix = args[1].toUpperCase(Locale.ROOT);
            return Arrays.stream(Particle.values())
                    .map(Enum::name)
                    .filter(n -> n.startsWith(prefix))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
