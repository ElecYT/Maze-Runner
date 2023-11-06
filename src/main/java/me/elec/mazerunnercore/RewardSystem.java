package me.elec.mazerunnercore;

import me.elec.mazerunnercore.MazeRunnerCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List; // Add this import
import java.util.Map;

public class RewardSystem {
    private MazeRunnerCore plugin;

    public RewardSystem(MazeRunnerCore plugin) {
        this.plugin = plugin;
        initializeRewards();
    }

    private final List<Reward> rewards = new ArrayList<>();

    // Initialize the rewards list with XP levels and corresponding rewards
    private void initializeRewards() {
        rewards.add(new Reward(1, "ultracosmetics.gadgets.batblaster", "§x§8§B§6§8§2§2§o§nB§x§9§8§7§1§1§E§o§na§x§A§5§7§9§1§A§o§nt §x§B§2§8§2§1§7§o§nB§x§B§F§8§A§1§3§o§nl§x§C§B§9§3§0§F§o§na§x§D§8§9§B§0§B§o§ns§x§E§5§A§4§0§8§o§nt§x§F§2§A§C§0§4§o§ne§x§F§F§B§5§0§0§o§nr (1 of 3)"));
        rewards.add(new Reward(1, "ultracosmetics.suits.astronaut", "§x§E§F§E§F§E§F§o§nA§x§E§A§E§A§E§8§o§ns§x§E§6§E§4§E§1§o§nt§x§E§1§D§F§D§A§o§nr§x§D§C§D§A§D§3§o§no§x§D§8§D§4§C§C§o§nn§x§D§3§C§F§C§5§o§na§x§C§E§C§A§B§E§o§nu§x§C§A§C§4§B§7§o§nt §x§C§5§B§F§B§0§o§nS§x§C§0§B§A§A§9§o§nu§x§B§C§B§4§A§2§o§ni§x§B§7§A§F§9§B§o§nt (2 of 3)"));
        rewards.add(new Reward(1, "ultracosmetics.emotes.smile", "§x§F§F§A§B§0§0§o§nS§x§F§7§A§B§1§1§o§nm§x§E§F§A§C§2§2§o§ni§x§E§7§A§C§3§4§o§nl§x§D§F§A§D§4§5§o§ne §x§D§7§A§D§5§6§o§nE§x§C§F§A§E§6§7§o§nm§x§C§7§A§E§7§9§o§no§x§B§F§A§F§8§A§o§nt§x§B§7§A§F§9§B§o§ne (3 of 3)"));
        rewards.add(new Reward(2, "ultracosmetics.hats.astronaut", "§x§F§F§F§F§F§F§o§nA§x§F§8§F§8§F§6§o§ns§x§F§2§F§0§E§D§o§nt§x§E§B§E§9§E§4§o§nr§x§E§5§E§2§D§B§o§no§x§D§E§D§B§D§2§o§nn§x§D§8§D§3§C§8§o§na§x§D§1§C§C§B§F§o§nu§x§C§B§C§5§B§6§o§nt §x§C§4§B§E§A§D§o§nH§x§B§E§B§6§A§4§o§na§x§B§7§A§F§9§B§o§nt (1 of 3)"));
        rewards.add(new Reward(2, "ultracosmetics.mounts.walkingdead", "§x§4§E§B§C§1§B§o§nZ§x§5§4§B§4§1§A§o§no§x§5§A§A§B§1§9§o§nm§x§6§0§A§3§1§8§o§nb§x§6§6§9§A§1§7§o§ni§x§6§D§9§2§1§7§o§ne §x§7§3§8§9§1§6§o§nH§x§7§9§8§1§1§5§o§no§x§7§F§7§8§1§4§o§nr§x§8§5§7§0§1§3§o§ns§x§8§B§6§7§1§2§o§ne (2 of 3)"));
        rewards.add(new Reward(2, "ultracosmetics.projectileeffects.crit", "§x§B§C§5§F§1§B§o§nC§x§B§9§5§F§1§B§o§nr§x§B§7§6§0§1§A§o§ni§x§B§4§6§0§1§A§o§nt §x§B§2§6§1§1§9§o§nP§x§A§F§6§1§1§9§o§nr§x§A§D§6§2§1§8§o§no§x§A§A§6§2§1§8§o§nj§x§A§7§6§2§1§7§o§ne§x§A§5§6§3§1§7§o§nc§x§A§2§6§3§1§6§o§nt§x§A§0§6§4§1§6§o§ni§x§9§D§6§4§1§5§o§nl§x§9§A§6§4§1§5§o§ne §x§9§8§6§5§1§4§o§nE§x§9§5§6§5§1§4§o§nf§x§9§3§6§6§1§3§o§nf§x§9§0§6§6§1§3§o§ne§x§8§E§6§7§1§2§o§nc§x§8§B§6§7§1§2§o§nt (3 of 3)"));
        rewards.add(new Reward(3, "ultracosmetics.hats.diamond", "§x§1§B§9§F§B§C§o§nD§x§2§7§A§6§C§0§o§ni§x§3§4§A§D§C§4§o§na§x§4§0§B§5§C§7§o§nm§x§4§C§B§C§C§B§o§no§x§5§9§C§3§C§F§o§nn§x§6§5§C§A§D§3§o§nd §x§7§1§D§2§D§6§o§nH§x§7§E§D§9§D§A§o§na§x§8§A§E§0§D§E§o§nt (1 of 2)"));
        rewards.add(new Reward(3, "ultracosmetics.mounts.easterbunny", "§x§F§E§F§E§F§E§o§nE§x§F§C§F§6§F§8§o§na§x§F§A§E§F§F§1§o§ns§x§F§8§E§7§E§B§o§nt§x§F§6§D§F§E§4§o§ne§x§F§4§D§7§D§E§o§nr §x§F§2§D§0§D§7§o§nB§x§F§0§C§8§D§1§o§nu§x§E§E§C§0§C§A§o§nn§x§E§C§B§8§C§4§o§nn§x§E§A§B§1§B§D§o§ny §x§E§8§A§9§B§7§o§nM§x§E§6§A§1§B§0§o§no§x§E§4§9§9§A§A§o§nu§x§E§2§9§2§A§3§o§nn§x§E§0§8§A§9§D§o§nt (2 of 2)"));
        // Add more levels and rewards as needed
    }

    public void checkForRewards(Player player) {
        int xpLevel = player.getLevel();

        for (Reward reward : rewards) {
            int level = reward.getLevel();
            String permission = reward.getPermission();
            String cosmeticName = reward.getCosmeticName();

            if (xpLevel >= level) {
                giveReward(player, permission, cosmeticName);
            }
        }
    }

    private void giveReward(Player player, String rewardPermission, String cosmeticName) {
        if (player.hasPermission(rewardPermission)) {
        } else {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set " + rewardPermission);
            player.sendMessage("§x§B§E§0§9§9§1C§x§B§8§1§0§9§3o§x§B§2§1§7§9§5s§x§A§C§1§E§9§7m§x§A§6§2§5§9§Ae§x§A§0§2§C§9§Ct§x§9§A§3§3§9§Ei§x§9§4§3§A§A§0c §x§8§E§4§1§A§2U§x§8§7§4§8§A§4n§x§8§1§4§F§A§6l§x§7§B§5§6§A§8o§x§7§5§5§D§A§Bc§x§6§F§6§4§A§Dk§x§6§9§6§B§A§Fe§x§6§3§7§2§B§1d§x§5§D§7§9§B§3: " + cosmeticName);
        }
    }
}