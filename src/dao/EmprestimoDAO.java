package dao;

import excecoes.ErroBancoException;
import excecoes.LivroIndisponivelException;
import excecoes.OperacaoInvalidaException;
import excecoes.RegistroNaoEncontradoException;
import modelo.Emprestimo;
import modelo.Livro;
import modelo.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
