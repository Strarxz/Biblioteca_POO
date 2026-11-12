package excessoes;

public class LivroIndisponivelException extends Exception {

    public LivroIndisponivelException(String mensagem) {
        super(mensagem);
    }
}
