package de.pauhull.hologramapi;

import lombok.Getter;
import net.minecraft.server.v1_12_R1.EntityArmorStand;
import net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_12_R1.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * Created by Paul
 * on 20.03.2019
 *
 * @author pauhull
 */
public class FloatingSkull {

    @Getter
    private String owner;

    @Getter
    private Location location;

    @Getter
    private String customName;

    public FloatingSkull(String owner, Location location) {
        this(owner, location, null);
    }

    public FloatingSkull(String owner, Location location, String customName) {
        this.owner = owner;
        this.location = location;
        this.customName = customName;
    }

    public FloatingSkull spawn() {
        return spawn(Bukkit.getOnlinePlayers().toArray(new Player[0]));
    }

    public FloatingSkull spawn(Player... players) {

        WorldServer world = ((CraftWorld) location.getWorld()).getHandle();
        EntityArmorStand armorStand = new EntityArmorStand(world);

        if (customName != null) {
            armorStand.setCustomName(customName);
            armorStand.setCustomNameVisible(true);
        }

        armorStand.setLocation(location.getX(), location.getY(), location.getZ(), 0, 0);
        armorStand.setNoGravity(true);
        armorStand.setSmall(true);
        armorStand.setInvisible(true);

        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(armorStand);

        for (Player player : players) {
            if (player.getWorld().getName().equals(location.getWorld().getName())) {
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }
        }

        return this;
    }

}