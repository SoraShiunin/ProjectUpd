package com.darkermoon.ability.lava;

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
import com.projectkorra.projectkorra.ability.LavaAbility;
import com.projectkorra.projectkorra.util.BlockSource;
import com.projectkorra.projectkorra.util.ClickType;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.util.ParticleEffect;
import com.projectkorra.projectkorra.util.TempBlock;
import com.projectkorra.projectkorra.waterbending.util.WaterReturn;

public class LavaSlip extends LavaAbility  {
   private long cooldown;
   private double range;
   private double damage;
   private TempBlock tempBlock;
   private Location location;
   private Block sourceBlock;
   private Block above;
   private Vector direction;
//shift charge
	private long maxchargeTime;
	private long time;

   public LavaSlip(Player player) {
      super(player);

      //set time
      time = System.currentTimeMillis();

      if (!this.bPlayer.isOnCooldown(this)) {
         if (player.isSneaking()) {       
            System.out.println("Sneaking1");
            this.setFields();
         }
      }
   }

   public void setFields() {
      this.cooldown = ProjectUpd.plugin.getConfig().getLong("Abilities.Earth.Lava.LavaSlip.Cooldown");
      this.range = ProjectUpd.plugin.getConfig().getDouble("Abilities.Earth.Lava.LavaSlip.Range");
      this.damage = ProjectUpd.plugin.getConfig().getDouble("Abilities.Earth.Lava.LavaSlip.Damage");
      this.maxchargeTime = ProjectUpd.plugin.getConfig().getLong("Abilities.Earth.Lava.LavaSlip.MaxChargeTime");
      //this range might need its own SourceRange
      this.sourceBlock = BlockSource.getEarthOrLavaSourceBlock(this.player, range, ClickType.SHIFT_DOWN);

      //this.tempBlock = new TempBlock(this.sourceBlock, Material.COBBLESTONE);
      //this.sourceBlock.setType(Material.STONE);
      //this.location = this.sourceBlock.getLocation();
      System.out.println("Sneaking Charge Time");

         ParticleEffect.SPELL_WITCH.display(player.getLocation(), 5, 0F, 0.2F, 0F, 0.5F);
         System.out.println(maxchargeTime + " maxchargeTime ---" + time + " time");
      //shift charge
      


      if (this.sourceBlock != null) {
         System.out.println("SourceBlock is " + this.sourceBlock);
         this.location = this.sourceBlock.getLocation().clone();
         System.out.println("Location is " + this.location);
         this.start();
         if (isEarthbendable(this.sourceBlock)) {
            this.location = this.sourceBlock.getLocation().clone();
            System.out.println("Earthbendable");
         } else if (isMetalbendable(this.sourceBlock)) {
            this.location = this.sourceBlock.getLocation().clone();
            System.out.println("Metalbendable");
         } else if (isLavabendable(this.sourceBlock)) {
            this.location = this.sourceBlock.getLocation().clone();
            System.out.println("Lavabendable");
         } else {
            System.out.println("Not bendable");
         }
      } else {
         System.out.println("SourceBlock is null");
      }
   }

   public void progress() {
      if (!this.bPlayer.canBendIgnoreBindsCooldowns(this)) {
         System.out.println("Cannot bend");
         this.remove();
      } else if (this.sourceBlock.getLocation().distance(this.location) > this.range) {
         System.out.println("Distance is greater than range");
         this.remove();
      } else if (!this.player.isSneaking()) {
         System.out.println("Not sneaking");
         this.remove();
      } else {
         this.bPlayer.addCooldown(this);
         Location currentLoc = this.location.clone();
         this.direction = this.player.getEyeLocation().getDirection();
         this.location.add(this.direction);
         Material blockType = currentLoc.getBlock().getType();
         if (blockType != Material.AIR && blockType != Material.WATER && blockType != Material.GRASS_BLOCK && blockType != Material.STONE && blockType != Material.LAVA && blockType != Material.GRAVEL && blockType != Material.DIRT) {
            System.out.println("Block is not air, water, grass, stone, lava, gravel, or dirt");
            this.remove();
         } else {
        

            System.out.println("Block is air, water, grass, stone, lava, gravel, or dirt");

            if (isEarthbendable(this.sourceBlock) && this.bPlayer.canUseSubElement(SubElement.LAVA)) {
               System.out.println("Earthbendable and can use Lava");
               this.tempBlock = new TempBlock(currentLoc.getBlock(), Material.LAVA);
               ParticleEffect.LANDING_LAVA.display(this.location, 5, 0.5D, 0.5D, 0.5D, 0.0D);
               this.location.getWorld().playSound(this.location, Sound.BLOCK_POINTED_DRIPSTONE_DRIP_LAVA, 1.0F, 1.0F);
               this.tempBlock.setRevertTime(2000L);
            }

            if (this.sourceBlock.getLocation().distance(this.location) > this.range) {
               System.out.println("Distance is greater than range 2");
               this.sourceBlock.setType(Material.AIR);
            }

            for (Entity entity : GeneralMethods.getEntitiesAroundPoint(this.location, 1.0D)) {
               if (entity instanceof LivingEntity && entity.getEntityId() != this.player.getEntityId()) {
                  Location location = this.player.getEyeLocation();
                  Vector vector = location.getDirection();
                  entity.setVelocity(vector.normalize().multiply(0.8F));
                  if (entity instanceof LivingEntity) {
                     DamageHandler.damageEntity(entity, this.damage, this);
                     ((LivingEntity)entity).setFireTicks(200);
                  }

                  if (!WaterReturn.hasWaterBottle(this.player) && WaterReturn.isEarth(this.sourceBlock)) {
                     ((LivingEntity)entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 2));
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
      return "LavaSlip";
   }

   public boolean isHarmlessAbility() {
      return false;
   }

   public boolean isSneakAbility() {
      return true;
   }

   public void remove() {
      super.remove();
   }

   @Override
	public boolean isEnabled() {
		return ProjectUpd.plugin.getConfig().getBoolean("Abilities.Earth.Lava.LavaSlip.Enabled");
	 }
	 @Override
	 public String getDescription() {
		return ProjectUpd.plugin.getConfig().getString("Language.Abilities.Earth.Lava.LavaSlip.Description");
	 }
  
	 @Override
	 public String getInstructions() {
		return ProjectUpd.plugin.getConfig().getString("Language.Abilities.Earth.Lava.LavaSlip.Instructions");
	 } 
}
