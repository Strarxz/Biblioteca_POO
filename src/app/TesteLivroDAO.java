package app;

import dao.ConexaoBanco;
import dao.LivroDAO;
import excecoes.ErroBancoException;
import excecoes.OperacaoInvalidaException;
import excecoes.RegistroNaoEncontradoException;
import modelo.Emprestimo;
import modelo.Livro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class TesteLivroDAO {

    public static void main(String[] args) {
        try {
            ConexaoBanco.criarTabelas();
            LivroDAO dao = new LivroDAO();

            System.out.println("=== CADASTRO (RF1.1) ===");
            Livro domCasmurro = dao.salvar(new Livro(0, "Dom Casmurro", "Machado de Assis", "978-85-359-0277-5", 2));
            Livro cortico = dao.salvar(new Livro(0, "O Cortiço", "Aluísio Azevedo", "978-85-359-0278-2", 1));
            Livro vidasSecas = dao.salvar(new Livro(0, "Vidas Secas", "Graciliano Ramos", "978-85-359-0279-9", 3));
            System.out.println("Salvos: " + domCasmurro + " | " + cortico + " | " + vidasSecas);

            System.out.println("\nLivros no banco:");
            for (Livro livro : dao.listar()) {
                System.out.println(livro);
            }

            System.out.println("\n=== BUSCA (RF1.5) ===");
            System.out.println("buscar(\"casmurro\"): " + dao.buscar("casmurro"));
            System.out.println("buscar(\"secas\", \"graciliano\"): " + dao.buscar("secas", "graciliano"));
            System.out.println("buscar(\"secas\", \"machado\"): " + dao.buscar("secas", "machado").size() + " resultado(s)");
            System.out.println("buscar(\"\", \"azevedo\") (só autor): " + dao.buscar("", "azevedo"));

            System.out.println("\n=== EDIÇÃO ===");
            vidasSecas.setTitulo("Vidas Secas (edição comemorativa)");
            dao.editar(vidasSecas);
            System.out.println("Depois de editar: " + dao.buscarPorId(vidasSecas.getId()));

            try {
                dao.editar(new Livro(9999, "Fantasma", "Ninguém", "", 1));
                System.out.println("PROBLEMA: deveria ter dado erro!");
            } catch (RegistroNaoEncontradoException e) {
                System.out.println("Erro esperado: " + e.getMessage());
            }

            System.out.println("\n=== REMOÇÃO (RN1.5) ===");
            int usuarioId = criarEmprestimoAberto(domCasmurro.getId());

            System.out.println("Teste 1: remover livro com empréstimo em aberto");
            try {
                dao.remover(domCasmurro.getId());
                System.out.println("PROBLEMA: deveria ter dado erro!");
            } catch (OperacaoInvalidaException e) {
                System.out.println("Erro esperado: " + e.getMessage());
            }
            System.out.println("Continua no banco: " + dao.buscarPorId(domCasmurro.getId()));

            System.out.println("\nTeste 2: remover livro que não existe");
            try {
                dao.remover(9999);
                System.out.println("PROBLEMA: deveria ter dado erro!");
            } catch (RegistroNaoEncontradoException e) {
                System.out.println("Erro esperado: " + e.getMessage());
            }

            System.out.println("\nTeste 3: remover livro sem empréstimo");
            dao.remover(cortico.getId());
            System.out.println("Removido. Busca por \"cortiço\": " + dao.buscar("Cortiço").size() + " resultado(s)");

            limpar(usuarioId, domCasmurro.getId(), vidasSecas.getId());
            System.out.println("\nDados de teste apagados.");

        } catch (ErroBancoException | RegistroNaoEncontradoException | OperacaoInvalidaException | SQLException e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }
    }

    // Enquanto o EmprestimoDAO não existe, o empréstimo de teste é gravado direto no banco.
    private static int criarEmprestimoAberto(int livroId) throws ErroBancoException, SQLException {
        try (Connection c = ConexaoBanco.getConexao()) {
            int usuarioId;
            try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO usuario (nome, matricula, tipo) VALUES ('Teste', 'T000', 'ALUNO')",
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    rs.next();
                    usuarioId = rs.getInt(1);
                }
            }
            try (PreparedStatement ps = c.prepareStatement("INSERT INTO emprestimo "
                    + "(usuario_id, livro_id, data_emprestimo, data_prevista, status) VALUES (?, ?, ?, ?, ?)")) {
                ps.setInt(1, usuarioId);
                ps.setInt(2, livroId);
                ps.setString(3, LocalDate.now().toString());
                ps.setString(4, LocalDate.now().plusDays(7).toString());
                ps.setString(5, Emprestimo.ABERTO);
                ps.executeUpdate();
            }
            return usuarioId;
        }
    }

    private static void limpar(int usuarioId, int... livroIds) throws ErroBancoException, SQLException {
        try (Connection c = ConexaoBanco.getConexao()) {
            try (PreparedStatement ps = c.prepareStatement("DELETE FROM emprestimo WHERE usuario_id = ?")) {
                ps.setInt(1, usuarioId);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = c.prepareStatement("DELETE FROM usuario WHERE id = ?")) {
                ps.setInt(1, usuarioId);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = c.prepareStatement("DELETE FROM livro WHERE id = ?")) {
                for (int id : livroIds) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }
            }
        }
    }
}
