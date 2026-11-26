package dao;

import excecoes.ErroBancoException;
import excecoes.LivroIndisponivelException;
import excecoes.OperacaoInvalidaException;
import excecoes.RegistroNaoEncontradoException;
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
