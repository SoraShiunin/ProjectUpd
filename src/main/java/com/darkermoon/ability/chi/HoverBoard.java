package com.darkermoon.ability.chi;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import com.darkermoon.ProjectUpd;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ability.ChiAbility;
import com.projectkorra.projectkorra.ability.CoreAbility;
import com.projectkorra.projectkorra.util.DamageHandler;

public class HoverBoard extends ChiAbility {
    private ArmorStand stand;
    private EntityEquipment equipment;
    private final List<ArmorStand> standList = new ArrayList<>();
    private Listener listener;
    private static Particle.DustOptions blueParticleOptions = new Particle.DustOptions(Color.fromRGB(137,207,240),1.5f);
    private static Particle.DustOptions whiteParticleOptions = new Particle.DustOptions(Color.fromRGB(255,255,255),1.5f);
    private static Particle.DustOptions redParticleOptions = new Particle.DustOptions(Color.fromRGB(139,0,0) ,1.5f);
    private static Particle.DustOptions blackParticleOptions = new Particle.DustOptions(Color.fromRGB(0,0,0),1.5f);
    private final long cooldown,duration;
    private final double shootSpeed,rideSpeed;
    private final int hoverHeight;
    private final double shootRange;
    private final double explodeSize,explodeDamage,playerExplodeDamage;
    private final double jumpVelocity;
    private static boolean jump;
    private Location floorLocation,shootOrigin;
    private Vector playerJumpDirection;
    private final double shootHitbox;

    public HoverBoard(Player player) {
        super(player);
        final FileConfiguration config = ProjectUpd.plugin.getConfig();
        String path = "Abilities.Chi.HoverBoard.";


        cooldown = config.getLong("Abilities.Chi.HoverBoard.Cooldown");
        duration = config.getLong("Abilities.Chi.HoverBoard.Duration");
        shootRange = config.getDouble("Abilities.Chi.HoverBoard.ShootRange");
        playerExplodeDamage = config.getDouble("Abilities.Chi.HoverBoard.PlayerExplodeDamage");
        explodeSize = config.getDouble("Abilities.Chi.HoverBoard.ExplodeSize");
        hoverHeight = config.getInt("Abilities.Chi.HoverBoard.HoverHeight");
        rideSpeed = config.getDouble(path +"RideSpeed");
        shootSpeed = config.getDouble("Abilities.Chi.HoverBoard.ShootSpeed");
        explodeDamage = config.getDouble("Abilities.Chi.HoverBoard.Damage");
        jumpVelocity = config.getDouble("Abilities.Chi.HoverBoard.JumpVelocity");
        shootHitbox = config.getDouble("Abilities.Chi.HoverBoard.ShootHitbox");

        if (CoreAbility.hasAbility(player,getClass()) || !bPlayer.canBend(this)) return;
        int x = 270;
        for (int i = 0; i < 3; i++) {
            stand = (ArmorStand) player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
            stand.setVisible(false);
            equipment = stand.getEquipment();
            equipment.setHelmet(new ItemStack(Material.HEAVY_WEIGHTED_PRESSURE_PLATE));
            stand.addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.REMOVING_OR_CHANGING);
            standList.add(stand);
            stand.setHeadPose(new EulerAngle(x, 0, 0));
            x -= 270;
            jump = false;
        }
        player.setAllowFlight(true);
        start();
    }


    @Override
    public void progress() {
        if (GeneralMethods.isRegionProtectedFromBuild(this, player.getLocation())) {
            remove();
            return;
        }else if (System.currentTimeMillis() - getStartTime() >= duration){
            remove();
            return;
        }else{
            if (!jump) {
                Location location = player.getLocation().subtract(0.0D, 1.35, 0.0D);
                location.add(player.getVelocity().getX() * 2.95, 0, player.getVelocity().getZ() * 2.95);
                //Don't rotate up/down
                location.setPitch(0);
                location.getWorld().playSound(location,Sound.BLOCK_BEACON_AMBIENT,.25f,1.9f);
                for (int i = 0; i < standList.size(); i++) {
                    //spawns particles on each plate
                    Location standLocation = standList.get(i).getLocation();
                    standLocation.getWorld().spawnParticle(Particle.DUST, standLocation.clone().add(0, 1.35, 0), 1, 0, 0, 0, .5, new Particle.DustOptions(Color.fromRGB(137, 207, 240), 1.5f));
                    //The first armorstand created was the one infront of the player
                    if (i == 0) {
                        standList.get(i).teleport(location.clone().add(location.getDirection().multiply(.55)));
                        if (standList.get(i).getLocation().add(0, 1.5, 0).getBlock().getType().isSolid()) {
                            explode(player.getLocation(), explodeSize);
                        }
                        for (Entity entity : GeneralMethods.getEntitiesAroundPoint(standList.get(i).getLocation().add(0, 1, 0), .5)) {
                            if (entity instanceof LivingEntity && !(entity instanceof ArmorStand)) {
                                if (entity.getUniqueId() == player.getUniqueId()) continue;
                                explode(player.getLocation(), explodeSize);
                            }
                        }
                    } else if (i == 1) { //The second armorstand was the one on the player location
                        standList.get(i).teleport(location);
                    } else { // The third armorstand was the one behind the player
                        standList.get(i).teleport(location.clone().add(location.getDirection().multiply(-.55)));

                        Location loc = standList.get(i).getLocation().clone();
                        Vector pDir = loc.getDirection().multiply(-.777);
                        for (int p = 0; p < 3; p++) {
                            loc.getWorld().spawnParticle
                                    (Particle.DUST, loc.clone().add(pDir).add(0, 1.35, 0), 1, .5, 0, .5, 1, blueParticleOptions);
                            loc.getWorld().spawnParticle
                                    (Particle.DUST, loc.clone().add(pDir).add(0, 1.35, 0), 1, .5, 0, .5, 1, whiteParticleOptions);
                            loc.getWorld().spawnParticle
                                    (Particle.FIREWORK, loc.clone().add(pDir).add(0, 1.35, 0), 1, .75, 0, .75, .15);
                        }
                    }
                }

                Block standingblock = location.getBlock();
                int i = 0;
                while (i <= 255) {
                    Block block = standingblock.getRelative(BlockFace.DOWN, i);
                    if (GeneralMethods.isSolid(block) || block.isLiquid()) {
                        floorLocation = block.getLocation();
                        break;
                    }
                    ++i;
                }


                if ((int) (player.getLocation().getY() - floorLocation.getY()) > hoverHeight) {
                    player.setVelocity(new Vector(location.getDirection().getX() * rideSpeed, -.25, location.getDirection().getZ() * rideSpeed));
                } else if ((int) (player.getLocation().getY() - floorLocation.getY()) < hoverHeight) {
                    player.setVelocity(new Vector(location.getDirection().getX() * rideSpeed, .25, location.getDirection().getZ() * rideSpeed));
                } else {
                    player.setVelocity(new Vector(location.getDirection().getX() * rideSpeed, 0, location.getDirection().getZ() * rideSpeed));
                }

            } else {
                if (standList.get(0).getLocation().distanceSquared(shootOrigin) >= (shootRange * shootRange)) {
                    explode(standList.get(0).getLocation(), explodeSize);
                    return;
                }
                standList.get(0).getWorld().playSound(standList.get(0).getLocation(),Sound.BLOCK_BEACON_DEACTIVATE,.65f,1.9f);
                for (int i = 0; i < standList.size(); i++) {
                    if (GeneralMethods.isSolid(standList.get(i).getLocation().add(playerJumpDirection.multiply(1)).getBlock())) {
                        explode(standList.get(i).getLocation(), explodeSize);
                    }

                    standList.get(i).setVelocity(playerJumpDirection);
                    if (i == 0) {
                        for (Entity entity : GeneralMethods.getEntitiesAroundPoint(standList.get(i).getLocation(),shootHitbox )) {
                            if (entity.equals(player) || !(entity instanceof LivingEntity) || entity instanceof ArmorStand)
                                continue;
                            explode(standList.get(i).getLocation(), explodeSize);
                        }
                    } else if (i == 2) {
                        standList.get(i).getWorld().spawnParticle
                                (Particle.DUST, standList.get(i).getLocation().add(playerJumpDirection).add(0, 1.35, 0), 3, .5, 0, .5, 1, redParticleOptions);
                        standList.get(i).getWorld().spawnParticle
                                (Particle.DUST, standList.get(i).getLocation().add(playerJumpDirection).add(0, 1.35, 0), 3, .5, 0, .5, 1, blackParticleOptions);
                        standList.get(i).getWorld().spawnParticle
                                (Particle.FIREWORK, standList.get(i).getLocation().add(playerJumpDirection).add(0, 1.35, 0), 3, .75, 0, .75, .15);
                    }
                }

            }
        }

    }



    private void explode(Location location,double size) {
        location.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME,location,150,1,1,1,2f);
        location.getWorld().spawnParticle(Particle.FIREWORK,location,5,1,1,1,.5f);
        location.getWorld().spawnParticle(Particle.FIREWORK,location,5,1,1,1,.5f);
        location.getWorld().spawnParticle(Particle.CLOUD,location,2,1,1,1,.5f);
        location.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, .5f, 1.34f);
        for (Entity e : GeneralMethods.getEntitiesAroundPoint(location, size)) {
            if (e instanceof LivingEntity){
                if (e.getUniqueId() == player.getUniqueId()){
                    DamageHandler.damageEntity(e,playerExplodeDamage,this);
                }else {
                    DamageHandler.damageEntity(e, explodeDamage, this);
                }
            }
        }
        remove();
    }

    public void setJump(){
        Location playerLoc = player.getLocation();
        shootOrigin = playerLoc.clone();
        playerLoc.setPitch(0);
        playerJumpDirection = playerLoc.getDirection().multiply(shootSpeed);
        player.setVelocity(new Vector(playerLoc.getDirection().getX() * -jumpVelocity,.5,playerLoc.getDirection().getZ() * -jumpVelocity));
        jump = true;
    }

    public void jump(Player player) {
        HoverBoard abil = getAbility(player, HoverBoard.class);
        if (abil != null) abil.setJump();
    }

    @Override
    public void remove() {
        super.remove();
        player.setAllowFlight(false);
        bPlayer.addCooldown(this,cooldown);
        for (ArmorStand stand : standList) {
            stand.remove();
        }
    }

    @Override
    public boolean isSneakAbility() {
        return false;
    }

    @Override
    public boolean isHarmlessAbility() {
        return false;
    }

    @Override
    public long getCooldown() {
        return cooldown ;
    }

    @Override
    public String getName() {
        return "HoverBoard";
    }

    @Override
    public Location getLocation() {
        return player.getLocation();
    }

    public static boolean getStatus(){
        return jump;
    }

    @Override
    public boolean isEnabled() {
        return ProjectUpd.plugin.getConfig().getBoolean("Abilities.Chi.HoverBoard.Enabled");
    }

    @Override
    public String getInstructions() {
        return ProjectUpd.plugin.getConfig().getString("Language.Abilities.Chi.HoverBoard.Instructions");       
    }

    @Override
    public String getDescription() {
        return ProjectUpd.plugin.getConfig().getString("Language.Abilities.Chi.HoverBoard.Description");
    }

}