package com.yj0524;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class TitlePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("[Title] Plugin Enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("[Title] Plugin Disabled");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("titles")) {
            if (!sender.isOp()) {
                sender.sendMessage("You must be an operator to use this command.");
                return false;
            }

            if (args.length == 3) {
                String playerName = args[0];
                String title = args[1];
                String subtitle = args[2];

                Player target = getServer().getPlayer(playerName);
                if (target != null) {
                    target.sendTitle(title, subtitle);
                    return true;
                } else {
                    sender.sendMessage("Player not found: " + playerName);
                    return false;
                }
            } else {
                sender.sendMessage("Usage: /titles <player> <title> <subtitle>");
                return false;
            }
        }
        return false;
    }
}
