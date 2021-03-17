package me.yonatan.login;
import java.lang.reflect.Field;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;

public class Main extends JavaPlugin{

	private static LoginManager manager;
	
	public static LoginManager getManager() {
		return manager;
	}
	
	public static ItemStack newHeadSkin(String url, String nome, int amount, List<String> lore) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, amount, (short) 3);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(nome);
        meta.setLore(lore);
        item.setItemMeta(meta);
        if (url.isEmpty())
            return item;

        SkullMeta itemMeta = (SkullMeta) item.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.getEncoder()
                .encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField;
        try {
            profileField = itemMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(itemMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        item.setItemMeta(itemMeta);
        return item;
    }
	
	private static Main instance;
	public static Main getInstance() {
        return instance;
    }
	public static void sendActionBar(Player player, String message) {
        PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(new ChatComponentText(message), (byte) 2);
      ((CraftPlayer) player).getHandle().playerConnection.sendPacket( packetPlayOutChat );

    }
	
	public static void sendTitle(Player player, String title, String subtitle) {
        PacketPlayOutTitle packetTitle;
        if (title != null) {
            packetTitle = new PacketPlayOutTitle(EnumTitleAction.TITLE, CraftChatMessage.fromString(title)[0]);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket( packetTitle );
        }
        PacketPlayOutTitle packetSubtitle;
        if (subtitle != null) {
            packetSubtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, CraftChatMessage.fromString(subtitle)[0]);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket( packetSubtitle );
        }

    }
	
	@Override
	public void onEnable() {
		instance = this;
		
		manager = new LoginManager();
		
		Bukkit.getConsoleSender().sendMessage("§aPlugin de login ativado.");
		Bukkit.getPluginCommand("registrar").setExecutor(new ComandoRegistrar());
		Bukkit.getPluginCommand("login").setExecutor(new ComandoLogin());
		Bukkit.getPluginManager().registerEvents(new LoginListener(), this);
		Bukkit.getPluginManager().registerEvents(new Captcha(), this);
		getDataFolder().mkdirs();
		
		
		manager.setUseSQL(true);
		manager.getSql().setDatabase("minecraft");
        manager.getSql().abrirMySQL();
        manager.getSql().criarTabela();
        manager.reloadFromSQL();
        
	}
	
	@Override
	public void onDisable() {
		manager.getSql().fechar();
		
	}
	
}
