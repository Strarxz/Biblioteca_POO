package modelo;

public class Aluno extends Usuario {

    private static final int PRAZO_DIAS = 7;
    private static final double TAXA_MULTA_DIARIA = 1.00;
    private static final int LIMITE_EMPRESTIMOS = 3;

    public Aluno(int id, String nome, String matricula) {
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
