 package me.elec.mazerunnercore.listeners;

import me.elec.mazerunnercore.CustomScoreboardManager;
import me.elec.mazerunnercore.GameEndings;
import me.elec.mazerunnercore.MazeRunnerCore;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener; // Import the Listener interface
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class LeaveGameInventoryClick implements Listener {
    private final GameEndings gameEndings;
    private final MazeRunnerCore plugin; // Add this field to store the MazeRunnerCore instance
    private CustomScoreboardManager scoreboardManager = null;

    public LeaveGameInventoryClick(GameEndings gameEndings, MazeRunnerCore plugin) {
        this.gameEndings = gameEndings;
        this.plugin = plugin;
        this.scoreboardManager = plugin.getScoreboardManager(); // Use the plugin field to get the scoreboardManager
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();
        InventoryHolder inventoryHolder = clickedInventory.getHolder();

        if (inventoryHolder instanceof Player) {
            // Check if the player clicked on the leave game GUI
            if (clickedInventory != null && event.getView().getTitle().equals("Leave the game?")) {
                event.setCancelled(true); // Prevent item manipulation

                // Handle clicks on specific slots
                int slot = event.getRawSlot();
                if (slot == 20) {
                    // Player clicked on the red concrete (confirmation to leave)
                    // Handle confirmation logic here, e.g., teleport to lobby, reset game state, etc.
                    player.sendMessage(plugin.getGradientPrefix() + "§cYou have left the game.");
                    scoreboardManager.setLobbyScoreboard(player);
                    plugin.playerIsLoser();
                    plugin.stopStopwatch(player);

                    // Close the inventory
                    player.closeInventory();
                } else if (slot == 24) {
                    // Player clicked on the green concrete (return to game)
                    // Handle return to game logic here, e.g., resume gameplay
                    player.sendMessage(plugin.getGradientPrefix() + "§aReturning to the game!");


                    // Close the inventory
                    player.closeInventory();
                }
            }
        }
    }
}
