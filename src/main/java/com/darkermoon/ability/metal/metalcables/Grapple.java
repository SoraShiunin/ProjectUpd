package com.darkermoon.ability.metal.metalcables;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.configuration.Config;
import com.projectkorra.projectkorra.waterbending.multiabilities.WaterArms.Arm;

public class Grapple extends MetalHookSubAbility {
   private static final double SPEED;
   public static final double PULL_SPEED;
   private static final double RANGE;
   private static final double SWING_THRESHOLD;
   private final Location hook;
   private final Vector dir;
   private boolean hit;
   private Arm side;

   static {
      FileConfiguration c = (new Config(new File("ExtraAbilities.yml"))).get();
      SPEED = c.getDouble("ExtraAbilities.Finn_Bueno_.MetalCables.Grapple.Speed");
      PULL_SPEED = c.getDouble("ExtraAbilities.Finn_Bueno_.MetalCables.Grapple.PullSpeed");
      RANGE = c.getDouble("ExtraAbilities.Finn_Bueno_.MetalCables.Grapple.Range");
      SWING_THRESHOLD = c.getDouble("ExtraAbilities.Finn_Bueno_.MetalCables.Grapple.MaximumSwingAngle");
   }

   public Grapple(Player player, int slot) {
      super(player, slot);
      this.side = slot == 0 ? Arm.LEFT : Arm.RIGHT;
      this.hook = MetalHookUtils.getHand(player, this.side);
      this.dir = player.getLocation().getDirection().multiply(0.5D);
      this.hit = false;
      this.start();
   }

   public long getCooldown() {
      return 0L;
   }

   public Location getLocation() {
      return this.hook;
   }

   public boolean isSneakAbility() {
      return false;
   }

   public boolean hasHit() {
      return this.hit;
   }

   public void progress() {
      if (this.player != null && !this.player.isDead() && this.player.isOnline()) {
         if (!this.hit) {
            this.moveCable();
         } else {
            this.handlePull();
         }

         if (this.hook.distanceSquared(MetalHookUtils.getHand(this.player, this.side)) > RANGE * RANGE) {
            this.remove();
         }

         this.animation();
      } else {
         this.remove();
      }
   }

   private void handlePull() {
      this.player.setAllowFlight(false);
      this.player.setFlying(false);
      if (!GeneralMethods.isSolid(this.hook.getBlock())) {
         this.hit = false;
      } else if (this.hasRequiredSlot() && this.requiredSlot != 1) {
         if (!this.player.isSneaking()) {
            double distance = this.player.getLocation().distanceSquared(this.hook);
            if (distance <= 1.0D) {
               this.remove();
            }

         } else {
            Vector pull = GeneralMethods.getDirection(this.player.getLocation(), this.hook);
            double factor;
            if (pull.length() > 3.0D) {
               factor = PULL_SPEED / pull.length();
               pull.multiply(factor);
            } else if (pull.length() > 1.0D) {
               factor = 1.0D / pull.length();
               pull.multiply(factor);
            } else {
               pull.multiply(0);
            }

            this.player.setAllowFlight(true);
            this.player.setFallDistance(0.0F);
            Vector playerDir = this.player.getLocation().getDirection().multiply(pull.length());
            double angle = Math.toDegrees((double)playerDir.angle(pull));
            this.player.setVelocity(angle < SWING_THRESHOLD ? playerDir : pull);
         }
      }
   }

   private void animation() {
      Location origin = MetalHookUtils.getHand(this.player, this.side);
      double distance = origin.distance(this.hook);
      Vector dir = GeneralMethods.getDirection(origin, this.hook).normalize();

      for(double step = 0.5D; step < distance; step += 0.5D) {
         origin.add(dir.getX() * step, dir.getY() * step, dir.getZ() * step);
         if (GeneralMethods.isSolid(origin.getBlock()) && step < distance - 1.5D) {
            this.remove();
            return;
         }

         MetalHookUtils.particles(this.player, origin);
         origin.subtract(dir.getX() * step, dir.getY() * step, dir.getZ() * step);
      }

   }

   private void moveCable() {
      for(double step = 0.0D; step < SPEED; step += 0.5D) {
         this.hook.add(this.dir);
         if (GeneralMethods.isSolid(this.hook.getBlock())) {
            this.hit = true;
            playMetalbendingSound(this.hook);
            playMetalbendingSound(this.player.getLocation());
            return;
         }
      }

   }

   public String getSubName() {
      return "Grapple";
   }

   public int UsePointsPerUse() {
      return 0;
   }

   public boolean oneAtATime() {
      return true;
   }

   public Vector getDirection() {
      return this.dir;
   }

   public Vector getActualDirection() {
      return GeneralMethods.getDirection(MetalHookUtils.getHand(this.player, this.side), this.hook);
   }
}
