package modelo;

import excecoes.OperacaoInvalidaException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Emprestimo {

    public static final String ABERTO = "ABERTO";
    public static final String DEVOLVIDO = "DEVOLVIDO";

    private int id;
    private Usuario usuario;
    private Livro livro;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate dataDevolucaoReal;
    private double valorMulta;
    private String status;

    public Emprestimo(int id, Usuario usuario, Livro livro, LocalDate dataEmprestimo) {
        this.id = id;
        this.usuario = usuario;
        this.livro = livro;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucaoPrevista = dataEmprestimo.plusDays(usuario.getPrazoDias());
        this.dataDevolucaoReal = null;
        this.valorMulta = 0.0;
        this.status = ABERTO;
    }

    public void finalizarDevolucao(LocalDate dataAtual) throws OperacaoInvalidaException {
        if (status.equals(DEVOLVIDO)) {
            throw new OperacaoInvalidaException("Este empréstimo já foi devolvido.");
        }
        this.dataDevolucaoReal = dataAtual;
        this.valorMulta = calcularMulta();
        this.status = DEVOLVIDO;
    }

    public double calcularMulta() {
        LocalDate referencia = (dataDevolucaoReal != null) ? dataDevolucaoReal : LocalDate.now();
        long diasAtraso = ChronoUnit.DAYS.between(dataDevolucaoPrevista, referencia);
        if (diasAtraso <= 0) {
            return 0.0;
        }
        return diasAtraso * usuario.getTaxaMultaDiaria();
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

    public LocalDate getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }

    public LocalDate getDataDevolucaoReal() {
        return dataDevolucaoReal;
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
                + " (previsto: " + dataDevolucaoPrevista + ", " + status + ")";
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDataDevolucaoReal(LocalDate dataDevolucaoReal) {
        this.dataDevolucaoReal = dataDevolucaoReal;
    }

    public void setValorMulta(double valorMulta) {
        this.valorMulta = valorMulta;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

