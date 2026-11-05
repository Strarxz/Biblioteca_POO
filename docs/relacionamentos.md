# Relacionamentos entre as Classes — Sistema de Biblioteca

## 1. Herança entre Usuario, Aluno e Professor

A classe `Usuario` é uma classe abstrata que representa os usuários do sistema de biblioteca. Ela possui os atributos comuns a todos os usuários: identificador, nome e matrícula.

As classes `Aluno` e `Professor` herdam de `Usuario`, compartilhando seus atributos e especializando os comportamentos relacionados aos empréstimos.

Essa herança permite que cada tipo de usuário tenha regras específicas, como prazo de devolução, limite de empréstimos e cálculo de multas.

## 2. Associação entre Usuario e Emprestimo

A classe `Emprestimo` está associada à classe `Usuario`, pois todo empréstimo precisa estar vinculado a um usuário responsável pela retirada do livro.

Um usuário pode possuir vários registros de empréstimos ao longo do tempo, enquanto cada empréstimo está relacionado a um único usuário.

## 3. Associação entre Livro e Emprestimo

A classe `Emprestimo` também está associada à classe `Livro`, pois é necessário identificar qual livro foi emprestado.

Um livro pode estar relacionado a vários registros de empréstimos ao longo do tempo, enquanto cada registro de empréstimo corresponde a um único livro.

## 4. Considerações gerais

Os relacionamentos estabelecidos permitem organizar as informações do sistema, identificar os usuários responsáveis pelos empréstimos e acompanhar os livros emprestados, suas datas de devolução e possíveis multas.
