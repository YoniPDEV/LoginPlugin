package me.yonatan.login;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class Captcha implements Listener {

//	private final static ItemStack vidroVerde;
//	private final static ItemStack vidroCinza;
	private final static String name = "§7Clique na cabeça vermelha";
	public static final Set<Player> playersCaptcha = new HashSet<Player>();
	
	
	static ItemStack ca1 = Main.newHeadSkin("http://textures.minecraft.net/texture/5fde3bfce2d8cb724de8556e5ec21b7f15f584684ab785214add164be7624b", 
			"§cClique aqui!", 1, new ArrayList<>());
	
	static ItemStack ca2 = Main.newHeadSkin("http://textures.minecraft.net/texture/608f323462fb434e928bd6728638c944ee3d812e162b9c6ba070fcac9bf9", 
			"§fClique na cabeça vermelha :(", 1, new ArrayList<>());
	
		
//	static {
//		
//
//		vidroVerde = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 3);
//		{
//			ItemMeta meta = vidroVerde.getItemMeta();
//			meta.setDisplayName("");
//			vidroVerde.setItemMeta(meta);
//		}
//		vidroCinza = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11);
//		{
//			ItemMeta meta = vidroCinza.getItemMeta();
//			meta.setDisplayName("");
//			vidroVerde.setItemMeta(meta);
//		}
//	}

	public static void setCaptcha(Player p) {
		playersCaptcha.add(p);
	}

	@EventHandler
	public void aosair(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (!playersCaptcha.contains(p))return;
			playersCaptcha.remove(p);
		
	}

	@EventHandler
	public void event(InventoryCloseEvent e) {
		Player player = (Player) e.getPlayer();
		if (playersCaptcha.contains(player)) {
			player.kickPlayer("§4§lATENÇÃO: §FVocê fechou o captcha.");
		}
	}

	public static void open(Player player) {
		Inventory menu = Bukkit.createInventory(null, 3 * 9, name);
		for (int id = 0; id < menu.getSize(); id++) {
			if (Math.random() < 0.1) {
				menu.setItem(id, ca1);
			} else {
				menu.setItem(id, ca2);
			}
		}
		if(completouCaptcha(menu)) {
			if(Math.random() < 0.5) {
				menu.setItem(4, ca1);
				menu.setItem(7, ca1);
				menu.setItem(14, ca1);
			} else {
				menu.setItem(1, ca1);
				menu.setItem(9, ca1);
				menu.setItem(17, ca1);
				menu.setItem(8, ca1);
			}
		}
		player.openInventory(menu);
	}

	public static boolean completouCaptcha(Inventory menu) {
		for (ItemStack item : menu.getContents()) {
			if (item == null)
				continue;
			if (item.equals(ca1))
				return false;
		}
		return true;
	}

	@EventHandler
	public void click(InventoryClickEvent e) {
		if (!e.getInventory().getName().equals(name))
			return;
		if (e.getCurrentItem() == null)
			return;
		Player player = (Player) e.getWhoClicked();
		e.setCancelled(true);
		if (e.getCurrentItem().equals(ca2)) {
			player.kickPlayer("§4§lATENÇÃO: §FVocê errou o captcha.");
		} else if (e.getCurrentItem().equals(ca1)) {
			e.getInventory().setItem(e.getRawSlot(), ca2);
			if (completouCaptcha(e.getInventory())) {
				playersCaptcha.remove(player);
				player.closeInventory();
				if(!Main.getManager().isRegistred(player.getName().toLowerCase())) {
					
					player.sendMessage("§aVocê precisa se registrar! Utilize: /registrar senha confirmar-senha.");
					Main.sendTitle(player, "§6MAZE MC", "§fRegistre-se!");
					
					return;
				}
				if(!Main.getManager().isLogado(player)) {
						
						player.sendMessage("§eVocê deve se logar! Utilize: §f/login <senha>");
						Main.sendTitle(player, "§6MAZE MC", "§fUse: /login <senha>!");
						
						new BukkitRunnable() {
							int segundos = 51;
							
							@Override
							public void run() {
								segundos--;
								if(Main.getManager().isLogado(player)) {
									cancel();
									return;
								}
								Main.sendActionBar(player, "§cVocê tem "+segundos+" §csegundos para completar o login.");
								if(segundos == 0) {
									cancel();
									player.kickPlayer("§4§lATENÇÃO: §FTempo para loguar-se foi esgotado.");
								}
							}
						}.runTaskTimer(Main.getPlugin(Main.class), 20, 20);
						
				}
			}
		}

	}

}
