package com.darkermoon.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.darkermoon.ability.fire.FieryCalm;
import com.darkermoon.ability.fire.FieryRage;
import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.attribute.AttributeModifier;
import com.projectkorra.projectkorra.event.AbilityStartEvent;

public class FieryRageListener implements Listener {

    @EventHandler
    public void onAbilityStart(AbilityStartEvent event) {
        CoreAbility ability = (CoreAbility) event.getAbility();
        Player p = ability.getPlayer();
        FieryRage fr = CoreAbility.getAbility(p, FieryRage.class);
        FieryCalm fc = CoreAbility.getAbility(p, FieryCalm.class);
        BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(p);

        if (bPlayer != null) {
        	 if (CoreAbility.hasAbility(p, FieryRage.class)) {
                 if (ability.getElement() == Element.FIRE) {
                     if (FieryRage.ragedPlayers.contains(p)) {
                         double modifier = fr.powerLevel;
                         double cmodifier = fr.cmodifier;
                         double chargemodifier = fr.chargemodifierlevel;
                         try {
                             if (fr.speed) {
                                 ability.addAttributeModifier("Speed", modifier, AttributeModifier.MULTIPLICATION);
                             }
                         } catch (Exception e) {
                         } try {
                             if (fr.damage) {
                                 ability.addAttributeModifier("Damage", modifier, AttributeModifier.MULTIPLICATION);
                             }
                         } catch (Exception e) {
                         } try {
                             if (fr.range) {
                                 ability.addAttributeModifier("Range", modifier, AttributeModifier.MULTIPLICATION);
                             }
                         } catch (Exception e) {
                         } try {
                             if (fr.cooldown) {
                                 ability.addAttributeModifier("Cooldown", cmodifier, AttributeModifier.MULTIPLICATION);
                             }
                         } catch (Exception e) {
                         } try {
                             if (fr.chargemodifier) {
                             	ability.addAttributeModifier("ChargeTime", chargemodifier, AttributeModifier.MULTIPLICATION);
                             }
                         } catch (Exception e) {
                         }
                     }
                 }
        	 }
        	 if (CoreAbility.hasAbility(p, FieryCalm.class)) {
                 if (ability.getElement() == Element.FIRE) {
                     if (FieryCalm.calmPlayers.contains(p)) {
                         double fcmodifier = fc.fccmodifier;
                         double fchargemodifier = fc.chargemodifierlevel;
                         try {
                             if (fr.cooldown) {
                                 ability.addAttributeModifier("Cooldown", fcmodifier, AttributeModifier.MULTIPLICATION);
                             }
                         } catch (Exception e) {
                         } try {
                             if (fr.chargemodifier) {
                             	ability.addAttributeModifier("ChargeTime", fchargemodifier, AttributeModifier.MULTIPLICATION);
                             }
                         } catch (Exception e) {
                         }
                     }
                     
                 }
             }
         }
        /*if (CoreAbility.hasAbility(p, WaterAffinity.class)) {
            if (ability.getElement() == Element.WATER) {
                if (WaterAffinity.waterAffinityPlayers.contains(p)) {
                }
                }
                }*/
        
    }
    
    
    
}

