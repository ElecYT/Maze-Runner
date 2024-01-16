package me.elec.mazerunnercore;

import me.elec.mazerunnercore.commands.*;
import me.elec.mazerunnercore.listeners.LeaveGameInventoryClick;
import me.elec.mazerunnercore.listeners.OnPlayerJoin;
import me.elec.mazerunnercore.listeners.OnPlayerLeave;
import me.elec.mazerunnercore.listeners.PressurePlateListener;
import org.bukkit.*;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;


import java.io.File;
import java.util.*;

public class MazeRunnerCore extends JavaPlugin implements Listener {

    //ENABLE CODE

    private File xpDataFile;
    private FileConfiguration xpDataConfig;
    private static MazeRunnerCore instance;
    private Map<UUID, Boolean> gameWinStates = new HashMap<>();

    public static class PlayerData {
        private boolean stopwatchRunning;
        private long startTimeNano;

        public PlayerData() {
            this.stopwatchRunning = false;
            this.startTimeNano = 0;
        }

        public void startStopwatch() {
            if (!stopwatchRunning) {
                startTimeNano = 0;
                stopwatchRunning = true;
                // Additional setup if needed
            }
        }

        public boolean isStopwatchRunning() {
            return stopwatchRunning;
        }


        public long getStartTimeNano() {
            return startTimeNano;
        }

        // Add any other methods or fields relevant to player data
    }

    // Getter method for the instance
    public Map<UUID, Boolean> getGameWinStates() {
        return gameWinStates;
    }

    private double elapsedTimeSeconds = 0;  // Define the variable
    public boolean isPlayerWinner = false;
    private static MazeRunnerCore plugin;
    private boolean stopwatchRunning = false;
    private long startTimeNano = 0;
    private BukkitTask countdownTask = null;
    private LeaderboardManager leaderboardManager;
    private boolean reconnectEnabled = false;
    private final Set<UUID> playersInGame = new HashSet<>();
    private GameEndings gameEndings; // Declare the variable
    private Player player;
    private String mazeName;
    private String mapName;

    // Method to check if a player is in a game
    public boolean isPlayerInGame(UUID playerId) {
        return playersInGame.contains(playerId);
    }

    private AutoReconnectManager reconnectManager;
    private CustomScoreboardManager scoreboardManager;
    private Scoreboard gameScoreboard;
    private Scoreboard lobbyScoreboard;
    private GameBarrier gameBarrier; // Reference to the GameBarrier class
    private final Map<UUID, DataManager> dataManagers = new HashMap<>();
    private DataManager dataManager;
    public String difficulty;
    public String maze;
    private Map<UUID, Long> startTimeMap = new HashMap<>();
    private Map<UUID, String> mazeNameMap = new HashMap<>();
    private Map<UUID, Boolean> stopwatchRunningMap = new HashMap<>();
    private Map<UUID, String> difficultyMap = new HashMap<>();

    public void onEnable() {
        plugin = this; // Initialize the plugin instance

        // Initialize leaderboard manager first
        leaderboardManager = new LeaderboardManager(this);

        // Initialize the AutoReconnectManager
        reconnectManager = new AutoReconnectManager(this);

        // Initialize the DataManager with the plugin's data folder
        dataManager = new DataManager(getDataFolder(), this, reconnectManager);

        // Set the DataManager instance in AutoReconnectManager
        reconnectManager.setDataManager(dataManager);

        scoreboardManager = new CustomScoreboardManager(this);
        // Initialize the GameBarrier class
        gameBarrier = new GameBarrier(this);

        gameEndings = new GameEndings(this, reconnectManager);


        // Register other event listeners
        LeaveGameInventoryClick leaveGameInventoryClick = new LeaveGameInventoryClick(gameEndings, this, dataManager);
        // Declare and initialize pressurePlateListener
        PressurePlateListener pressurePlateListener = new PressurePlateListener(this, gameEndings, dataManager);

        Bukkit.getPluginManager().registerEvents(new OnPlayerJoin(this, scoreboardManager, gameBarrier, dataManager), plugin);

        Bukkit.getPluginManager().registerEvents(new OnPlayerLeave(this, scoreboardManager, gameBarrier, dataManager), plugin);

        // Register the event listener
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(pressurePlateListener, this);
        getServer().getPluginManager().registerEvents(leaveGameInventoryClick, this);

        // Register the /mazegame command (if needed)
        getCommand("mazegame").setExecutor(new MazeGameCommand(this));
        getCommand("spawn").setExecutor(new TpLobbyCommand(this));
        getCommand("leave").setExecutor(new LeaveCommand(this));
        getCommand("reconnect").setExecutor(new ReconnectCommand(reconnectManager));
        getCommand("website").setExecutor(new WebsiteCommand());
        getCommand("discord").setExecutor(new DiscordCommand());

        // Register the leaderboard command
        getCommand("leaderboard").setExecutor(new LeaderboardCommand(leaderboardManager, this));
    }

    public void onDisable() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            dataManager.savePlayerData(player.getUniqueId(), player);
        }
    }

    // Method to add a player to the playerDataMap
    public void addPlayerToDataMap(UUID playerId, PlayerData playerData) {
        playerDataMap.put(playerId, playerData);
    }

    public CustomScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    // Getter method to retrieve the GameBarrier instance
    public GameBarrier getGameBarrier() {
        return gameBarrier;
    }
    //GUI CODE


    public void openMazeGameGUI(Player player) {
        // Create a new inventory with 9 slots (for simplicity)
        Inventory gui = Bukkit.createInventory(player, 54, "ꜱᴇʟᴇᴄᴛ ᴀ ᴍᴀᴘ...");

        //Jungle Mazes
        ItemStack jungleMazeEasy = createMazeItem("Jungle", "Beginner Jungle Maze", "[Beginner]", Material.valueOf("STONE_BRICKS"));
        ItemStack jungleMazeModerate = createMazeItem("Jungle", "Moderate Jungle Maze", "[Moderate]", Material.valueOf("CRACKED_STONE_BRICKS"));
        ItemStack jungleMazeHard = createMazeItem("Jungle", "Hard Jungle Maze", "[Hard]", Material.valueOf("MOSSY_STONE_BRICKS"));
        ItemStack jungleMazeAdventure = createMazeItem("Jungle", "Adventure Jungle Maze", "[Adventure]", Material.valueOf("MOSSY_COBBLESTONE_WALL"));
        gui.setItem(0, jungleMazeEasy);
        gui.setItem(1, jungleMazeModerate);
        gui.setItem(2, jungleMazeHard);
        gui.setItem(3, jungleMazeAdventure);
        //Nether Mazes
        ItemStack netherMazeEasy = createMazeItem("Nether", "Beginner Nether Maze", "[Beginner]", Material.valueOf("NETHERRACK"));
        ItemStack netherMazeModerate = createMazeItem("Nether", "Moderate Nether Maze", "[Moderate]", Material.valueOf("CHISELED_NETHER_BRICKS"));
        ItemStack netherMazeHard = createMazeItem("Nether", "Hard Nether Maze", "[Hard]", Material.valueOf("RED_NETHER_BRICKS"));
        ItemStack netherMazeAdventure = createMazeItem("Nether", "Adventure Nether Maze", "[Adventure]", Material.valueOf("NETHERITE_BLOCK"));
        gui.setItem(18, netherMazeEasy);
        gui.setItem(19, netherMazeModerate);
        gui.setItem(20, netherMazeHard);
        gui.setItem(21, netherMazeAdventure);
        //Spooky Mazes
        ItemStack spookyMazeEasy = createMazeItem("Spooky", "Beginner Spooky Maze", "[Beginner]", Material.valueOf("DEAD_BUSH"));
        ItemStack spookyMazeModerate = createMazeItem("Spooky", "Moderate Spooky Maze", "[Moderate]", Material.valueOf("ORANGE_TERRACOTTA"));
        ItemStack spookyMazeHard = createMazeItem("Spooky", "Hard Spooky Maze", "[Hard]", Material.valueOf("BLACK_TERRACOTTA"));
        ItemStack spookyMazeAdventure = createMazeItem("Spooky", "Adventure Spooky Maze", "[Adventure]", Material.valueOf("JACK_O_LANTERN"));
        gui.setItem(36, spookyMazeEasy);
        gui.setItem(37, spookyMazeModerate);
        gui.setItem(38, spookyMazeHard);
        gui.setItem(39, spookyMazeAdventure);
        //Desert Mazes
        ItemStack desertMazeEasy = createMazeItem("Desert", "Beginner Desert Maze", "[Beginner]", Material.valueOf("SAND"));
        ItemStack desertMazeModerate = createMazeItem("Desert", "Moderate Desert Maze", "[Moderate]", Material.valueOf("SMOOTH_SANDSTONE"));
        ItemStack desertMazeHard = createMazeItem("Desert", "Hard Desert Maze", "[Hard]", Material.valueOf("STRIPPED_BIRCH_LOG"));
        ItemStack desertMazeAdventure = createMazeItem("Desert", "Adventure Desert Maze", "[Adventure]", Material.valueOf("JUNGLE_LOG"));
        gui.setItem(5, desertMazeEasy);
        gui.setItem(6, desertMazeModerate);
        gui.setItem(7, desertMazeHard);
        gui.setItem(8, desertMazeAdventure);
        //Mangrove Mazes
        ItemStack mangroveMazeEasy = createMazeItem("Mangrove", "Beginner Mangrove Maze", "[Beginner]", Material.valueOf("MANGROVE_PLANKS"));
        ItemStack mangroveMazeModerate = createMazeItem("Mangrove", "Moderate Mangrove Maze", "[Moderate]", Material.valueOf("STRIPPED_MANGROVE_WOOD"));
        ItemStack mangroveMazeHard = createMazeItem("Mangrove", "Hard Mangrove Maze", "[Hard]", Material.valueOf("MANGROVE_LOG"));
        ItemStack mangroveMazeAdventure = createMazeItem("Mangrove", "Adventure Mangrove Maze", "[Adventure]", Material.valueOf("MANGROVE_STAIRS"));
        gui.setItem(23, mangroveMazeEasy);
        gui.setItem(24, mangroveMazeModerate);
        gui.setItem(25, mangroveMazeHard);
        gui.setItem(26, mangroveMazeAdventure);
        //Ice Mazes
        ItemStack iceMazeEasy = createMazeItem("Ice", "Beginner Ice Maze", "[Beginner]", Material.valueOf("SNOW_BLOCK"));
        ItemStack iceMazeModerate = createMazeItem("Ice", "Moderate Ice Maze", "[Moderate]", Material.valueOf("ICE"));
        ItemStack iceMazeHard = createMazeItem("Ice", "Hard Ice Maze", "[Hard]", Material.valueOf("PACKED_ICE"));
        ItemStack iceMazeAdventure = createMazeItem("Ice", "Adventure Ice Maze", "[Adventure]", Material.valueOf("BLUE_ICE"));
        gui.setItem(41, iceMazeEasy);
        gui.setItem(42, iceMazeModerate);
        gui.setItem(43, iceMazeHard);
        gui.setItem(44, iceMazeAdventure);
        //Filler Items
        ItemStack fillerItem = createFillerItem(Material.BLACK_STAINED_GLASS_PANE, "");
        gui.setItem(9, fillerItem);
        gui.setItem(10, fillerItem);
        gui.setItem(11, fillerItem);
        gui.setItem(12, fillerItem);
        gui.setItem(13, fillerItem);
        gui.setItem(14, fillerItem);
        gui.setItem(15, fillerItem);
        gui.setItem(16, fillerItem);
        gui.setItem(17, fillerItem);
        gui.setItem(27, fillerItem);
        gui.setItem(28, fillerItem);
        gui.setItem(29, fillerItem);
        gui.setItem(30, fillerItem);
        gui.setItem(31, fillerItem);
        gui.setItem(32, fillerItem);
        gui.setItem(33, fillerItem);
        gui.setItem(34, fillerItem);
        gui.setItem(35, fillerItem);
        gui.setItem(45, fillerItem);
        gui.setItem(46, fillerItem);
        gui.setItem(47, fillerItem);
        gui.setItem(48, fillerItem);
        gui.setItem(49, fillerItem);
        gui.setItem(50, fillerItem);
        gui.setItem(51, fillerItem);
        gui.setItem(52, fillerItem);
        gui.setItem(53, fillerItem);
        gui.setItem(4, fillerItem);
        gui.setItem(22, fillerItem);
        gui.setItem(40, fillerItem);
        // Open the GUI for the player
        player.openInventory(gui);


    }


    public ItemStack createMazeItem(String mazeName, String mazeDisplayName, String mazeDifficulty, Material material) {
        // Create an ItemStack for the maze item
        ItemStack itemStack = new ItemStack(material != null ? material : Material.STONE_BRICKS);

        // Create an ItemMeta to customize the item's display name and lore
        ItemMeta itemMeta = itemStack.getItemMeta();

        String mazeColor = "§f"; // Default to white
        String difficultyColor = "§f"; // Default to white

        if (mazeDifficulty.equalsIgnoreCase("[Beginner]")) {
            difficultyColor = "§a"; // Green
        } else if (mazeDifficulty.equalsIgnoreCase("[Moderate]")) {
            difficultyColor = "§6"; // Gold
        } else if (mazeDifficulty.equalsIgnoreCase("[Hard]")) {
            difficultyColor = "§c"; // Red
        } else if (mazeDifficulty.equalsIgnoreCase("[Adventure]")) {
            difficultyColor = "§2"; // Dark Green
        }

        if (mazeName.equalsIgnoreCase("Jungle")) {
            mazeColor = "§2"; // Dark Green
        } else if (mazeName.equalsIgnoreCase("Nether")) {
            mazeColor = "§c"; // Red
        } else if (mazeName.equalsIgnoreCase("Spooky")) {
            mazeColor = "§6"; // Gold
        } else if (mazeName.equalsIgnoreCase("Desert")) {
            mazeColor = "§e"; // Yellow
        } else if (mazeName.equalsIgnoreCase("Mangrove")) {
            mazeColor = "§4"; // Dark Red
        } else if (mazeName.equalsIgnoreCase("Ice")) {
            mazeColor = "§b"; // Aqua
        }


        // Set the display name to the maze name with mazeColor
        itemMeta.setDisplayName(mazeColor + mazeDisplayName);

        // Create lore (description) for the item
        List<String> lore = new ArrayList<>();
        lore.add("§7Click to teleport to the " + mazeColor + mazeDisplayName);
        lore.add("§7Difficulty: " + difficultyColor + mazeDifficulty);

        // You can add more lore lines if desired

        // Set the lore for the item
        itemMeta.setLore(lore);

        // Apply the custom ItemMeta to the ItemStack
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Check if the clicked inventory is the Maze Game GUI
        if (!event.getView().getTitle().equals("ꜱᴇʟᴇᴄᴛ ᴀ ᴍᴀᴘ...")) {
            return;
        }

        // Prevent players from moving items in the GUI
        event.setCancelled(true);

        // Handle clicks on the items here
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem != null && clickedItem.getType() != Material.AIR) {
            // Check which item was clicked and teleport the player accordingly
            if (clickedItem.getType() == Material.STONE_BRICKS) {
                teleportPlayer(player, "Jungle", -11.692, -60, 5.016, 4.508f, 269.73f);
                teleportPlayer(player, "Jungle", -11.692, -60, 5.016, 4.508f, 269.73f);
                pregameProcess(player, "JungleEasy");
                difficultyMap.put(player.getUniqueId(), "Easy");
                mazeNameMap.put(player.getUniqueId(), "Jungle");
            } else if (clickedItem.getType() == Material.CRACKED_STONE_BRICKS) {
                teleportPlayer(player, "Jungle", -8.691, -60, 26.116, 0.41f, 268.021f);
                teleportPlayer(player, "Jungle", -8.691, -60, 26.116, 0.41f, 268.021f);
                pregameProcess(player, "JungleModerate");
                difficultyMap.put(player.getUniqueId(), "Moderate");
                mazeNameMap.put(player.getUniqueId(), "Jungle");
            } else if (clickedItem.getType() == Material.MOSSY_STONE_BRICKS) {
                teleportPlayer(player, "Jungle", 19.678, -60, 3.307, 2.724f, 356.001f);
                teleportPlayer(player, "Jungle", 19.678, -60, 3.307, 2.724f, 356.001f);
                pregameProcess(player, "JungleHard");
                difficultyMap.put(player.getUniqueId(), "Hard");
                mazeNameMap.put(player.getUniqueId(), "Jungle");
            } else if (clickedItem.getType() == Material.MOSSY_COBBLESTONE_WALL) {
                teleportPlayer(player, "Jungle", -17.574, -60, 121.699, 3.953f, 180.739f);
                teleportPlayer(player, "Jungle", -17.574, -60, 121.699, 3.953f, 180.739f);
                pregameProcess(player, "JungleAdventure");
                difficultyMap.put(player.getUniqueId(), "Adventure");
                mazeNameMap.put(player.getUniqueId(), "Jungle");
            } else if (clickedItem.getType() == Material.NETHERRACK) {
                teleportPlayer(player, "Nether", -18.868, -60, 29.7, .4f, -179.8f);
                teleportPlayer(player, "Nether", -18.868, -60, 29.7, .4f, -179.8f);
                pregameProcess(player, "NetherEasy");
                difficultyMap.put(player.getUniqueId(), "Easy");
                mazeNameMap.put(player.getUniqueId(), "Nether");
            } else if (clickedItem.getType() == Material.CHISELED_NETHER_BRICKS) {
                teleportPlayer(player, "Nether", 23.108, -60, 119.7, .9f, 180f);
                teleportPlayer(player, "Nether", 23.108, -60, 119.7, .9f, 180f);
                pregameProcess(player, "NetherModerate");
                difficultyMap.put(player.getUniqueId(), "Moderate");
                mazeNameMap.put(player.getUniqueId(), "Nether");
            } else if (clickedItem.getType() == Material.RED_NETHER_BRICKS) {
                teleportPlayer(player, "Nether", 58.397, -60, -6.022, 7.4f, -1.4f);
                teleportPlayer(player, "Nether", 58.397, -60, -6.022, 7.4f, -1.4f);
                pregameProcess(player, "NetherHard");
                difficultyMap.put(player.getUniqueId(), "Hard");
                mazeNameMap.put(player.getUniqueId(), "Nether");
            } else if (clickedItem.getType() == Material.NETHERITE_BLOCK) {
                teleportPlayer(player, "NetherAdventure", -27.516, -60, 29.586, 3.0f, -135.1f);
                teleportPlayer(player, "NetherAdventure", -27.516, -60, 29.586, 3.0f, -135.1f);
                pregameProcess(player, "NetherAdventure");
                difficultyMap.put(player.getUniqueId(), "Adventure");
                mazeNameMap.put(player.getUniqueId(), "Nether");
            } else if (clickedItem.getType() == Material.DEAD_BUSH) {
                teleportPlayer(player, "Spooky", 11.7, -60, 40.859, 3.155f, 89.897f);
                teleportPlayer(player, "Spooky", 11.7, -60, 40.859, 3.155f, 89.897f);
                pregameProcess(player, "SpookyEasy");
                difficultyMap.put(player.getUniqueId(), "Easy");
                mazeNameMap.put(player.getUniqueId(), "Spooky");
            } else if (clickedItem.getType() == Material.ORANGE_TERRACOTTA) {
                teleportPlayer(player, "Spooky", 27.964, -60, 8.598, 0.456f, -0.611f);
                teleportPlayer(player, "Spooky", 27.964, -60, 8.598, 0.456f, -0.611f);
                pregameProcess(player, "SpookyModerate");
                difficultyMap.put(player.getUniqueId(), "Moderate");
                mazeNameMap.put(player.getUniqueId(), "Spooky");
            } else if (clickedItem.getType() == Material.BLACK_TERRACOTTA) {
                teleportPlayer(player, "Spooky", 43.699, -60, -29.057, 3.785f, 89.104f);
                teleportPlayer(player, "Spooky", 43.699, -60, -29.057, 3.785f, 89.104f);
                pregameProcess(player, "SpookyHard");
                difficultyMap.put(player.getUniqueId(), "Hard");
                mazeNameMap.put(player.getUniqueId(), "Spooky");
            } else if (clickedItem.getType() == Material.SAND) {
                teleportPlayer(player, "Desert", 7.002, -60, 22.700, 6.725f, 179.23f);
                teleportPlayer(player, "Desert", 7.002, -60, 22.700, 6.725f, 179.23f);
                pregameProcess(player, "DesertEasy");
                difficultyMap.put(player.getUniqueId(), "Easy");
                mazeNameMap.put(player.getUniqueId(), "Desert");
            } else if (clickedItem.getType() == Material.SMOOTH_SANDSTONE) {
                teleportPlayer(player, "Desert", -53.007, -60, 16.7, 3.522f, 180.178f);
                teleportPlayer(player, "Desert", -53.007, -60, 16.7, 3.522f, 180.178f);
                pregameProcess(player, "DesertModerate");
                difficultyMap.put(player.getUniqueId(), "Moderate");
                mazeNameMap.put(player.getUniqueId(), "Desert");
            } else if (clickedItem.getType() == Material.STRIPPED_BIRCH_LOG) {
                teleportPlayer(player, "Desert", -18.841, -60, 7.989, 2.317f, 179.969f);
                teleportPlayer(player, "Desert", -18.841, -60, 7.989, 2.317f, 179.969f);
                pregameProcess(player, "DesertHard");
                difficultyMap.put(player.getUniqueId(), "Hard");
                mazeNameMap.put(player.getUniqueId(), "Desert");
            } else if (clickedItem.getType() == Material.MANGROVE_PLANKS) {
                teleportPlayer(player, "Mangrove", 12.851, -60, -58.7, 0.552f, 359.822f);
                teleportPlayer(player, "Mangrove", 12.851, -60, -58.7, 0.552f, 359.822f);
                pregameProcess(player, "MangroveEasy");
                difficultyMap.put(player.getUniqueId(), "Easy");
                mazeNameMap.put(player.getUniqueId(), "Mangrove");
            } else if (clickedItem.getType() == Material.STRIPPED_MANGROVE_WOOD) {
                teleportPlayer(player, "Mangrove", 48.972, -60, -29.700, 2.317f, 1.579f);
                teleportPlayer(player, "Mangrove", 48.972, -60, -29.700, 2.317f, 1.579f);
                pregameProcess(player, "MangroveModerate");
                difficultyMap.put(player.getUniqueId(), "Moderate");
                mazeNameMap.put(player.getUniqueId(), "Mangrove");
            } else if (clickedItem.getType() == Material.MANGROVE_LOG) {
                teleportPlayer(player, "Mangrove", -17.699, -60, 30.509, 0.525f, 270.376f);
                teleportPlayer(player, "Mangrove", -17.699, -60, 30.509, 0.525f, 270.376f);
                pregameProcess(player, "MangroveHard");
                difficultyMap.put(player.getUniqueId(), "Hard");
                mazeNameMap.put(player.getUniqueId(), "Mangrove");
            } else if (clickedItem.getType() == Material.SNOW_BLOCK) {
                teleportPlayer(player, "Ice", -13.996, -60, 12.3, 0.506f, 0.207f);
                teleportPlayer(player, "Ice", -13.996, -60, 12.3, 0.506f, 0.207f);
                pregameProcess(player, "IceEasy");
                difficultyMap.put(player.getUniqueId(), "Easy");
                mazeNameMap.put(player.getUniqueId(), "Ice");
            } else if (clickedItem.getType() == Material.ICE) {
                teleportPlayer(player, "Ice", -40.039, -60, -8.7, 1.664f, 0.267f);
                teleportPlayer(player, "Ice", -40.039, -60, -8.7, 1.664f, 0.267f);
                pregameProcess(player, "IceModerate");
                difficultyMap.put(player.getUniqueId(), "Moderate");
                mazeNameMap.put(player.getUniqueId(), "Ice");
            } else if (clickedItem.getType() == Material.PACKED_ICE) {
                teleportPlayer(player, "Ice", -114.7, -60, -16.980, 2.217f, 270.325f);
                teleportPlayer(player, "Ice", -114.7, -60, -16.980, 2.217f, 270.325f);
                pregameProcess(player, "IceHard");
                difficultyMap.put(player.getUniqueId(), "Hard");
                mazeNameMap.put(player.getUniqueId(), "Ice");
            }
        }
    }

    public void teleportPlayer(Player player, String worldName, double x, double y, double z, float pitch, float yaw) {
        World world = Bukkit.getWorld(worldName);

        if (world != null) {
            Location location = new Location(world, x, y, z, yaw, pitch);
            player.teleport(location);
        } else {
            player.sendMessage(getGradientPrefix() + "§cThe specified world does not exist");
        }
    }

    //LEAVE GUI

    public void openLeaveGameGUI(Player player) {
        if (plugin.isPlayerInGame(player.getUniqueId())) {
            Inventory gui = Bukkit.createInventory(player, 54, "Leave the game?");
            ItemStack redConcrete = createLeaveItem(ChatColor.RED + "Are you sure?", ChatColor.GRAY + "You will lose all XP", Material.RED_CONCRETE);
            ItemStack greenConcrete = createLeaveItem(ChatColor.GREEN + "Return to game!", ChatColor.GRAY + "You will continue playing, and still earn XP", Material.GREEN_CONCRETE);

            gui.setItem(20, redConcrete);
            gui.setItem(24, greenConcrete);

            player.openInventory(gui);
        } else {
            player.sendMessage(getGradientPrefix() + "§cYou are not currently in a game...");
        }
    }

    public String getGradientPrefix() {
        return "§x§F§F§A§0§0§0§l[§x§D§E§8§F§2§6§lM§x§B§E§7§D§4§C§la§x§9§D§6§C§7§1§lz§x§7§D§5§B§9§7§le §x§5§C§4§A§B§D§lR§x§4§6§4§5§C§C§lu§x§3§9§4§E§C§5§ln§x§2§D§5§6§B§D§ln§x§2§1§5§F§B§5§le§x§1§4§6§7§A§E§lr§x§0§8§7§0§A§6§l] ";
    }

    private ItemStack createLeaveItem(String displayName, String lore, Material material) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(Collections.singletonList(lore));

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private ItemStack createFillerItem(Material material, String displayName) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        item.setItemMeta(meta);
        return item;
    }

    //GAME CODE

    // Update the pregameProcess method to accept a MazeRunnerCore instance
    public void pregameProcess(Player player, String mazeName) {
        // Set the player to adventure mode
        player.setGameMode(GameMode.ADVENTURE);

        // Do countdown stuff
        startCountdown(player, mazeName); // Pass a list with a single player
        //Set the mapName variable
        mapName = mazeName;
    }

    public void teleportMaze(Player player, String mazeName) {
        // Teleport the player to the specified maze location
        if (mazeName.equalsIgnoreCase("JungleEasy")) {
            teleportPlayer(player, "Jungle", -11.692, -60, 5.016, 4.508f, 269.73f);
        } else if (mazeName.equalsIgnoreCase("JungleModerate")) {
            teleportPlayer(player, "Jungle", -8.691, -60, 26.116, 0.41f, 268.021f);
        } else if (mazeName.equalsIgnoreCase("JungleHard")) {
            teleportPlayer(player, "Jungle", 19.678, -60, 3.307, 2.724f, 356.001f);
        } else if (mazeName.equalsIgnoreCase("JungleAdventure")) {
            teleportPlayer(player, "Jungle", -17.574, -60, 121.699, 3.953f, 180.739f);
        } else if (mazeName.equalsIgnoreCase("NetherEasy")) {
            teleportPlayer(player, "Nether", -18.868, -60, 29.7, .4f, -179.8f);
        } else if (mazeName.equalsIgnoreCase("NetherModerate")) {
            teleportPlayer(player, "Nether", 23.108, -60, 119.7, .9f, 180f);
        } else if (mazeName.equalsIgnoreCase("NetherHard")) {
            teleportPlayer(player, "Nether", 58.397, -60, -6.022, 7.4f, -1.4f);
        } else if (mazeName.equalsIgnoreCase("NetherAdventure")) {
            teleportPlayer(player, "NetherAdventure", -27.516, -60, 29.586, 3.0f, -135.1f);
        } else if (mazeName.equalsIgnoreCase("SpookyEasy")) {
            teleportPlayer(player, "Spooky", 11.7, -60, 40.859, 3.155f, 89.897f);
        } else if (mazeName.equalsIgnoreCase("SpookyModerate")) {
            teleportPlayer(player, "Spooky", 27.964, -60, 8.598, 0.456f, -0.611f);
        } else if (mazeName.equalsIgnoreCase("SpookyHard")) {
            teleportPlayer(player, "Spooky", 43.699, -60, -29.057, 3.785f, 89.104f);
        } else if (mazeName.equalsIgnoreCase("DesertEasy")) {
            teleportPlayer(player, "Desert", 6.981, -60, -22.695, 6.725f, 179.23f);
        } else if (mazeName.equalsIgnoreCase("DesertModerate")) {
            teleportPlayer(player, "Desert", -53.007, -60, 16.7, 3.522f, 180.178f);
        } else if (mazeName.equalsIgnoreCase("DesertHard")) {
            teleportPlayer(player, "Desert", -18.841, -60, 7.989, 2.317f, 179.969f);
        } else if (mazeName.equalsIgnoreCase("MangroveEasy")) {
            teleportPlayer(player, "Mangrove", 12.851, -60, -58.7, 0.552f, 359.822f);
        } else if (mazeName.equalsIgnoreCase("MangroveModerate")) {
            teleportPlayer(player, "Mangrove", 48.972, -60, -29.700, 2.317f, 1.579f);
        } else if (mazeName.equalsIgnoreCase("MangroveHard")) {
            teleportPlayer(player, "Mangrove", -17.699, -60, 30.509, 0.525f, 270.376f);
        } else if (mazeName.equalsIgnoreCase("IceEasy")) {
            teleportPlayer(player, "Ice", -13.996, -60, 12.3, 0.506f, 0.207f);
        } else if (mazeName.equalsIgnoreCase("IceModerate")) {
            teleportPlayer(player, "Ice", -40.039, -60, -8.7, 1.664f, 0.267f);
        } else if (mazeName.equalsIgnoreCase("IceHard")) {
            teleportPlayer(player, "Ice", -114.7, -60, -16.980, 2.217f, 270.325f);
        }
    }


    public void playerIsWinner() {
        isPlayerWinner = true;
    }

    public void playerIsLoser() {
        isPlayerWinner = false;
    }

    private void startCountdown(Player player, String mazeName) {
        final int countdownTime = 3;

        if (countdownTask != null) {
            countdownTask.cancel();
        }

        countdownTask = new BukkitRunnable() {
            int timeLeft = countdownTime;

            @Override
            public void run() {
                if (timeLeft > 0) {
                    String title = "Game starting";
                    String subtitle = "in... " + timeLeft;
                    sendTitlesToPlayers(player, ChatColor.GOLD, title, subtitle);
                    teleportMaze(player, mazeName);
                    sendActionBarToPlayers(player, ChatColor.GOLD + "Game starts in " + timeLeft);
                    player.playSound(player.getLocation(), Sound.valueOf("BLOCK_NOTE_BLOCK_HAT"), 1.0f, 1.0f);
                    timeLeft--;
                    scoreboardManager.setGameScoreboard(player);
                    startTimeMap.put(player.getUniqueId(), System.nanoTime());
                } else {
                    sendTitlesToPlayers(player, ChatColor.GREEN, "Game Started!", "Good Luck!");
                    playNote(player, Sound.BLOCK_NOTE_BLOCK_BASS);
                    playNote(player, Sound.BLOCK_NOTE_BLOCK_BASEDRUM);
                    // ... (other playNote calls)
                    startStopwatch(player); // Move the startStopwatch call here

                    this.cancel();

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (isStopwatchRunning(player)) {
                                sendElapsedTimeToActionBar(player);
                            }
                        }
                    }.runTaskTimer(plugin, 0, 2);
                }
            }
        }.runTaskTimer(this, 0, 20);
    }

    public void playNote(Player player, Sound sound) {
        player.playSound(player.getLocation(), sound, 1, 1);
    }

    public Player getPlayer() {
        return player;
    }

    public String getDifficulty(Player player) {
        return difficultyMap.getOrDefault(player.getUniqueId(), "easy");
    }

    public boolean isStopwatchRunning(Player player) {
        return stopwatchRunningMap.getOrDefault(player.getUniqueId(), false);
    }

    public void startStopwatch(Player player) {
        PlayerData playerData = playerDataMap.get(player.getUniqueId());

        if (playerData != null) {
            playerData.startStopwatch();
        }

        if (!isStopwatchRunning(player)) {
            // Reset the start time to 0 for each new game

            stopwatchRunningMap.put(player.getUniqueId(), true);
            gameBarrier.giveBarrierToPlayer(player);
            dataManager.loadPlayerData(player.getUniqueId(), player);
            addPlayerToGame(player.getUniqueId());
        }
    }

    public void stopStopwatch(Player player) {
        if (isStopwatchRunning(player)) {
            String playerName = player.getName();
            double timeInSeconds = getElapsedTimeSeconds(player);
            String mazeName = mazeNameMap.getOrDefault(player.getUniqueId(), null);
            String difficulty = difficultyMap.getOrDefault(player.getUniqueId(), "easy");

            // Check if mazeName is not null before stopping the game
            if (mazeName != null) {
                gameEndings.stopGame(player, timeInSeconds, mazeName, playerName, difficulty);

                if (isPlayerWinner) {
                    leaderboardManager.addPlayerTime(playerName, timeInSeconds, mazeName + difficulty);
                }
            }

            scoreboardManager.setLobbyScoreboard(player);
            gameBarrier.removeBarrierFromPlayer(player);
            dataManager.savePlayerData(player.getUniqueId(), player);
            dataManager.loadPlayerData(player.getUniqueId(), player);
            stopwatchRunningMap.put(player.getUniqueId(), false);
            removePlayerFromGame(player.getUniqueId());
        }
    }

    //COUNTDOWN CODE
    private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();


    public double getElapsedTimeSeconds(Player player) {
        UUID playerId = player.getUniqueId();
        if (playerDataMap.containsKey(playerId) && playerDataMap.get(playerId).isStopwatchRunning()) {
            long startTimeNano = startTimeMap.getOrDefault(playerId, System.nanoTime());
            long elapsedTimeNano = System.nanoTime() - startTimeNano;
            return elapsedTimeNano / 1e9;
        }
        return 0;
    }

    public void sendElapsedTimeToActionBar(Player player) {
        if(isStopwatchRunning(player)) {
            double elapsedTime = getElapsedTimeSeconds(player);
            String formattedTime = formatElapsedTime(elapsedTime);
            String actionBarMessage = formattedTime;
            sendActionBarToPlayers(player, actionBarMessage);
        }
    }

    // Method to send titles to multiple players
    public void sendTitlesToPlayers(Player player, ChatColor color, String title, String subtitle) {
        player.sendTitle(color + title, subtitle, 10, 70, 20);
    }

    public static void sendActionBarToPlayers(Player player, String message) {
        if (message.length() > 64) {
            message = message.substring(0, 64); // Truncate the message if it's too long
        }

        // Send the action bar message to each player in the list
        player.sendActionBar(ChatColor.GREEN + message);
    }

    private String formatElapsedTime(double elapsedTime) {
        int minutes = (int) (elapsedTime / 60);
        int seconds = (int) (elapsedTime % 60);
        int millis = (int) ((elapsedTime - Math.floor(elapsedTime)) * 100);

        return String.format("%02d:%02d.%02d", minutes, seconds, millis);
    }

    public Boolean isReconnectEnabled() {
        return  reconnectManager.isReconnectEnabled();
    }

    // Method to add a player to the game
    public void addPlayerToGame(UUID playerId) {
        playersInGame.add(playerId);
    }

    // Method to remove a player from the game
    public void removePlayerFromGame(UUID playerId) {
        playersInGame.remove(playerId);
    }
}

