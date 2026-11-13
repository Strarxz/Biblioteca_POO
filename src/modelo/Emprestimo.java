package modelo;

import excessoes.OperacaoInvalidaException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Emprestimo {

    public static final String ABERTO = "ABERTO";
    public static final String FINALIZADO = "FINALIZADO";

    private int id;
    private Usuario usuario;
    private Livro livro;
    private LocalDate dataEmprestimo;
    private LocalDate dataPrevista;
    private LocalDate dataDevolucao; 
    private double valorMulta;
    private String status;

    public Emprestimo(int id, Usuario usuario, Livro livro, LocalDate dataEmprestimo) {
        this.id = id;
        this.usuario = usuario;
        this.livro = livro;
        this.dataEmprestimo = dataEmprestimo;
        this.dataPrevista = dataEmprestimo.plusDays(usuario.getPrazoDias());
        this.dataDevolucao = null;
        this.valorMulta = 0.0;
        this.status = ABERTO;
    }

    public void finalizarDevolucao(LocalDate dataAtual) throws OperacaoInvalidaException {
        if (status.equals(FINALIZADO)) {
            throw new OperacaoInvalidaException("Este empréstimo já foi devolvido.");
        }
        this.dataDevolucao = dataAtual;
        this.valorMulta = calcularMulta(dataAtual);
        this.status = FINALIZADO;
    }

    
    private double calcularMulta(LocalDate dataAtual) {
        long diasAtraso = ChronoUnit.DAYS.between(dataPrevista, dataAtual);
        if (diasAtraso <= 0) {
            return 0.0;
        }
        return diasAtraso * usuario.getTaxaMultaDiaria();
    }

    public boolean isAtrasado(LocalDate hoje) {
        return status.equals(ABERTO) && hoje.isAfter(dataPrevista);
    }

    public int getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Livro getLivro() {
        return livro;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public LocalDate getDataPrevista() {
        return dataPrevista;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public double getValorMulta() {
        return valorMulta;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Empréstimo #" + id + " - " + usuario.getNome() + " / " + livro.getTitulo()
                + " (previsto: " + dataPrevista + ", " + status + ")";
    }
}
