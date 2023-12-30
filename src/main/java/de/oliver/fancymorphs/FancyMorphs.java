package de.oliver.fancymorphs;

import de.oliver.fancylib.FancyLib;
import de.oliver.fancylib.Metrics;
import de.oliver.fancylib.VersionConfig;
import de.oliver.fancylib.serverSoftware.ServerSoftware;
import de.oliver.fancylib.serverSoftware.schedulers.BukkitScheduler;
import de.oliver.fancylib.serverSoftware.schedulers.FancyScheduler;
import de.oliver.fancylib.serverSoftware.schedulers.FoliaScheduler;
import de.oliver.fancylib.versionFetcher.MasterVersionFetcher;
import de.oliver.fancylib.versionFetcher.VersionFetcher;
import de.oliver.fancymorphs.commands.FancyMorphsCMD;
import de.oliver.fancymorphs.commands.MorphCMD;
import de.oliver.fancymorphs.commands.UnmorphCMD;
import de.oliver.fancymorphs.listeners.EntityDamageListener;
import de.oliver.fancymorphs.listeners.PlayerJoinQuitListener;
import de.oliver.fancymorphs.listeners.PlayerMoveListener;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class FancyMorphs extends JavaPlugin {

    private static FancyMorphs INSTANCE;

    private final VersionFetcher versionFetcher = new MasterVersionFetcher("FancyMorphs");
    private final VersionConfig versionConfig = new VersionConfig(this, versionFetcher);
    private final FancyScheduler scheduler;
    private final MorphManager morphManager;

    public FancyMorphs() {
        INSTANCE = this;
        scheduler = ServerSoftware.isFolia() ? new FoliaScheduler(INSTANCE) : new BukkitScheduler(INSTANCE);
        morphManager = new MorphManager();
    }

    public static FancyMorphs getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        FancyLib.setPlugin(INSTANCE);

        Metrics metrics = new Metrics(INSTANCE, 20323);

        getCommand("FancyMorphs").setExecutor(new FancyMorphsCMD());
        getCommand("Morph").setExecutor(new MorphCMD());
        getCommand("Unmorph").setExecutor(new UnmorphCMD());

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerMoveListener(), INSTANCE);
        pluginManager.registerEvents(new PlayerJoinQuitListener(), INSTANCE);
        pluginManager.registerEvents(new EntityDamageListener(), INSTANCE);

        checkForNewerVersion();
    }

    @Override
    public void onDisable() {

    }

    private void checkForNewerVersion() {
        versionConfig.load();

        final var current = new ComparableVersion(versionConfig.getVersion());

        supplyAsync(getVersionFetcher()::fetchNewestVersion).thenApply(Objects::requireNonNull).whenComplete((newest, error) -> {
            if (error != null || newest.compareTo(current) <= 0) {
                return; // could not get the newest version or already on latest
            }

            getLogger().warning("""
                                                            
                    -------------------------------------------------------
                    You are not using the latest version the FancyMorphs plugin.
                    Please update to the newest version (%s).
                    %s
                    -------------------------------------------------------
                    """.formatted(newest, getVersionFetcher().getDownloadUrl()));
        });
    }

    public VersionConfig getVersionConfig() {
        return versionConfig;
    }

    public VersionFetcher getVersionFetcher() {
        return versionFetcher;
    }

    public FancyScheduler getScheduler() {
        return scheduler;
    }

    public MorphManager getMorphManager() {
        return morphManager;
    }
}
