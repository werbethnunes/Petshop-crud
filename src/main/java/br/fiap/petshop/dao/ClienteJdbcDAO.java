package br.fiap.petshop.dao;

import br.fiap.petshop.exception.DataAccessException;
import br.fiap.petshop.exception.NotFoundException;
import br.fiap.petshop.exception.ValidationException;
import br.fiap.petshop.infra.ConnectionFactory;
import br.fiap.petshop.model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteJdbcDAO implements CrudDAO<Cliente> {

    private void validar(Cliente c) {
        if (c.getNome() == null || c.getNome().isBlank())
            throw new ValidationException("O Nome nao pode ser vazio");
        if (c.getEmail() == null || !c.getEmail().contains("@"))
            throw new ValidationException("Email invalido");
    }

    @Override
    public void insert(Cliente c) {
        validar(c);
        String sql = "INSERT INTO clientes (nome, telefone, email) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getNome());
            ps.setString(2, c.getTelefone());
            ps.setString(3, c.getEmail());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) c.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Erro ao inserir cliente: " + e.getMessage(), e);
        }
    }

    @Override
    public Cliente findById(Long id) {
        String sql = "SELECT * FROM clientes WHERE id = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(
                            rs.getLong("id"),
                            rs.getString("nome"),
                            rs.getString("telefone"),
                            rs.getString("email")
                    );
                }
            }
            throw new NotFoundException("Cliente nao encontrado");
        } catch (SQLException e) {
            throw new DataAccessException("Erro ao buscar cliente: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Cliente> findAll() {
        String sql = "SELECT * FROM clientes ORDER BY id";
        List<Cliente> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Cliente(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("telefone"),
                        rs.getString("email")
                ));
            }
            return list;
        } catch (SQLException e) {
            throw new DataAccessException("Erro ao listar clientes: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Cliente c) {
        validar(c);
        String sql = "UPDATE clientes SET nome=?, telefone=?, email=? WHERE id=?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getNome());
            ps.setString(2, c.getTelefone());
            ps.setString(3, c.getEmail());
            ps.setLong(4, c.getId());

            int linhas = ps.executeUpdate();
            if (linhas == 0) throw new NotFoundException("Cliente nao encontrado");
        } catch (SQLException e) {
            throw new DataAccessException("Erro ao atualizar cliente: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM clientes WHERE id=?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            int linhas = ps.executeUpdate();
            if (linhas == 0) throw new NotFoundException("Cliente nao encontrado");
        } catch (SQLException e) {
            throw new DataAccessException("Erro ao remover cliente: " + e.getMessage(), e);
        }
    }
}