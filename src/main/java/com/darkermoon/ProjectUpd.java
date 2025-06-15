package com.darkermoon;

import org.bukkit.plugin.java.JavaPlugin;

import com.darkermoon.ability.metal.metalcables.MetalHookListener;
import com.darkermoon.configs.Config;
import com.darkermoon.listener.DarkerListeners;
import com.darkermoon.listener.FieryRageListener;
import com.projectkorra.projectkorra.ability.CoreAbility;

public final class ProjectUpd extends JavaPlugin {

    public static ProjectUpd plugin;

    @Override
    public void onEnable() {
        plugin = this;

        getLogger().info("Init config");
        new Config(this);
        getLogger().info("Init abilities");

        try {
            CoreAbility.registerPluginAbilities(plugin, "com.darkermoon.ability");
            getLogger().info("Abilities registered successfully.");
        } catch (Exception e) {
            getLogger().severe("Error registering abilities: " + e.getMessage());
            e.printStackTrace();
        }

        registerListeners();

        plugin.getLogger().info("Successfully enabled ProjectUpd.");
    }

    @Override
    public void onDisable() {
        plugin.getLogger().info("Successfully disabled ProjectUpd.");
    }

    public static ProjectUpd getInstance() {
        return plugin;
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new DarkerListeners(), this);
        getServer().getPluginManager().registerEvents(new FieryRageListener(), this);
        getServer().getPluginManager().registerEvents(new MetalHookListener(), this);
    }
}