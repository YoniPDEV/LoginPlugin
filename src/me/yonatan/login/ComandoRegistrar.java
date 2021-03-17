package me.yonatan.login;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ComandoRegistrar implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(!(sender instanceof Player)) return true;
		
		Player p = (Player) sender;
		String nick = p.getName();
		
		if(Main.getManager().isRegistred(nick)) {
			p.sendMessage("§cVocê já está registrado.");
			return true;
		}
		
		if(args.length < 2) {
			p.sendMessage("§cUtilize: \"/registrar senha confirmar-senha\"!");
			return true;
		}
		String senha = args[0];
		String confirmar = args[1];
		
		if(!senha.equals(confirmar)) {
			p.sendMessage("§cAs senhas não são as mesmas!");
			return true;
		}
		
		p.sendMessage("§aConta registrada com sucesso.");
		Main.getManager().register(nick, senha);
		p.sendMessage("§aAgora logue-se: /login senha!");
		
		return true;
	}

}
