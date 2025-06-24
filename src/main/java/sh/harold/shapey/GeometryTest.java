package sh.harold.shapey;

import org.bukkit.plugin.java.JavaPlugin;
import sh.harold.shapey.commands.RenderShapeCommand;
import sh.harold.shapey.commands.DeleteShapeCommand;
import sh.harold.shapey.commands.SpinShapeCommand;
import sh.harold.shapey.commands.TransformShapeCommand;
import sh.harold.shapey.commands.ColourShapeCommand;
import sh.harold.shapey.commands.ParticleShapeCommand;
import sh.harold.shapey.geometry.ShapeManager;

public final class GeometryTest extends JavaPlugin {
    @Override
    public void onEnable() {
        ShapeManager.initialize(this);
        RenderShapeCommand.register(this);
        DeleteShapeCommand.register(this);
        SpinShapeCommand.register(this);
        TransformShapeCommand.register(this);
        ColourShapeCommand.register(this);
        ParticleShapeCommand.register(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
