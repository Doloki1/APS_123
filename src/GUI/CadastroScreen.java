package GUI;


import javax.swing.*;
import java.awt.*;

public class CadastroScreen extends JPanel {

    public CadastroScreen() {
        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = Main.criarCard(500);

        JLabel titulo = Main.criarTitulo("Criar conta");

        JTextField nome = Main.criarCampo("");
        JTextField email = Main.criarCampo("");
        JTextField login = Main.criarCampo("");

        JPasswordField senha = new JPasswordField();
        JPasswordField confirmar = new JPasswordField();

        Main.estilizarCampo(senha);
        Main.estilizarCampo(confirmar);

        JButton btnCadastrar = Main.criarBotaoGradiente("Cadastrar");
        JButton btnVoltar = Main.criarBotaoSecundario("← Voltar para o login");

        btnVoltar.addActionListener(e -> Main.trocarTela("login"));

        card.add(Box.createVerticalStrut(20));
        card.add(titulo);
        card.add(Box.createVerticalStrut(20));
        card.add(nome);
        card.add(Box.createVerticalStrut(10));
        card.add(email);
        card.add(Box.createVerticalStrut(10));
        card.add(login);
        card.add(Box.createVerticalStrut(10));
        card.add(senha);
        card.add(Box.createVerticalStrut(10));
        card.add(confirmar);
        card.add(Box.createVerticalStrut(25));
        card.add(btnCadastrar);
        card.add(Box.createVerticalStrut(10));
        card.add(btnVoltar);

        add(card);
    }
}