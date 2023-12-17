package de.oliver.fancymorphs.commands;

import de.oliver.fancylib.MessageHelper;
import de.oliver.fancylib.UUIDFetcher;
import de.oliver.fancymorphs.FancyMorphs;
import de.oliver.fancynpcs.api.Npc;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class UnmorphCMD implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        UUID uuid;
        Player target;

        if (args.length == 0) {
            if (!(sender instanceof Player p)) {
                MessageHelper.error(sender, "/Unmorph <player> - unmorphs a player");
                return false;
            }

            uuid = p.getUniqueId();
            target = p;
        } else {
            uuid = UUIDFetcher.getUUID(args[0]);
            if (uuid == null) {
                MessageHelper.error(sender, "Could not find player: '" + args[0] + "'");
                return false;
            }

            target = Bukkit.getPlayer(uuid);
        }

        Npc morph = FancyMorphs.getInstance().getMorphManager().getNpc(uuid);
        if (morph == null) {
            MessageHelper.warning(sender, "This player is not morphed");
            return false;
        }

        morph.removeForAll();

        FancyMorphs.getInstance().getMorphManager().unregister(uuid);

        if (target != null) {
            target.setInvisible(false);
        }

        MessageHelper.success(sender, "Successfully unmorphed");
        return true;
    }
}
