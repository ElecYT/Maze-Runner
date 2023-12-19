package me.elec.mazerunnercore;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

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
        String[] mazeNames = {"Jungle", "Nether", "Spooky", "Desert", "Mangrove", "Ice"};
        String[] difficulties = {"Easy", "Moderate", "Hard", "Adventure"};

        for (String mazeName : mazeNames) {
            for (String difficulty : difficulties) {
                String fileName = "leaderboard_" + mazeName + difficulty + ".yml";

                // Use the class loader to load the resource
                try (InputStream resourceStream = plugin.getResource(fileName)) {
                    if (resourceStream != null) {
                        // Save the resource to the plugin's data folder if it doesn't exist
                        File mazeDataFile = new File(plugin.getDataFolder(), fileName);

                        if (!mazeDataFile.exists()) {
                            Files.copy(resourceStream, mazeDataFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        }

                        mazeDataFiles.put(mazeName + difficulty, mazeDataFile);
                    } else {
                        // Log a warning if the resource is not found
                        plugin.getLogger().warning("Could not find resource: " + fileName);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private FileConfiguration getMazeDataFile(String mazeName) {
        File mazeDataFile = mazeDataFiles.get(mazeName);

        if (mazeDataFile == null || !mazeDataFile.exists()) {
            throw new IllegalArgumentException("Invalid maze name: " + mazeName);
        }
        return YamlConfiguration.loadConfiguration(mazeDataFile);
    }

    public void addPlayerTime(String playerName, double time, String mazeName) {
        FileConfiguration mazeDataConfig = getMazeDataFile(mazeName);
        ConfigurationSection leaderboardSection = mazeDataConfig.getConfigurationSection("leaderboards");

        if (leaderboardSection == null) {
            leaderboardSection = mazeDataConfig.createSection("leaderboards");
        }

        List<Map<?, ?>> leaderboardList = leaderboardSection.getMapList("leaderboards");

        // Add the player and time to the leaderboard
        Map<String, Object> entry = new LinkedHashMap<>();
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


