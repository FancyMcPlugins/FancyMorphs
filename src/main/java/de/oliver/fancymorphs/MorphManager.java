package de.oliver.fancymorphs;

import de.oliver.fancynpcs.api.Npc;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MorphManager {

    private final Map<UUID, Npc> morphedPlayers;

    public MorphManager() {
        this.morphedPlayers = new HashMap<>();
    }

    public Npc getNpc(UUID uuid) {
        return morphedPlayers.getOrDefault(uuid, null);
    }

    public void register(UUID uuid, Npc npc) {
        morphedPlayers.put(uuid, npc);
    }

    public void unregister(UUID uuid) {
        morphedPlayers.remove(uuid);
    }

    public Map<UUID, Npc> getMorphedPlayers() {
        return morphedPlayers;
    }

    /**
     * Spawn the npc to everyone except the player
     */
    public void spawnForEveryoneElse(Npc npc, UUID e) {
        FancyMorphs.getInstance().getScheduler().runTaskAsynchronously(() -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getUniqueId().equals(e) && npc.getEyeHeight() > .75f) continue;

                npc.spawn(p);
                npc.spawn(p);
            }
        });
    }
}
