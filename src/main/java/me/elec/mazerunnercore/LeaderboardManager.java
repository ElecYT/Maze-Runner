package me.elec.mazerunnercore;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaderboardManager {

    private final MazeRunnerCore plugin;
    private FileConfiguration leaderboardConfig;
    private Map<String, File> mazeDataFiles = new HashMap<>();

    public LeaderboardManager(MazeRunnerCore plugin) {
        this.plugin = plugin;
        loadConfig();
        loadMazeDataFiles();
    }

    public void loadConfig() {
        plugin.saveDefaultConfig(); // Copies the default config from resources if not present
        leaderboardConfig = plugin.getConfig();

        // Check if the leaderboard section exists
        if (!leaderboardConfig.contains("leaderboards")) {
            // Create the section if it doesn't exist
            leaderboardConfig.createSection("leaderboards");
            plugin.saveConfig();
        }
    }

    private void loadMazeDataFiles() {
        // Specify the maze names you want to support
        String[] mazeNames = {"Nether", "Ice", "Jungle", "Desert"};

        for (String mazeName : mazeNames) {
            File mazeDataFile = new File(plugin.getDataFolder(), "leaderboard_" + mazeName + ".yml");
            if (!mazeDataFile.exists()) {
                plugin.saveResource("leaderboard_" + mazeName + ".yml", false);
            }
            mazeDataFiles.put(mazeName, mazeDataFile);
        }
    }

    private FileConfiguration getMazeDataFile(String mazeName) {
        return YamlConfiguration.loadConfiguration(mazeDataFiles.get(mazeName));
    }

    public void addPlayerTime(String playerName, double time, String mazeName) {
        FileConfiguration mazeDataConfig = getMazeDataFile(mazeName);
        ConfigurationSection leaderboardSection = mazeDataConfig.getConfigurationSection("leaderboards");


        if (leaderboardSection == null) {
            leaderboardSection = mazeDataConfig.createSection("leaderboards");
        }

        List<Map<?, ?>> leaderboardList = leaderboardSection.getMapList("leaderboards");

        // Add the player and time to the leaderboard
        Map<String, Object> entry = new HashMap<>();
        entry.put("player", playerName);
        entry.put("time", time);
        leaderboardList.add(entry);

        // Sort the leaderboard based on time (ascending order)
        leaderboardList.sort((o1, o2) -> Double.compare((double) o1.get("time"), (double) o2.get("time")));

        // Update the configuration
        leaderboardSection.set("leaderboards", leaderboardList);

        // Save the configuration to the file
        try {
            mazeDataConfig.save(mazeDataFiles.get(mazeName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Map<?, ?>> getLeaderboard(String mazeName) {
        FileConfiguration mazeDataConfig = getMazeDataFile(mazeName);
        ConfigurationSection leaderboardSection = mazeDataConfig.getConfigurationSection("leaderboards");

        if (leaderboardSection == null) {
            leaderboardSection = mazeDataConfig.createSection("leaderboards");
            mazeDataConfig.set("leaderboards", new ArrayList<>());
        }

        return leaderboardSection.getMapList("leaderboards");
    }

    // Your other methods and properties
}


