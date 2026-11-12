package modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.time.temporal.ChronoUnit;

public class Biblioteca {
    
    private List<Livro> livros = new ArrayList<>();
    private List<Usuario> usuarios = new ArrayList<>();
    private List<Emprestimo> emprestimos = new ArrayList<>();

    public void emprestar(Usuario usuario, Livro livro) throws LivroIndisponivelException, LimiteEmprestimosException {
        int totalAtivos = 0;
        for (Emprestimo emp : emprestimos) {
            if (emp.getUsuario() == usuario && emp.getStatus().equals("Ativo")) {
                totalAtivos++;
            }
        }
        
        if (totalAtivos >= usuario.getLimiteEmprestimos()) {
            throw new LimiteEmprestimosException("Erro: Usuário atingiu o limite de empréstimos.");
        }

        if (livro.getQuantidadeExemplares() <= 0) {
            throw new LivroIndisponivelException("Erro: Livro sem exemplares disponíveis.");
        }

        LocalDate hoje = LocalDate.now();
        LocalDate previsao = hoje.plusDays(usuario.getPrazoDias());
        
        Emprestimo novoEmp = new Emprestimo(hoje, previsao, usuario, livro);
        livro.emprestarExemplar();
        emprestimos.add(novoEmp);
    }

    public void devolver(Emprestimo emprestimo, LocalDate dataDevolucao) {
        emprestimo.finalizarDevolucao(dataDevolucao);
        emprestimo.getLivro().devolverExemplar();
        
        int diasAtraso = (int) ChronoUnit.DAYS.between(emprestimo.getDataDevolucaoPrevista(), dataDevolucao);
        float multa = emprestimo.getUsuario().calcularMulta(diasAtraso);
        
        System.out.println("Devolução finalizada. Multa a pagar: R$ " + multa);
    }
}
