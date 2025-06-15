package com.darkermoon.ability.metal.metalcables;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Exit extends MetalHookSubAbility {
   public Exit(Player player, int slot) {
      super(player, slot);
      MetalHookMain mhm = (MetalHookMain)getAbility(player, MetalHookMain.class);
      mhm.remove();
   }

   public long getCooldown() {
      return 0L;
   }

   public Location getLocation() {
      return null;
   }

   public boolean isSneakAbility() {
      return false;
   }

   public void progress() {
   }

   public String getSubName() {
      return "Exit";
   }

   public int UsePointsPerUse() {
      return Integer.MAX_VALUE;
   }

   public boolean oneAtATime() {
      return true;
   }
}
