package com.darkermoon.ability.water;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.darkermoon.ProjectUpd;
import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ability.PassiveAbility;
import com.projectkorra.projectkorra.ability.WaterAbility;
import com.projectkorra.projectkorra.util.ParticleEffect;

public final class WaterAffinity extends WaterAbility implements PassiveAbility {

    public double ifHealth;
    public double powerLevel;
    public int regenLv;
    private double step = 0.0;
    private boolean particles;

    public WaterAffinity(Player player) {
        super(player);

        setFields();
        start();
    }

    public void setFields() {
        this.ifHealth = ProjectUpd.plugin.getConfig().getDouble("Abilities.Water.WaterAffinity.ifHealth");
        this.particles = ProjectUpd.plugin.getConfig().getBoolean("Abilities.Water.WaterAffinity.particles");
    }

    @Override
    public void progress() {

                if (player.getHealth() <= this.ifHealth) {
                    if (particles) {
                        step += 0.05;
                        if (step >= 2) {
                            step = 0;
                            if (bPlayer.hasElement(Element.WATER)) {
                                if (player.getLocation().getBlock().getType() == Material.WATER) {
                                	ParticleEffect.DRIP_WATER.display(player.getLocation().add(0, 1.7, 0).add(player.getLocation().getDirection().normalize().multiply(0.6)), 6, .2, .2, .2, 0.02);
                                	player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 50, 0));
                                }
                            }

                        }
                    }
                } 
         }

    @Override
    public boolean isSneakAbility() {
        return false;
    }

    @Override
    public boolean isHarmlessAbility() {
        return true;
    }

    @Override
    public long getCooldown() {
        return 0;
    }

    @Override
    public String getName() {
        return "WaterAffinity";
    }

    @Override
    public Location getLocation() {
        return null;
    }

    @Override
    public boolean isInstantiable() {
        return true;
    }

    @Override
    public boolean isProgressable() {
        return true;
    }
    @Override
	public boolean isEnabled() {
		return ProjectUpd.plugin.getConfig().getBoolean("Abilities.Water.WaterAffinity.Enabled");
	 }
	 @Override
	 public String getDescription() {
		return ProjectUpd.plugin.getConfig().getString("Language.Abilities.Water.WaterAffinity.Description");
	 }
  
	 @Override
	 public String getInstructions() {
		return ProjectUpd.plugin.getConfig().getString("Language.Abilities.Water.WaterAffinity.Instructions");
	 } 
}
