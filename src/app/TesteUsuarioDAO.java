package app;

import dao.ConexaoBanco;
import dao.UsuarioDAO;
import excecoes.ErroBancoException;
import excecoes.OperacaoInvalidaException;
import excecoes.RegistroNaoEncontradoException;
import modelo.Aluno;
import modelo.Professor;
import modelo.Usuario;

public class TesteUsuarioDAO {

    public static void main(String[] args) {
        try {
            ConexaoBanco.criarTabelas();
            UsuarioDAO dao = new UsuarioDAO();

            System.out.println("=== CADASTRO (RF1.2) ===");
            Usuario ana = dao.salvar(new Aluno(0, "Ana", "2024001"));
            Usuario carlos = dao.salvar(new Professor(0, "Carlos", "P001"));
            System.out.println("Salvos: " + ana + " | " + carlos);

            System.out.println("\n=== LISTAGEM: VOLTA COMO ALUNO OU PROFESSOR ===");
            for (Usuario u : dao.listar()) {
                System.out.println(u + " -> " + u.getClass().getSimpleName()
                        + ": prazo " + u.getPrazoDias() + " dias, limite " + u.getLimiteEmprestimos()
                        + " livros, multa R$ " + String.format("%.2f", u.getTaxaMultaDiaria()) + "/dia");
            }

            System.out.println("\n=== EDIÇÃO ===");
            ana.setNome("Ana Souza");
            dao.editar(ana);
            System.out.println("Depois de editar: " + dao.buscarPorId(ana.getId()));

            try {
                dao.editar(new Aluno(9999, "Fantasma", "X"));
                System.out.println("PROBLEMA: deveria ter dado erro!");
            } catch (RegistroNaoEncontradoException e) {
                System.out.println("Erro esperado: " + e.getMessage());
            }

            System.out.println("\n=== REMOÇÃO ===");
            try {
                dao.remover(9999);
                System.out.println("PROBLEMA: deveria ter dado erro!");
            } catch (RegistroNaoEncontradoException e) {
                System.out.println("Erro esperado: " + e.getMessage());
            }

            dao.remover(ana.getId());
            dao.remover(carlos.getId());
            System.out.println("Ana e Carlos removidos. Ainda existe a Ana? ");
            try {
                dao.buscarPorId(ana.getId());
                System.out.println("PROBLEMA: deveria ter sido removida!");
            } catch (RegistroNaoEncontradoException e) {
                System.out.println("Não: " + e.getMessage());
            }

        } catch (ErroBancoException | RegistroNaoEncontradoException | OperacaoInvalidaException e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }
    }
}