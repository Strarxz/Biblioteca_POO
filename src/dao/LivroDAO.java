package dao;

import modelo.Emprestimo;
import modelo.Livro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import excessoes.ErroBancoException;
import excessoes.OperacaoInvalidaException;
import excessoes.RegistroNaoEncontradoException;

public class LivroDAO {

    public Livro salvar(Livro livro) throws ErroBancoException {
        String sql = "INSERT INTO livro (titulo, autor, isbn, quantidade_exemplares) VALUES (?, ?, ?, ?)";

        try (Connection c = ConexaoBanco.getConexao();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, livro.getTitulo());
            ps.setString(2, livro.getAutor());
            ps.setString(3, livro.getIsbn());
            ps.setInt(4, livro.getQuantidadeExemplares());
            ps.executeUpdate();

            try (ResultSet chaves = ps.getGeneratedKeys()) {
                chaves.next();
                int id = chaves.getInt(1);
                return new Livro(id, livro.getTitulo(), livro.getAutor(), livro.getIsbn(),
                        livro.getQuantidadeExemplares());
            }
        } catch (SQLException e) {
            throw new ErroBancoException("Erro ao salvar o livro.", e);
        }
    }

    public List<Livro> listar() throws ErroBancoException {
        return consultar("SELECT * FROM livro ORDER BY titulo");
    }

    public Livro buscarPorId(int id) throws ErroBancoException, RegistroNaoEncontradoException {
        List<Livro> resultado = consultar("SELECT * FROM livro WHERE id = ?", id);
        if (resultado.isEmpty()) {
            throw new RegistroNaoEncontradoException("Não existe livro com o id " + id + ".");
        }
        return resultado.get(0);
    }

    public List<Livro> buscar(String titulo) throws ErroBancoException {
        return consultar("SELECT * FROM livro WHERE titulo LIKE ? ORDER BY titulo",
                "%" + titulo + "%");
    }

    public List<Livro> buscar(String titulo, String autor) throws ErroBancoException {
        return consultar("SELECT * FROM livro WHERE titulo LIKE ? AND autor LIKE ? ORDER BY titulo",
                "%" + titulo + "%", "%" + autor + "%");
    }

    public void editar(Livro livro) throws ErroBancoException, RegistroNaoEncontradoException {
        String sql = "UPDATE livro SET titulo = ?, autor = ?, isbn = ?, quantidade_exemplares = ? WHERE id = ?";

        try (Connection c = ConexaoBanco.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, livro.getTitulo());
            ps.setString(2, livro.getAutor());
            ps.setString(3, livro.getIsbn());
            ps.setInt(4, livro.getQuantidadeExemplares());
            ps.setInt(5, livro.getId());

            if (ps.executeUpdate() == 0) {
                throw new RegistroNaoEncontradoException("Não existe livro com o id " + livro.getId() + ".");
            }
        } catch (SQLException e) {
            throw new ErroBancoException("Erro ao editar o livro.", e);
        }
    }

    public void remover(int id)
            throws ErroBancoException, RegistroNaoEncontradoException, OperacaoInvalidaException {

        Livro livro = buscarPorId(id);

        try (Connection c = ConexaoBanco.getConexao()) {
            if (contarEmprestimos(c, id, true) > 0) {
                throw new OperacaoInvalidaException("O livro \"" + livro.getTitulo()
                        + "\" está emprestado e não pode ser removido.");
            }
            if (contarEmprestimos(c, id, false) > 0) {
                throw new OperacaoInvalidaException("O livro \"" + livro.getTitulo()
                        + "\" tem histórico de empréstimos e não pode ser removido.");
            }

            try (PreparedStatement ps = c.prepareStatement("DELETE FROM livro WHERE id = ?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new ErroBancoException("Erro ao remover o livro.", e);
        }
    }

    private int contarEmprestimos(Connection c, int livroId, boolean soAbertos) throws SQLException {
        String sql = "SELECT COUNT(*) FROM emprestimo WHERE livro_id = ?";
        if (soAbertos) {
            sql += " AND status = ?";
        }
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, livroId);
            if (soAbertos) {
                ps.setString(2, Emprestimo.ABERTO);
            }
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    private List<Livro> consultar(String sql, Object... parametros) throws ErroBancoException {
        List<Livro> livros = new ArrayList<>();

        try (Connection c = ConexaoBanco.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 0; i < parametros.length; i++) {
                ps.setObject(i + 1, parametros[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    livros.add(new Livro(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getString("autor"),
                            rs.getString("isbn"),
                            rs.getInt("quantidade_exemplares")));
                }
            }
        } catch (SQLException e) {
            throw new ErroBancoException("Erro ao consultar os livros.", e);
        }
        return livros;
    }
}
