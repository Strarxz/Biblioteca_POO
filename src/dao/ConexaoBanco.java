package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import excessoes.ErroBancoException;

public class ConexaoBanco {

    private static final String URL = "jdbc:sqlite:biblioteca.db";

    public static Connection getConexao() throws ErroBancoException {
        try {
            Connection conexao = DriverManager.getConnection(URL);
            try (Statement st = conexao.createStatement()) {
                st.execute("PRAGMA foreign_keys = ON");
            }
            return conexao;
        } catch (SQLException e) {
            throw new ErroBancoException("Não foi possível conectar ao banco de dados.", e);
        }
    }

    public static void criarTabelas() throws ErroBancoException {
        String usuario = "CREATE TABLE IF NOT EXISTS usuario ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nome TEXT NOT NULL, "
                + "matricula TEXT NOT NULL, "
                + "tipo TEXT NOT NULL CHECK (tipo IN ('ALUNO', 'PROFESSOR')))";

        String livro = "CREATE TABLE IF NOT EXISTS livro ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "titulo TEXT NOT NULL, "
                + "autor TEXT NOT NULL, "
                + "isbn TEXT, "
                + "quantidade_exemplares INTEGER NOT NULL CHECK (quantidade_exemplares >= 0))";

        String emprestimo = "CREATE TABLE IF NOT EXISTS emprestimo ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "usuario_id INTEGER NOT NULL, "
                + "livro_id INTEGER NOT NULL, "
                + "data_emprestimo TEXT NOT NULL, "
                + "data_prevista TEXT NOT NULL, "
                + "data_devolucao TEXT, "
                + "valor_multa REAL NOT NULL DEFAULT 0, "
                + "status TEXT NOT NULL, "
                + "FOREIGN KEY (usuario_id) REFERENCES usuario(id), "
                + "FOREIGN KEY (livro_id) REFERENCES livro(id))";

        try (Connection conexao = getConexao(); Statement st = conexao.createStatement()) {
            st.execute(usuario);
            st.execute(livro);
            st.execute(emprestimo);
        } catch (SQLException e) {
            throw new ErroBancoException("Erro ao criar as tabelas.", e);
        }
    }
}
