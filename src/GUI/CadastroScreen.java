package GUI;


import javax.swing.*;
import java.awt.*;

public class CadastroScreen extends JPanel {

    public CadastroScreen() {
        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = AppGUi.criarCard(300);

        JLabel titulo = AppGUi.criarTitulo("Criar conta");

        JTextField nome = AppGUi.criarCampo("");
        JTextField email = AppGUi.criarCampo("");
        JTextField login = AppGUi.criarCampo("");

        JPasswordField senha = new JPasswordField();
        JPasswordField confirmar = new JPasswordField();

        AppGUi.estilizarCampo(senha);
        AppGUi.estilizarCampo(confirmar);

        JButton btnCadastrar = AppGUi.criarBotaoGradiente("Cadastrar");
        JButton btnVoltar = AppGUi.criarBotaoSecundario("← Voltar para o login");

        btnVoltar.addActionListener(e -> AppGUi.trocarTela("login"));

        card.add(Box.createVerticalStrut(20));
        card.add(titulo);
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