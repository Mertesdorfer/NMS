package me.arvnar.system.api;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Tablist_NMS {

	public static void sendTablist(Player p, String Header, String Footer) {

		try {

			Object tabHeader = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + Header + "\"}");
			Object tabFooter = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + Footer + "\"}");

			Constructor<?> titleConstructor = getNMSClass("PacketPlayOutPlayerListHeaderFooter").getConstructor(getNMSClass("IChatBaseComponent"));

			Object packet = titleConstructor.newInstance(tabHeader);
			Field field = packet.getClass().getDeclaredField("b");

			field.setAccessible(true);
			field.set(packet, tabFooter);

			sendPacket(p, packet);

		} catch (Exception ex) {

			ex.printStackTrace();
		}
	}

	public static Class<?> getNMSClass(String Name) {

		String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

		try {

			return Class.forName("net.minecraft.server." + version + "." + Name);

		} catch (ClassNotFoundException e) {

			e.printStackTrace();

			return null;
		}
	}

	public static void sendPacket(Player player, Object packet) {

		try {

			Object handle = player.getClass().getMethod("getHandle").invoke(player);
			Object playerConnection = handle.getClass().getField("playerConnection").get(handle);

			playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}
}