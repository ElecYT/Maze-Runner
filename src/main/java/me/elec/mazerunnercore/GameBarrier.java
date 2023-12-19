package me.elec.mazerunnercore;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class GameBarrier implements Listener {

    private final Plugin plugin;
    private final ItemStack gameBarrierItem;

    private Player targetPlayer; // The player to receive the barrier
    private boolean barrierGiven = false; // Track if the barrier has been given to the player

    public GameBarrier(Plugin plugin) {
        this.plugin = plugin;

        // Initialize the game barrier item
        gameBarrierItem = createGameBarrierItem();

        // Register the listener
        Bukkit.getPluginManager().registerEvents(this, plugin);

        // Start the task to check player interactions
        startInteractionCheckTask();
    }

    private ItemStack createGameBarrierItem() {
        ItemStack barrierItem = new ItemStack(Material.BARRIER);
        ItemMeta meta = barrierItem.getItemMeta();

        // Set the display name
        meta.setDisplayName("§x§F§F§0§0§0§0R§x§F§F§0§0§0§9e§x§F§F§0§0§1§2t§x§F§F§0§0§1§Bu§x§F§F§0§0§2§4r§x§F§F§0§0§2§Dn §x§F§F§0§0§3§7t§x§F§F§0§0§4§0o §x§F§F§0§0§4§9L§x§F§F§0§0§5§2o§x§F§F§0§0§5§Bb§x§F§F§0§0§6§4b§x§F§F§0§0§6§Dy §7(Right-click)");

        // Add lore, enchantments, or other properties as needed
        // meta.setLore(Arrays.asList("Lore line 1", "Lore line 2"));
        meta.setLore(Arrays.asList("§7Right-click to return to lobby!"));

        barrierItem.setItemMeta(meta);
        return barrierItem;
    }

    // Existing methods...

    private void startInteractionCheckTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (targetPlayer != null && targetPlayer.isOnline()) {
                    if (!barrierGiven) {
                        // Give the barrier if not already given
                        giveBarrierToPlayer(targetPlayer);
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 20); // Run every second (20 ticks)
    }

    // Existing event handlers...

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if (player.equals(targetPlayer) && event.getSlot() == 3) {
                event.setCancelled(true); // Cancel inventory click for the target player in the 4th slot
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getPlayer().equals(targetPlayer)
                && event.getAction() == Action.RIGHT_CLICK_AIR
                && event.getItem() != null
                && event.getItem().isSimilar(gameBarrierItem)) {
            // Player right-clicked with the game barrier item, open the LeaveGame GUI
            ((MazeRunnerCore) plugin).openLeaveGameGUI(targetPlayer);
        }

        // Cancel block placement by the player
        if (event.getPlayer().equals(targetPlayer)
                && event.getAction() == Action.RIGHT_CLICK_BLOCK
                && event.getItem() != null
                && event.getItem().isSimilar(gameBarrierItem)) {
            event.setCancelled(true);
        }
    }

    // Adjusted giveBarrierToPlayer and removeBarrierFromPlayer methods...

    public void giveBarrierToPlayer(Player player) {
        player.getInventory().setItem(3, gameBarrierItem);
        targetPlayer = player; // Set the target player for interaction tracking
        barrierGiven = true; // Mark the barrier as given
    }

    public void removeBarrierFromPlayer(Player player) {
        player.getInventory().remove(gameBarrierItem);
        targetPlayer = null; // Reset the target player
        barrierGiven = false; // Mark the barrier as not given
    }
}
