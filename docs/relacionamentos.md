# Relacionamentos entre as Classes — Sistema de Biblioteca

## 1. Herança entre Usuario, Aluno e Professor

A classe `Usuario` é uma classe abstrata que representa os usuários do sistema de biblioteca[cite: 6]. Ela possui os atributos comuns a todos os usuários: identificador, nome e matrícula[cite: 6].

As classes `Aluno` e `Professor` herdam de `Usuario`, compartilhando seus atributos e especializando os comportamentos relacionados aos empréstimos[cite: 6].

Essa herança permite que cada tipo de usuário tenha regras específicas, como prazo de devolução, limite de empréstimos e cálculo de multas[cite: 6, 7].

## 2. Agregação entre Usuario e Emprestimo

A classe `Emprestimo` estabelece uma relação de **agregação** com a classe `Usuario`, pois todo empréstimo precisa estar vinculado a um usuário responsável pela retirada do livro[cite: 6, 8].

Trata-se de uma agregação porque os objetos possuem ciclos de vida independentes: um usuário pode possuir vários registros de empréstimos ao longo do tempo, mas caso o registro de empréstimo seja encerrado ou apagado, o cadastro do aluno ou professor permanece intacto no sistema[cite: 8].

## 3. Agregação entre Livro e Emprestimo

A classe `Emprestimo` também estabelece uma relação de **agregação** com a classe `Livro`, pois é necessário identificar qual exemplar foi retirado do acervo[cite: 6, 8].

Um livro pode estar relacionado a vários registros de empréstimos ao longo da sua vida útil, enquanto cada registro de empréstimo corresponde a um único livro. O livro possui existência autônoma e continua cadastrado e disponível na biblioteca mesmo após a devolução[cite: 8].

## 4. Considerações gerais

Os relacionamentos estabelecidos permitem organizar as informações do sistema, identificar os usuários responsáveis pelos empréstimos e acompanhar os livros emprestados, suas datas de devolução e possíveis multas, garantindo a integridade e a independência dos dados[cite: 6, 7, 8].
