package me.yonatan.login;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ComandoLogin implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) return true;
		
		Player p = (Player) sender;
		String nick = p.getName();
		
		if(Captcha.playersCaptcha.contains(p)) return true;
		
		if(!Main.getManager().isRegistred(nick)) {
			p.sendMessage("§cVocê deve se registrar primeiro.");
			return true;
		}
		
		if(Main.getManager().isLogado(p)) {
			p.sendMessage("§cVocê já está logado.");
			return true;
		}
		// login senha
		
		if(args.length == 0) {
			p.sendMessage("§cUtilize: /login senha.");
			return true;
		}
		
		String digitou = args[0];
		String senha = Main.getManager().getSenhaSQL(nick);
		
		if(!digitou.equals(senha)) {
			p.kickPlayer("§4§lATENÇÃO: §FVocê errou a senha.");
			return true;
		}
		Main.getManager().login(p);
		p.playSound(p.getLocation(), Sound.ANVIL_BREAK, 1.0F, 1.0F);
		p.sendMessage("§eLogado com sucesso, seja bem-vindo!");
		
		return true;
	}

}
