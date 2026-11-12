package modelo;

/**
 * Professor: pode ficar 15 dias com o livro, paga R$ 0,50 por dia de atraso
 * e pode ter até 5 livros emprestados ao mesmo tempo.
 */
public class Professor extends Usuario {

    // Valores das regras RN1.1, RN1.2 e RN1.3 (o guia diz que podem mudar)
    private static final int PRAZO_DIAS = 15;
    private static final double TAXA_MULTA_DIARIA = 0.50;
    private static final int LIMITE_EMPRESTIMOS = 5;

    public Professor(int id, String nome, String matricula) {
        super(id, nome, matricula, PRAZO_DIAS);   // envia o prazo para o Usuario
    }

    @Override
    public int getLimiteEmprestimos() {
        return LIMITE_EMPRESTIMOS;
    }

    @Override
    public double getTaxaMultaDiaria() {
        return TAXA_MULTA_DIARIA;
    }
}
