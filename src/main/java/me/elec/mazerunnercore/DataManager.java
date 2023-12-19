package me.elec.mazerunnercore;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class DataManager {

    private final File dataFile;
    private final FileConfiguration dataConfig;
    private final MazeRunnerCore plugin;
    private final AutoReconnectManager autoReconnectManager;

    public DataManager(File dataFolder, MazeRunnerCore plugin, AutoReconnectManager autoReconnectManager) {
        this.dataFile = new File(dataFolder, "data.yml");
        this.dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        this.plugin = plugin;
        this.autoReconnectManager = autoReconnectManager;
    }

    public void loadPlayerData(UUID playerUUID, Player player) {
        int xp = dataConfig.getInt(playerUUID.toString() + ".xp", 0);
        boolean autoReconnect = dataConfig.getBoolean(playerUUID.toString() + ".autoReconnect", false);
        // Handle loading other data if needed

        // Do something with the loaded data (e.g., set it in the player's DataManager instance)
        setPlayerXP(playerUUID, xp, player);
        setAutoReconnect(playerUUID, autoReconnect, player);
    }

    public void savePlayerData(UUID playerUUID, Player player) {
        // Save XP and auto-reconnect status
        dataConfig.set(playerUUID.toString() + ".xp", getPlayerXP(playerUUID, player));
        dataConfig.set(playerUUID.toString() + ".autoReconnect", plugin.isReconnectEnabled());
        // Save other data if needed

        // Save changes to the data file
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void givePlayerXP(Player player, String difficulty) {
        if (difficulty.equalsIgnoreCase("Easy")) {
            player.giveExp(5);
        } else if (difficulty.equalsIgnoreCase("Moderate")) {
            player.giveExp(10);
        } else if (difficulty.equalsIgnoreCase("Hard")) {
            player.giveExp(15);
        }
        savePlayerData(player.getUniqueId(), player);
    }

    public int getPlayerXP(UUID playerUUID, Player player) {
        // Implement your logic to get XP data for a player
        // Return the XP value or handle it as needed
        return player.getTotalExperience();
    }

    public void setPlayerXP(UUID playerUUID, int xp, Player player) {
        // Reset player xp
        player.setExp(0);
        player.setLevel(0);
        player.setTotalExperience(0);

        // Set the total experience to the desired value
        player.giveExp(xp);
    }

    public void setAutoReconnect(UUID playerUUID, boolean enabled, Player player) {
        // Implement your logic to set auto-reconnect status for a player
        autoReconnectManager.setReconnect(enabled);
        // Update the boolean value in the dataConfig
        dataConfig.set(playerUUID.toString() + ".autoReconnect", enabled);

        // Save changes to the data file
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}