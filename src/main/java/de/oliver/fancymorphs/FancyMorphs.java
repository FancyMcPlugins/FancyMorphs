package de.oliver.fancymorphs;

import de.oliver.fancylib.FancyLib;
import de.oliver.fancymorphs.commands.FancyMorphsCMD;
import de.oliver.fancymorphs.commands.MorphCMD;
import org.bukkit.plugin.java.JavaPlugin;

public class FancyMorphs extends JavaPlugin {

    private static FancyMorphs INSTANCE;

    public FancyMorphs() {
        INSTANCE = this;
    }

    public static FancyMorphs getInstance() {
        return INSTANCE;
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        FancyLib.setPlugin(INSTANCE);

        getCommand("FancyMorphs").setExecutor(new FancyMorphsCMD());
        getCommand("Morph").setExecutor(new MorphCMD());
    }

    @Override
    public void onDisable() {

    }

}
