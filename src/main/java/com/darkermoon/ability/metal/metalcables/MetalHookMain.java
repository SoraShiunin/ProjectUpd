package com.darkermoon.ability.metal.metalcables;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.Vector;

import com.darkermoon.ProjectUpd;
import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.EarthAbility;
import com.projectkorra.projectkorra.ability.MetalAbility;
import com.projectkorra.projectkorra.ability.MultiAbility;
import com.projectkorra.projectkorra.ability.util.MultiAbilityManager;
import com.projectkorra.projectkorra.ability.util.MultiAbilityManager.MultiAbilityInfoSub;
import com.projectkorra.projectkorra.waterbending.multiabilities.WaterArms.Arm;

public class MetalHookMain extends MetalAbility implements MultiAbility {
   private static final List<Class<? extends MetalHookSubAbility>> ABILITIES;
   public static final String PATH = "Abilities.Earth.Metal.";
   private static long COOLDOWN;
   private static int MAX_USES;
   public final MetalHookSubAbility[] activeAbilities;
   private int counter;
   private int uses;
   private final boolean hadFlight;
   private Material helmetType;
   private Material chestplateType;
   private Material leggingsType;
   private Material bootsType;
   private Material mainType;
   private Material offType;

   static {
      (ABILITIES = new ArrayList()).add(Grapple.class);
      ABILITIES.add((Class<? extends MetalHookSubAbility>)null);
      ABILITIES.add(Grapple.class);
      ABILITIES.add(Clear.class);
      ABILITIES.add(Grab.class);
      ABILITIES.add(Grab.class);
      ABILITIES.add(Slam.class);
      ABILITIES.add(Slam.class);
      ABILITIES.add(Exit.class);
      COOLDOWN = ProjectUpd.plugin.getConfig().getLong("Abilities.Earth.Metal.MetalCables.Cooldown");
      MAX_USES = ProjectUpd.plugin.getConfig().getInt("Abilities.Earth.Metal.MetalCables.Uses");
   }

   public MetalHookMain(Player player) {
      super(player);
      this.activeAbilities = new MetalHookSubAbility[ABILITIES.size()];
      this.uses = MAX_USES;
      this.hadFlight = player.getAllowFlight();
      MultiAbilityManager.bindMultiAbility(player, this.getName());
      this.helmetType = MetalHookUtils.getTypeSafe(player.getInventory().getHelmet());
      this.chestplateType = MetalHookUtils.getTypeSafe(player.getInventory().getChestplate());
      this.leggingsType = MetalHookUtils.getTypeSafe(player.getInventory().getLeggings());
      this.bootsType = MetalHookUtils.getTypeSafe(player.getInventory().getBoots());
      this.mainType = MetalHookUtils.getTypeSafe(player.getInventory().getItemInMainHand());
      this.offType = MetalHookUtils.getTypeSafe(player.getInventory().getItemInOffHand());
      this.counter = 0;
      playMetalbendingSound(player.getEyeLocation());
      this.start();
   }

   public void progress() {
      ++this.counter;
      if (this.player != null && !this.player.isDead() && this.player.isOnline()) {
         if (this.counter % 4 == 0 && !this.checkItems()) {
            this.remove();
         } else {
            Arm[] values;
            int i = (values = Arm.values()).length;

            for(int j = 0; j < i; ++j) {
               Arm arm = values[j];
               Location loc = MetalHookUtils.getHand(this.player, arm);

               for(double y = -0.6D; y < 0.0D; y += 0.2D) {
                  loc.add(0.0D, y, 0.0D);
                  MetalHookUtils.particles(this.player, loc);
                  loc.subtract(0.0D, y, 0.0D);
               }
            }

            if (this.uses < 1) {
               this.remove();
            }

            for(i = 0; i < this.activeAbilities.length; ++i) {
               MetalHookSubAbility ab = this.activeAbilities[i];
               if (ab != null && ab.isRemoved()) {
                  this.activeAbilities[i] = null;
               }
            }

            this.pullBoth();
         }
      } else {
         this.remove();
      }
   }

   private boolean checkItems() {
      Material localHelmetType = MetalHookUtils.getTypeSafe(this.player.getInventory().getHelmet());
      Material localChestplateType = MetalHookUtils.getTypeSafe(this.player.getInventory().getChestplate());
      Material localLeggingsType = MetalHookUtils.getTypeSafe(this.player.getInventory().getLeggings());
      Material localBootsType = MetalHookUtils.getTypeSafe(this.player.getInventory().getBoots());
      Material localMainType = MetalHookUtils.getTypeSafe(this.player.getInventory().getItemInMainHand());
      Material localOffType = MetalHookUtils.getTypeSafe(this.player.getInventory().getItemInOffHand());
      if (localHelmetType != this.helmetType || localChestplateType != this.chestplateType || localLeggingsType != this.leggingsType || localBootsType != this.bootsType || localMainType != this.mainType || localOffType != this.offType) {
         boolean stillCorrectItems = MetalHookUtils.hasAppropriateItems(this.player);
         if (!stillCorrectItems) {
            return false;
         }

         this.helmetType = localHelmetType;
         this.chestplateType = localChestplateType;
         this.leggingsType = localLeggingsType;
         this.bootsType = localBootsType;
         this.mainType = localMainType;
         this.offType = localOffType;
      }

      return true;
   }

   private void pullBoth() {
      this.player.setAllowFlight(false);
      this.player.setFlying(false);
      int slot = this.player.getInventory().getHeldItemSlot();
      if (this.player.isSneaking() && slot == 1) {
         Vector direction = null;

         for(int i = 0; i <= 2; i += 2) {
            MetalHookSubAbility ab = this.activeAbilities[i];
            if (ab instanceof Grapple) {
               Grapple grapple = (Grapple)ab;
               if (grapple.hasHit()) {
                  if (direction == null) {
                     direction = new Vector();
                  }

                  direction.add(grapple.getActualDirection());
               }
            }
         }

         if (direction != null) {
            direction.multiply(0.5D);
            double factor;
            if (direction.length() > 3.0D) {
               factor = Grapple.PULL_SPEED / direction.length();
               direction.multiply(factor);
            } else if (direction.length() > 1.0D) {
               factor = 1.0D / direction.length();
               direction.multiply(factor);
            } else {
               direction.multiply(0);
            }

            this.player.setAllowFlight(true);
            this.player.setFlying(false);
            this.player.setFallDistance(0.0F);
            this.player.setVelocity(direction);
         }
      }
   }

   public ArrayList<MultiAbilityInfoSub> getMultiAbilities() {
      ArrayList<MultiAbilityInfoSub> list = new ArrayList();
      list.add(new MultiAbilityInfoSub("Grapple Left", Element.METAL));
      list.add(new MultiAbilityInfoSub("Grapple Both", Element.METAL));
      list.add(new MultiAbilityInfoSub("Grapple Right", Element.METAL));
      list.add(new MultiAbilityInfoSub("Grapple Clear", Element.METAL));
      list.add(new MultiAbilityInfoSub("Grab Left", Element.METAL));
      list.add(new MultiAbilityInfoSub("Grab Right", Element.METAL));
      list.add(new MultiAbilityInfoSub("Slam Left", Element.METAL));
      list.add(new MultiAbilityInfoSub("Slam Right", Element.METAL));
      list.add(new MultiAbilityInfoSub("Exit", Element.METAL));
      return list;
   }

   public void activateSubAbility(Player player) {
      int slot = player.getInventory().getHeldItemSlot();
      if (slot <= ABILITIES.size() - 1) {
         Class ability = (Class)ABILITIES.get(slot);

         try {
            MetalHookSubAbility newInstance;
            MetalHookSubAbility oldInstance;
            if (ability == null) {
               for(int i = 0; i <= 2; i += 2) {
                  ability = (Class)ABILITIES.get(i);
                  newInstance = (MetalHookSubAbility)ability.getConstructor(Player.class, Integer.TYPE).newInstance(player, i);
                  oldInstance = this.activeAbilities[i];
                  if (newInstance.oneAtATime() && oldInstance != null) {
                     oldInstance.remove();
                  }

                  this.activeAbilities[i] = newInstance;
               }

               return;
            }

            try {
               Constructor<? extends MetalHookSubAbility> constructor = ability.getConstructor(Player.class, Integer.TYPE);
               newInstance = (MetalHookSubAbility)constructor.newInstance(player, player.getInventory().getHeldItemSlot());
               oldInstance = this.activeAbilities[slot];
               if (newInstance.oneAtATime() && oldInstance != null) {
                  oldInstance.remove();
               }

               this.activeAbilities[slot] = newInstance;
               if (newInstance.UsePointsPerUse() != 0) {
                  this.uses -= newInstance.UsePointsPerUse();
                  if (this.uses < 0) {
                     this.uses = 0;
                  }

                  MultiAbilityInfoSub ab = (MultiAbilityInfoSub)this.getMultiAbilities().get(slot);
                  player.sendMessage(ab.getAbilityColor() + "Uses left: " + this.uses);
               }
            } catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException | IllegalAccessException var8) {
               var8.printStackTrace();
            }
         } catch (InstantiationException var9) {
         } catch (IllegalAccessException var10) {
         } catch (IllegalArgumentException var11) {
         } catch (InvocationTargetException var12) {
         } catch (NoSuchMethodException var13) {
         } catch (SecurityException var14) {
         }

      }
   }

   public void showActiveAbility(int slot, Player player) {
      List<MultiAbilityInfoSub> list = this.getMultiAbilities();
      if (slot <= list.size() - 1) {
         MultiAbilityInfoSub ability = (MultiAbilityInfoSub)list.get(slot);
         player.sendMessage(ability.getAbilityColor() + ability.getName().replace("_", " ") + " - Uses left: " + this.uses);
      }
   }

   public void remove() {
      super.remove();
      EarthAbility.playMetalbendingSound(this.player.getEyeLocation());
      MultiAbilityManager.unbindMultiAbility(this.player);
      BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(this.player);
      if (bPlayer != null) {
         bPlayer.addCooldown(getAbility(MetalHookMain.class));
      }

      this.player.setAllowFlight(this.hadFlight);
      this.player.setFlying(false);

      for(int i = 0; i < this.activeAbilities.length; ++i) {
         MetalHookSubAbility ab = this.activeAbilities[i];
         if (ab != null) {
            ab.remove();
         }
      }

   }

   public void stop() {
      PluginManager pm = ProjectKorra.plugin.getServer().getPluginManager();
      pm.removePermission("bending.ability." + this.getName());
      Iterator var3 = this.getMultiAbilities().iterator();

      while(var3.hasNext()) {
         MultiAbilityInfoSub mais = (MultiAbilityInfoSub)var3.next();
         pm.removePermission("bending.ability." + this.getName() + "." + mais.getName());
      }

   }

   public long getCooldown() {
      return COOLDOWN;
   }

   public Location getLocation() {
      return this.player.getLocation();
   }

   public String getName() {
      return "MetalCables";
   }

   public boolean isHarmlessAbility() {
      return false;
   }

   public boolean isSneakAbility() {
      return true;
   }
   @Override
	public boolean isEnabled() {
		return ProjectUpd.plugin.getConfig().getBoolean("Abilities.Earth.Metal.MetalCables.Enabled");
	 }
	 @Override
	 public String getDescription() {
		return ProjectUpd.plugin.getConfig().getString("Language.Abilities.Earth.Metal.MetalCables.Description");
	 }
  
	 @Override
	 public String getInstructions() {
		return ProjectUpd.plugin.getConfig().getString("Language.Abilities.Earth.Metal.MetalCables.Instructions");
	 } 
}
