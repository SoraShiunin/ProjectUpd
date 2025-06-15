package com.darkermoon.ability.metal.metalcables;

import org.bukkit.entity.Player;

import com.projectkorra.projectkorra.ability.MetalAbility;

public abstract class MetalHookSubAbility extends MetalAbility  {
   protected final int requiredSlot;

   public MetalHookSubAbility(Player player, int slot) {
      super(player);
      this.requiredSlot = slot;
   }

   protected boolean hasRequiredSlot() {
      return this.player.getInventory().getHeldItemSlot() == this.requiredSlot;
   }

   public abstract String getSubName();

   public abstract int UsePointsPerUse();

   public abstract boolean oneAtATime();

   public String getName() {
      return "MetalCables Sub Ability [" + this.getSubName() + "]";
   }

   public boolean isHarmlessAbility() {
      return false;
   }

   public String getAuthor() {
      return "Finn_Bueno_";
   }

   public String getVersion() {
      return "1.5";
   }

   public boolean isHiddenAbility() {
      return true;
   }

   public void load() {
   }

   public void stop() {
   }
}
