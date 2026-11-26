package dao;

import modelo.Aluno;
import modelo.Emprestimo;
import modelo.Professor;
import modelo.Usuario;

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

public class UsuarioDAO {

    private static final String ALUNO = "ALUNO";
    private static final String PROFESSOR = "PROFESSOR";

    public Usuario salvar(Usuario usuario) throws ErroBancoException {
        String sql = "INSERT INTO usuario (nome, matricula, tipo) VALUES (?, ?, ?)";

        try (Connection c = ConexaoBanco.getConexao();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getMatricula());
            ps.setString(3, tipoDe(usuario));
            ps.executeUpdate();

            try (ResultSet chaves = ps.getGeneratedKeys()) {
                chaves.next();
                int id = chaves.getInt(1);
                return criarUsuario(id, usuario.getNome(), usuario.getMatricula(), tipoDe(usuario));
            }
        } catch (SQLException e) {
            throw new ErroBancoException("Erro ao salvar o usuário.", e);
        }
    }

    public List<Usuario> listar() throws ErroBancoException {
        return consultar("SELECT * FROM usuario ORDER BY nome");
    }

    public Usuario buscarPorId(int id) throws ErroBancoException, RegistroNaoEncontradoException {
        List<Usuario> resultado = consultar("SELECT * FROM usuario WHERE id = ?", id);
        if (resultado.isEmpty()) {
            throw new RegistroNaoEncontradoException("Não existe usuário com o id " + id + ".");
        }
        return resultado.get(0);
    }

    public void editar(Usuario usuario) throws ErroBancoException, RegistroNaoEncontradoException {
        String sql = "UPDATE usuario SET nome = ?, matricula = ? WHERE id = ?";

        try (Connection c = ConexaoBanco.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getMatricula());
            ps.setInt(3, usuario.getId());

            if (ps.executeUpdate() == 0) {
                throw new RegistroNaoEncontradoException("Não existe usuário com o id " + usuario.getId() + ".");
            }
        } catch (SQLException e) {
            throw new ErroBancoException("Erro ao editar o usuário.", e);
        }
    }

    public void remover(int id)
            throws ErroBancoException, RegistroNaoEncontradoException, OperacaoInvalidaException {

        Usuario usuario = buscarPorId(id);

        try (Connection c = ConexaoBanco.getConexao()) {
            if (contarEmprestimos(c, id, true) > 0) {
                throw new OperacaoInvalidaException(usuario.getNome()
                        + " tem empréstimo em aberto e não pode ser removido(a).");
            }
            if (contarEmprestimos(c, id, false) > 0) {
                throw new OperacaoInvalidaException(usuario.getNome()
                        + " tem histórico de empréstimos e não pode ser removido(a).");
            }

            try (PreparedStatement ps = c.prepareStatement("DELETE FROM usuario WHERE id = ?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new ErroBancoException("Erro ao remover o usuário.", e);
        }
    }

    // Converte o objeto no texto gravado na coluna "tipo".
    private String tipoDe(Usuario usuario) {
        return (usuario instanceof Professor) ? PROFESSOR : ALUNO;
    }

    // Converte o texto da coluna "tipo" de volta no objeto certo: Aluno ou Professor.
    private Usuario criarUsuario(int id, String nome, String matricula, String tipo) {
        if (tipo.equals(PROFESSOR)) {
            return new Professor(id, nome, matricula);
        }
        return new Aluno(id, nome, matricula);
    }

    private int contarEmprestimos(Connection c, int usuarioId, boolean soAbertos) throws SQLException {
        String sql = "SELECT COUNT(*) FROM emprestimo WHERE usuario_id = ?";
        if (soAbertos) {
            sql += " AND status = ?";
        }
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            if (soAbertos) {
                ps.setString(2, Emprestimo.ABERTO);
            }
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    private List<Usuario> consultar(String sql, Object... parametros) throws ErroBancoException {
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection c = ConexaoBanco.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 0; i < parametros.length; i++) {
                ps.setObject(i + 1, parametros[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    usuarios.add(criarUsuario(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("matricula"),
                            rs.getString("tipo")));
                }
            }
        } catch (SQLException e) {
            throw new ErroBancoException("Erro ao consultar os usuários.", e);
        }
        return usuarios;
    }
}