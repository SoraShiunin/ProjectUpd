package com.darkermoon.ability.water;

import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.darkermoon.ProjectUpd;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.WaterAbility;
import com.projectkorra.projectkorra.util.BlockSource;
import com.projectkorra.projectkorra.util.ClickType;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.util.TempBlock;
import com.projectkorra.projectkorra.waterbending.util.WaterReturn;

public class Stream extends WaterAbility {
   private long cooldown;
   private long duration;
   private double damage;
   private double range;
   private double startHealth;
   private TempBlock tempBlock;
   private Block sourceBlock;
   private Location location;
   private Location sourceLoc;

   public Stream(Player player) {
      super(player);
      if (this.bPlayer.canBend(this)) {
         if (player.isSneaking()) {
            if (!WaterReturn.hasWaterBottle(player)) {
               this.sourceBlock = BlockSource.getWaterSourceBlock(player, 8.0D, ClickType.SHIFT_DOWN, true, true, true, true, true);
            } else {
               this.sourceBlock = player.getEyeLocation().clone().getBlock();
            }

            if (!WaterReturn.hasWaterBottle(player) && this.sourceBlock != BlockSource.getWaterSourceBlock(player, 8.0D, ClickType.SHIFT_DOWN, true, true, true, true, true)) {
               this.sourceBlock = null;
            }

            if (this.sourceBlock != null) {
               this.setFields();
               this.start();
            }
         }
      }
   }

   public void setFields() {
      this.cooldown = ProjectUpd.plugin.getConfig().getLong("Abilities.Water.Stream.Cooldown");
      this.duration = ProjectUpd.plugin.getConfig().getLong("Abilities.Water.Stream.Duration");
      this.range = ProjectUpd.plugin.getConfig().getDouble("Abilities.Water.Stream.Range");
      this.damage = ProjectUpd.plugin.getConfig().getDouble("Abilities.Water.Stream.Damage");
      this.startHealth = this.player.getHealth();
      this.sourceLoc = this.sourceBlock.getLocation();
      if (this.sourceBlock.getLocation().getY() >= this.player.getLocation().add(0.0D, 1.0D, 0.0D).getY()) {
         this.location = this.sourceBlock.getLocation().clone();
      } else {
         this.location = this.sourceBlock.getLocation().add(0.0D, 1.0D, 0.0D).clone();
      }

      if (this.player.getLocation().distance(this.location) > 3.0D && this.player.isSneaking()) {
         Vector first = GeneralMethods.getDirection(this.sourceLoc, this.player.getLocation().add(0.0D, 1.0D, 0.0D).add(this.player.getEyeLocation().getDirection().multiply(3)));
         first.normalize().multiply(1);
         this.location.add(first);
      }

      if (WaterReturn.hasWaterBottle(this.player)) {
         WaterReturn.emptyWaterBottle(this.player);
      }

   }

   public void progress() {
      if (!this.bPlayer.canBendIgnoreCooldowns(this)) {
         this.remove();
      } else if (System.currentTimeMillis() - this.getStartTime() > this.duration) {
         this.remove();
      } else {//This is what lets the block spawn as.
         if (!WaterReturn.hasWaterBottle(this.player) && !isWaterbendable(this.sourceBlock.getType())) {
            this.sourceBlock.setType(Material.AIR);
         } 

         if (this.player.getLocation().distance(this.location) > this.range && !this.player.isSneaking()) {
            this.remove();
         } else {
            if (isTransparent(this.player, this.location.getBlock()) || this.location.getBlock().getType() == Material.CACTUS && !this.location.getBlock().isLiquid()) {
               GeneralMethods.breakBlock(this.location.getBlock());
            } else if (this.location.getBlock().getType() != Material.AIR && !isWaterbendable(this.location.getBlock()) && !isIcebendable(this.location.getBlock()) && 
            		!isPlantbendable(this.location.getBlock())) {
               this.remove();
               return;
            }

            if (this.player.getHealth() < this.startHealth - 2.0D) {
               this.remove();
            } else {
                if (isPlantbendable(this.sourceBlock)) {
                	//System.out.println("Source block is short grass");
                	this.tempBlock = new TempBlock(this.location.getBlock(), Material.OAK_LEAVES);
                	this.location.getWorld().playSound(this.location, Sound.BLOCK_GRASS_BREAK, 0.2F, 1.0F);
                }
                else if (isIcebendable(this.sourceBlock)) {
                	//System.out.println("Source block is Ice");
                	this.tempBlock = new TempBlock(this.location.getBlock(), Material.ICE);
                	playIcebendingSound(this.location);
                }
                else {
                	//System.out.println("Defaulting to water");
                	this.tempBlock = new TempBlock(this.location.getBlock(), Material.WATER, GeneralMethods.getWaterData(8));
                	this.location.getWorld().playSound(this.location, Sound.BLOCK_WATER_AMBIENT, 0.2F, 0.8F);
                }
               
               this.tempBlock.setRevertTime(600L);
               if (!this.player.isSneaking()) {
                  this.location.add(this.player.getEyeLocation().getDirection());
               }

               if (this.player.isSneaking()) {
                  Vector back = GeneralMethods.getDirection(this.location, this.player.getLocation().add(0.0D, 1.0D, 0.0D).add(this.player.getLocation().getDirection().multiply(3)));
                  back.normalize().multiply(1);
                  this.location.add(back);
               }

               Iterator var2 = GeneralMethods.getEntitiesAroundPoint(this.location, 1.5D).iterator();

               Location location;
               Vector vector;
               Entity e;
               while(var2.hasNext()) {
                  e = (Entity)var2.next();
                  if (!this.player.isSneaking() && this.player.getLocation().distance(this.location) > 4.0D && e instanceof LivingEntity && e.getEntityId() != this.player.getEntityId()) {
                     location = this.player.getEyeLocation();
                     vector = location.getDirection();
                     e.setVelocity(vector.normalize().multiply(0.5F));
                     DamageHandler.damageEntity(e, this.damage, this);
                  }

                  if (this.player.isSneaking() && this.player.getLocation().distance(this.location) > 4.0D && e instanceof LivingEntity && e.getEntityId() != this.player.getEntityId()) {
                     location = this.player.getEyeLocation();
                     vector = location.getDirection();
                     e.setVelocity(vector.normalize().multiply(-0.5F));
                     DamageHandler.damageEntity(e, this.damage, this);
                  }

                  if (this.player.getLocation().distance(this.location) <= 4.0D && e instanceof LivingEntity && e.getEntityId() != this.player.getEntityId()) {
                     location = this.player.getEyeLocation();
                     vector = location.getDirection();
                     e.setVelocity(vector.normalize().multiply(0.5F));
                     DamageHandler.damageEntity(e, this.damage, this);
                  }
               }

               var2 = GeneralMethods.getEntitiesAroundPoint(this.location, 1.0D).iterator();

               while(var2.hasNext()) {
                  e = (Entity)var2.next();
                  if (!this.player.isSneaking() && this.player.getLocation().distance(this.location) > 4.0D && e instanceof Entity && e.getEntityId() != this.player.getEntityId()) {
                     location = this.player.getEyeLocation();
                     vector = location.getDirection();
                     e.setVelocity(vector.normalize().multiply(-0.5F));
                  }

                  if (this.player.isSneaking() && this.player.getLocation().distance(this.location) > 4.0D && e instanceof Entity && e.getEntityId() != this.player.getEntityId()) {
                     location = this.player.getEyeLocation();
                     vector = location.getDirection();
                     e.setVelocity(vector.normalize().multiply(-0.5F));
                  }

                  if (this.player.getLocation().distance(this.location) <= 4.0D && e instanceof Entity && e.getEntityId() != this.player.getEntityId()) {
                     location = this.player.getEyeLocation();
                     vector = location.getDirection();
                     e.setVelocity(vector.normalize().multiply(0.5F));
                  }
               }

            }
         }
      }
   }

   public long getCooldown() {
      return this.cooldown;
   }

   public Location getLocation() {
      return this.location;
   }

   public String getName() {
      return "Stream";
   }

   public boolean isHarmlessAbility() {
      return false;
   }

   public boolean isSneakAbility() {
      return true;
   }

   public void remove() {
      this.bPlayer.addCooldown(this);
      new WaterReturn(this.player, this.location.add(0.0D, 1.0D, 0.0D).getBlock());
      super.remove();
   }
   @Override
	public boolean isEnabled() {
		return ProjectUpd.plugin.getConfig().getBoolean("Abilities.Water.Stream.Enabled");
	 }
	 @Override
	 public String getDescription() {
		return ProjectUpd.plugin.getConfig().getString("Language.Abilities.Water.Stream.Description");
	 }
  
	 @Override
	 public String getInstructions() {
		return ProjectUpd.plugin.getConfig().getString("Language.Abilities.Water.Stream.Instructions");
	 } 

}
