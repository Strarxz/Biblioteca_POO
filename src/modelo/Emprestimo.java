
package modelo;

import java.time.LocalDate;

public class Emprestimo {

    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate dataDevolucaoReal;
    private double valorMulta;
    private String status;
    private Usuario usuario;
    private Livro livro;

    public Emprestimo(LocalDate dataEmprestimo, LocalDate dataDevolucaoPrevista, Usuario usuario, Livro livro) {

        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
        this.dataDevolucaoReal = null;
        this.valorMulta = 0.0;
        this.status = "Ativo";
        this.usuario = usuario;
        this.livro = livro;
    }

    public boolean realizarEmprestimo() {
    
        return false;
    }

    public void finalizarDevolucao(LocalDate dataAtual) {
        this.dataDevolucaoReal = dataAtual;
        this.status = "Finalizado";
    }

    public double calcularMulta() {
    
        return valorMulta;
    }
}
