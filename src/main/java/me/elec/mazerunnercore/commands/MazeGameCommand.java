package me.elec.mazerunnercore.commands;

import me.elec.mazerunnercore.MazeRunnerCore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MazeGameCommand implements CommandExecutor {

    private final MazeRunnerCore plugin;

    public MazeGameCommand(MazeRunnerCore plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getGradientPrefix() + "§cOnly players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        // Check if the player is in a game
        if (plugin.isPlayerInGame(player.getUniqueId())) {
            player.sendMessage(plugin.getGradientPrefix() + ChatColor.RED + "You are already in a game. To leave the game, type /leave");
            return true; // Exit the command
        }

        // Open the Maze Game GUI
        plugin.openMazeGameGUI(player);

        return true;
    }
}

