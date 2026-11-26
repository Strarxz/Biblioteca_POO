package dao;

import modelo.Emprestimo;
import modelo.Livro;
import modelo.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import excessoes.ErroBancoException;
import excessoes.LivroIndisponivelException;
import excessoes.OperacaoInvalidaException;
import excessoes.RegistroNaoEncontradoException;

public class EmprestimoDAO {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final LivroDAO livroDAO = new LivroDAO();

    public Emprestimo registrarEmprestimo(Usuario usuario, Livro livro)
            throws ErroBancoException, LivroIndisponivelException {

        String sqlBaixarEstoque = "UPDATE livro SET quantidade_exemplares = quantidade_exemplares - 1 "
                + "WHERE id = ? AND quantidade_exemplares > 0";
        String sqlInserir = "INSERT INTO emprestimo "
                + "(usuario_id, livro_id, data_emprestimo, data_prevista, data_devolucao, valor_multa, status) "
                + "VALUES (?, ?, ?, ?, NULL, 0, ?)";

        LocalDate hoje = LocalDate.now();
        Emprestimo novo = new Emprestimo(0, usuario, livro, hoje);

        try (Connection c = ConexaoBanco.getConexao()) {
            c.setAutoCommit(false);

            try {
                try (PreparedStatement psLivro = c.prepareStatement(sqlBaixarEstoque)) {
                    psLivro.setInt(1, livro.getId());
                    if (psLivro.executeUpdate() == 0) {
                        c.rollback();
                        throw new LivroIndisponivelException("Não há exemplares disponíveis de \"" + livro.getTitulo() + "\".");
                    }
                }

                int idGerado;
                try (PreparedStatement psEmp = c.prepareStatement(sqlInserir, Statement.RETURN_GENERATED_KEYS)) {
                    psEmp.setInt(1, usuario.getId());
                    psEmp.setInt(2, livro.getId());
                    psEmp.setString(3, novo.getDataEmprestimo().toString());
                    psEmp.setString(4, novo.getDataDevolucaoPrevista().toString());
                    psEmp.setString(5, Emprestimo.ABERTO);
                    psEmp.executeUpdate();

                    try (ResultSet chaves = psEmp.getGeneratedKeys()) {
                        chaves.next();
                        idGerado = chaves.getInt(1);
                    }
                }

                c.commit();
                return new Emprestimo(idGerado, usuario, livro, hoje);

            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new ErroBancoException("Erro ao registrar o empréstimo.", e);
        }
    }
    
    public double registrarDevolucao(Emprestimo emprestimo, LocalDate dataDevolucao)
            throws ErroBancoException, OperacaoInvalidaException {

        emprestimo.finalizarDevolucao(dataDevolucao);

        String sqlDevolucao = "UPDATE emprestimo SET data_devolucao = ?, valor_multa = ?, status = ? WHERE id = ?";
        String sqlSubirEstoque = "UPDATE livro SET quantidade_exemplares = quantidade_exemplares + 1 WHERE id = ?";

        try (Connection c = ConexaoBanco.getConexao()) {
            c.setAutoCommit(false);

            try {
                try (PreparedStatement psEmp = c.prepareStatement(sqlDevolucao)) {
                    psEmp.setString(1, emprestimo.getDataDevolucaoReal().toString());
                    psEmp.setDouble(2, emprestimo.getValorMulta());
                    psEmp.setString(3, emprestimo.getStatus());
                    psEmp.setInt(4, emprestimo.getId());
                    psEmp.executeUpdate();
                }

                try (PreparedStatement psLivro = c.prepareStatement(sqlSubirEstoque)) {
                    psLivro.setInt(1, emprestimo.getLivro().getId());
                    psLivro.executeUpdate();
                }

                c.commit();
                return emprestimo.getValorMulta();

            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new ErroBancoException("Erro ao registrar a devolução.", e);
        }
    }
    
    public List<Emprestimo> listarAbertos() throws ErroBancoException {
        List<Emprestimo> abertos = new ArrayList<>();
        String sql = "SELECT * FROM emprestimo WHERE status = ? ORDER BY id";

        try (Connection c = ConexaoBanco.getConexao();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, Emprestimo.ABERTO);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Usuario usuario = usuarioDAO.buscarPorId(rs.getInt("usuario_id"));
                    Livro livro = livroDAO.buscarPorId(rs.getInt("livro_id"));

                    LocalDate dataEmp = LocalDate.parse(rs.getString("data_emprestimo"));
                    LocalDate dataPrev = LocalDate.parse(rs.getString("data_prevista"));
                    String dataDevTexto = rs.getString("data_devolucao");
                    LocalDate dataDev = (dataDevTexto != null) ? LocalDate.parse(dataDevTexto) : null;

                    abertos.add(new Emprestimo(
                            rs.getInt("id"),
                            usuario,
                            livro,
                            dataEmp,
                            dataPrev,
                            dataDev,
                            rs.getDouble("valor_multa"),
                            rs.getString("status")));
                }
            }
        } catch (SQLException | RegistroNaoEncontradoException e) {
            throw new ErroBancoException("Erro ao listar empréstimos em aberto.", e);
        }

        return abertos;
    }
}
