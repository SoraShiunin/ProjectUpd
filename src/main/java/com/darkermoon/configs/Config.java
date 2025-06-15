package com.darkermoon.configs;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import com.darkermoon.ProjectUpd;
import com.projectkorra.projectkorra.configuration.ConfigManager;

public class Config {

    private static ProjectUpd plugin;

    public Config(ProjectUpd plugin) {
        Config.plugin = plugin;
        loadConfig();
    }

    private void loadConfig() {
        FileConfiguration config = ProjectUpd.plugin.getConfig();
        FileConfiguration language = ConfigManager.languageConfig.get();

        //Descriptions & Instructions
        config.addDefault("Language.Abilities.BlueFire.SuperHeat.Description", "This ability allows the user to superheat items in hand, causing them to cook or melt faster and even provide bonuses due to the accuracy of the heat");
        config.addDefault("Language.Abilities.BlueFire.SuperHeat.Instructions", "Hold shift: Cook");
      
        config.addDefault("Language.Abilities.Fire.FieryCalm.Description", "While in high spirits, you have higher control. While you are high on hp, your cooldown and charge times are decreased slightly.");
        config.addDefault("Language.Abilities.Fire.FieryCalm.Instructions", "Passive Ability");
        
        config.addDefault("Language.Abilities.Fire.FieryRage.Description", "Adrenaline changes you. It's time for a flight or fight response. Now that you are reaching the end point, you know to focus and reach your full potential. When low on HP, your bending prowess is increased.");
        config.addDefault("Language.Abilities.Fire.FieryRage.Instructions", "Passive Ability");
                
        config.addDefault("Language.Abilities.Water.WaterAffinity.Description", "Due to waters natural spiritual properties, you gain Regeneration in water when low on health as you get closer to the spirit world.");
        config.addDefault("Language.Abilities.Water.WaterAffinity.Instructions", "Passive Ability");

        config.addDefault("Language.Abilities.Water.Stream.Description", "Stream is a versatile move for waterbenders. This technique can be used to deflect projectiles and swipe close range threats, and when fired knocks your opponent backwards. Additionally the whip can be used to pull entities towards you. If the player is damaged while controlling the stream it will dissapate. ");
        config.addDefault("Language.Abilities.Water.Stream.Instructions", "(Stream) Hold Shift to select a source, then click to raise a stream of water that can be directed. (Whip) Release shift to fire, and hold shift to bring it back. ");
        
        config.addDefault("Language.Abilities.Water.Whip.Description", "Use a more concentrated source of bending to create a whip. This whip can be used to attack entities, and can be used drag entities along it.");
        config.addDefault("Language.Abilities.Water.Whip.Instructions", "Hold Shift and Left Click to shoot.");
        
        config.addDefault("Language.Abilities.Water.Blood.Siphon.Description", "Using your bloodbending skills, as well as your skills of a healer, use your bending prowess to corrupt someones blood, meanwhile enhancing your own using their spiritual energy.");
        config.addDefault("Language.Abilities.Water.Blood.Siphon.Instructions", "HealingWaters (Left Click) > Bloodbending (Left click)");
        
        config.addDefault("Language.Abilities.Earth.Metal.MetalCables.Description", "MetalCables allows a metalbender to extend metal cables from their arms, granting great mobility and a small attack function. The cables can be used to pull yourself to certain places, pull entities towards you, and damage entities. Each sub ability has a left and right version, one for both hands. All functions activate on click.\\n" + //
                        "Grapple: Launch a cable that will travel forward. Once it has hit something, you'll hear a metal sound. Then hold sneak to pull yourself towards the hit point. Releasing sneak near the hit point will cause the cable to disappear.\\n" + //
                        "Grapple both: This function will launch both grapple cables at once. Sneaking on this ability will pull you towards the middle point of both cables, or to a single one if only a single one is shot and has hit something.\\n" + //
                        "Grapple clear: This function will make all shot grapple cables dissapear.\\n" + //
                        "Grab: This function will shoot out a cable that will wrap around the first entity in it's path (Not the user). Sneak to pull the entity towards yourself. Note that the tangled entity can resist. A firejetting target will be harder to pull in than a walking one. The tangled entity can only use basic bending moves while tangled.\\n" + //
                        "Slam: This function will shoot out a cable that will damage the first entity it hits (Not the user).\\n" + //
                        "Exit: This function will put the user out of the multiability. Once this is done, the cooldown is applied.");
        config.addDefault("Language.Abilities.Earth.Metal.MetalCables.Instructions", "Click and either: Bonk, Yoink, Yeet");
        
        config.addDefault("Language.Abilities.Chi.HoverBoard.Description", "HoverBoard allows a chi blocker to create a hoverboard that they can ride on. The hoverboard can be used to fly aroun. The hoverboard will explode if it hits something.");
        config.addDefault("Language.Abilities.Chi.HoverBoard.Instructions", "Left click to put the hoverboard in motion, then (tap shift) to dismount your hoverboard and send it flying forwards");
        
        
        config.addDefault("Language.Abilities.Earth.Lava.LavaSlip.Description", "Use a more concentrated source of bending to control lava. This ability allows you to shoot a stream of lava that can be directed. It increases the size the longer you use it");
        config.addDefault("Language.Abilities.Earth.Lava.LavaSlip.Instructions", "Hold Shift and Left Click to shoot.");
        
        config.addDefault("Abilities.BlueFire.SuperHeat.Enabled", true);
        config.addDefault("Abilities.BlueFire.SuperHeat.Cook.Interval", 300);

        config.addDefault("Abilities.Fire.FieryCalm.Enabled", true);
        config.addDefault("Abilities.Fire.FieryCalm.powerLevel", 1.0);
        config.addDefault("Abilities.Fire.FieryCalm.coolDownLevel", 0.8);
        config.addDefault("Abilities.Fire.FieryCalm.chargedurationlevel", 0.8);
        config.addDefault("Abilities.Fire.FieryCalm.ifHealth", 36.0);
        config.addDefault("Abilities.Fire.FieryCalm.chargeduration", true);

        config.addDefault("Abilities.Fire.FieryRage.Enabled", true);
        config.addDefault("Abilities.Fire.FieryRage.powerLevel", 1.5);
        config.addDefault("Abilities.Fire.FieryRage.coolDownLevel", 0.8);
        config.addDefault("Abilities.Fire.FieryRage.chargedurationlevel", 1);
        config.addDefault("Abilities.Fire.FieryRage.ifHealth", 8.0);
        config.addDefault("Abilities.Fire.FieryRage.speed", true);
        config.addDefault("Abilities.Fire.FieryRage.damage", true);
        config.addDefault("Abilities.Fire.FieryRage.range", true);
        config.addDefault("Abilities.Fire.FieryRage.cooldown", true);
        config.addDefault("Abilities.Fire.FieryRage.particles", true);
        config.addDefault("Abilities.Fire.FieryRage.chargeduration", true);
        
        config.addDefault("Abilities.Water.WaterAffinity.Enabled", true);
        config.addDefault("Abilities.Water.WaterAffinity.ifHealth", 10.0);
        config.addDefault("Abilities.Water.WaterAffinity.particles", true);
        
        config.addDefault("Abilities.Water.Stream.Enabled", true);
        config.addDefault("Abilities.Water.Stream.Cooldown", 1200);
        config.addDefault("Abilities.Water.Stream.Duration", 20000);
        config.addDefault("Abilities.Water.Stream.Range", 15);
        config.addDefault("Abilities.Water.Stream.Damage", 2);
        config.addDefault("Abilities.Water.Stream.Harmless", true);

        config.addDefault("Abilities.Water.Whip.Enabled", true);
        config.addDefault("Abilities.Water.Whip.Cooldown", 1000);
        config.addDefault("Abilities.Water.Whip.Range", 11);
        config.addDefault("Abilities.Water.Whip.Damage", 4);
        config.addDefault("Abilities.Water.Whip.Swim.Disabled", false);

        config.addDefault("Abilities.Earth.Lava.LavaSlip.Enabled", false);
        config.addDefault("Abilities.Earth.Lava.LavaSlip.Cooldown", 2000);
        config.addDefault("Abilities.Earth.Lava.LavaSlip.Range", 14);
        config.addDefault("Abilities.Earth.Lava.LavaSlip.Damage", 4);
        config.addDefault("Abilities.Earth.Lava.LavaSlip.Swim.Disabled", false);
        config.addDefault("Abilities.Earth.Lava.LavaSlip.MaxChargeTime", 3000);
    
        config.addDefault("Abilities.Water.Blood.Combo.Siphon.Enabled", true);
        config.addDefault("Abilities.Water.Blood.Combo.Siphon.Cooldown", 4000);
        config.addDefault("Abilities.Water.Blood.Combo.Siphon.SelectRange", 6.0);
        config.addDefault("Abilities.Water.Blood.Combo.Siphon.Duration", 1700);
        config.addDefault("Abilities.Water.Blood.Combo.Siphon.Damage", 3);

        config.addDefault("Abilities.Earth.Metal.MetalCables.Enabled", true);
        config.addDefault("Abilities.Earth.Metal.MetalCables.AllowUseWithoutItems", true);
        config.addDefault("Abilities.Earth.Metal.MetalCables.ArmorRequirements.Helmet", new String[]{Material.IRON_HELMET.name(), Material.GOLDEN_HELMET.name(), Material.CHAINMAIL_HELMET.name()});
        config.addDefault("Abilities.Earth.Metal.MetalCables.ArmorRequirements.Chestplate", new String[]{Material.IRON_CHESTPLATE.name(), Material.GOLDEN_CHESTPLATE.name(), Material.CHAINMAIL_CHESTPLATE.name()});
        config.addDefault("Abilities.Earth.Metal.MetalCables.ArmorRequirements.Leggings", new String[]{Material.IRON_LEGGINGS.name(), Material.GOLDEN_LEGGINGS.name(), Material.CHAINMAIL_LEGGINGS.name()});
        config.addDefault("Abilities.Earth.Metal.MetalCables.ArmorRequirements.Boots", new String[]{Material.IRON_BOOTS.name(), Material.GOLDEN_BOOTS.name(), Material.CHAINMAIL_BOOTS.name()});
        config.addDefault("Abilities.Earth.Metal.MetalCables.ItemRequirements", new String[]{Material.GOLD_ORE.name(), Material.IRON_ORE.name(), Material.GOLD_BLOCK.name(), Material.IRON_BLOCK.name(), Material.IRON_DOOR.name(), Material.IRON_BARS.name(), Material.GOLDEN_CHESTPLATE.name(), Material.IRON_CHESTPLATE.name(), Material.IRON_TRAPDOOR.name(), Material.IRON_SHOVEL.name(), Material.IRON_PICKAXE.name(), Material.IRON_AXE.name(), Material.IRON_INGOT.name(), Material.GOLD_INGOT.name(), Material.IRON_SWORD.name(), Material.GOLDEN_SWORD.name(), Material.GOLDEN_SHOVEL.name(), Material.GOLDEN_PICKAXE.name(), Material.GOLDEN_AXE.name(), Material.IRON_HOE.name(), Material.GOLDEN_HOE.name(), Material.IRON_HELMET.name(), Material.IRON_CHESTPLATE.name(), Material.IRON_LEGGINGS.name(), Material.IRON_BOOTS.name(), Material.GOLDEN_HELMET.name(), Material.GOLDEN_CHESTPLATE.name(), Material.GOLDEN_LEGGINGS.name(), Material.GOLDEN_BOOTS.name(), Material.GOLDEN_APPLE.name(), Material.IRON_DOOR.name(), Material.IRON_HORSE_ARMOR.name(), Material.GOLDEN_HORSE_ARMOR.name()});
        config.addDefault("Abilities.Earth.Metal.MetalCables.Cooldown", 10000L);
        config.addDefault("Abilities.Earth.Metal.MetalCables.Uses", 100);
        config.addDefault("Abilities.Earth.Metal.MetalCables.Grapple.Range", 150);
        config.addDefault("Abilities.Earth.Metal.MetalCables.Grapple.Speed", 5.0D);
        config.addDefault("Abilities.Earth.Metal.MetalCables.Grapple.PullSpeed", 2.0D);
        config.addDefault("Abilities.Earth.Metal.MetalCables.Grapple.MaximumSwingAngle", 15.0D);
        config.addDefault("Abilities.Earth.Metal.MetalCables.Grab.Cooldown", 2000);
        config.addDefault("Abilities.Earth.Metal.MetalCables.Grab.Range", 60);
        config.addDefault("Abilities.Earth.Metal.MetalCables.Grab.Speed", 5.0D);
        config.addDefault("Abilities.Earth.Metal.MetalCables.Grab.PullSpeed", 0.5D);
        config.addDefault("Abilities.Earth.Metal.MetalCables.Slam.Cooldown", 1500);
        config.addDefault("Abilities.Earth.Metal.MetalCables.Slam.Range", 60);
        config.addDefault("Abilities.Earth.Metal.MetalCables.Slam.Speed", 5.0D);
        config.addDefault("Abilities.Earth.Metal.MetalCables.Slam.Damage", 1.0D);


        config.addDefault("Abilities.Chi.HoverBoard.Enabled",true);
        config.addDefault("Abilities.Chi.HoverBoard.JumpVelocity",1.5);
        config.addDefault("Abilities.Chi.HoverBoard.Damage",3);
        config.addDefault("Abilities.Chi.HoverBoard.Duration",20000);
        config.addDefault("Abilities.Chi.HoverBoard.ShootRange",13);
        config.addDefault("Abilities.Chi.HoverBoard.Cooldown",30000);
        config.addDefault("Abilities.Chi.HoverBoard.ExplodeSize",2);
        config.addDefault("Abilities.Chi.HoverBoard.PlayerExplodeDamage",3);
        config.addDefault("Abilities.Chi.HoverBoard.HoverHeight",2);
        config.addDefault("Abilities.Chi.HoverBoard.RideSpeed",0.75);
        config.addDefault("Abilities.Chi.HoverBoard.ShootSpeed",1.0);
        config.addDefault("Abilities.Chi.HoverBoard.ShootHitbox",0.6);
        
        
        
        
        
        
        ConfigManager.languageConfig.save();
        config.options().copyDefaults(true);
        plugin.saveConfig();
    }
}