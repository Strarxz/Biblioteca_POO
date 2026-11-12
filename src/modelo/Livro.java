package modelo;

public class Livro {

    private final int id;
    private final String titulo;
    private final String autor;
    private final String isbn;
    private final int quantidadeTotalExemplares;
    private int quantidadeDeExemplares;

    public Livro(int id, String titulo, String autor, String isbn, int quantidadeDeExemplares) {
        if (id <= 0) {
            throw new IllegalArgumentException("O id do livro deve ser maior que zero.");
        }
        this.id = id;
        this.titulo = validarTexto(titulo, "título");
        this.autor = validarTexto(autor, "autor");
        this.isbn = validarTexto(isbn, "ISBN");
        if (quantidadeDeExemplares < 0) {
            throw new IllegalArgumentException("A quantidade de exemplares não pode ser negativa.");
        }
        this.quantidadeTotalExemplares = quantidadeDeExemplares;
        this.quantidadeDeExemplares = quantidadeDeExemplares;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getQuantidadeTotalExemplares() {
        return quantidadeTotalExemplares;
    }

    public int getQuantidadeDeExemplares() {
        return quantidadeDeExemplares;
    }

    public void emprestarExemplar() {
        if (quantidadeDeExemplares <= 0) {
            throw new EstoqueInsuficienteException("Não há exemplares disponíveis para empréstimo.");
        }
        quantidadeDeExemplares--;
    }

    public void devolverExemplar() {
        if (quantidadeDeExemplares >= quantidadeTotalExemplares) {
            throw new RegraNegocioException(
                    "A devolução excederia a quantidade total de exemplares cadastrada.");
        }
        quantidadeDeExemplares++;
    }

    private static String validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("O " + campo + " do livro é obrigatório.");
        }
        return valor.trim();
    }
}
