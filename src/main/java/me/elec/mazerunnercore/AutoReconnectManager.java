package me.elec.mazerunnercore;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoReconnectManager {
    private MazeRunnerCore plugin;
    private boolean reconnectEnabled = true;
    private DataManager dataManager;

    public AutoReconnectManager(MazeRunnerCore plugin) {
        this.plugin = plugin;
    }

    public void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void toggleReconnect(Player player) {
        reconnectEnabled = !reconnectEnabled;
        if (reconnectEnabled) {
            player.sendMessage(plugin.getGradientPrefix() + "§r§aAuto-reconnect is now enabled.");
        } else {
            player.sendMessage(plugin.getGradientPrefix() + "§r§cAuto-reconnect is now disabled.");
        }
        dataManager.savePlayerData(player.getUniqueId(), player);
    }

    public void setReconnect(Boolean reconnect) {
        reconnectEnabled = reconnect;
    }

    public boolean isReconnectEnabled() {
        return reconnectEnabled;
    }

    public void scheduleReconnect(Player player, String mazeName) {
        if (reconnectEnabled) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (player.isOnline()) {
                        player.sendMessage(plugin.getGradientPrefix() + "§x§B§B§E§6§5§ET§x§A§8§E§9§5§Eo§x§9§6§E§B§5§Eg§x§8§3§E§E§5§Eg§x§7§0§F§0§5§El§x§5§E§F§3§5§Fe §x§4§B§F§5§5§Fu§x§3§8§F§8§5§Fs§x§2§5§F§A§5§Fi§x§1§3§F§D§5§Fn§x§0§0§F§F§5§Fg §x§1§3§F§D§5§F/§x§2§5§F§A§5§Fr§x§3§8§F§8§5§Fe§x§4§B§F§5§5§Fc§x§5§E§F§3§5§Fo§x§7§0§F§0§5§En§x§8§3§E§E§5§En§x§9§6§E§B§5§Ee§x§A§8§E§9§5§Ec§x§B§B§E§6§5§Et");
                        player.playSound(player.getLocation(), Sound.valueOf("BLOCK_NOTE_BLOCK_BELL"), 1.0f, 0.4f);
                        // Implement logic to teleport the player to a new game on the same map
                        plugin.pregameProcess(player, mazeName);
                    }
                }
            }.runTaskLater(plugin, 100); // 100 ticks = 5 seconds
        }
    }
}
