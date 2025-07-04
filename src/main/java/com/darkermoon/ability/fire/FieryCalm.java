package com.darkermoon.ability.fire;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.darkermoon.ProjectUpd;
import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ability.FireAbility;
import com.projectkorra.projectkorra.ability.PassiveAbility;
import com.projectkorra.projectkorra.util.ParticleEffect;

public class FieryCalm extends FireAbility implements PassiveAbility {

    public static final Set<Player> calmPlayers = new HashSet<>();

    public double ifHealth;
    public double fcpowerLevel;
    public double fccmodifier;
    private double step = 0.0;
    private boolean particles;
    public boolean speed;
    public boolean damage;
    public boolean range;
    public boolean cooldown;
    public boolean chargemodifier;
    public double chargemodifierlevel;

    public FieryCalm(Player player) {
        super(player);

        setFields();
    }

    public void setFields() {
        this.fcpowerLevel = ProjectUpd.plugin.getConfig().getDouble("Abilities.Fire.FieryCalm.powerLevel");
        this.fccmodifier = ProjectUpd.plugin.getConfig().getDouble("Abilities.Fire.FieryCalm.coolDownLevel");
        this.chargemodifierlevel = ProjectUpd.plugin.getConfig().getDouble("Abilities.Fire.FieryCalm.chargedurationlevel");
        this.chargemodifier = ProjectUpd.plugin.getConfig().getBoolean("Abilities.Fire.FieryCalm.chargeduration");
        this.ifHealth = ProjectUpd.plugin.getConfig().getDouble("Abilities.Fire.FieryCalm.ifHealth");
        this.cooldown = ProjectUpd.plugin.getConfig().getBoolean("Abilities.Fire.FieryCalm.cooldown");
        this.particles = ProjectUpd.plugin.getConfig().getBoolean("Abilities.Fire.FieryCalm.particles");
    }

    @Override
    public void progress() {
                if (player.getHealth() >= this.ifHealth) {
                    calmPlayers.add(player);
                    if (particles) {
                        step += 0.05;
                        if (step >= 2) {
                            step = 0;
                            if (bPlayer.hasElement(Element.BLUE_FIRE)) {
                            	ParticleEffect.SOUL_FIRE_FLAME.display(player.getLocation().add(0, 1.7, 0).add(player.getLocation().getDirection().normalize().multiply(0.6)), 6, .2, .2, .2, 0.02);
                            }
                            else {
                            	ParticleEffect.FLAME.display(player.getLocation().add(0, 1.7, 0).add(player.getLocation().getDirection().normalize().multiply(0.6)), 6, .2, .2, .2, 0.02);
                            }
                            
                        }
                    }
                } else {
                    calmPlayers.remove(player);
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
        return "FieryCalm";
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
		return ProjectUpd.plugin.getConfig().getBoolean("Abilities.Fire.FieryCalm.Enabled");
	 }
	 @Override
	 public String getDescription() {
		return ProjectUpd.plugin.getConfig().getString("Language.Abilities.Fire.FieryCalm.Description");
	 }
  
	 @Override
	 public String getInstructions() {
		return ProjectUpd.plugin.getConfig().getString("Language.Abilities.Fire.FieryCalm.Instructions");
	 } 
}
