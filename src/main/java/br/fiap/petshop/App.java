package br.fiap.petshop;

import br.fiap.petshop.dao.ClienteJdbcDAO;
import br.fiap.petshop.model.Cliente;

import java.util.List;
import java.util.Scanner;

public class App {

    private static final Scanner sc = new Scanner(System.in);
    private static final ClienteJdbcDAO dao = new ClienteJdbcDAO();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n1 - Cadastrar cliente");
            System.out.println("2 - Listar clientes");
            System.out.println("3 - Buscar por ID");
            System.out.println("4 - Atualizar cliente");
            System.out.println("5 - Remover cliente");
            System.out.println("0 - Sair");
            System.out.print("Opcao: ");
            int opcao = Integer.parseInt(sc.nextLine());

            switch (opcao) {
                case 1 -> cadastrar();
                case 2 -> listar();
                case 3 -> buscar();
                case 4 -> atualizar();
                case 5 -> remover();
                case 0 -> {
                    System.out.println("Saindo");
                    return;
                }
                default -> System.out.println("Opcao invalida!");
            }
        }
    }

    private static void cadastrar() {
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("Telefone: ");
        String tel = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();

        Cliente c = new Cliente(null, nome, tel, email);
        dao.insert(c);
        System.out.println("Cliente cadastrado com ID: " + c.getId());
    }

    private static void listar() {
        List<Cliente> clientes = dao.findAll();
        clientes.forEach(System.out::println);
    }

    private static void buscar() {
        System.out.print("ID: ");
        long id = Long.parseLong(sc.nextLine());
        Cliente c = dao.findById(id);
        System.out.println(c);
    }

    private static void atualizar() {
        System.out.print("ID: ");
        long id = Long.parseLong(sc.nextLine());
        System.out.print("Novo nome: ");
        String nome = sc.nextLine();
        System.out.print("Novo telefone: ");
        String tel = sc.nextLine();
        System.out.print("Novo email: ");
        String email = sc.nextLine();

        Cliente c = new Cliente(id, nome, tel, email);
        dao.update(c);
        System.out.println("Cliente atualizado");
    }

    private static void remover() {
        System.out.print("ID: ");
        long id = Long.parseLong(sc.nextLine());
        dao.deleteById(id);
        System.out.println("Cliente removido");
    }
}