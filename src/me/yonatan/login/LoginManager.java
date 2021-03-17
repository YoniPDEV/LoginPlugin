package me.yonatan.login;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class LoginManager {
	
	private final HashMap<Player, String> registrados = new HashMap<>();
	private final HashMap<Player, Long> logados = new HashMap<>();
	private final LoginSQL sql = new LoginSQL();
	private boolean useSQL = false;
	
	private BukkitConfigs config = new BukkitConfigs("config.yml");
	
	public void reloadFromConfig(){

    }
	
	public boolean isRegistred(String player) {
		return sql.isRegistred(player);
	}
	
	public HashMap<Player, String> getRegistrados() {
		return registrados;
	}
	
	public BukkitConfigs getConfigs(){
		return config;
	}
	
	public LoginSQL getSql() {
        return sql;
    }
	
	public void reloadFromSQL() {
        try {
            ResultSet rs = sql.getConnection().prepareStatement("SELECT * FROM " + sql.getTableName()).executeQuery();
            while (rs.next()) {
            	String nick = rs.getString("nickname");
            	Player p = Bukkit.getPlayer(nick);
                registrados.put(p, rs.getString("senha"));
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
	
	public void register(String player, String senha) {
		if(useSQL) {
			sql.register(player, senha);
		}
	}
	
	public void login(Player player) {
		logados.put(player, System.currentTimeMillis());
	}
	
	public boolean isLogado(Player player) {
		return getLogados().containsKey(player);
	}
	
	public HashMap<Player, Long> getLogados() {
		return logados;
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public String getSenha(String player) {
		return registrados.get(player.toLowerCase());
	}
	
	public String getSenhaSQL(String player) {
		return sql.getSenha(player);
	}
	
	public boolean isUseSQL() {
        return useSQL;
    }
	
	public void setUseSQL(boolean useSQL) {
        this.useSQL = useSQL;
    }
}
