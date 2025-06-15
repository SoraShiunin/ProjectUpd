package com.darkermoon.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.darkermoon.ability.bluefire.SuperHeat;
import com.darkermoon.ability.bluefire.SuperHeat.SuperHeatType;
import com.darkermoon.ability.chi.HoverBoard;
import com.darkermoon.ability.lava.LavaSlip;
import com.darkermoon.ability.water.Stream;
import com.darkermoon.ability.water.Whip;
import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.util.BlockSource;
import com.projectkorra.projectkorra.util.ClickType;

public class DarkerListeners implements Listener {

   @EventHandler
   public void onClick(PlayerAnimationEvent event) {
      if (!event.isCancelled()) {
         BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(event.getPlayer());
         if (bPlayer != null && bPlayer.canBend(CoreAbility.getAbility("Stream"))) {
            new Stream(event.getPlayer());
         }
         else if (bPlayer != null && bPlayer.canBend(CoreAbility.getAbility("Whip"))) {
            System.out.println("Whip");
            new Whip(event.getPlayer());
         }
         else if (bPlayer != null && bPlayer.canBend(CoreAbility.getAbility("LavaSlip"))) {
            System.out.println("LavaSlip");
            new LavaSlip(event.getPlayer());
         }

      }
   }
   
    @EventHandler
    public void onClick(PlayerInteractEvent e){
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) return;
        Player p = e.getPlayer();
        BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(p);
        if (bPlayer.getBoundAbilityName().equalsIgnoreCase("HoverBoard")){
            new HoverBoard(p);
        }
    }

  @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerSneak(final PlayerToggleSneakEvent event) {
		final Player player = event.getPlayer();
		if (BendingPlayer.isWorldDisabled(player.getWorld())) {
			return;
		}
		final BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
		if (bPlayer == null) {
			return;
		}
		
		if (bPlayer.isChiBlocked()) {
			event.setCancelled(true);
			return;
		}

		if (!player.isSneaking()) {
			BlockSource.update(player, ClickType.SHIFT_DOWN);
		}

      if (bPlayer != null && bPlayer.canBend(CoreAbility.getAbility("SuperHeat"))) {
         new SuperHeat(event.getPlayer(), SuperHeatType.COOK);
	  	}

		else if(CoreAbility.hasAbility(player, HoverBoard.class) && bPlayer.getBoundAbilityName().equalsIgnoreCase("HoverBoard") && !HoverBoard.getStatus()) {
            CoreAbility.getAbility(player, HoverBoard.class).jump(player);
        }
      
   }
}
      
		/*Copy Paste for Future Updates
      if (coreAbil == null || !coreAbil.isSneakAbility()) {
			if (PassiveManager.hasPassive(player, CoreAbility.getAbility(FerroControl.class))) {
				new FerroControl(player);
			}

			if (PassiveManager.hasPassive(player, CoreAbility.getAbility(FastSwim.class))) {
				new FastSwim(player);
			}
		}
*/
