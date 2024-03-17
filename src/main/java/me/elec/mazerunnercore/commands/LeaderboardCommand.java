package me.elec.mazerunnercore.commands;

import me.elec.mazerunnercore.LeaderboardManager;
import me.elec.mazerunnercore.MazeRunnerCore;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

import java.util.*;

public class LeaderboardCommand implements CommandExecutor, TabCompleter {

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

        if (args.length < 2) {
            player.sendMessage(plugin.getGradientPrefix() + "§cUsage: /leaderboard <Maze> <Difficulty>");
            return true;
        }

        String mazeName = args[0];
        String difficulty = args[1];

        if (!isValidMazeName(mazeName) || !isValidDifficulty(difficulty)) {
            player.sendMessage(plugin.getGradientPrefix() + "§cInvalid Maze or Difficulty. Supported maze names: Jungle, Nether, Spooky, Desert, Mangrove, and Ice. Supported difficulties: Easy, Moderate, Hard, and Adventure.");
            return true;
        }

        try {
            // Retrieve leaderboard data from the manager for the specified maze
            List<Map<?, ?>> leaderboard = leaderboardManager.getLeaderboard(mazeName + difficulty);

            // Display the leaderboard to the player (e.g., send it as a chat message)
            player.sendMessage( "§e=== " + mazeName + " " + difficulty +  " Maze Leaderboard ===");
            if (leaderboard.isEmpty()) {
                player.sendMessage( "§eNo data available.");
            } else {
                int rank = 1;

                for (Map<?, ?> entry : leaderboard) {
                    if (rank <= 100) {
                        String playerName = (String) entry.get("player");
                        double time = (double) entry.get("time");
                        player.sendMessage(ChatColor.GRAY + "#" + rank + ": " + ChatColor.GREEN + playerName + ChatColor.GRAY + " - " + formatTime(time));
                    }
                    rank++;
                }
            }
        } catch (Exception e) {
            // Log any exceptions for debugging purposes
            e.printStackTrace();
            player.sendMessage(plugin.getGradientPrefix() +  "§cAn error occurred while processing the command. Please make a bug report in our discord server.");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            // Provide suggestions for maze names
            completions.addAll(getMatchingMazeNames(args[0]));
        } else if (args.length == 2) {
            // Provide suggestions for difficulties
            completions.addAll(getMatchingDifficulties(args[1]));
        }

        return completions;
    }

    private List<String> getMatchingMazeNames(String partial) {
        List<String> mazeNames = Arrays.asList("Jungle", "Nether", "Spooky", "Desert", "Mangrove", "Ice");
        List<String> matchingNames = new ArrayList<>();

        for (String mazeName : mazeNames) {
            if (mazeName.toLowerCase().startsWith(partial.toLowerCase())) {
                matchingNames.add(mazeName);
            }
        }

        return matchingNames;
    }

    private List<String> getMatchingDifficulties(String partial) {
        List<String> difficulties = Arrays.asList("Easy", "Moderate", "Hard", "Adventure");
        List<String> matchingDifficulties = new ArrayList<>();

        for (String difficulty : difficulties) {
            if (difficulty.toLowerCase().startsWith(partial.toLowerCase())) {
                matchingDifficulties.add(difficulty);
            }
        }

        return matchingDifficulties;
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

    private boolean isValidMazeName(String mazeName) {
        Set<String> validMazeNames = new HashSet<>(Set.of("Jungle", "Nether", "Spooky", "Desert", "Mangrove", "Ice"));
        return validMazeNames.contains(mazeName);
    }

    private boolean isValidDifficulty(String difficulty) {
        Set<String> validDifficulties = new HashSet<>(Set.of("Easy", "Moderate", "Hard", "Adventure"));
        return validDifficulties.contains(difficulty);
    }
}