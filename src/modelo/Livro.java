
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
        if (quantidadeExemplares <= 0) {
            throw new LivroIndisponivelException("Livro sem exemplares disponíveis.");
        }
        quantidadeExemplares--;
    }

    public void devolverExemplar() {
        quantidadeExemplares++;
    }
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo){
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getQuantidadeExemplares() {
        return quantidadeExemplares;
    }
}
