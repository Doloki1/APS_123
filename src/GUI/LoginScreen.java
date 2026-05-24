package GUI;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

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
        btnEntrar.addActionListener(e -> {
            String str = new String(senha.getPassword());

            if (!str.isBlank() && str != null && !usuario.getText().isBlank() && usuario.getText() != null){
            try {
                AppGUi.abrirChat(usuario.getText(), str);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            }else{
                JOptionPane.showMessageDialog(null, "O campo senha precisa ser preenchido!");
            }
        });

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