package me.elec.mazerunnercore;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.UUID;


public class GameEndings {
    private String mapName;
    private MazeRunnerCore plugin;
    private AutoReconnectManager reconnectManager;


    public GameEndings(MazeRunnerCore plugin, AutoReconnectManager reconnectManager) {
        this.plugin = plugin;
        this.reconnectManager = reconnectManager;
    }


    public void stopGame(Player player, Double timeInSeconds, String mazeName, String playerName, String difficulty) {
        // Check if the player is a winner using isPlayerWinner method
        if (plugin.isPlayerWinner) {
            // Player is a winner
            if (mazeName.equalsIgnoreCase("Jungle")) {
                Bukkit.broadcastMessage("§a" + playerName + " §2finished the maze §6" + mazeName + " §2with a time of §b" + timeInSeconds + "§2!");
            } else if (mazeName.equalsIgnoreCase("Nether")) {
                Bukkit.broadcastMessage("§a" + playerName + " §4finished the maze §6" + mazeName + " §4with a time of §b" + timeInSeconds + "§4!");
            } else if (mazeName.equalsIgnoreCase("Spooky")) {
                Bukkit.broadcastMessage("§a" + playerName + " §7finished the maze §6" + mazeName + " §7with a time of §b" + timeInSeconds + "§7!");
            } else if (mazeName.equalsIgnoreCase("Desert")) {
                Bukkit.broadcastMessage("§a" + playerName + " §efinished the maze §6" + mazeName + " §ewith a time of §b" + timeInSeconds + "§e!");
            } else if (mazeName.equalsIgnoreCase("Ice")) {
                Bukkit.broadcastMessage("§a" + playerName + " §9finished the maze §6" + mazeName + " §9with a time of §b" + timeInSeconds + "§9!");
            } else if (mazeName.equalsIgnoreCase("Mangrove")) {
                Bukkit.broadcastMessage("§a" + playerName + " §4finished the maze §6" + mazeName + " §4with a time of §b" + timeInSeconds + "§4!");
            } else if (mazeName.equalsIgnoreCase("null")) {
                Bukkit.broadcastMessage("§a" + playerName + " §4finished the maze §6" + mazeName + " §4with a time of §b" + timeInSeconds + "§4!");
            }


            sendTitle(player, "§x§B§E§6§C§0§9Y§x§B§8§6§4§1§Do§x§B§2§5§C§3§1u §x§A§C§5§4§4§5c§x§A§6§4§C§5§9o§x§9§F§4§5§6§Cm§x§9§9§3§D§8§0p§x§9§3§3§5§9§4l§x§8§D§2§D§A§8e§x§8§7§2§5§B§Ct§x§8§2§2§3§C§9e§x§7§D§2§7§C§Fd §x§7§8§2§B§D§5t§x§7§3§2§F§D§Bh§x§6§E§3§3§E§1e §x§6§9§3§7§E§7m§x§6§4§3§B§E§Da§x§5§F§3§F§F§3z§x§5§A§4§3§F§9e§x§5§5§4§7§F§F!", "§x§B§E§6§C§0§9N§x§A§D§5§7§3§Fi§x§9§D§4§1§7§5c§x§8§C§2§C§A§Be §x§7§D§2§6§C§EJ§x§7§0§3§1§D§Eo§x§6§2§3§C§E§Fb§x§5§5§4§7§F§F!");
            postGame(playerName, mazeName, timeInSeconds, player, difficulty);
            scheduleReconnect(player, mazeName);
            playNote(player, Sound.BLOCK_NOTE_BLOCK_BASS);
            playNote(player, Sound.BLOCK_NOTE_BLOCK_BASEDRUM);
            playNote(player, Sound.BLOCK_NOTE_BLOCK_HARP);
            playNote(player, Sound.BLOCK_NOTE_BLOCK_HAT);
            playNote(player, Sound.BLOCK_NOTE_BLOCK_PLING);
            playNote(player, Sound.BLOCK_NOTE_BLOCK_SNARE);
            playNote(player, Sound.BLOCK_NOTE_BLOCK_BASEDRUM);
            playNote(player, Sound.BLOCK_NOTE_BLOCK_BASS);
            playNote(player, Sound.BLOCK_NOTE_BLOCK_PLING);
            playNote(player, Sound.BLOCK_NOTE_BLOCK_HAT);
            playNote(player, Sound.BLOCK_NOTE_BLOCK_HARP);
            playNote(player, Sound.BLOCK_NOTE_BLOCK_SNARE);
        } else {
            // Player is not a winner
            player.sendActionBar(losingMessage());
            sendTitle(player, "§x§B§E§6§C§0§9Y§x§B§7§6§3§2§1o§x§B§0§5§9§3§8u §x§A§8§5§0§5§0l§x§A§1§4§7§6§8e§x§9§A§3§D§7§Ff§x§9§3§3§4§9§7t §x§8§B§2§A§A§Et§x§8§4§2§1§C§6h§x§7§E§2§6§C§De §x§7§8§2§B§D§4g§x§7§2§2§F§D§Ba§x§6§D§3§4§E§3m§x§6§7§3§9§E§Ae§x§6§1§3§E§F§1.§x§5§B§4§2§F§8.§x§5§5§4§7§F§F.", "§x§B§E§6§C§0§9N§x§B§6§6§2§2§2o §x§A§F§5§8§3§Bx§x§A§7§4§E§5§5p §x§9§F§4§4§6§Ew§x§9§7§3§A§8§7a§x§9§0§3§0§A§0s §x§8§8§2§6§B§9e§x§8§1§2§4§C§Aa§x§7§B§2§9§D§1r§x§7§4§2§E§D§9n§x§6§E§3§3§E§1e§x§6§8§3§8§E§8d§x§6§2§3§D§F§0.§x§5§B§4§2§F§7.§x§5§5§4§7§F§F.");
            plugin.teleportPlayer(player, "Lobby", 21.526, 156, 107.594, -1.3f, -90f);
            // Stop the stopwatch
        }
    }

    public String losingMessage() {
        return "§x§B§E§6§C§0§9Y§x§B§A§6§7§1§5o§x§B§7§6§3§2§1u §x§B§3§5§E§2§Cl§x§B§0§5§9§3§8e§x§A§C§5§5§4§4f§x§A§8§5§0§5§0t §x§A§5§4§B§5§Ct§x§A§1§4§7§6§8h§x§9§D§4§2§7§3e §x§9§A§3§D§7§Fg§x§9§6§3§8§8§Ba§x§9§3§3§4§9§7m§x§8§F§2§F§A§3e§x§8§B§2§A§A§E.§x§8§8§2§6§B§A.§x§8§4§2§1§C§6. §x§8§1§2§3§C§AN§x§7§E§2§6§C§Do §x§7§B§2§8§D§1x§x§7§8§2§B§D§4p §x§7§5§2§D§D§8w§x§7§2§2§F§D§Ba§x§6§F§3§2§D§Fs §x§6§D§3§4§E§3e§x§6§A§3§6§E§6a§x§6§7§3§9§E§Ar§x§6§4§3§B§E§Dn§x§6§1§3§E§F§1e§x§5§E§4§0§F§4d§x§5§B§4§2§F§8.§x§5§8§4§5§F§B.§x§5§5§4§7§F§F.";
    }

    public void playNote(Player player, Sound sound) {
        player.playSound(player.getLocation(), sound, 1, 1);
    }

    public void scheduleReconnect(Player player, String mazeName) {
        if (reconnectManager.isReconnectEnabled()) {
            reconnectManager.scheduleReconnect(player, mazeName, plugin.getDifficulty(player));
            player.sendMessage(plugin.getGradientPrefix() + "§x§2§6§B§E§1§AY§x§2§3§C§4§2§0o§x§1§F§C§9§2§6u §x§1§C§C§F§2§Ch§x§1§9§D§5§3§2a§x§1§5§D§A§3§8v§x§1§2§E§0§3§Ee §x§0§F§E§6§4§4r§x§0§C§E§B§4§Ae§x§0§8§F§1§5§0c§x§0§5§F§7§5§6o§x§0§2§F§C§5§Cn§x§0§8§F§E§5§Fn§x§1§8§F§C§5§Fe§x§2§9§F§A§5§Fc§x§3§9§F§7§5§Ft §x§4§9§F§5§5§Fe§x§5§9§F§3§5§Fn§x§6§A§F§1§5§Ea§x§7§A§E§F§5§Eb§x§8§A§E§D§5§El§x§9§A§E§A§5§Ee§x§A§B§E§8§5§Ed§x§B§B§E§6§5§E. §x§B§B§E§6§5§ER§x§A§B§E§8§5§Ee§x§9§A§E§A§5§Ec§x§8§A§E§D§5§Eo§x§7§A§E§F§5§En§x§6§A§F§1§5§En§x§5§9§F§3§5§Fe§x§4§9§F§5§5§Fc§x§3§9§F§7§5§Ft§x§2§9§F§A§5§Fi§x§1§8§F§C§5§Fn§x§0§8§F§E§5§Fg §x§0§8§F§E§5§Ft§x§1§8§F§C§5§Fo §x§2§9§F§A§5§Fa §x§3§9§F§7§5§Fn§x§4§9§F§5§5§Fe§x§5§9§F§3§5§Fw §x§6§A§F§1§5§Eg§x§7§A§E§F§5§Ea§x§8§A§E§D§5§Em§x§9§A§E§A§5§Ee §x§A§B§E§8§5§Eo§x§B§B§E§6§5§En " + mazeName + "...");
            player.playSound(player.getLocation(), Sound.valueOf("BLOCK_NOTE_BLOCK_BELL"), 1.0f, 1.0f);
        }
    }

    public void postGame(String playerName, String mazeName, double gameTimeInSeconds, Player player, String difficulty) {

        int playerLevel = player.getLevel();

        // Send a summary message to the player with the game time, etc.
        player.sendMessage("§x§B§E§6§C§0§9-§x§B§9§6§5§1§A-§x§B§3§5§E§2§B-§x§A§E§5§8§3§D-§x§A§9§5§1§4§E-§x§A§4§4§A§5§F-§x§9§E§4§3§7§0-§x§9§9§3§C§8§1-§x§9§4§3§5§9§2-§x§8§F§2§F§A§4-§x§8§9§2§8§B§5-§x§8§4§2§1§C§6-§x§8§0§2§4§C§B-§x§7§B§2§8§D§0-§x§7§7§2§B§D§6-§x§7§3§2§F§D§B-§x§6§F§3§2§E§0-§x§6§A§3§6§E§5-§x§6§6§3§9§E§A-§x§6§2§3§D§E§F-§x§5§E§4§0§F§5-§x§5§9§4§4§F§A-§x§5§5§4§7§F§F-");
        player.sendMessage("§x§0§0§4§A§F§F§o§nG§x§1§F§3§B§F§F§o§na§x§3§D§2§C§F§F§o§nm§x§5§C§1§E§F§F§o§ne §x§7§A§0§F§F§F§o§nS§x§9§9§0§0§F§F§o§nu§x§9§D§1§C§D§1§o§nm§x§A§1§3§8§A§3§o§nm§x§A§4§5§5§7§6§o§na§x§A§8§7§1§4§8§o§nr§x§A§C§8§D§1§A§o§ny");
        player.sendMessage("");
        player.sendMessage("§x§0§0§2§B§F§F§o§nT§x§0§0§4§A§F§F§o§ni§x§0§0§6§9§F§F§o§nm§x§0§0§8§8§F§F§o§ne§x§0§0§A§7§F§F§o§n: " + formatTime(gameTimeInSeconds));
        player.sendMessage("§x§0§0§2§B§F§F§o§nM§x§0§0§4§A§F§F§o§na§x§0§0§6§9§F§F§o§nz§x§0§0§8§8§F§F§o§ne§x§0§0§A§7§F§F§o§n: " + mazeName);
        player.sendMessage("§x§0§0§0§2§F§F§o§nL§x§0§0§2§D§F§F§o§ne§x§0§0§5§9§F§F§o§nv§x§0§0§8§4§F§F§o§ne§x§0§0§B§0§F§F§o§nl§x§0§0§A§7§F§F§o§n: " + playerLevel);
        player.sendMessage("");
        player.sendMessage("&#1B18E9ᴘ&#2620DCʟ&#3028CEᴀ&#3B2FC1ʏ&#4537B4ɪ&#503FA6ɴ&#5A4799ɢ &#654F8Cᴏ&#70577Fɴ &#7A5E71ʙ&#856664ɪ&#8F6E57ᴏ&#9A7649ᴍ&#A47E3Cᴇ&#AF852Fꜱ&#B98D21ᴍ&#C49514ᴄ");
        player.sendMessage("§x§B§E§6§C§0§9-§x§B§9§6§5§1§A-§x§B§3§5§E§2§B-§x§A§E§5§8§3§D-§x§A§9§5§1§4§E-§x§A§4§4§A§5§F-§x§9§E§4§3§7§0-§x§9§9§3§C§8§1-§x§9§4§3§5§9§2-§x§8§F§2§F§A§4-§x§8§9§2§8§B§5-§x§8§4§2§1§C§6-§x§8§0§2§4§C§B-§x§7§B§2§8§D§0-§x§7§7§2§B§D§6-§x§7§3§2§F§D§B-§x§6§F§3§2§E§0-§x§6§A§3§6§E§5-§x§6§6§3§9§E§A-§x§6§2§3§D§E§F-§x§5§E§4§0§F§5-§x§5§9§4§4§F§A-§x§5§5§4§7§F§F-");
        mapName = mazeName;
        player.setGameMode(GameMode.SPECTATOR);

        // Create clickable text for "Play again" and "Spectate players"
        // These should execute the respective commands when clicked

        // You can use Spigot's TextComponent for clickable text

        // Send the clickable summary message to the player
        sendClickableText(player, "§c§nReturn To Lobby§f§7 (/spawn)", "/spawn");
        sendClickableText(player, "§a§nPlay Again§f§7 (/mazegame)", "/mazegame");
        sendClickableText(player, "§e§nView The Leaderboard§f§7 (/leaderboard <mazeName> <difficulty>)", "/leaderboard " + mazeName + " " + difficulty);
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

    public void sendClickableText(Player player, String text, String command) {
        // Create a TextComponent with the text you want to display
        ComponentBuilder componentBuilder = new ComponentBuilder(text);

        // Add additional formatting if needed

        // Define the action when the text is clicked (in this case, run a command)
        componentBuilder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));

        // Create the TextComponent
        net.md_5.bungee.api.chat.TextComponent textComponent = new net.md_5.bungee.api.chat.TextComponent(componentBuilder.create());

        // Send the TextComponent to the player
        player.spigot().sendMessage(textComponent);
    }

    public static void sendTitle(Player player, String title, String subtitle) {
        player.sendTitle(title, subtitle, 10, 70, 20);

    }
}


