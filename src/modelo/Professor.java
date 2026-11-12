package modelo;

public class Professor extends Usuario {

    private static final int PRAZO_DIAS = 15;
    private static final double TAXA_MULTA_DIARIA = 0.50;
    private static final int LIMITE_EMPRESTIMOS = 5;

    public Professor(int id, String nome, String matricula) {
        super(id, nome, matricula, PRAZO_DIAS);
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
