package com.darkermoon.ability.metal.metalcables;

import java.io.File;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.configuration.Config;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.waterbending.multiabilities.WaterArms.Arm;

public class Slam extends MetalHookSubAbility {
   private static final long COOLDOWN;
   private static final double SPEED;
   private static final double DAMAGE;
   private static final double RANGE;
   private final Location hook;
   private final Vector dir;
   private Arm side;

   static {
      FileConfiguration c = (new Config(new File("ExtraAbilities.yml"))).get();
      COOLDOWN = c.getLong("ExtraAbilities.Finn_Bueno_.MetalCables.Slam.Cooldown");
      SPEED = c.getDouble("ExtraAbilities.Finn_Bueno_.MetalCables.Slam.Speed");
      DAMAGE = c.getDouble("ExtraAbilities.Finn_Bueno_.MetalCables.Slam.Damage");
      RANGE = c.getDouble("ExtraAbilities.Finn_Bueno_.MetalCables.Slam.Range");
   }

   public Slam(Player player, int slot) {
      super(player, slot);
      this.side = slot == 6 ? Arm.LEFT : Arm.RIGHT;
      this.hook = MetalHookUtils.getHand(player, this.side);
      this.dir = player.getLocation().getDirection().multiply(0.5D);
      this.start();
      this.bPlayer.addCooldown(this);
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
         this.moveCable();
         if (this.hook.distanceSquared(MetalHookUtils.getHand(this.player, this.side)) > RANGE * RANGE) {
            this.remove();
         }

         this.animation();
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
            if (ent.getEntityId() != this.player.getEntityId() && ent instanceof LivingEntity && !GeneralMethods.isRegionProtectedFromBuild(this, ent.getLocation())) {
               LivingEntity target = (LivingEntity)ent;
               DamageHandler.damageEntity(target, DAMAGE, this);
               playMetalbendingSound(target.getLocation());
               playMetalbendingSound(this.player.getLocation());
               this.remove();
               return;
            }
         }
      }

   }

   public void remove() {
      super.remove();
   }

   public String getSubName() {
      return "Slam";
   }

   public int UsePointsPerUse() {
      return 1;
   }

   public boolean oneAtATime() {
      return true;
   }
}
