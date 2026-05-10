package GUI;

import javax.swing.*;
import java.awt.*;

public class LoginScreen extends JPanel {

    public LoginScreen() {
        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = AppGUi.criarCard(400);

        JLabel titulo = AppGUi.criarTitulo("Pulse App");

        JTextField usuario = AppGUi.criarCampo("");
        JPasswordField senha = new JPasswordField();
        AppGUi.estilizarCampo(senha);

        JButton btnEntrar = AppGUi.criarBotaoGradiente("Entrar");
        JButton btnCriar = AppGUi.criarBotaoSecundario("Criar nova conta");

        btnCriar.addActionListener(e -> AppGUi.trocarTela("cadastro"));

        // 🚀 abre chat
        btnEntrar.addActionListener(e -> AppGUi.abrirChat());

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