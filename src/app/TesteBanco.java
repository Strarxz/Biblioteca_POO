package app;

import dao.ConexaoBanco;
import excessoes.ErroBancoException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TesteBanco {

    public static void main(String[] args) {
        try {
            ConexaoBanco.criarTabelas();
            System.out.println("Tabelas criadas. Tabelas no banco:");

            try (Connection c = ConexaoBanco.getConexao();
                 Statement st = c.createStatement();
                 ResultSet rs = st.executeQuery(
                         "SELECT name FROM sqlite_master WHERE type = 'table' AND name NOT LIKE 'sqlite_%'")) {
                while (rs.next()) {
                    System.out.println("- " + rs.getString("name"));
                }
            }
        } catch (ErroBancoException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e);
        }
    }
}