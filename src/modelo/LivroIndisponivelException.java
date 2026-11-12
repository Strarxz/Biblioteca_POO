package modelo;

public class LivroIndisponivelException extends Exception {
  public LivroIndisponivelException(String mensagem){
    super(mensagem);
  }
}
