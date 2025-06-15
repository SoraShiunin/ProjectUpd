package com.darkermoon.ability.metal.metalcables;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.configuration.Config;
import com.projectkorra.projectkorra.waterbending.multiabilities.WaterArms.Arm;

public class MetalHookUtils {
   public static final String METAL_COLOR = "#777777";
   public static final String AUTHOR = "Finn_Bueno_";
   public static final String VERSION = "1.5";
   public static final ConcurrentHashMap<UUID, String> EASTEREGG_PLAYERS = new ConcurrentHashMap();
   public static List<Material> helmetTypes = new ArrayList();
   public static List<Material> chestplateTypes = new ArrayList();
   public static List<Material> leggingsTypes = new ArrayList();
   public static List<Material> bootsTypes = new ArrayList();
   public static List<Material> handTypes = new ArrayList();
   public static boolean itemRequired;

   static {
      FileConfiguration c = (new Config(new File("ExtraAbilities.yml"))).get();
      itemRequired = !c.getBoolean("ExtraAbilities.Finn_Bueno_.MetalCables.AllowUseWithoutItems");
      if (itemRequired) {
         List<String> list = c.getStringList("ExtraAbilities.Finn_Bueno_.MetalCables.ArmorRequirements.Helmet");
         Iterator var3 = list.iterator();

         String s;
         while(var3.hasNext()) {
            s = (String)var3.next();

            try {
               helmetTypes.add(Material.valueOf(s));
            } catch (IllegalArgumentException var9) {
               ProjectKorra.log.warning(String.valueOf(s) + " is an invalid material!");
            }
         }

         list = c.getStringList("ExtraAbilities.Finn_Bueno_.MetalCables.ArmorRequirements.Chestplate");
         var3 = list.iterator();

         while(var3.hasNext()) {
            s = (String)var3.next();

            try {
               chestplateTypes.add(Material.valueOf(s));
            } catch (IllegalArgumentException var8) {
               ProjectKorra.log.warning(String.valueOf(s) + " is an invalid material!");
            }
         }

         list = c.getStringList("ExtraAbilities.Finn_Bueno_.MetalCables.ArmorRequirements.Leggings");
         var3 = list.iterator();

         while(var3.hasNext()) {
            s = (String)var3.next();

            try {
               leggingsTypes.add(Material.valueOf(s));
            } catch (IllegalArgumentException var7) {
               ProjectKorra.log.warning(String.valueOf(s) + " is an invalid material!");
            }
         }

         list = c.getStringList("ExtraAbilities.Finn_Bueno_.MetalCables.ArmorRequirements.Boots");
         var3 = list.iterator();

         while(var3.hasNext()) {
            s = (String)var3.next();

            try {
               bootsTypes.add(Material.valueOf(s));
            } catch (IllegalArgumentException var6) {
               ProjectKorra.log.warning(String.valueOf(s) + " is an invalid material!");
            }
         }

         list = c.getStringList("ExtraAbilities.Finn_Bueno_.MetalCables.ItemRequirements");
         var3 = list.iterator();

         while(var3.hasNext()) {
            s = (String)var3.next();

            try {
               handTypes.add(Material.valueOf(s));
            } catch (IllegalArgumentException var5) {
               ProjectKorra.log.warning(String.valueOf(s) + " is an invalid material!");
            }
         }
      }

   }

   private MetalHookUtils() {
   }

   public static void particles(Player player, Location loc) {
      particles(player, loc, 1);
   }

   public static void particles(Player player, Location loc, int count) {
      if (EASTEREGG_PLAYERS.containsKey(player.getUniqueId())) {
         GeneralMethods.displayColoredParticle((String)EASTEREGG_PLAYERS.get(player.getUniqueId()), loc);
      } else {
         GeneralMethods.displayColoredParticle("#777777", loc);
      }

   }

   public static Location getHand(Player player, Arm side) {
      return side == Arm.LEFT ? GeneralMethods.getLeftSide(player.getEyeLocation(), 0.4D).subtract(0.0D, 0.6D, 0.0D) : GeneralMethods.getRightSide(player.getEyeLocation(), 0.4D).subtract(0.0D, 0.6D, 0.0D);
   }

   public static Material getTypeSafe(ItemStack item) {
      return item == null ? null : item.getType();
   }

   public static boolean hasAppropriateItems(Player player) {
      if (!itemRequired) {
         return true;
      } else {
         Material current = getTypeSafe(player.getInventory().getHelmet());
         Iterator var3 = helmetTypes.iterator();

         Material mat;
         while(var3.hasNext()) {
            mat = (Material)var3.next();
            if (mat == current) {
               return true;
            }
         }

         current = getTypeSafe(player.getInventory().getChestplate());
         var3 = chestplateTypes.iterator();

         while(var3.hasNext()) {
            mat = (Material)var3.next();
            if (mat == current) {
               return true;
            }
         }

         current = getTypeSafe(player.getInventory().getLeggings());
         var3 = leggingsTypes.iterator();

         while(var3.hasNext()) {
            mat = (Material)var3.next();
            if (mat == current) {
               return true;
            }
         }

         current = getTypeSafe(player.getInventory().getBoots());
         var3 = bootsTypes.iterator();

         while(var3.hasNext()) {
            mat = (Material)var3.next();
            if (mat == current) {
               return true;
            }
         }

         current = getTypeSafe(player.getInventory().getItemInMainHand());
         var3 = handTypes.iterator();

         while(var3.hasNext()) {
            mat = (Material)var3.next();
            if (mat == current) {
               return true;
            }
         }

         current = getTypeSafe(player.getInventory().getItemInOffHand());
         var3 = handTypes.iterator();

         while(var3.hasNext()) {
            mat = (Material)var3.next();
            if (mat == current) {
               return true;
            }
         }

         return false;
      }
   }
}
