package GUI;


import javax.swing.*;
import java.awt.*;

public class ChatUI {

    public static void criarTela() {

        JFrame frame = new JFrame("Pulse Chat");
        frame.setSize(1200, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(15, 20, 40));

        root.add(criarSidebar(), BorderLayout.WEST);
        root.add(criarChatArea(), BorderLayout.CENTER);

        frame.setContentPane(root);
        frame.setVisible(true);
    }

    // ---------------- SIDEBAR ----------------

    private static JPanel criarSidebar() {

        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBackground(new Color(20, 25, 45));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        sidebar.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Logo
        JLabel logo = new JLabel("Pulse");
        logo.setForeground(Color.CYAN);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Busca
        JTextField busca = new JTextField("Buscar...");
        estilizarCampo(busca);

        // Botão novo contato
        JButton btnNovo = new JButton("+ Novo contato");
        btnNovo.setBackground(new Color(0, 200, 180));
        btnNovo.setForeground(Color.BLACK);
        btnNovo.setFocusPainted(false);
        btnNovo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // Espaçamentos
        sidebar.add(logo);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        sidebar.add(busca);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(btnNovo);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        // Lista contatos
        DefaultListModel<String> model = new DefaultListModel<>();
        model.addElement("Ana Lima");
        model.addElement("Bruno Costa");
        model.addElement("Carla Souza");

        JList<String> lista = new JList<>(model);

        lista.setBackground(new Color(20, 25, 45));
        lista.setForeground(Color.WHITE);
        lista.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lista.setSelectionBackground(new Color(60, 70, 120));
        lista.setFixedCellHeight(45);

        JScrollPane scrollLista = new JScrollPane(lista);
        scrollLista.setBorder(null);

        sidebar.add(scrollLista);

        return sidebar;
    }

    // ---------------- CHAT ----------------

    private static JPanel criarChatArea() {

        JPanel chat = new JPanel(new BorderLayout());
        chat.setBackground(new Color(10, 15, 30));

        // Header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setPreferredSize(new Dimension(0, 60));
        header.setBackground(new Color(20, 25, 45));

        JLabel titulo = new JLabel("Time Pulse (3 membros)");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));

        header.add(titulo);

        // Área mensagens
        JTextArea mensagens = new JTextArea();

        mensagens.setEditable(false);
        mensagens.setBackground(new Color(10, 15, 30));
        mensagens.setForeground(Color.LIGHT_GRAY);
        mensagens.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        mensagens.setText(
                "\n\n\n" +
                        "                    Nenhuma mensagem ainda. Diga olá! 👋"
        );

        JScrollPane scrollMensagens = new JScrollPane(mensagens);

        scrollMensagens.setBorder(null);

        // Input inferior
        JPanel inputPanel = new JPanel(new BorderLayout());

        inputPanel.setBackground(new Color(20, 25, 45));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextField campoMensagem = new JTextField();
        estilizarCampo(campoMensagem);

        JButton enviar = new JButton("➤");

        enviar.setBackground(new Color(0, 200, 180));
        enviar.setForeground(Color.BLACK);
        enviar.setFocusPainted(false);

        enviar.setPreferredSize(new Dimension(60, 40));

        // Enviar mensagem
        enviar.addActionListener(e -> {

            String msg = campoMensagem.getText();

            if (!msg.trim().isEmpty()) {

                mensagens.append("\n\nVocê: " + msg);

                campoMensagem.setText("");
            }
        });

        // Enter envia mensagem
        campoMensagem.addActionListener(e -> enviar.doClick());

        inputPanel.add(campoMensagem, BorderLayout.CENTER);
        inputPanel.add(Box.createRigidArea(new Dimension(10, 0)), BorderLayout.WEST);
        inputPanel.add(enviar, BorderLayout.EAST);

        // Adiciona tudo
        chat.add(header, BorderLayout.NORTH);
        chat.add(scrollMensagens, BorderLayout.CENTER);
        chat.add(inputPanel, BorderLayout.SOUTH);

        return chat;
    }

    // ---------------- ESTILO ----------------

    private static void estilizarCampo(JTextField campo) {

        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        campo.setBackground(new Color(40, 40, 55));

        campo.setForeground(Color.WHITE);

        campo.setCaretColor(Color.WHITE);

        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 80), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
    }
}