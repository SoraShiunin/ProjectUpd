package com.darkermoon.ability.blood;

import com.darkermoon.ProjectUpd;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.BloodAbility;
import com.projectkorra.projectkorra.ability.ComboAbility;
import com.projectkorra.projectkorra.ability.util.ComboManager.AbilityInformation;
import com.projectkorra.projectkorra.attribute.Attribute;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.ClickType;
import com.projectkorra.projectkorra.util.DamageHandler;

import java.util.ArrayList;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Siphon extends BloodAbility implements ComboAbility {
   @Attribute("Cooldown")
   private long cooldown;
   private double selectRange;
   private double damage;
   private long duration;
   private Entity entity;
   private Location destination;
   private Location lineLoc;
   double lineLength = 0.0D;

   public Siphon(Player player) {
      super(player);
      if (this.bPlayer.canBendIgnoreBindsCooldowns(this) && !this.bPlayer.isOnCooldown(this)) {
         this.setFields();
         this.entity = GeneralMethods.getTargetedEntity(player, this.selectRange);
         if (this.entity != null) {
            this.lineLoc = player.getLocation().add(0.0D, 1.0D, 0.0D);
            this.bPlayer.addCooldown(this);
            //((LivingEntity)this.entity).addPotionEffect(new PotionEffect(PotionEffectType.HARM, 10, 0));
            DamageHandler.damageEntity(entity, damage, this);
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 70, 2));
            this.start();
         }
      }
   }

   public void setFields() {
      this.cooldown = ProjectUpd.plugin.getConfig().getLong("Abilities.Water.Blood.Combo.Siphon.Cooldown");
      this.damage = ProjectUpd.plugin.getConfig().getLong("Abilities.Water.Blood.Combo.Siphon.Damage"); 
      this.selectRange = ProjectUpd.plugin.getConfig().getDouble("Abilities.Water.Combo.Blood.Siphon.SelectRange");
      this.duration = ProjectUpd.plugin.getConfig().getLong("Abilities.Water.Blood.Combo.Siphon.Duration");
   }

   public void progress() {
      if (!this.player.isDead() && this.player.isOnline()) {
         if (this.entity.isDead()) {
            this.remove();
         } else if (this.entity instanceof Player && !((Player)this.entity).isOnline()) {
            this.remove();
         } else {
            this.destination = this.player.getLocation().add(0.0D, 1.0D, 0.0D);
            this.lineLoc = this.entity.getLocation().add(0.0D, 1.0D, 0.0D);
            Vector lineDir = GeneralMethods.getDirection(this.lineLoc, this.destination);
            double distance = this.entity.getLocation().distance(this.destination);
            if (this.lineLength <= distance) {
               this.lineLength += 0.2D;
            } else {
               this.lineLength = distance;
            }

            for(double i = 0.0D; i < this.lineLength; i += 0.3D) {
               this.lineLoc = this.lineLoc.add(lineDir.normalize().multiply(0.3D));
               if (Math.random() < 0.7D) {
                  this.player.getWorld().spawnParticle(Particle.DUST, this.lineLoc, 2, 0.0D, 0.0D, 0.0D, 0.04D, new DustOptions(Color.fromRGB(44, 85, 219), 1.0F));
               }
            }

            if (this.lineLength > this.selectRange * 3.0D) {
               this.remove();
            }

            if (this.player.getWorld() != this.entity.getWorld()) {
               this.remove();
            }

            if (System.currentTimeMillis() > this.getStartTime() + this.duration) {
               this.remove();
            }
         }
      } else {
         this.remove();
      }
   }

   public boolean isSneakAbility() {
      return false;
   }

   public boolean isHarmlessAbility() {
      return false;
   }

   public long getCooldown() {
      return this.cooldown;
   }

   public String getName() {
      return "Siphon";
   }

   public Location getLocation() {
      return null;
   }

   public void stop() {
   }


   public Object createNewComboInstance(Player player) {
      return new Siphon(player);
   }

   public ArrayList<AbilityInformation> getCombination() {
      ArrayList<AbilityInformation> combo = new ArrayList<AbilityInformation>();
      combo.add(new AbilityInformation("HealingWaters", ClickType.LEFT_CLICK));
      combo.add(new AbilityInformation("Bloodbending", ClickType.LEFT_CLICK));
      return combo;
   }

   
   @Override
   public boolean isEnabled() {
      return ProjectUpd.plugin.getConfig().getBoolean("Abilities.Water.Blood.Combo.Siphon.Enabled");
   }
   @Override
   public String getDescription() {
      return ProjectUpd.plugin.getConfig().getString("Language.Abilities.Water.Blood.Combo.Siphon.Description");
   }

   @Override
   public String getInstructions() {
      return ProjectUpd.plugin.getConfig().getString("Language.Abilities.Water.Blood.Combo.Siphon.Instructions");
   } 

   /*public boolean isEnabled() {
      return ConfigManager.getConfig().getBoolean("ExtraAbilities.xLumos.Transfusion.Enabled", true);
   }*/

}
