package me.elec.mazerunnercore.commands;

import me.elec.mazerunnercore.MazeRunnerCore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LevelCommand implements CommandExecutor {
    private final MazeRunnerCore plugin;

    public LevelCommand(MazeRunnerCore plugin) {
        this.plugin = plugin;
    }

    @Override
// Example command handler
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage(plugin.getGradientPrefix() + "Your level is: " + player.getLevel());
        }
        return false;
    }
}
