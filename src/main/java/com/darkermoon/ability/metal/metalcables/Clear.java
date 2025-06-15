package com.darkermoon.ability.metal.metalcables;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.projectkorra.projectkorra.ability.EarthAbility;

public class Clear extends MetalHookSubAbility {
   public Clear(Player player, int slot) {
      super(player, slot);
      EarthAbility.playMetalbendingSound(player.getEyeLocation());
      MetalHookMain mhm = (MetalHookMain)getAbility(player, MetalHookMain.class);

      for(int i = 0; i <= 2; i += 2) {
         if (mhm.activeAbilities[i] != null) {
            mhm.activeAbilities[i].remove();
         }
      }

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
      return 0;
   }

   public boolean oneAtATime() {
      return true;
   }
}
