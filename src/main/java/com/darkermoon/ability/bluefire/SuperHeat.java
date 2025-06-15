package com.darkermoon.ability.bluefire;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.darkermoon.ProjectUpd;
import com.projectkorra.projectkorra.ability.BlueFireAbility;
import com.projectkorra.projectkorra.attribute.Attribute;
import com.projectkorra.projectkorra.util.ParticleEffect;

public class SuperHeat extends BlueFireAbility {

	public enum SuperHeatType {
		COOK, MELT
	}

	private static final Material[] COOKABLE_MATERIALS = { Material.BEEF, Material.CHICKEN, Material.COD, 
		Material.PORKCHOP, Material.POTATO, Material.RABBIT, Material.MUTTON, Material.SALMON, Material.KELP, 
		Material.WET_SPONGE, Material.CHORUS_FRUIT, Material.STICK, Material.RAW_COPPER, Material.RAW_GOLD, 
		Material.RAW_IRON, Material.RAW_COPPER_BLOCK, Material.RAW_GOLD_BLOCK,
		Material.RAW_IRON_BLOCK, Material.DIAMOND_ORE, Material.EMERALD_ORE, Material.NETHER_GOLD_ORE, 
		Material.NETHER_QUARTZ_ORE, Material.ANCIENT_DEBRIS};

	private SuperHeatType SuperHeatType;

	// SuperHeat Cook variables.

	private long cookTime;
	@Attribute("CookDuration")
	private long cookInterval;

	public SuperHeat(final Player player, final SuperHeatType SuperHeatType) {
		super(player);

		this.SuperHeatType = SuperHeatType;
		this.setFields();

		if (this.SuperHeatType == SuperHeatType.COOK) {
			if (!isCookable(player.getInventory().getItemInMainHand().getType())) {
				this.remove();
				return;
			}
			this.start();

		} 
	}

	public void setFields() {
		if (this.SuperHeatType == SuperHeatType.COOK) {
			this.cookTime = System.currentTimeMillis();
			//this.cookInterval = getConfig().getLong("Abilities.BlueFire.SuperHeat.Cook.Interval");
			this.cookInterval = ProjectUpd.plugin.getConfig().getLong("Abilities.BlueFire.SuperHeat.Cook.Interval");
	}
}

	@Override
	public void progress() {

		if (!this.bPlayer.canBend(this)) {
			this.remove();
			return;
		}

		if (this.SuperHeatType == SuperHeatType.COOK) {

			if (!this.player.isSneaking()) {
				this.remove();
				return;
			}

			if (!isCookable(this.player.getInventory().getItemInMainHand().getType())) {
				this.remove();
				return;
			}

			if (System.currentTimeMillis() - this.cookTime > this.cookInterval) {
				this.cook();
				this.cookTime = System.currentTimeMillis();
				return;
			}

			this.displayCookParticles();

		}
} 

	private void cook() {
		final ItemStack cooked = this.getCooked(this.player.getInventory().getItemInMainHand());
		final HashMap<Integer, ItemStack> cantFit = this.player.getInventory().addItem(cooked);
		for (final int id : cantFit.keySet()) {
			this.player.getWorld().dropItem(this.player.getEyeLocation(), cantFit.get(id));
		}

		final int amount = this.player.getInventory().getItemInMainHand().getAmount();
		if (amount == 1) {
			this.player.getInventory().clear(this.player.getInventory().getHeldItemSlot());
		} else {
			this.player.getInventory().getItemInMainHand().setAmount(amount - 1);
		}
	}

	private ItemStack getCooked(final ItemStack is) {
		ItemStack cooked = new ItemStack(Material.AIR);
		final Material material = is.getType();

		switch (material) {
			case BEEF:
				cooked = new ItemStack(Material.COOKED_BEEF);
				break;
			case COD:
				cooked = new ItemStack(Material.COOKED_COD);
				break;
			case CHICKEN:
				cooked = new ItemStack(Material.COOKED_CHICKEN);
				break;
			case PORKCHOP:
				cooked = new ItemStack(Material.COOKED_PORKCHOP);
				break;
			case POTATO:
				cooked = new ItemStack(Material.BAKED_POTATO);
				break;
			case MUTTON:
				cooked = new ItemStack(Material.COOKED_MUTTON);
				break;
			case RABBIT:
				cooked = new ItemStack(Material.COOKED_RABBIT);
				break;
			case SALMON:
				cooked = new ItemStack(Material.COOKED_SALMON);
				break;
			case KELP:
				cooked = new ItemStack(Material.DRIED_KELP, 2);
				break;
			case CHORUS_FRUIT:
				cooked = new ItemStack(Material.POPPED_CHORUS_FRUIT);
				break;
			case WET_SPONGE:
				cooked = new ItemStack(Material.SPONGE);
				break;
			case RAW_COPPER:
				cooked = new ItemStack(Material.COPPER_INGOT, 2);
				break;
			case RAW_IRON:
				cooked = new ItemStack(Material.IRON_INGOT, 2);
				break;
			case RAW_GOLD:
				cooked = new ItemStack(Material.GOLD_INGOT, 2);
				break;
			case STICK:
				cooked = new ItemStack(Material.SOUL_TORCH);
				break;
			case RAW_COPPER_BLOCK:
				cooked = new ItemStack(Material.COPPER_BLOCK, 2);
				break;
			case RAW_IRON_BLOCK:
				cooked = new ItemStack(Material.IRON_BLOCK, 2);
				break;
			case RAW_GOLD_BLOCK:
				cooked = new ItemStack(Material.GOLD_BLOCK, 2);
				break;
			case DIAMOND_ORE:
				cooked = new ItemStack(Material.DIAMOND, 2);
				break;
			case EMERALD_ORE:
				cooked = new ItemStack(Material.EMERALD, 2);
				break;
			case NETHER_GOLD_ORE:
				cooked = new ItemStack(Material.GOLD_NUGGET, 2);
				break;
			case NETHER_QUARTZ_ORE:
				cooked = new ItemStack(Material.QUARTZ, 2);
				break;
			case ANCIENT_DEBRIS:
				cooked = new ItemStack(Material.NETHERITE_SCRAP, 2);
				break;

			default:
				break;
		}

		return cooked;
	}

	public void displayCookParticles() {
		playFirebendingParticles(this.player.getLocation().clone().add(0, 1, 0), 3, 0.5, 0.5, 0.5);
		ParticleEffect.SOUL_FIRE_FLAME.display(this.player.getLocation().clone().add(0, 1, 0), 8, 0.5, 0.5, 0.5);
		ParticleEffect.SMOKE_NORMAL.display(this.player.getLocation().clone().add(0, 1, 0), 2, 0.5, 0.5, 0.5);
	}

	public static boolean isCookable(final Material material) {
		return Arrays.asList(COOKABLE_MATERIALS).contains(material);
	}


	@Override
	public boolean isSneakAbility() {
		return true;
	}

	@Override
	public boolean isHarmlessAbility() {
		if (this.SuperHeatType != null) {
			return this.SuperHeatType.equals(SuperHeatType.COOK);
		} else {
			return false;
		}
	}

	@Override
	public long getCooldown() {
		return 0;
	}

	@Override
	public String getName() {
		return "SuperHeat";
	}

	@Override
	public Location getLocation() {
		return this.player.getLocation();
	}

	@Override
	public boolean isEnabled() {
		return ProjectUpd.plugin.getConfig().getBoolean("Abilities.BlueFire.SuperHeat.Enabled");
	 }
	 @Override
	 public String getDescription() {
		return ProjectUpd.plugin.getConfig().getString("Language.Abilities.BlueFire.SuperHeat.Description");
	 }
  
	 @Override
	 public String getInstructions() {
		return ProjectUpd.plugin.getConfig().getString("Language.Abilities.BlueFire.SuperHeat.Instructions");
	 } 
}