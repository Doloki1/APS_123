package GUI;


import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class CadastroScreen extends JPanel {

    JTextField nome;
    JTextField email;
    JTextField login;

    JPasswordField senha;
    JPasswordField confirmar;

    public CadastroScreen() {
        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel card = AppGUi.criarCard(300);

        JLabel titulo = AppGUi.criarTitulo("Criar conta");

        nome = AppGUi.criarCampo("");
        email = AppGUi.criarCampo("");
        login = AppGUi.criarCampo("");

        senha = new JPasswordField();
        confirmar = new JPasswordField();

        AppGUi.estilizarCampo(senha);
        AppGUi.estilizarCampo(confirmar);

        JButton btnCadastrar = AppGUi.criarBotaoGradiente("Cadastrar");
        JButton btnVoltar = AppGUi.criarBotaoSecundario("← Voltar para o login");

        btnVoltar.addActionListener(e -> AppGUi.trocarTela("login"));

        btnCadastrar.addActionListener(e -> {
            try {
                String response;
                response = ChatUI.client.Cadastro(login.getText(), new String(senha.getPassword()),new String(confirmar.getPassword()));

                switch(response){
                    case("Blank"):
                        JOptionPane.showMessageDialog(null,"Um ou mais campos estão vazios!");
                        break;
                    case("passNE"):
                        JOptionPane.showMessageDialog(null,"Senhas não correspondem!");
                        break;
                    case("passAC"):
                        login.setText("");
                        senha.setText("");
                        confirmar.setText("");
                        break;
                    default:
                        JOptionPane.showMessageDialog(null,"Um erro desconhecido ocorreu.");
                        break;
                }

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

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