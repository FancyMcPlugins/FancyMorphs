package de.oliver.fancymorphs;

import de.oliver.fancylib.FancyLib;
import de.oliver.fancylib.serverSoftware.ServerSoftware;
import de.oliver.fancylib.serverSoftware.schedulers.BukkitScheduler;
import de.oliver.fancylib.serverSoftware.schedulers.FancyScheduler;
import de.oliver.fancylib.serverSoftware.schedulers.FoliaScheduler;
import de.oliver.fancymorphs.commands.FancyMorphsCMD;
import de.oliver.fancymorphs.commands.MorphCMD;
import de.oliver.fancymorphs.listeners.PlayerMoveListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class FancyMorphs extends JavaPlugin {

    private static FancyMorphs INSTANCE;

    private final FancyScheduler scheduler;
    private final MorphManager morphManager;

    public FancyMorphs() {
        INSTANCE = this;
        scheduler = ServerSoftware.isBukkit() ? new BukkitScheduler(INSTANCE) : new FoliaScheduler(INSTANCE);
        morphManager = new MorphManager();
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

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerMoveListener(), INSTANCE);
    }

    @Override
    public void onDisable() {

    }

    public FancyScheduler getScheduler() {
        return scheduler;
    }

    public MorphManager getMorphManager() {
        return morphManager;
    }
}
