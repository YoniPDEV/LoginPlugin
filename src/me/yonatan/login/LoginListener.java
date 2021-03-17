package me.yonatan.login;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;


@SuppressWarnings("deprecation")
public class LoginListener implements Listener{
	
	@EventHandler
	public void aoentrar(PlayerJoinEvent e) {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				Player p = (Player) e.getPlayer();
				Captcha.open(p);
				Captcha.setCaptcha(p);
				new BukkitRunnable() {
					int segundos = 51;
					
					@Override
					public void run() {
						segundos--;
						if(!Captcha.playersCaptcha.contains(p)) {
							cancel();
							return;
						}
						Main.sendActionBar(p, "§cVocê tem "+segundos+" §csegundos para completar o captcha.");
						if(segundos == 0) {
							cancel();
							p.kickPlayer("§4§lATENÇÃO: §FTempo para completar o captcha foi esgotado.");
						}
					}
				}.runTaskTimer(Main.getPlugin(Main.class), 20, 20);
				
			}
		}.runTaskLater(Main.getPlugin(Main.class), 10);
	}
	
	@EventHandler
	public void aomexer(PlayerMoveEvent e) {
		Player p = (Player) e.getPlayer();
		String nick = p.getName();
		if (e.getFrom().getBlock().equals(e.getTo().getBlock()))return;
		if(!Main.getManager().isRegistred(nick)) {
			e.setTo(e.getFrom());
		}
		if(!Main.getManager().isLogado(p)) {
			e.setTo(e.getFrom());
		}
	}
	@EventHandler
	public void comando(PlayerCommandPreprocessEvent e) {
		Player p = (Player) e.getPlayer();
		String nick = p.getName();
		String comando = e.getMessage();
		if(comando.toLowerCase().startsWith("/registrar") || comando.toLowerCase().startsWith("/login")) {
			return;
		}
		if(!Main.getManager().isRegistred(nick)) {
			e.setCancelled(true);
			p.sendMessage("§cVocê não pode utilizar comando, registre-se primeiro.");
			return;
		}
		if(!Main.getManager().isLogado(p)) {
			e.setCancelled(true);
			p.sendMessage("§cVocê não pode utilizar comandos, logue-se primeiro.");
		}
	}
	@EventHandler
	public void chatevento(PlayerChatEvent e) {
		Player p = (Player) e.getPlayer();
		String nick = p.getName();
		if(!Main.getManager().isRegistred(nick)) {
			e.setMessage("");
			p.sendMessage("§cVocê deve");
		}
		if(!Main.getManager().isLogado(p)) {
			e.setCancelled(true);
			p.sendMessage("§cVocê não pode falar enquanto não se registra.");
		}
	}
	
}
