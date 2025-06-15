package com.darkermoon.ability.water;

import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.darkermoon.ProjectUpd;
import com.projectkorra.projectkorra.Element.SubElement;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.WaterAbility;
import com.projectkorra.projectkorra.util.BlockSource;
import com.projectkorra.projectkorra.util.ClickType;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.util.ParticleEffect;
import com.projectkorra.projectkorra.util.TempBlock;
import com.projectkorra.projectkorra.waterbending.util.WaterReturn;

public class Whip extends WaterAbility  {
   private long cooldown;
   private double range;
   private double damage;
   private TempBlock tempBlock;
   private Location location;
   private Block sourceBlock;
   private Block above;
   private Vector direction;

   public Whip(Player player) {
      super(player);
      if (!this.bPlayer.isOnCooldown(this)) {
         if (player.isSneaking()) {
            this.setFields();
         }
      }
   }

   public void setFields() {
      this.cooldown = ProjectUpd.plugin.getConfig().getLong("Abilities.Water.Whip.Cooldown");
      this.range = ProjectUpd.plugin.getConfig().getDouble("Abilities.Water.Whip.Range");
      this.damage = ProjectUpd.plugin.getConfig().getDouble("Abilities.Water.Whip.Damage");
         
      //this range might need its own SourceRange
      if (!WaterReturn.hasWaterBottle(this.player)) {
         this.sourceBlock = BlockSource.getWaterSourceBlock(this.player, range, ClickType.SHIFT_DOWN, true, true, true, true, true);
      } else {
         this.sourceBlock = this.player.getEyeLocation().clone().getBlock();
      }

      if (!WaterReturn.hasWaterBottle(this.player) && this.sourceBlock != BlockSource.getWaterSourceBlock(this.player, 8.0D, ClickType.SHIFT_DOWN, true, true, true, true, true) && 
    		  this.sourceBlock != this.player.getEyeLocation().clone().getBlock()) {
         this.sourceBlock = null;
      }

      if (!WaterReturn.hasWaterBottle(this.player) && this.sourceBlock != null) {
         this.location = this.sourceBlock.getLocation().clone();
         this.above = this.sourceBlock.getLocation().add(0.0D, 1.0D, 0.0D).getBlock();
      }

      if (WaterReturn.hasWaterBottle(this.player) && this.sourceBlock != null) {
         this.location = this.sourceBlock.getLocation().clone();
      }

      if (this.sourceBlock != null) {
         this.start();
         if (!WaterReturn.hasWaterBottle(this.player) && this.sourceBlock.getType() == Material.FERN || this.sourceBlock.getType() == Material.SHORT_GRASS || 
        		 this.sourceBlock.getType() == Material.POPPY || this.sourceBlock.getType() == Material.SUNFLOWER || this.sourceBlock.getType() == Material.OAK_SAPLING || 
        		 this.sourceBlock.getType() == Material.RED_MUSHROOM || this.sourceBlock.getType() == Material.BROWN_MUSHROOM) {
            this.location = this.above.getLocation().clone();
         }

         if (!WaterReturn.hasWaterBottle(this.player) && this.sourceBlock.getType() == Material.WATER || this.sourceBlock.getType() == Material.WATER || 
        		 this.sourceBlock.getType() == Material.ICE || this.sourceBlock.getType() == Material.PACKED_ICE || this.sourceBlock.getType() == Material.SNOW_BLOCK) {
            this.location = this.above.getLocation().clone();
         }

      }
   }

   public void progress() {
      if (!this.bPlayer.canBendIgnoreBindsCooldowns(this)) {
         this.remove();
      } else if (this.sourceBlock.getLocation().distance(this.location) > this.range) {
         this.remove();
      } else if (!this.player.isSneaking()) {
         this.remove();
      } else {
         this.bPlayer.addCooldown(this);
         Location currentLoc = this.location.clone();
         this.direction = this.player.getEyeLocation().getDirection();
         this.location.add(this.direction);
         if (currentLoc.getBlock().getType() != Material.AIR && currentLoc.getBlock().getType() != Material.WATER && currentLoc.getBlock().getType() != 
        		 Material.TALL_GRASS && currentLoc.getBlock().getType() != Material.ALLIUM && currentLoc.getBlock().getType() != Material.OXEYE_DAISY && 
        		 currentLoc.getBlock().getType() != Material.AZURE_BLUET && currentLoc.getBlock().getType() != Material.BLUE_ORCHID && currentLoc.getBlock().getType() != 
        		 Material.ICE && currentLoc.getBlock().getType() != Material.PACKED_ICE && currentLoc.getBlock().getType() != Material.BIRCH_LEAVES && currentLoc.getBlock().getType() != 
        		 Material.SPRUCE_LEAVES && currentLoc.getBlock().getType() != Material.DARK_OAK_LEAVES && currentLoc.getBlock().getType() != Material.JUNGLE_LEAVES && 
        		 currentLoc.getBlock().getType() != Material.ACACIA_LEAVES && currentLoc.getBlock().getType() != Material.OAK_LEAVES && currentLoc.getBlock().getType() != 
        		 Material.OAK_LEAVES && currentLoc.getBlock().getType() != Material.SNOW_BLOCK && currentLoc.getBlock().getType() != Material.SHORT_GRASS && 
        		 currentLoc.getBlock().getType() != Material.FERN && currentLoc.getBlock().getType() != Material.OAK_LOG && currentLoc.getBlock().getType() != 
        		 Material.BIRCH_LOG && currentLoc.getBlock().getType() != Material.POPPY && currentLoc.getBlock().getType() != Material.SUNFLOWER && currentLoc.getBlock().getType() !=
        		 Material.SNOW && currentLoc.getBlock().getType() != Material.CACTUS && currentLoc.getBlock().getType() != Material.SUGAR_CANE && currentLoc.getBlock().getType() !=
        		 Material.OAK_SAPLING && currentLoc.getBlock().getType() != Material.PUMPKIN && currentLoc.getBlock().getType() != Material.PUMPKIN_STEM && currentLoc.getBlock().getType() != 
        		 Material.MELON && currentLoc.getBlock().getType() != Material.MELON_STEM && currentLoc.getBlock().getType() != Material.WHEAT && currentLoc.getBlock().getType() != 
        		 Material.LILY_PAD && currentLoc.getBlock().getType() != Material.BROWN_MUSHROOM && currentLoc.getBlock().getType() != Material.RED_MUSHROOM && 
        		 currentLoc.getBlock().getType() != Material.RED_MUSHROOM_BLOCK && currentLoc.getBlock().getType() != Material.BROWN_MUSHROOM_BLOCK && currentLoc.getBlock().getType() !=
        		 Material.VINE && currentLoc.getBlock().getType() != Material.ROSE_BUSH && currentLoc.getBlock().getType() != Material.PEONY && currentLoc.getBlock().getType() != 
        		 Material.ORANGE_TULIP && currentLoc.getBlock().getType() != Material.PINK_TULIP && currentLoc.getBlock().getType() != Material.RED_TULIP && currentLoc.getBlock().getType() !=
        		 Material.WHITE_TULIP && currentLoc.getBlock().getType() != Material.DANDELION) {
            this.remove();
         } else {
            if (WaterReturn.hasWaterBottle(this.player)) {
               this.tempBlock = new TempBlock(currentLoc.getBlock(), Material.WATER, GeneralMethods.getWaterData(2));
               WaterReturn.isBendableWaterTempBlock(currentLoc.getBlock());
               ParticleEffect.WATER_DROP.display(this.location, 5, 0.5D, 0.5D, 0.5D, 0.0D);
               playWaterbendingSound(this.location);
               this.tempBlock.setRevertTime(800L);
            }

            if (!WaterReturn.hasWaterBottle(this.player) && WaterReturn.isWater(this.sourceBlock)) {
               this.tempBlock = new TempBlock(this.location.getBlock(), Material.WATER);
               ParticleEffect.WATER_DROP.display(this.location, 5, 0.5D, 0.5D, 0.5D, 0.0D);
               playWaterbendingSound(this.location);
               this.tempBlock.setRevertTime(800L);
            }

            if (!WaterReturn.hasWaterBottle(this.player) && WaterReturn.isSnow(this.sourceBlock)) {
               this.tempBlock = new TempBlock(currentLoc.getBlock(), Material.SNOW_BLOCK);
               this.location.getWorld().playSound(this.location, Sound.BLOCK_SNOW_BREAK, 1.0F, 1.0F);
               this.tempBlock.setRevertTime(1200L);
            }
/*
            if (!WaterReturn.hasWaterBottle(this.player) && this.bPlayer.canUseSubElement(SubElement.PLANT) && this.sourceBlock.getType() == Material.OAK_LEAVES) {
               this.tempBlock = new TempBlock(currentLoc.getBlock(), Material.OAK_LEAVES);
               this.location.getWorld().playSound(this.location, Sound.BLOCK_GRASS_BREAK, 1.0F, 1.0F);
               this.tempBlock.setRevertTime(3200L);
            }

            if (!WaterReturn.hasWaterBottle(this.player) && this.bPlayer.canUseSubElement(SubElement.PLANT) && this.sourceBlock.getType() == Material.ACACIA_LEAVES) {
               this.tempBlock = new TempBlock(currentLoc.getBlock(), Material.ACACIA_LEAVES);
               this.location.getWorld().playSound(this.location, Sound.BLOCK_GRASS_BREAK, 1.0F, 1.0F);
               this.tempBlock.setRevertTime(3200L);
            }

            if (!WaterReturn.hasWaterBottle(this.player) && this.bPlayer.canUseSubElement(SubElement.PLANT) && this.sourceBlock.getType() == Material.BIRCH_LEAVES) {
               this.tempBlock = new TempBlock(currentLoc.getBlock(), Material.BIRCH_LEAVES);
               this.location.getWorld().playSound(this.location, Sound.BLOCK_GRASS_BREAK, 1.0F, 1.0F);
               this.tempBlock.setRevertTime(3200L);
            }

            if (!WaterReturn.hasWaterBottle(this.player) && this.bPlayer.canUseSubElement(SubElement.PLANT) && this.sourceBlock.getType() == Material.SPRUCE_LEAVES) {
               this.tempBlock = new TempBlock(currentLoc.getBlock(), Material.SPRUCE_LEAVES);
               this.location.getWorld().playSound(this.location, Sound.BLOCK_GRASS_BREAK, 1.0F, 1.0F);
               this.tempBlock.setRevertTime(3200L);
            }

            if (!WaterReturn.hasWaterBottle(this.player) && this.bPlayer.canUseSubElement(SubElement.PLANT) && this.sourceBlock.getType() == Material.JUNGLE_LEAVES) {
               this.tempBlock = new TempBlock(currentLoc.getBlock(), Material.JUNGLE_LEAVES);
               this.location.getWorld().playSound(this.location, Sound.BLOCK_GRASS_BREAK, 1.0F, 1.0F);
               this.tempBlock.setRevertTime(2500L);
            }

            if (!WaterReturn.hasWaterBottle(this.player) && this.bPlayer.canUseSubElement(SubElement.PLANT) && this.sourceBlock.getType() == Material.DARK_OAK_LEAVES) {
               this.tempBlock = new TempBlock(currentLoc.getBlock(), Material.DARK_OAK_LEAVES);
               this.location.getWorld().playSound(this.location, Sound.BLOCK_GRASS_BREAK, 1.0F, 1.0F);
               this.tempBlock.setRevertTime(2500L);
            }

            if (!WaterReturn.hasWaterBottle(this.player) && this.bPlayer.canUseSubElement(SubElement.PLANT) && this.sourceBlock.getType() == Material.CACTUS) {
               this.tempBlock = new TempBlock(currentLoc.getBlock(), Material.OAK_LEAVES);
               this.location.getWorld().playSound(this.location, Sound.BLOCK_GRASS_BREAK, 1.0F, 1.0F);
               this.tempBlock.setRevertTime(1200L);
            }

            if (!WaterReturn.hasWaterBottle(this.player) && this.bPlayer.canUseSubElement(SubElement.PLANT) && this.sourceBlock.getType() == Material.FERN || 
            		this.sourceBlock.getType() == Material.SHORT_GRASS || this.sourceBlock.getType() == Material.TALL_GRASS) {
               this.tempBlock = new TempBlock(currentLoc.getBlock(), Material.SHORT_GRASS);
               this.location.getWorld().playSound(this.location, Sound.BLOCK_GRASS_BREAK, 1.0F, 1.0F);
               this.tempBlock.setRevertTime(1200L);
            }

            if (!WaterReturn.hasWaterBottle(this.player) && this.bPlayer.canUseSubElement(SubElement.PLANT) && this.sourceBlock.getType() == Material.POPPY || 
            		this.sourceBlock.getType() == Material.ALLIUM || this.sourceBlock.getType() == Material.SUNFLOWER || this.sourceBlock.getType() == Material.BLUE_ORCHID || 
            		this.sourceBlock.getType() == Material.OXEYE_DAISY || this.sourceBlock.getType() == Material.AZURE_BLUET || this.sourceBlock.getType() == Material.PEONY || 
            		this.sourceBlock.getType() == Material.RED_TULIP || this.sourceBlock.getType() == Material.ORANGE_TULIP || this.sourceBlock.getType() == Material.WHITE_TULIP || 
            		this.sourceBlock.getType() == Material.PINK_TULIP) {
               this.tempBlock = new TempBlock(currentLoc.getBlock(), Material.WATER, GeneralMethods.getWaterData(2));
               ParticleEffect.WATER_DROP.display(this.location, 5, 0.5D, 0.5D, 0.5D, 0.0D);
               playWaterbendingSound(this.location);
               this.tempBlock.setRevertTime(1200L);
            }

            if (!WaterReturn.hasWaterBottle(this.player) && this.bPlayer.canUseSubElement(SubElement.PLANT) && this.sourceBlock.getType() == Material.RED_MUSHROOM ||
            		this.sourceBlock.getType() == Material.BROWN_MUSHROOM) {
               this.tempBlock = new TempBlock(currentLoc.getBlock(), Material.WATER, GeneralMethods.getWaterData(2));
               ParticleEffect.WATER_DROP.display(this.location, 5, 0.5D, 0.5D, 0.5D, 0.0D);
               playWaterbendingSound(this.location);
               this.tempBlock.setRevertTime(1200L);
            }

            if (!WaterReturn.hasWaterBottle(this.player) && this.bPlayer.canUseSubElement(SubElement.PLANT) && this.sourceBlock.getType() == Material.PUMPKIN ||
            		this.sourceBlock.getType() == Material.MELON) {
               this.tempBlock = new TempBlock(currentLoc.getBlock(), Material.WATER);
               ParticleEffect.WATER_DROP.display(this.location, 5, 0.5D, 0.5D, 0.5D, 0.0D);
               playWaterbendingSound(this.location);
               this.tempBlock.setRevertTime(1200L);
            }

            if (!WaterReturn.hasWaterBottle(this.player) && this.bPlayer.canUseSubElement(SubElement.PLANT) && this.sourceBlock.getType() == Material.OAK_SAPLING || 
            		this.sourceBlock.getType() == Material.PUMPKIN_STEM || this.sourceBlock.getType() == Material.MELON_STEM) {
               this.tempBlock = new TempBlock(currentLoc.getBlock(), Material.WATER);
               ParticleEffect.WATER_DROP.display(this.location, 5, 0.5D, 0.5D, 0.5D, 0.0D);
               playWaterbendingSound(this.location);
               this.tempBlock.setRevertTime(1200L);
            }

            if (!WaterReturn.hasWaterBottle(this.player) && this.bPlayer.canUseSubElement(SubElement.PLANT) && this.sourceBlock.getType() == Material.VINE || 
            		this.sourceBlock.getType() == Material.LILY_PAD || this.sourceBlock.getType() == Material.WHEAT) {
               this.tempBlock = new TempBlock(currentLoc.getBlock(), Material.WATER);
               ParticleEffect.WATER_DROP.display(this.location, 5, 0.5D, 0.5D, 0.5D, 0.0D);
               playWaterbendingSound(this.location);
               this.tempBlock.setRevertTime(1200L);
            }

            if (!WaterReturn.hasWaterBottle(this.player) && this.bPlayer.canUseSubElement(SubElement.PLANT) && this.sourceBlock.getType() == Material.SUGAR_CANE) {
               this.tempBlock = new TempBlock(currentLoc.getBlock(), Material.SUGAR_CANE);
               this.location.getWorld().playSound(this.location, Sound.BLOCK_GRASS_BREAK, 1.0F, 1.0F);
               this.tempBlock.setRevertTime(1200L);
            }

            if (!WaterReturn.hasWaterBottle(this.player) && this.bPlayer.canUseSubElement(SubElement.PLANT) && this.sourceBlock.getType() == Material.BROWN_MUSHROOM_BLOCK || 
            		this.sourceBlock.getType() == Material.RED_MUSHROOM_BLOCK) {
               this.tempBlock = new TempBlock(currentLoc.getBlock(), Material.WATER);
               ParticleEffect.WATER_DROP.display(this.location, 5, 0.5D, 0.5D, 0.5D, 0.0D);
               playWaterbendingSound(this.location);
               this.tempBlock.setRevertTime(1200L);
            }

            if (!WaterReturn.hasWaterBottle(this.player) && this.sourceBlock.getType() == Material.OAK_LOG || this.sourceBlock.getType() == Material.BIRCH_LOG) {
               this.tempBlock = new TempBlock(currentLoc.getBlock(), Material.WATER);
               ParticleEffect.WATER_DROP.display(this.location, 5, 0.5D, 0.5D, 0.5D, 0.0D);
               playWaterbendingSound(this.location);
               this.tempBlock.setRevertTime(1200L);
            } */
            
            if (!WaterReturn.hasWaterBottle(this.player) && isPlantbendable(this.sourceBlock) && this.bPlayer.canUseSubElement(SubElement.PLANT) ) {
            	//this.tempBlock = new TempBlock(currentLoc.getBlock(), Material.WATER, GeneralMethods.getWaterData(2));
            	this.tempBlock = new TempBlock(currentLoc.getBlock(), Material.SHORT_GRASS);
            	//ParticleEffect.WATER_DROP.display(this.location, 5, 0.5D, 0.5D, 0.5D, 0.0D);
            	
            	//ParticleEffect.WATER_DROP.display(this.location, 5, 0.5D, 0.5D, 0.5D, 0.0D);
            	this.location.getWorld().playSound(this.location, Sound.BLOCK_GRASS_BREAK, 1.0F, 1.0F);
                this.tempBlock.setRevertTime(800L);
            }
          /*  if (!WaterReturn.hasWaterBottle(this.player) && WaterReturn.isPlant(this.sourceBlock)) { //Check Here
               this.tempBlock = new TempBlock(currentLoc.getBlock(), Material.WATER, GeneralMethods.getWaterData(2));
               ParticleEffect.WATER_DROP.display(this.location, 5, 0.5D, 0.5D, 0.5D, 0.0D);
               playWaterbendingSound(this.location);
               this.tempBlock.setRevertTime(1200L);
            }*/
            if (!WaterReturn.hasWaterBottle(this.player) && this.bPlayer.canUseSubElement(SubElement.ICE) && isIcebendable(this.sourceBlock)) {
               this.tempBlock = new TempBlock(currentLoc.getBlock(), Material.ICE);
               playIcebendingSound(this.location);
               this.tempBlock.setRevertTime(1200L);
            }

            if (!WaterReturn.hasWaterBottle(this.player) && this.bPlayer.canUseSubElement(SubElement.ICE) && this.sourceBlock.getType() == Material.PACKED_ICE) {
               this.tempBlock = new TempBlock(currentLoc.getBlock(), Material.PACKED_ICE);
               playIcebendingSound(this.location);
               this.tempBlock.setRevertTime(1200L);
            }

           /* if (!WaterReturn.hasWaterBottle(this.player) && WaterReturn.isNight(this.player.getWorld())) {
               this.damage = 5.0D;
               this.range = 22.0D;
            }

            if (!WaterReturn.hasWaterBottle(this.player) && WaterReturn.isFullMoon(this.player.getWorld())) {
               this.damage = 6.0D;
               this.range = 24.0D;
            }*/

            if (WaterReturn.hasWaterBottle(this.player) && this.sourceBlock.getLocation().distance(this.location) > this.range) {
               WaterReturn.emptyWaterBottle(this.player);
            } else {
               if (!WaterReturn.hasWaterBottle(this.player) && this.sourceBlock.getLocation().distance(this.location) > this.range) {
                  this.sourceBlock.setType(Material.AIR);
               }

               Iterator var3 = GeneralMethods.getEntitiesAroundPoint(this.location, 2.0D).iterator();

               while(var3.hasNext()) {
                  Entity entity = (Entity)var3.next();
                  if (entity instanceof LivingEntity && entity.getEntityId() != this.player.getEntityId()) {
                     Location location = this.player.getEyeLocation();
                     Vector vector = location.getDirection();
                     entity.setVelocity(vector.normalize().multiply(0.8F));
                     if (entity instanceof LivingEntity) {
                        DamageHandler.damageEntity(entity, this.damage, this);
                     }

                     if (!WaterReturn.hasWaterBottle(this.player) && WaterReturn.isIce(this.sourceBlock)) {
                        ((LivingEntity)entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 2));
                     }

                     if (!WaterReturn.hasWaterBottle(this.player) && WaterReturn.isPlant(this.sourceBlock)) {
                        ((LivingEntity)entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 2));
                     }

                     if (!WaterReturn.hasWaterBottle(this.player) && WaterReturn.isSnow(this.sourceBlock)) {
                        ((LivingEntity)entity).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 2));
                     }
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
      return "Whip";
   }

   public boolean isHarmlessAbility() {
      return false;
   }

   public boolean isSneakAbility() {
      return ProjectUpd.plugin.getConfig().getBoolean("Abilities.Water.Whip.Swim.Disabled");
   }

   public void remove() {
      new WaterReturn(this.player, this.location.getBlock());
      super.remove();
   }

   @Override
	public boolean isEnabled() {
		return ProjectUpd.plugin.getConfig().getBoolean("Abilities.Water.Whip.Enabled");
	 }
	 @Override
	 public String getDescription() {
		return ProjectUpd.plugin.getConfig().getString("Language.Abilities.Water.Whip.Description");
	 }
  
	 @Override
	 public String getInstructions() {
		return ProjectUpd.plugin.getConfig().getString("Language.Abilities.Water.Whip.Instructions");
	 } 
}
