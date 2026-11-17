package excecoes;

public class ErroBancoException extends Exception {

    public ErroBancoException(String mensagem) {
        super(mensagem);
    }

    public ErroBancoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}