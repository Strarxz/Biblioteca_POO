package app;

import dao.ConexaoBanco;
import dao.EmprestimoDAO;
import dao.LivroDAO;
import dao.UsuarioDAO;
import modelo.Aluno;
import modelo.Emprestimo;
import modelo.Livro;
import modelo.Usuario;

import java.time.LocalDate;
import java.util.List;

public class TesteFinal {

    public static void main(String[] args) {
        try {
            ConexaoBanco.criarTabelas();

            LivroDAO livroDAO = new LivroDAO();
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            EmprestimoDAO emprestimoDAO = new EmprestimoDAO();

            List<Emprestimo> abertos = emprestimoDAO.listarAbertos();

            if (abertos.isEmpty()) {
                System.out.println("=== 1ª EXECUÇÃO: CADASTRO E EMPRÉSTIMO ===");

                Livro livro = livroDAO.salvar(new Livro(0, "Dom Casmurro", "Machado de Assis", "978-85-359-0277-5", 2));
                Usuario aluna = usuarioDAO.salvar(new Aluno(0, "Ana", "2024001"));

                System.out.println("Livro cadastrado: " + livro);
                System.out.println("Usuário cadastrado: " + aluna);

                Emprestimo emp = emprestimoDAO.registrarEmprestimo(aluna, livro);
                System.out.println("Empréstimo registrado: " + emp);

                Livro livroAtualizado = livroDAO.buscarPorId(livro.getId());
                System.out.println("Exemplares disponíveis após empréstimo: " + livroAtualizado.getQuantidadeExemplares());

                System.out.println("\n>>> Feche e execute o programa novamente para testar a persistência e a devolução!");
            } else {
                System.out.println("=== 2ª EXECUÇÃO: PERSISTÊNCIA E DEVOLUÇÃO COM ATRASO ===");

                System.out.println("Empréstimos encontrados no banco após reabrir o programa:");
                for (Emprestimo e : abertos) {
                    System.out.println(" - " + e);
                }

                Emprestimo emp = abertos.get(0);
                LocalDate dataComAtraso = emp.getDataDevolucaoPrevista().plusDays(3);

                double multa = emprestimoDAO.registrarDevolucao(emp, dataComAtraso);
                System.out.println("\nDevolução registrada com 3 dias de atraso (" + dataComAtraso + ").");
                System.out.println("Multa calculada: R$ " + String.format("%.2f", multa));

                Livro livroAtualizado = livroDAO.buscarPorId(emp.getLivro().getId());
                System.out.println("Exemplares disponíveis após devolução: " + livroAtualizado.getQuantidadeExemplares());
                System.out.println("Empréstimos ainda abertos: " + emprestimoDAO.listarAbertos().size());
            }

        } catch (Exception e) {
            System.out.println("Erro durante a execução: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
