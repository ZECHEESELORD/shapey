package sh.harold.geometryTest;

import org.bukkit.plugin.java.JavaPlugin;
import sh.harold.geometryTest.commands.RenderShapeCommand;
import sh.harold.geometryTest.commands.DeleteShapeCommand;
import sh.harold.geometryTest.commands.SpinShapeCommand;
import sh.harold.geometryTest.commands.TransformShapeCommand;
import sh.harold.geometryTest.commands.ColourShapeCommand;
import sh.harold.geometryTest.commands.ParticleShapeCommand;
import sh.harold.geometryTest.geometry.ShapeManager;

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
