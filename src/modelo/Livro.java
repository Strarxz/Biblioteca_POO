
package modelo;

public class Livro {

    private int id;
    private String titulo;
    private String autor;
    private String isbn;
    private int quantidadeExemplares;

    public Livro(int id, String titulo, String autor,  String isbn, int quantidadeExemplares) {

        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.quantidadeExemplares = quantidadeExemplares;
    }

    public void emprestarExemplar() {
        if (quantidadeExemplares > 0) {
            quantidadeExemplares--;
        }
    }

    public void devolverExemplar() {
        quantidadeExemplares++;
    }
}
