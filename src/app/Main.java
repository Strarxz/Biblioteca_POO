package app;

import excessoes.LimiteEmprestimosException;
import excessoes.LivroIndisponivelException;
import excessoes.OperacaoInvalidaException;
import excessoes.RegistroNaoEncontradoException;
import modelo.Aluno;
import modelo.Biblioteca;
import modelo.Emprestimo;
import modelo.Livro;
import modelo.Professor;
import modelo.Usuario;

public class Main {

    public static void main(String[] args) {
        Biblioteca biblioteca = new Biblioteca();

        biblioteca.cadastrarLivro(new Livro(1, "Dom Casmurro", "Machado de Assis", "978-85-359-0277-5", 2));
        biblioteca.cadastrarLivro(new Livro(2, "O Cortiço", "Aluísio Azevedo", "978-85-359-0278-2", 1));
        biblioteca.cadastrarLivro(new Livro(3, "Vidas Secas", "Graciliano Ramos", "978-85-359-0279-9", 3));

        biblioteca.cadastrarUsuario(new Aluno(1, "Ana", "2024001"));
        biblioteca.cadastrarUsuario(new Professor(2, "Carlos", "P001"));

        System.out.println("=== LIVROS ===");
        for (Livro livro : biblioteca.getLivros()) {
            System.out.println(livro);
        }

        System.out.println("\n=== USUÁRIOS ===");
        for (Usuario usuario : biblioteca.getUsuarios()) {
            System.out.println(usuario);
        }

        System.out.println("\n=== PARTE 2: EMPRÉSTIMOS ===");

        Usuario ana = biblioteca.getUsuarios().get(0);
        Usuario carlos = biblioteca.getUsuarios().get(1);
        Livro domCasmurro = biblioteca.getLivros().get(0);
        Livro cortico = biblioteca.getLivros().get(1);
        Livro vidasSecas = biblioteca.getLivros().get(2);

        try {
            Emprestimo emp1 = biblioteca.emprestar(ana, domCasmurro);
            Emprestimo emp2 = biblioteca.emprestar(carlos, cortico);

            System.out.println(emp1);
            System.out.println("Aluna: prazo de " + ana.getPrazoDias()
                    + " dias, devolver até " + emp1.getDataPrevista());

            System.out.println(emp2);
            System.out.println("Professor: prazo de " + carlos.getPrazoDias()
                    + " dias, devolver até " + emp2.getDataPrevista());

            System.out.println("\nExemplares depois dos empréstimos:");
            System.out.println(domCasmurro);
            System.out.println(cortico);
        } catch (LimiteEmprestimosException | LivroIndisponivelException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        System.out.println("\n=== PARTE 3: ERROS ===");

        System.out.println("Teste 1: emprestar livro sem exemplar");
        try {
            biblioteca.emprestar(ana, cortico);
            System.out.println("PROBLEMA: deveria ter dado erro!");
        } catch (LimiteEmprestimosException | LivroIndisponivelException e) {
            System.out.println("Erro esperado: " + e.getMessage());
        }
        System.out.println("Empréstimos registrados: " + biblioteca.getEmprestimos().size());
        System.out.println("Exemplares do Cortiço: " + cortico.getQuantidadeExemplares());

        System.out.println("\nTeste 2: limite de empréstimos do aluno");
        try {
            biblioteca.emprestar(ana, vidasSecas);
            System.out.println("Empréstimo 2 da Ana: ok");
            biblioteca.emprestar(ana, domCasmurro);
            System.out.println("Empréstimo 3 da Ana: ok");
            biblioteca.emprestar(ana, vidasSecas);
            System.out.println("PROBLEMA: o quarto empréstimo deveria ter sido recusado!");
        } catch (LimiteEmprestimosException | LivroIndisponivelException e) {
            System.out.println("Erro esperado: " + e.getMessage());
        }
        System.out.println("Empréstimos registrados: " + biblioteca.getEmprestimos().size());
        System.out.println("Exemplares de Vidas Secas: " + vidasSecas.getQuantidadeExemplares());

        System.out.println("\n=== PARTE 4: DEVOLUÇÃO E MULTA ===");

        Emprestimo empAna1 = biblioteca.getEmprestimos().get(0);
        Emprestimo empCarlos = biblioteca.getEmprestimos().get(1);
        Emprestimo empAna2 = biblioteca.getEmprestimos().get(2);

        System.out.println("Teste 1: aluna devolve com 3 dias de atraso");
        try {
            double multa = biblioteca.devolver(empAna1, empAna1.getDataPrevista().plusDays(3));
            System.out.println("Multa: R$ " + String.format("%.2f", multa));
        } catch (OperacaoInvalidaException e) {
            System.out.println("Erro: " + e.getMessage());
        }
        System.out.println("Exemplares de Dom Casmurro: " + domCasmurro.getQuantidadeExemplares());

        System.out.println("\nTeste 2: professor devolve com 3 dias de atraso");
        try {
            double multa = biblioteca.devolver(empCarlos, empCarlos.getDataPrevista().plusDays(3));
            System.out.println("Multa: R$ " + String.format("%.2f", multa));
        } catch (OperacaoInvalidaException e) {
            System.out.println("Erro: " + e.getMessage());
        }
        System.out.println("Exemplares do Cortiço: " + cortico.getQuantidadeExemplares());

        System.out.println("\nTeste 3: aluna devolve no prazo");
        try {
            double multa = biblioteca.devolver(empAna2, empAna2.getDataPrevista());
            System.out.println("Multa: R$ " + String.format("%.2f", multa));
        } catch (OperacaoInvalidaException e) {
            System.out.println("Erro: " + e.getMessage());
        }
        System.out.println("Exemplares de Vidas Secas: " + vidasSecas.getQuantidadeExemplares());

        System.out.println("\nTeste 4: devolver de novo o mesmo empréstimo");
        try {
            biblioteca.devolver(empAna1, empAna1.getDataPrevista().plusDays(3));
            System.out.println("PROBLEMA: deveria ter dado erro!");
        } catch (OperacaoInvalidaException e) {
            System.out.println("Erro esperado: " + e.getMessage());
        }
        System.out.println("Exemplares de Dom Casmurro: " + domCasmurro.getQuantidadeExemplares());

        System.out.println("\n=== PARTE 5: BUSCA E REMOÇÃO ===");

        System.out.println("Busca por \"casmurro\":");
        for (Livro livro : biblioteca.buscar("casmurro")) {
            System.out.println(livro);
        }

        System.out.println("\nBusca por \"secas\" e autor \"graciliano\":");
        for (Livro livro : biblioteca.buscar("secas", "graciliano")) {
            System.out.println(livro);
        }

        System.out.println("\nBusca por \"secas\" e autor \"machado\":");
        System.out.println("Resultados: " + biblioteca.buscar("secas", "machado").size());

        System.out.println("\nTeste 1: remover livro com empréstimo em aberto");
        try {
            biblioteca.removerLivro(1);
            System.out.println("PROBLEMA: deveria ter dado erro!");
        } catch (RegistroNaoEncontradoException | OperacaoInvalidaException e) {
            System.out.println("Erro esperado: " + e.getMessage());
        }
        System.out.println("Livros cadastrados: " + biblioteca.getLivros().size());

        System.out.println("\nTeste 2: remover livro que não existe");
        try {
            biblioteca.removerLivro(99);
            System.out.println("PROBLEMA: deveria ter dado erro!");
        } catch (RegistroNaoEncontradoException | OperacaoInvalidaException e) {
            System.out.println("Erro esperado: " + e.getMessage());
        }

        System.out.println("\nTeste 3: remover livro sem empréstimo em aberto");
        try {
            biblioteca.removerLivro(3);
            System.out.println("Livro removido.");
        } catch (RegistroNaoEncontradoException | OperacaoInvalidaException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        System.out.println("\nLivros cadastrados agora:");
        for (Livro livro : biblioteca.getLivros()) {
            System.out.println(livro);
        }
    }
}