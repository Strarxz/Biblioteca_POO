package app;

import dao.ConexaoBanco;
import excecoes.ErroBancoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class TesteInserir {

    public static void main(String[] args) {
        try {
            ConexaoBanco.criarTabelas();

            try (Connection c = ConexaoBanco.getConexao()) {

                try (Statement st = c.createStatement()) {
                    st.execute("DELETE FROM emprestimo");
                    st.execute("DELETE FROM usuario");
                    st.execute("DELETE FROM livro");
                }

                String sqlLivro = "INSERT INTO livro (titulo, autor, isbn, quantidade_exemplares) VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = c.prepareStatement(sqlLivro)) {
                    ps.setString(1, "Jogos Vorazes");
                    ps.setString(2, "Suzanne Collins");
                    ps.setString(3, "978-8579800245");
                    ps.setInt(4, 10);
                    ps.executeUpdate();

                    ps.setString(1, "O Príncipe Cruel");
                    ps.setString(2, "Holly Black");
                    ps.setString(3, "978-8501115553");
                    ps.setInt(4, 15);
                    ps.executeUpdate();
                }

                String sqlUsuario = "INSERT INTO usuario (nome, matricula, tipo) VALUES (?, ?, ?)";
                try (PreparedStatement ps = c.prepareStatement(sqlUsuario)) {
                    ps.setString(1, "Malu");
                    ps.setString(2, "2024001");
                    ps.setString(3, "ALUNO");
                    ps.executeUpdate();

                    ps.setString(1, "Michael");
                    ps.setString(2, "P001");
                    ps.setString(3, "PROFESSOR");
                    ps.executeUpdate();
                }

                System.out.println("=== LIVROS NO BANCO ===");
                try (Statement st = c.createStatement();
                     ResultSet rs = st.executeQuery("SELECT * FROM livro")) {
                    while (rs.next()) {
                        System.out.println("[" + rs.getInt("id") + "] " + rs.getString("titulo")
                                + " - " + rs.getString("autor")
                                + " (ISBN " + rs.getString("isbn") + ", "
                                + rs.getInt("quantidade_exemplares") + " exemplares)");
                    }
                }

                System.out.println("\n=== USUÁRIOS NO BANCO ===");
                try (Statement st = c.createStatement();
                     ResultSet rs = st.executeQuery("SELECT * FROM usuario")) {
                    while (rs.next()) {
                        System.out.println("[" + rs.getInt("id") + "] " + rs.getString("nome")
                                + " (matrícula " + rs.getString("matricula")
                                + ", " + rs.getString("tipo") + ")");
                    }
                }
            }
        } catch (ErroBancoException e) {
            System.out.println("Erro: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e);
        }
    }
}