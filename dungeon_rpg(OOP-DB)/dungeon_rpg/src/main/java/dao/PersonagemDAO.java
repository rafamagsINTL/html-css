package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.Personagem;
import util.DBconnection;

public class PersonagemDAO {
    public void salvar(Personagem p) {
        String sql = "INSERT INTO personagem (nome, nivel, xp, hp, ataque, defesa) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getNome());
            stmt.setInt(2, p.getNivel());
            stmt.setInt(3, p.getXp());
            stmt.setInt(4, p.getHp());
            stmt.setInt(5, p.getAtaque());
            stmt.setInt(6, p.getDefesa());
            stmt.executeUpdate();
            System.out.println("✅ Personagem salvo no banco de dados!");
        } catch (SQLException e) {
            System.out.println("❌ Erro ao salvar personagem: " + e.getMessage());
        }
    }

    public boolean testarConexao() {
        try (Connection conn = DBconnection.getConnection()) {
            System.out.println("✅ Conexão com banco de dados estabelecida!");
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Erro de conexão com banco: " + e.getMessage());
            return false;
        }
    }
}
