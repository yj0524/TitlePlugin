package com.yj0524;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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

                List<Entity> targets = new ArrayList<>();
                switch (playerName) {
                    case "@a":
                        targets = Bukkit.getOnlinePlayers().stream().collect(Collectors.toList());
                        break;
                    case "@e":
                        targets = Bukkit.getWorlds().stream().flatMap(w -> w.getEntities().stream()).collect(Collectors.toList());
                        break;
                    case "@p":
                        targets.add((Entity) sender);
                        break;
                    case "@r":
                        List<Player> players = Bukkit.getOnlinePlayers().stream().collect(Collectors.toList());
                        targets.add(players.get(new Random().nextInt(players.size())));
                        break;
                    case "@s":
                        if (sender instanceof Entity) {
                            targets.add((Entity) sender);
                        } else {
                            sender.sendMessage("The sender is not an entity.");
                            return false;
                        }
                        break;
                    default:
                        if (playerName.startsWith("@a[tag=")) {
                            String tag = playerName.substring(7, playerName.length() - 1);
                            targets = Bukkit.getOnlinePlayers().stream()
                                    .filter(p -> p.getScoreboardTags().contains(tag))
                                    .collect(Collectors.toList());
                            if (targets.isEmpty()) {
                                sender.sendMessage("No player found with the tag " + tag);
                                return false;
                            }
                        } else if (playerName.startsWith("@e[tag=")) {
                            String tag = playerName.substring(7, playerName.length() - 1);
                            targets = Bukkit.getWorlds().stream()
                                    .flatMap(w -> w.getEntities().stream())
                                    .filter(e -> e.getScoreboardTags().contains(tag))
                                    .collect(Collectors.toList());
                            if (targets.isEmpty()) {
                                sender.sendMessage("No entity found with the tag " + tag);
                                return false;
                            }
                        } else {
                            Player target = Bukkit.getPlayer(playerName);
                            if (target != null) {
                                targets.add(target);
                            } else {
                                sender.sendMessage("Player not found: " + playerName);
                                return false;
                            }
                        }
                }

                for (Entity target : targets) {
                    if (target instanceof Player) {
                        ((Player) target).sendTitle(title, subtitle);
                    }
                }
                return true;
            } else {
                sender.sendMessage("Usage: /titles <player> <title> <subtitle>");
                return false;
            }
        }
        return false;
    }
}
