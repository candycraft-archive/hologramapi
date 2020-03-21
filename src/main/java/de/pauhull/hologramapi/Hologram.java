package de.pauhull.hologramapi;

import lombok.Getter;
import net.minecraft.server.v1_12_R1.EntityArmorStand;
import net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_12_R1.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Paul
 * on 20.03.2019
 *
 * @author pauhull
 */
public class Hologram {

    private static final double LINE_SPACE = 0.25;

    @Getter
    private List<String> lines;

    @Getter
    private Location location;

    public Hologram(String text, Location location) {
        this(Arrays.asList(text.split("\n")), location);
    }

    public Hologram(List<String> lines, Location location) {
        this.lines = lines;
        this.location = location;
    }

    public Hologram spawn() {
        return spawn(Bukkit.getOnlinePlayers().toArray(new Player[0]));
    }

    public Hologram spawn(Player... players) {
        WorldServer world = ((CraftWorld) location.getWorld()).getHandle();

        Location holoLocation = location.clone();

        for (String line : lines) {
            for (Player player : players) {
                if (player.getWorld().getName().equals(location.getWorld().getName())) {

                    EntityArmorStand armorStand = new EntityArmorStand(world);

                    armorStand.setLocation(holoLocation.getX(), holoLocation.getY(), holoLocation.getZ(), 0, 0);
                    armorStand.setCustomName(ChatColor.translateAlternateColorCodes('&', line.replace("%PLAYER%", player.getName())));
                    armorStand.setCustomNameVisible(true);
                    armorStand.setNoGravity(true);
                    armorStand.setSmall(true);
                    armorStand.setInvisible(true);

                    PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(armorStand);
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                }
            }

            holoLocation.subtract(0, LINE_SPACE, 0);
        }

        return this;
    }

}
