/*
 * Copyright (C) 2023-2025 Jyguy
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>. 
 */

package xyz.reknown.fastercrystals.commands.impl;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.BooleanArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.reknown.fastercrystals.FasterCrystals;
import xyz.reknown.fastercrystals.commands.AbstractCommand;
import xyz.reknown.fastercrystals.user.User;

public class FastercrystalsCommand extends AbstractCommand {
    public FastercrystalsCommand() {
        super("fastercrystals");
    }

    @Override
    public void register() {
        new CommandAPICommand(name)
                .withAliases("fastcrystals")
                .withSubcommand(new CommandAPICommand("reload")
                        .withPermission("fastercrystals.reload")
                        .executesPlayer(this::runReload))
                .withArguments(new PlayerArgument("player"), new BooleanArgument("toggle"))
                .withPermission("fastercrystals.toggle")
                .executes(this::run)
                .register();
    }

    @Override
    public void run(CommandSender sender, CommandArguments args) {
        FasterCrystals plugin = JavaPlugin.getPlugin(FasterCrystals.class);
        Player targetPlayer = (Player) args.get(0);
        boolean toggle = (boolean) args.get(1);

        if (targetPlayer == null) {
            sender.sendMessage(Component.text("Player not found: " + targetPlayer.getName(), NamedTextColor.RED));
            return;
        }

        User user = plugin.getUsers().get(targetPlayer);
        if (user == null) {
            sender.sendMessage(Component.text("User not found: " + targetPlayer.getName(), NamedTextColor.RED));
            return;
        }

        boolean currentState = user.isFasterCrystals();
        user.setFasterCrystals(toggle);

        sender.sendMessage(Component.text("Set " + targetPlayer.getName() + "'s FastCrystals state from " + (currentState ? "ON" : "OFF") + " to " + (toggle ? "ON" : "OFF") + ".", NamedTextColor.GREEN));
    }

    public void runReload(Player player, CommandArguments args) {
        FasterCrystals plugin = JavaPlugin.getPlugin(FasterCrystals.class);
        plugin.reloadConfig();
        player.sendMessage(Component.text("Reloaded FasterCrystals config!", NamedTextColor.GREEN));
    }
}
