package me.elec.mazerunnercore;

public class Reward {
    private int level;
    private String permission;
    private String cosmeticName;

    public Reward(int level, String permission, String cosmeticName) {
        this.level = level;
        this.permission = permission;
        this.cosmeticName = cosmeticName;
    }

    public int getLevel() {
        return level;
    }

    public String getPermission() {
        return permission;
    }

    public String getCosmeticName() {
        return cosmeticName;
    }
}