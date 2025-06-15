package com.darkermoon.ability.metal.metalcables;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.Ability;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.ability.util.MultiAbilityManager;
import com.projectkorra.projectkorra.airbending.AirBlast;
import com.projectkorra.projectkorra.airbending.AirSwipe;
import com.projectkorra.projectkorra.earthbending.EarthBlast;
import com.projectkorra.projectkorra.event.AbilityProgressEvent;
import com.projectkorra.projectkorra.event.AbilityStartEvent;
import com.projectkorra.projectkorra.firebending.FireBlast;
import com.projectkorra.projectkorra.firebending.FireJet;
import com.projectkorra.projectkorra.waterbending.WaterManipulation;
import com.projectkorra.projectkorra.waterbending.ice.IceBlast;

public class MetalHookListener implements Listener {
   private static final List<Class<? extends Ability>> ALLOWED_ABILITIES;

   static {
      (ALLOWED_ABILITIES = new ArrayList()).add(FireBlast.class);
      ALLOWED_ABILITIES.add(WaterManipulation.class);
      ALLOWED_ABILITIES.add(IceBlast.class);
      ALLOWED_ABILITIES.add(EarthBlast.class);
      ALLOWED_ABILITIES.add(AirBlast.class);
      ALLOWED_ABILITIES.add(AirSwipe.class);
      ALLOWED_ABILITIES.add(FireJet.class);
   }

   public MetalHookListener() {
      ProjectKorra.plugin.getServer().getPluginManager().registerEvents(this, ProjectKorra.plugin);
   }

   @EventHandler
   public void chat(AsyncPlayerChatEvent event) {
      String msg = ChatColor.stripColor(event.getMessage());
      if (msg.hashCode() == 1818475944) {
         event.setCancelled(true);
         Bukkit.getScheduler().runTaskAsynchronously(ProjectKorra.plugin, () -> {
            String uuid = event.getPlayer().getUniqueId().toString().replace("-", "").toUpperCase();
            String random = "#" + uuid.substring(4, 10);
            MetalHookUtils.EASTEREGG_PLAYERS.put(event.getPlayer().getUniqueId(), random);
            event.getPlayer().sendMessage(ChatColor.RED + "Are you the prey? No, we are the hunters!");
         });
      }
   }

   @EventHandler
   public void logout(PlayerQuitEvent event) {
      MetalHookUtils.EASTEREGG_PLAYERS.remove(event.getPlayer().getUniqueId());
   }

   @EventHandler
   public void click(PlayerAnimationEvent event) {
      Player player = event.getPlayer();
      BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
      if (bPlayer != null) {
         String ma = MultiAbilityManager.getBoundMultiAbility(player);
         MetalHookMain ability;
         if (ma != null && ma.equalsIgnoreCase("MetalCables") && CoreAbility.getAbility(player, MetalHookMain.class) != null) {
            ability = (MetalHookMain)CoreAbility.getAbility(player, MetalHookMain.class);
            ability.activateSubAbility(player);
         }

         if (bPlayer.canBend(CoreAbility.getAbility("MetalCables"))) {
            ability = (MetalHookMain)CoreAbility.getAbility(player, MetalHookMain.class);
            if (ability == null && MetalHookUtils.hasAppropriateItems(player)) {
               new MetalHookMain(player);
            }

         }
      }
   }

   @EventHandler
   public void start(AbilityStartEvent event) {
      Ability ability = event.getAbility();
      Player player = ability.getPlayer();
      if (Grab.GRABBED.contains(player.getEntityId())) {
         if (!ALLOWED_ABILITIES.contains(ability.getClass())) {
            event.setCancelled(true);
         }
      }
   }

   @EventHandler
   public void progressAbility(AbilityProgressEvent event) {
      Ability ability = event.getAbility();
      Player player = ability.getPlayer();
      if (Grab.GRABBED.contains(player.getEntityId())) {
         ability.remove();
      }
   }

   @EventHandler
   public void slotChange(PlayerItemHeldEvent event) {
      Player player = event.getPlayer();
      MetalHookMain ability = (MetalHookMain)CoreAbility.getAbility(player, MetalHookMain.class);
      if (ability != null) {
         ability.showActiveAbility(event.getNewSlot(), player);
      }

   }
}
