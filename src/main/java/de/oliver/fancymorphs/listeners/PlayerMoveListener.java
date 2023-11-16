package de.oliver.fancymorphs.listeners;

import de.oliver.fancymorphs.FancyMorphs;
import de.oliver.fancymorphs.MorphManager;
import de.oliver.fancynpcs.api.Npc;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerMoveListener implements Listener {

    private static final MorphManager MANAGER = FancyMorphs.getInstance().getMorphManager();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();

        Npc morph = MANAGER.getNpc(p.getUniqueId());
        if (morph != null) {
            morph.getData().setLocation(event.getTo());
            morph.moveForAll();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();

        Npc morph = MANAGER.getNpc(p.getUniqueId());
        if (morph == null) {
            p.setInvisible(false);
        }

        for (UUID uuid : MANAGER.getMorphedPlayers().keySet()) {
            Npc m = MANAGER.getNpc(uuid);
            MANAGER.spawnForEveryoneElse(m, p);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();

        Npc morph = MANAGER.getNpc(p.getUniqueId());
        if (morph != null) {
            morph.removeForAll();
        }
    }

}
