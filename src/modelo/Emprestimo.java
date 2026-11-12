package modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Emprestimo {

    private enum Status {
        PENDENTE,
        ATIVO,
        FINALIZADO
    }

    private final LocalDate dataEmprestimo;
    private final LocalDate dataDevolucaoPrevista;
    private final Usuario usuario;
    private final Livro livro;
    private LocalDate dataDevolucaoReal;
    private BigDecimal valorMulta = new BigDecimal("0.00");
    private Status status = Status.PENDENTE;

    public Emprestimo(LocalDate dataEmprestimo, LocalDate dataDevolucaoPrevista,
            Usuario usuario, Livro livro) {
        if (dataEmprestimo == null) {
            throw new IllegalArgumentException("A data do empréstimo é obrigatória.");
        }
        if (usuario == null) {
            throw new IllegalArgumentException("O usuário é obrigatório.");
        }
        if (livro == null) {
            throw new IllegalArgumentException("O livro é obrigatório.");
        }
        if (dataDevolucaoPrevista == null) {
            throw new IllegalArgumentException("A data prevista para devolução é obrigatória.");
        }
        this.dataEmprestimo = dataEmprestimo;
        this.usuario = usuario;
        this.livro = livro;
        if (usuario.getPrazoDias() <= 0) {
            throw new IllegalArgumentException("O prazo de devolução deve ser maior que zero.");
        }
        LocalDate prazoEsperado = dataEmprestimo.plusDays(usuario.getPrazoDias());
        if (!prazoEsperado.equals(dataDevolucaoPrevista)) {
            throw new IllegalArgumentException(
                    "A data prevista deve corresponder ao prazo de " + usuario.getPrazoDias() + " dias do usuário.");
        }
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
    }

    public Emprestimo(LocalDate dataEmprestimo, Usuario usuario, Livro livro) {
        this(dataEmprestimo, calcularDataPrevista(dataEmprestimo, usuario), usuario, livro);
    }

    public boolean realizarEmprestimo() {
        if (status != Status.PENDENTE) {
            throw new RegraNegocioException("Este empréstimo já foi realizado ou finalizado.");
        }
        if (usuario.isBloqueado(dataEmprestimo)) {
            throw new RegraNegocioException("O usuário possui pendências e está bloqueado para novos empréstimos.");
        }
        if (usuario.getQuantidadeEmprestimosAtivos() >= usuario.getLimiteEmprestimos()) {
            throw new RegraNegocioException(
                    "O usuário atingiu o limite de " + usuario.getLimiteEmprestimos() + " empréstimos simultâneos.");
        }

        livro.emprestarExemplar();
        usuario.registrarEmprestimo(this);
        status = Status.ATIVO;
        return true;
    }

    public void finalizarDevolucao(LocalDate dataAtual) {
        if (dataAtual == null) {
            throw new IllegalArgumentException("A data da devolução é obrigatória.");
        }
        if (status != Status.ATIVO) {
            throw new RegraNegocioException("Somente um empréstimo ativo pode ser devolvido.");
        }
        if (dataAtual.isBefore(dataEmprestimo)) {
            throw new IllegalArgumentException("A devolução não pode ocorrer antes da data do empréstimo.");
        }

        long diasAtraso = Math.max(0, ChronoUnit.DAYS.between(dataDevolucaoPrevista, dataAtual));
        valorMulta = usuario.getTaxaMultaDiaria()
                .multiply(BigDecimal.valueOf(diasAtraso))
                .setScale(2);
        livro.devolverExemplar();
        usuario.registrarDevolucao(this, valorMulta);
        dataDevolucaoReal = dataAtual;
        status = Status.FINALIZADO;
    }

    public BigDecimal calcularMulta() {
        return valorMulta;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public Livro getLivro() {
        return livro;
    }

    public String getStatus() {
        return status.name();
    }

    public boolean isAtivo() {
        return status == Status.ATIVO;
    }

    private static LocalDate calcularDataPrevista(LocalDate dataEmprestimo, Usuario usuario) {
        if (dataEmprestimo == null) {
            throw new IllegalArgumentException("A data do empréstimo é obrigatória.");
        }
        if (usuario == null) {
            throw new IllegalArgumentException("O usuário é obrigatório.");
        }
        return dataEmprestimo.plusDays(usuario.getPrazoDias());
    }
}
