package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class Main {

    static CardLayout layout = new CardLayout();
    static JPanel container = new JPanel(layout);
    static JFrame frame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::criarTela);
    }

    public static void criarTela() {
        frame = new JFrame("Pulse");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel background = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(10, 20, 40),
                        getWidth(), getHeight(), new Color(40, 0, 60)
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        background.setLayout(new GridBagLayout());

        container.setOpaque(false);
        container.add(new LoginScreen(), "login");
        container.add(new CadastroScreen(), "cadastro");

        background.add(container);
        frame.setContentPane(background);
        frame.setVisible(true);
    }

    // 🔁 navegação global
    public static void trocarTela(String nome) {
        layout.show(container, nome);
    }

    // 🚀 abrir chat
    public static void abrirChat() {
        frame.dispose();
        ChatUI.criarTela();
    }

    // 🎨 COMPONENTES REUTILIZÁVEIS

    public static JPanel criarCard(int altura) {
        JPanel card = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(25, 25, 35, 230));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 30, 30));
            }
        };
        card.setPreferredSize(new Dimension(350, altura));
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        return card;
    }

    public static JLabel criarTitulo(String texto) {
        JLabel titulo = new JLabel(texto);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        return titulo;
    }

    public static JTextField criarCampo(String placeholder) {
        JTextField campo = new JTextField();
        estilizarCampo(campo);
        return campo;
    }

    public static void estilizarCampo(JTextField campo) {
        campo.setMaximumSize(new Dimension(280, 40));
        campo.setBackground(new Color(45, 45, 65));
        campo.setForeground(Color.WHITE);
        campo.setCaretColor(Color.WHITE);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 80), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        campo.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public static JButton criarBotaoGradiente(String texto) {
        JButton botao = new JButton(texto) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(0, 210, 255),
                        getWidth(), getHeight(), new Color(150, 60, 255)
                );
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        botao.setForeground(Color.WHITE);
        botao.setContentAreaFilled(false);
        botao.setBorderPainted(false);
        botao.setFocusPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setMaximumSize(new Dimension(280, 45));
        botao.setAlignmentX(Component.CENTER_ALIGNMENT);

        return botao;
    }

    public static JButton criarBotaoSecundario(String texto) {
        JButton btn = new JButton(texto);
        btn.setForeground(new Color(180, 180, 200));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(280, 30));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        return btn;
    }
}
