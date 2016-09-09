package me.arvnar.system.api;

import java.lang.reflect.Constructor;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Title {

	public static void sendPacket(Player player, Object packet) {

		try {

			Object handle = player.getClass().getMethod("getHandle").invoke(player);
			Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
			playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	public static Class<?> getNMSClass(String name) {

		String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

		try {

			return Class.forName("net.minecraft.server." + version + "." + name);

		} catch (ClassNotFoundException e) {

			e.printStackTrace();
			return null;
		}
	}

	public static void sendTitle(Player player, String title, String subtitle, int fadein, int stay, int fadeout) {

		title = ChatColor.translateAlternateColorCodes('&', title);
		subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);

		Class<?> chatSerializer = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0];
		Class<?> chatComponent = getNMSClass("IChatBaseComponent");
		Class<?> packetTitle = getNMSClass("PacketPlayOutTitle");

		try {

			Object enumTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
			Constructor<?> constructorTitle = packetTitle.getDeclaredConstructor(packetTitle.getDeclaredClasses()[0],chatComponent, int.class, int.class, int.class);
			Object chatTitle = chatSerializer.getMethod("a", String.class).invoke(chatSerializer, "{\"text\": \"" + title + "\"}");
			Object packet = constructorTitle.newInstance(enumTitle, chatTitle, fadein, stay, fadeout);
			sendPacket(player, packet);

		} catch (Exception ex) {

			ex.printStackTrace();
		}

		try {

			Object enumSubtitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
			Constructor<?> constructorSubtitle = packetTitle.getDeclaredConstructor(packetTitle.getDeclaredClasses()[0], chatComponent, int.class, int.class, int.class);
			Object chatSubtitle = chatSerializer.getMethod("a", String.class).invoke(chatSerializer, "{\"text\": \"" + subtitle + "\"}");
			Object packet = constructorSubtitle.newInstance(enumSubtitle, chatSubtitle, fadein, stay, fadeout);
			sendPacket(player, packet);

		} catch (Exception ex) {

			ex.printStackTrace();
		}
	}
}
