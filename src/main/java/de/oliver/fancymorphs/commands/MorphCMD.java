package de.oliver.fancymorphs.commands;

import de.oliver.fancylib.MessageHelper;
import de.oliver.fancymorphs.FancyMorphs;
import de.oliver.fancynpcs.api.FancyNpcsPlugin;
import de.oliver.fancynpcs.api.Npc;
import de.oliver.fancynpcs.api.NpcData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class MorphCMD implements CommandExecutor, TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return Stream.concat(
                    Arrays.stream(EntityType.values())
                            .map(e -> e.name().toLowerCase())
                            .filter(type -> sender.hasPermission("fancymorphs.morph." + type.toLowerCase()) || sender.hasPermission("fancymorphs.morph.*")),
                    Stream.of("player", "none")
            ).toList();
        }
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player p)) {
            MessageHelper.error(sender, "Only players can execute this command");
            return false;
        }

        // perhaps unmorph if no arguments are provided (?
        if (args.length == 0) {
            MessageHelper.error(sender, "Usage: /morph <entity-type>");
            return false;
        }

        Npc morph = FancyMorphs.getInstance().getMorphManager().getNpc(p.getUniqueId());

        String entityName = args[0];
        if (entityName.equalsIgnoreCase("none") || entityName.equalsIgnoreCase("player")) {
            if (morph == null) {
                MessageHelper.error(p, "You are already unmorphed");
                return false;
            }

            morph.removeForAll();
            FancyMorphs.getInstance().getMorphManager().unregister(p.getUniqueId());

            p.setInvisible(false);

            MessageHelper.success(p, "Successfully unmorphed");
            return true;
        }


        final EntityType type;
        try {
            type = EntityType.valueOf(entityName.toUpperCase());
        } catch (IllegalArgumentException ignored) {
            MessageHelper.error(sender, "No entity type could be found for name: " + entityName);
            return false;
        }

        if (!(p.hasPermission("fancymorphs.morph." + type.name().toLowerCase()) || p.hasPermission("fancymorphs.morph.*"))) {
            MessageHelper.error(p, "You don't have permission to morph as this entity type.");
            return false;
        }

        p.setInvisible(true);

        NpcData npcData = new NpcData(UUID.randomUUID().toString(), p.getUniqueId(), p.getLocation());
        npcData.setCollidable(false);
        npcData.setDisplayName(p.getName());
        npcData.setType(type);
        Npc npc = FancyNpcsPlugin.get().getNpcAdapter().apply(npcData);
        npc.create();


        if (morph != null) {
            morph.removeForAll();
        }

        FancyMorphs.getInstance().getMorphManager().spawnForEveryoneElse(npc, p.getUniqueId());

        FancyMorphs.getInstance().getMorphManager().register(p.getUniqueId(), npc);

        MessageHelper.success(p, "Successfully morphed");
        return false;
    }
}
