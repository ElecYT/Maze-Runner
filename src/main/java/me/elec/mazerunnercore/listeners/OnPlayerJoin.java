package me.elec.mazerunnercore.listeners;

import me.elec.mazerunnercore.CustomScoreboardManager;
import me.elec.mazerunnercore.DataManager;
import me.elec.mazerunnercore.GameBarrier;
import me.elec.mazerunnercore.MazeRunnerCore;
import me.elec.mazerunnercore.MazeRunnerCore.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnPlayerJoin implements Listener {

    private final MazeRunnerCore plugin;
    private final CustomScoreboardManager scoreboardManager;
    private final GameBarrier gameBarrier;
    private final DataManager dataManager;

    public OnPlayerJoin(MazeRunnerCore plugin, CustomScoreboardManager scoreboardManager, GameBarrier gameBarrier, DataManager dataManager) {
        this.plugin = plugin;
        this.scoreboardManager = scoreboardManager; // Use the provided CustomScoreboardManager
        this.gameBarrier = gameBarrier; // Use the provided GameBarrier instance
        this.dataManager = dataManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Teleport the player to the game lobby
        plugin.teleportPlayer(player, "Lobby", 21.520, 156, 107.512, 0.215f, 270.163f);

        // Set up scoreboard for the player
        scoreboardManager.setLobbyScoreboard(player);

        dataManager.loadPlayerData(player.getUniqueId(), player);

        // Check if the player is in-game
        if (plugin.isPlayerInGame(player.getUniqueId())) {
            // Actions for players in-game
            plugin.playerIsLoser();
            plugin.stopStopwatch(player);
            gameBarrier.removeBarrierFromPlayer(player);
        } else {
            // Actions for players not in-game (you can add more logic here if needed)

        }
        int xpLevel = player.getLevel();

        // Customize the prefix format as needed
        String prefix = ChatColor.GREEN + "[" + xpLevel + "] " + ChatColor.WHITE;
        player.setPlayerListName(player.getName() + prefix);

        MazeRunnerCore.PlayerData playerData = new MazeRunnerCore.PlayerData();
        plugin.addPlayerToDataMap(player.getUniqueId(), playerData);

        if (!player.hasPlayedBefore()) {
            plugin.teleportPlayer(player, "Lobby", 21.520, 156, 107.512, 0.215f, 270.163f);
        }
    }
}
