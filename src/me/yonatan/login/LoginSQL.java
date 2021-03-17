package me.yonatan.login;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class LoginSQL extends SQLConnection{

	private String tableName = "login_table";
	
	public void criarTabela(){
		try {
			Statement stmt = getConnection().createStatement();
			stmt.execute("CREATE TABLE IF NOT EXISTS "+getTableName()+" (nickname TEXT, senha TEXT);");
			stmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void register(String player, String senha) {
		if(isRegistred(player)) {
			edit(player, senha);
		} else {
			try {
				PreparedStatement stmt = getConnection().prepareStatement("INSERT INTO "+getTableName()+" (nickname, senha) VALUES (?,?);");
				stmt.setString(1, player);
				stmt.setString(2, senha);
				
				stmt.execute();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public boolean isRegistred(String player) {
		boolean Registred = false;
		try {
			PreparedStatement stmt = getConnection().prepareStatement("SELECT * FROM "+getTableName()+" where nickname = ?;");
			stmt.setString(1, player);
			ResultSet rs = stmt.executeQuery();
			
			if(rs.next()) {
				Registred = true;
			}
			rs.close();
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return Registred;
	}
	
	public void edit(String player, String senha) {
		try {
			PreparedStatement stmt = getConnection().prepareStatement("update "+getTableName()+" set senha = ? where nickname = ?;");
			stmt.setString(1, senha);
			stmt.setString(2, player);
			stmt.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getSenha(String player) {
		
		try {
			PreparedStatement stmt = getConnection().prepareStatement("SELECT * FROM "+getTableName()+" where nickname = ?;");
			stmt.setString(1, player);
			ResultSet rs = stmt.executeQuery();
			String senha = "password";
			
			if(rs.next()) {
				senha = rs.getString("senha");
			}
			rs.close();
			stmt.close();
			return senha;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
