package GUI;

import javax.swing.*;
import java.awt.*;

public class LoginScreen extends JPanel {

    public LoginScreen() {
        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = Main.criarCard(400);

        JLabel titulo = Main.criarTitulo("Pulse App");

        JTextField usuario = Main.criarCampo("");
        JPasswordField senha = new JPasswordField();
        Main.estilizarCampo(senha);

        JButton btnEntrar = Main.criarBotaoGradiente("Entrar");
        JButton btnCriar = Main.criarBotaoSecundario("Criar nova conta");

        btnCriar.addActionListener(e -> Main.trocarTela("cadastro"));

        // 🚀 abre chat
        btnEntrar.addActionListener(e -> Main.abrirChat());

        card.add(Box.createVerticalStrut(30));
        card.add(titulo);
        card.add(Box.createVerticalStrut(30));
        card.add(usuario);
        card.add(Box.createVerticalStrut(10));
        card.add(senha);
        card.add(Box.createVerticalStrut(30));
        card.add(btnEntrar);
        card.add(Box.createVerticalStrut(15));
        card.add(btnCriar);

        add(card);
    }
}