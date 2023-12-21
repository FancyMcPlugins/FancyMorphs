package de.oliver.fancymorphs.listeners;

import de.oliver.fancymorphs.FancyMorphs;
import de.oliver.fancynpcs.api.Npc;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player p)) return;

        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK || event.getCause() == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK || event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
            Npc morph = FancyMorphs.getInstance().getMorphManager().getNpc(p.getUniqueId());
            if (morph != null) {
                morph.removeForAll();
                FancyMorphs.getInstance().getMorphManager().unregister(p.getUniqueId());
                p.setInvisible(false);
            }
        }
    }
}
