package me.elec.mazerunnercore.commands;

import me.elec.mazerunnercore.LeaderboardManager;
import me.elec.mazerunnercore.MazeRunnerCore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class LeaderboardCommand implements CommandExecutor {

    private final LeaderboardManager leaderboardManager;
    private final MazeRunnerCore plugin;

    public LeaderboardCommand(LeaderboardManager leaderboardManager, MazeRunnerCore plugin) {
        this.leaderboardManager = leaderboardManager;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getGradientPrefix() + "§cThis command can only be executed by a player.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(plugin.getGradientPrefix() + "§cPlease provide a Maze. Maze names are: Jungle, Nether, Spooky, Desert, Mangrove, and Ice.");
            return true;
        }

        if (args.length == 1) {
            player.sendMessage(plugin.getGradientPrefix() + "§cPlease provide a Difficulty. Difficulties are: Easy, Moderate, Hard, and Adventure.");
        }

        String mazeName = args[0];

        String difficulty = args[1];

        // Check if the provided maze name is valid (Nether, Ice, Stone, Sand)
        if (!isValidMazeName(mazeName)) {
            player.sendMessage(plugin.getGradientPrefix() + "§cInvalid maze name. Supported maze names: Jungle, Nether, Spooky, Desert, Mangrove, and Ice.");
            return true;
        }

        if (!isValidDifficulty(difficulty)) {
            player.sendMessage(plugin.getGradientPrefix() + "§cInvalid difficulty. Supported difficulties: Easy, Moderate, Hard, and Adventure.");
            return true;
        }

        try {
            // Retrieve leaderboard data from the manager for the specified maze
            List<Map<?, ?>> leaderboard = leaderboardManager.getLeaderboard(mazeName);

            // Display the leaderboard to the player (e.g., send it as a chat message)
            player.sendMessage(plugin.getGradientPrefix() + "§e=== " + mazeName + " " + difficulty +  " Maze Leaderboard ===");
            if (leaderboard.isEmpty()) {
                player.sendMessage(plugin.getGradientPrefix() + "§eNo data available.");
            } else {
                int rank = 1;
                for (Map<?, ?> entry : leaderboard) {
                    String playerName = (String) entry.get("player");
                    double time = (double) entry.get("time");
                    player.sendMessage(ChatColor.GRAY + "#" + rank + ": " + ChatColor.GREEN + playerName + ChatColor.GRAY + " - " + formatTime(time));
                    rank++;
                }
            }
        } catch (Exception e) {
            // Log any exceptions for debugging purposes
            e.printStackTrace();
            player.sendMessage(plugin.getGradientPrefix() +  "§cAn error occurred while processing the command.");
        }

        return true;
    }

    // Helper method to format time as "00:00.00"
    private String formatTime(double time) {
        // Implement your time formatting logic here
        // Example: Convert seconds to "mm:ss.SS" format
        int minutes = (int) (time / 60);
        int seconds = (int) (time % 60);
        int milliseconds = (int) ((time % 1) * 100);
        return String.format("%02d:%02d.%02d", minutes, seconds, milliseconds);
    }

    // Helper method to check if the provided maze name is valid
    private boolean isValidMazeName(String mazeName) {
        return mazeName.equalsIgnoreCase("Jungle")
                || mazeName.equalsIgnoreCase("Nether")
                || mazeName.equalsIgnoreCase("Spooky")
                || mazeName.equalsIgnoreCase("Desert")
                || mazeName.equalsIgnoreCase("Mangrove")
                || mazeName.equalsIgnoreCase("Ice");
    }

    private boolean isValidDifficulty(String difficulty) {
        return difficulty.equalsIgnoreCase("Easy")
                || difficulty.equalsIgnoreCase("Moderate")
                || difficulty.equalsIgnoreCase("Hard")
                || difficulty.equalsIgnoreCase("Adventure");
    }
}
