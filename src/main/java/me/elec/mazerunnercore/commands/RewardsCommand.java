package me.elec.mazerunnercore.commands;

import me.elec.mazerunnercore.MazeRunnerCore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RewardsCommand implements CommandExecutor {
    private final MazeRunnerCore plugin;

    public RewardsCommand(MazeRunnerCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getGradientPrefix() + "§cThis command can only be executed by a player.");
            return true;
        }

        Player player = (Player) sender;
        plugin.openLevelingRewardsGUI(player);

        return true;
    }
}

