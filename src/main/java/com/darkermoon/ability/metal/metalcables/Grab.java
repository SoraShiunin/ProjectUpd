package com.darkermoon.ability.metal.metalcables;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.configuration.Config;
import com.projectkorra.projectkorra.waterbending.multiabilities.WaterArms.Arm;

public class Grab extends MetalHookSubAbility {
   private static final long COOLDOWN;
   private static final double SPEED;
   private static final double PULL_SPEED;
   private static final double RANGE;
   public static final List<Integer> GRABBED;
   private Location hook;
   private final Vector dir;
   private Entity target;
   private Arm side;

   static {
      FileConfiguration c = (new Config(new File("ExtraAbilities.yml"))).get();
      COOLDOWN = c.getLong("ExtraAbilities.Finn_Bueno_.MetalCables.Grab.Cooldown");
      SPEED = c.getDouble("ExtraAbilities.Finn_Bueno_.MetalCables.Grab.Speed");
      PULL_SPEED = c.getDouble("ExtraAbilities.Finn_Bueno_.MetalCables.Grab.PullSpeed");
      RANGE = c.getDouble("ExtraAbilities.Finn_Bueno_.MetalCables.Grab.Range");
      GRABBED = new ArrayList();
   }

   public Grab(Player player, int slot) {
      super(player, slot);
      this.side = slot == 4 ? Arm.LEFT : Arm.RIGHT;
      this.hook = MetalHookUtils.getHand(player, this.side);
      this.dir = player.getLocation().getDirection().multiply(0.5D);
      this.start();
   }

   public long getCooldown() {
      return COOLDOWN;
   }

   public Location getLocation() {
      return this.hook;
   }

   public boolean isSneakAbility() {
      return false;
   }

   public void progress() {
      if (this.player != null && !this.player.isDead() && this.player.isOnline()) {
         if (this.target == null) {
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
      if ((!(this.target instanceof Player) || ((Player)this.target).isOnline() || !this.target.isDead()) && this.target.getWorld() == this.player.getWorld() && !(this.target.getLocation().distance(this.player.getLocation()) > RANGE)) {
         if (this.hasRequiredSlot()) {
            this.hook = this.target.getLocation().add(0.0D, 1.0D, 0.0D);
            Location dest = this.player.getEyeLocation().add(this.player.getLocation().getDirection().multiply(1.5D));
            if (!this.player.isSneaking()) {
               double distance = dest.distanceSquared(this.hook);
               if (distance <= 1.0D) {
                  this.remove();
               }

            } else {
               Vector pull = GeneralMethods.getDirection(dest, this.hook).multiply(-1);
               double factor;
               if (pull.length() > 5.0D) {
                  factor = PULL_SPEED / pull.length();
                  pull.multiply(factor);
               } else if (pull.length() > 1.0D) {
                  factor = 0.3D / pull.length();
                  pull.multiply(factor);
               } else {
                  pull.multiply(0);
               }

               this.target.setFallDistance(0.0F);
               if (this.target instanceof Player) {
                  ((Player)this.target).setAllowFlight(true);
                  ((Player)this.target).setFlying(false);
               }

               this.target.setVelocity(this.target.getVelocity().add(pull));
            }
         }
      } else {
         this.remove();
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
         Iterator var4 = GeneralMethods.getEntitiesAroundPoint(this.hook, 2.0D).iterator();

         while(var4.hasNext()) {
            Entity ent = (Entity)var4.next();
            if (ent.getEntityId() != this.player.getEntityId() && !GRABBED.contains(ent.getEntityId()) && !GeneralMethods.isRegionProtectedFromBuild(this, ent.getLocation())) {
               this.target = ent;
               if (this.target instanceof Player) {
                  Player player = (Player)this.target;
                  player.setFlying(false);
                  player.setAllowFlight(false);
               }

               GRABBED.add(this.target.getEntityId());
               playMetalbendingSound(this.target.getLocation());
               playMetalbendingSound(this.player.getLocation());
               return;
            }
         }
      }

   }

   public void remove() {
	    super.remove();
	    this.bPlayer.addCooldown(this);
	    if (this.target != null) {
	        int entityId = this.target.getEntityId();
	        if (GRABBED.contains(entityId)) {
	            GRABBED.remove((Integer) entityId);
	        }
	    }

   }

   public String getSubName() {
      return "Grab";
   }

   public int UsePointsPerUse() {
      return 2;
   }

   public boolean oneAtATime() {
      return true;
   }
}
