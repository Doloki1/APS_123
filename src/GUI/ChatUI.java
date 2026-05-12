package GUI;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatUI {

    private static JPanel chatArea;

    // contato selecionado
    private static String contatoAtual = "Ana Lima";

    // armazena conversas individuais
    private static final Map<String, StringBuilder> conversas = new HashMap<>();

    // componentes globais
    private static JPanel painelMensagens;
    private static JScrollPane scrollMensagens;
    // painel individual de cada conversa
    private static final Map<String, JPanel> paineisConversas = new HashMap<>();
    private static JLabel tituloContato;
    private static JPanel inputPanel;
    private static JPanel painelVazio;
    private static JPanel header;
    private static JButton botaoAdicionarParticipante;
    private static DefaultListModel<String> modelContatos;
    private static JList<String> listaContatos;
    private static final List<String> todosContatos = new ArrayList<>();
    public static Client client;

    private static boolean atualizandoLista = false;
    private static int contadorContatos = 1;

    // identifica chats em grupo
    private static final Set<String> grupos = new HashSet<>();

    // participantes dos grupos
    private static final Map<String, List<String>> participantesGrupo = new HashMap<>();

    public static void criarTela() throws IOException {

        client.listenForMessage();

        // cria conversas vazias
        conversas.put("Ana Lima", new StringBuilder());
        paineisConversas.put("Ana Lima", criarPainelConversa());
        conversas.put("Bruno Costa", new StringBuilder());
        paineisConversas.put("Bruno Costa", criarPainelConversa());
        conversas.put("Carla Souza", new StringBuilder());
        paineisConversas.put("Carla Souza", criarPainelConversa());


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

        atualizarMensagens();
    }

    private static void abrirJanelaNovoContato(DefaultListModel<String> model) {

        JDialog dialog = new JDialog();

        dialog.setTitle("Novo contato");
        dialog.setSize(400, 360);
        dialog.setModal(true);
        dialog.setLocationRelativeTo(null);
        dialog.setUndecorated(true);

        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBackground(new Color(15, 20, 35));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título
        JLabel titulo = new JLabel("Novo contato");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitulo = new JLabel("Adicione alguém à sua lista.");
        subtitulo.setForeground(new Color(180, 180, 180));
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Campo nome
        JLabel lblNome = new JLabel("Nome");
        lblNome.setForeground(Color.WHITE);
        lblNome.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JTextField campoNome = new JTextField();
        estilizarCampo(campoNome);
        campoNome.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // Campo usuário
        JLabel lblUsuario = new JLabel("Usuário");
        lblUsuario.setForeground(Color.WHITE);
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JTextField campoUsuario = new JTextField();
        estilizarCampo(campoUsuario);
        campoUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // Botão adicionar
        JButton adicionar = new JButton("Adicionar");

        JButton fechar = new JButton("Fechar");

        fechar.setBackground(new Color(255, 80, 80));
        fechar.setForeground(Color.WHITE);
        fechar.setFocusPainted(false);
        fechar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        adicionar.setBackground(new Color(0, 210, 255));
        adicionar.setForeground(Color.BLACK);
        adicionar.setFocusPainted(false);
        adicionar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        adicionar.addActionListener(e -> {

            String nome = campoNome.getText().trim();
            String nomeVisual = nome;
            nome = gerarIdContato(nome);
            String usuario = campoUsuario.getText().trim();

            if (!nome.isEmpty() && !usuario.isEmpty()) {

                // adiciona contato na lista
                model.addElement(nome);
                todosContatos.add(nome);

                // cria conversa vazia para o novo contato
                conversas.put(nome, new StringBuilder());
                paineisConversas.put(nome, criarPainelConversa());

                // seleciona automaticamente o novo contato
                listaContatos.setSelectedValue(nome, true);

                // define como conversa atual
                contatoAtual = nome;

                // atualiza área de mensagens
                atualizarMensagens();

                dialog.dispose();
            }
        });

        fechar.addActionListener(e -> dialog.dispose());

        JPanel botaoPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        botaoPanel.setOpaque(false);
        botaoPanel.add(adicionar);
        botaoPanel.add(fechar);

        painel.add(titulo);
        painel.add(Box.createRigidArea(new Dimension(0, 5)));
        painel.add(subtitulo);
        painel.add(Box.createRigidArea(new Dimension(0, 20)));

        painel.add(lblNome);
        painel.add(Box.createRigidArea(new Dimension(0, 5)));
        painel.add(campoNome);
        painel.add(Box.createRigidArea(new Dimension(0, 15)));

        painel.add(lblUsuario);
        painel.add(Box.createRigidArea(new Dimension(0, 5)));
        painel.add(campoUsuario);
        painel.add(Box.createRigidArea(new Dimension(0, 20)));

        painel.add(botaoPanel);

        dialog.setContentPane(painel);
        dialog.setVisible(true);
    }

    // ---------------- SIDEBAR ----------------

    private static JPanel criarSidebar() {

        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBackground(new Color(20, 25, 45));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        sidebar.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel logo = new JLabel("Pulse");
        logo.setForeground(Color.CYAN);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField busca = new JTextField();
        estilizarCampo(busca);

        JButton btnNovo = new JButton("+ Novo contato");
        btnNovo.setBackground(new Color(0, 200, 180));
        btnNovo.setForeground(Color.BLACK);
        btnNovo.setFocusPainted(false);
        btnNovo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JButton btnGrupo = new JButton("+ Novo grupo");

        btnGrupo.setBackground(new Color(120, 180, 255));
        btnGrupo.setForeground(Color.BLACK);
        btnGrupo.setFocusPainted(false);
        btnGrupo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        // nova janela


        sidebar.add(logo);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        sidebar.add(busca);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(btnNovo);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        sidebar.add(btnGrupo);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));


        // contatos
        modelContatos = new DefaultListModel<>();

        btnNovo.addActionListener(e -> abrirJanelaNovoContato(modelContatos));
        btnGrupo.addActionListener(e -> criarGrupo());

        todosContatos.add("Ana Lima");
        todosContatos.add("Bruno Costa");
        todosContatos.add("Carla Souza");

        for (String contato : todosContatos) {
            modelContatos.addElement(contato);
        }

        listaContatos = new JList<>(modelContatos);

        listaContatos.setCellRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus
            ) {

                JLabel label = (JLabel) super.getListCellRendererComponent(
                        list,
                        value,
                        index,
                        isSelected,
                        cellHasFocus
                );

                String texto = value.toString();

                // remove (#id)
                texto = texto.replaceAll("\\s\\(#\\d+\\)", "");

                label.setText(texto);

                return label;
            }
        });
        // busca dinâmica
        busca.getDocument().addDocumentListener(new DocumentListener() {

            private void filtrar() {

                atualizandoLista = true;

                String texto = busca.getText().trim().toLowerCase();

                modelContatos.clear();

                if (texto.isEmpty()) {

                    for (String contato : todosContatos) {
                        modelContatos.addElement(contato);
                    }

                } else {

                    for (String contato : todosContatos) {

                        if (contato.toLowerCase().contains(texto)) {
                            modelContatos.addElement(contato);
                        }
                    }
                }

                atualizandoLista = false;
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                filtrar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filtrar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filtrar();
            }
        });

        listaContatos.setBackground(new Color(20, 25, 45));
        listaContatos.setForeground(Color.WHITE);
        listaContatos.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        listaContatos.setSelectionBackground(new Color(60, 70, 120));
        listaContatos.setFixedCellHeight(45);

        // seleciona primeiro contato
        listaContatos.setSelectedIndex(0);

        // troca conversa ao clicar
        listaContatos.addListSelectionListener(e -> {

            if (e.getValueIsAdjusting()) return;

            // impede erro durante filtro
            if (atualizandoLista) return;

            String selecionado = listaContatos.getSelectedValue();

            if (selecionado == null) return;

            contatoAtual = selecionado;

            atualizarMensagens();

            // restaura lista completa
            atualizandoLista = true;

            busca.setText("");

            modelContatos.clear();

            for (String contato : todosContatos) {
                modelContatos.addElement(contato);
            }

            listaContatos.setSelectedValue(contatoAtual, true);

            atualizandoLista = false;
        });

        JScrollPane scrollLista = new JScrollPane(listaContatos);
        scrollLista.setBorder(null);

        sidebar.add(scrollLista);

        return sidebar;
    }

    // ---------------- CHAT ----------------

    private static JPanel criarChatArea() {

        JPanel chat = new JPanel(new BorderLayout());
        chat.setBackground(new Color(10, 15, 30));

        chatArea = chat;

        // header
        header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setPreferredSize(new Dimension(0, 60));
        header.setBackground(new Color(20, 25, 45));

        tituloContato = new JLabel(contatoAtual);

        tituloContato.setForeground(Color.WHITE);
        tituloContato.setFont(new Font("Segoe UI", Font.BOLD, 18));

        header.add(tituloContato);

        header.add(Box.createHorizontalStrut(20));

        // botão editar
        JButton editar = new JButton("✏");

        editar.setFocusPainted(false);
        editar.setBackground(new Color(255, 200, 0));

        editar.addActionListener(e -> editarContato());

        header.add(editar);

        // botão excluir
        JButton excluir = new JButton("x");

        // botão adicionar participante
        botaoAdicionarParticipante = new JButton("+");

        botaoAdicionarParticipante.setFocusPainted(false);
        botaoAdicionarParticipante.setBackground(new Color(120, 255, 120));

        botaoAdicionarParticipante.addActionListener(e -> adicionarParticipante());

        header.add(Box.createHorizontalStrut(10));
        header.add(botaoAdicionarParticipante);

        excluir.setFocusPainted(false);
        excluir.setBackground(new Color(255, 80, 80));

        excluir.addActionListener(e -> excluirContato());

        header.add(Box.createHorizontalStrut(10));
        header.add(excluir);

        // mensagens
        painelMensagens = new JPanel();

        painelMensagens.setLayout(new BoxLayout(painelMensagens, BoxLayout.Y_AXIS));

        painelMensagens.setBackground(new Color(10, 15, 30));

        scrollMensagens = new JScrollPane(painelMensagens);

        scrollMensagens.setBorder(null);

        // input
        inputPanel = new JPanel(new BorderLayout());

        inputPanel.setBackground(new Color(20, 25, 45));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTextArea campoMensagem = new JTextArea(2, 20);

        campoMensagem.setLineWrap(true);

        campoMensagem.setWrapStyleWord(true);

        campoMensagem.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        campoMensagem.setBackground(new Color(40, 40, 55));

        campoMensagem.setForeground(Color.WHITE);

        campoMensagem.setCaretColor(Color.WHITE);

        campoMensagem.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(60, 60, 80),
                                1
                        ),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                )
        );


        JButton enviar = new JButton("➤");

        enviar.setBackground(new Color(0, 200, 180));
        enviar.setForeground(Color.BLACK);
        enviar.setFocusPainted(false);
        enviar.setPreferredSize(new Dimension(60, 40));

        JButton arquivo = new JButton("📎");

        arquivo.setBackground(new Color(120, 180, 255));
        arquivo.setForeground(Color.BLACK);
        arquivo.setFocusPainted(false);
        arquivo.setPreferredSize(new Dimension(60, 40));

        arquivo.addActionListener(e -> enviarArquivo());

        // enviar mensagem privada
        enviar.addActionListener(e -> {

            String msg = campoMensagem.getText();

            if (!msg.trim().isEmpty()) {

                // adiciona mensagem apenas nessa conversa
                adicionarMensagemTexto("Você: " + msg);

                campoMensagem.setText("");
            }
        });

        campoMensagem.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {

                // ENTER envia
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER
                        && !e.isShiftDown()) {

                    e.consume();

                    enviar.doClick();
                }
            }
        });

        JPanel botoes = new JPanel(new GridLayout(1, 2, 5, 0));

        botoes.setOpaque(false);

        botoes.add(arquivo);
        botoes.add(enviar);

        JScrollPane scrollCampo = new JScrollPane(campoMensagem);

        scrollCampo.setBorder(null);

        scrollCampo.setPreferredSize(new Dimension(0, 55));

        inputPanel.add(scrollCampo, BorderLayout.CENTER);
        inputPanel.add(botoes, BorderLayout.EAST);

        campoMensagem.getDocument().addDocumentListener(new DocumentListener() {

            private void atualizarAltura() {

                int linhas = campoMensagem.getLineCount();

                linhas = Math.max(2, Math.min(linhas, 6));

                campoMensagem.setRows(linhas);

                inputPanel.revalidate();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                atualizarAltura();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                atualizarAltura();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                atualizarAltura();
            }
        });

        painelVazio = new JPanel(new GridBagLayout());

        painelVazio.setBackground(new Color(10, 15, 30));

        JLabel vazio = new JLabel("Nenhuma conversa selecionada");

        vazio.setForeground(new Color(120, 120, 140));

        vazio.setFont(new Font("Segoe UI", Font.BOLD, 20));

        painelVazio.add(vazio);

        chat.add(header, BorderLayout.NORTH);
        chat.add(painelVazio, BorderLayout.CENTER);
        chat.add(inputPanel, BorderLayout.SOUTH);

        header.setVisible(false);

        inputPanel.setVisible(false);

        return chat;
    }


    // ---------------- ATUALIZA CHAT ----------------
    private static JPanel criarPainelConversa() {

        JPanel painel = new JPanel();

        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));

        painel.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );

        painel.setBackground(new Color(10, 15, 30));

        return painel;
    }

    private static void adicionarMensagemTexto(String texto) {

        JPanel conversaAtual = paineisConversas.get(contatoAtual);
        client.sendMessage(6,texto.substring(6));
        if (conversaAtual == null) return;

        texto = formatarMensagem(texto);

        JPanel linha = new JPanel(new BorderLayout());

        linha.setOpaque(false);

        linha.setBorder(
                BorderFactory.createEmptyBorder(5, 20, 5, 20)
        );

        // bolha
        JPanel bolha = new JPanel(new BorderLayout());

        bolha.setBackground(new Color(35, 45, 70));

        bolha.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(60, 80, 120),
                                1,
                                true
                        ),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)
                )
        );

        JTextArea mensagem = new JTextArea(texto);

        mensagem.setLineWrap(true);

        mensagem.setWrapStyleWord(true);

        mensagem.setEditable(false);

        mensagem.setFocusable(false);

        mensagem.setOpaque(false);

        mensagem.setForeground(Color.WHITE);

        mensagem.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        int larguraMaxima = 450;

        mensagem.setSize(
                new Dimension(larguraMaxima, Short.MAX_VALUE)
        );

        Dimension tamanho = mensagem.getPreferredSize();

        mensagem.setPreferredSize(
                new Dimension(larguraMaxima, tamanho.height)
        );

        JLabel horario = new JLabel(horarioAtual());

        horario.setForeground(new Color(180, 180, 180));

        horario.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));

        rodape.setOpaque(false);

        rodape.add(horario);

        bolha.add(mensagem, BorderLayout.CENTER);

        bolha.add(rodape, BorderLayout.SOUTH);

        JPanel alinhamento = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        alinhamento.setOpaque(false);

        alinhamento.add(bolha);

        linha.add(alinhamento, BorderLayout.CENTER);

        conversaAtual.add(linha);

        conversaAtual.revalidate();

        conversaAtual.repaint();

        rolarParaBaixo();
    }

    private static void adicionarMensagemRecebida(
            String usuario,
            String texto
    ) {

        JPanel conversaAtual = paineisConversas.get(contatoAtual);

        if (conversaAtual == null) return;

        texto = formatarMensagem(texto);

        JPanel linha = new JPanel(new BorderLayout());

        linha.setOpaque(false);

        linha.setBorder(
                BorderFactory.createEmptyBorder(5, 20, 5, 20)
        );

        JPanel bolha = new JPanel(new BorderLayout());

        bolha.setBackground(new Color(55, 55, 80));

        bolha.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(90, 90, 120),
                                1,
                                true
                        ),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)
                )
        );

        JLabel nome = new JLabel(usuario);

        nome.setForeground(new Color(120, 180, 255));

        nome.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JTextArea mensagem = new JTextArea(texto);

        mensagem.setLineWrap(true);

        mensagem.setWrapStyleWord(true);

        mensagem.setEditable(false);

        mensagem.setFocusable(false);

        mensagem.setOpaque(false);

        mensagem.setForeground(Color.WHITE);

        mensagem.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        int larguraMaxima = 450;

        mensagem.setSize(
                new Dimension(larguraMaxima, Short.MAX_VALUE)
        );

        Dimension tamanho = mensagem.getPreferredSize();

        mensagem.setPreferredSize(
                new Dimension(larguraMaxima, tamanho.height)
        );

        JLabel horario = new JLabel(horarioAtual());

        horario.setForeground(new Color(180, 180, 180));

        horario.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        JPanel topo = new JPanel(new BorderLayout());

        topo.setOpaque(false);

        topo.add(nome, BorderLayout.WEST);

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));

        rodape.setOpaque(false);

        rodape.add(horario);

        bolha.add(topo, BorderLayout.NORTH);

        bolha.add(mensagem, BorderLayout.CENTER);

        bolha.add(rodape, BorderLayout.SOUTH);

        JPanel alinhamento = new JPanel(new FlowLayout(FlowLayout.LEFT));

        alinhamento.setOpaque(false);

        alinhamento.add(bolha);

        linha.add(alinhamento, BorderLayout.CENTER);

        conversaAtual.add(linha);

        conversaAtual.revalidate();

        conversaAtual.repaint();

        rolarParaBaixo();
    }

    private static void adicionarMensagemSistema(String texto) {

        JPanel conversaAtual = paineisConversas.get(contatoAtual);

        if (conversaAtual == null) return;

        JLabel sistema = new JLabel(texto);

        sistema.setForeground(new Color(120, 180, 255));

        sistema.setFont(new Font("Segoe UI", Font.ITALIC, 13));

        sistema.setAlignmentX(Component.CENTER_ALIGNMENT);

        sistema.setBorder(
                BorderFactory.createEmptyBorder(10, 5, 10, 5)
        );

        conversaAtual.add(sistema);

        conversaAtual.revalidate();

        conversaAtual.repaint();

        rolarParaBaixo();
    }

    private static void adicionarImagem(File arquivo) {

        JPanel conversaAtual = paineisConversas.get(contatoAtual);

        if (conversaAtual == null) return;

        try {

            ImageIcon icon = new ImageIcon(arquivo.getAbsolutePath());

            Image imagem = icon.getImage().getScaledInstance(
                    250,
                    -1,
                    Image.SCALE_SMOOTH
            );

            JLabel imagemLabel = new JLabel(new ImageIcon(imagem));

            imagemLabel.setBorder(
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            );

            conversaAtual.add(imagemLabel);

            conversaAtual.revalidate();

            conversaAtual.repaint();

            rolarParaBaixo();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    null,
                    "Erro ao carregar imagem."
            );
        }
    }

    private static void rolarParaBaixo() {

        SwingUtilities.invokeLater(() -> {

            JScrollBar vertical = scrollMensagens.getVerticalScrollBar();

            vertical.setValue(vertical.getMaximum());
        });
    }

    private static void atualizarMensagens() {

        // sem conversa
        if (contatoAtual == null) {

            header.setVisible(false);

            inputPanel.setVisible(false);

            chatArea.remove(scrollMensagens);

            chatArea.add(painelVazio, BorderLayout.CENTER);

            chatArea.revalidate();

            chatArea.repaint();

            return;
        }

        // com conversa
        header.setVisible(true);

        inputPanel.setVisible(true);

        tituloContato.setText(contatoAtual);

        botaoAdicionarParticipante.setVisible(
                grupos.contains(contatoAtual)
        );

        JPanel conversaAtual = paineisConversas.get(contatoAtual);

        if (conversaAtual == null) {

            conversaAtual = criarPainelConversa();

            paineisConversas.put(contatoAtual, conversaAtual);
        }

        scrollMensagens.setViewportView(conversaAtual);

        chatArea.remove(painelVazio);

        chatArea.add(scrollMensagens, BorderLayout.CENTER);

        conversaAtual.revalidate();

        conversaAtual.repaint();

        chatArea.revalidate();

        chatArea.repaint();

        rolarParaBaixo();
    }

    private static void criarGrupo() {

        String nomeGrupo = JOptionPane.showInputDialog(
                null,
                "Nome do grupo:"
        );

        if (nomeGrupo == null || nomeGrupo.trim().isEmpty()) return;

        nomeGrupo = "👥 " + nomeGrupo.trim();

        // adiciona grupo
        grupos.add(nomeGrupo);

        // cria conversa
        conversas.put(nomeGrupo, new StringBuilder());

        paineisConversas.put(nomeGrupo, criarPainelConversa());

        // participantes
        participantesGrupo.put(nomeGrupo, new ArrayList<>());

        // adiciona lista
        todosContatos.add(nomeGrupo);

        modelContatos.addElement(nomeGrupo);

        // seleciona
        contatoAtual = nomeGrupo;

        listaContatos.setSelectedValue(nomeGrupo, true);

        atualizarMensagens();
    }

    // ---------------- ESTILO ----------------

    private static void editarContato() {

        if (contatoAtual == null) return;

        // grupo
        if (grupos.contains(contatoAtual)) {

            List<String> participantes = participantesGrupo.get(contatoAtual);

            if (participantes.isEmpty()) {

                JOptionPane.showMessageDialog(
                        null,
                        "Nenhum participante no grupo."
                );

                return;
            }

            String remover = (String) JOptionPane.showInputDialog(
                    null,
                    "Remover participante:",
                    "Editar grupo",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    participantes.toArray(),
                    participantes.get(0)
            );

            if (remover != null) {

                participantes.remove(remover);

                adicionarMensagemSistema(
                        "🔔 " + remover + " foi removido do grupo"
                );

                atualizarMensagens();
            }

            return;
        }

        // contato privado
        String novoNome = JOptionPane.showInputDialog(
                null,
                "Novo nome do contato:",
                contatoAtual
        );

        if (novoNome == null || novoNome.trim().isEmpty()) return;

        novoNome = novoNome.trim();

        StringBuilder conversa = conversas.get(contatoAtual);

        JPanel painelConversa = paineisConversas.get(contatoAtual);

        conversas.remove(contatoAtual);
        paineisConversas.remove(contatoAtual);

        conversas.put(novoNome, conversa);
        paineisConversas.put(novoNome, painelConversa);

        todosContatos.remove(contatoAtual);

        todosContatos.add(novoNome);

        modelContatos.clear();

        for (String contato : todosContatos) {
            modelContatos.addElement(contato);
        }

        contatoAtual = novoNome;

        listaContatos.setSelectedValue(contatoAtual, true);

        atualizarMensagens();
    }

    private static void adicionarParticipante() {

        // garante que é grupo
        if (!grupos.contains(contatoAtual)) return;

        // lista apenas contatos privados
        List<String> contatosDisponiveis = new ArrayList<>();

        for (String contato : todosContatos) {

            // ignora grupos
            if (!grupos.contains(contato)) {

                // evita adicionar duplicado
                if (!participantesGrupo.get(contatoAtual).contains(contato)) {

                    // não adiciona o próprio grupo
                    if (!contato.equals(contatoAtual)) {

                        contatosDisponiveis.add(contato);
                    }
                }
            }
        }

        // sem contatos disponíveis
        if (contatosDisponiveis.isEmpty()) {

            JOptionPane.showMessageDialog(
                    null,
                    "Nenhum contato disponível para adicionar."
            );

            return;
        }

        // seleção do contato
        String selecionado = (String) JOptionPane.showInputDialog(
                null,
                "Adicionar integrante:",
                "Novo participante",
                JOptionPane.PLAIN_MESSAGE,
                null,
                contatosDisponiveis.toArray(),
                contatosDisponiveis.get(0)
        );

        if (selecionado == null) return;

        // adiciona participante
        participantesGrupo.get(contatoAtual).add(selecionado);

        // mensagem automática
        adicionarMensagemSistema(
                "🔔 " + selecionado + " foi adicionado ao grupo"
        );

        atualizarMensagens();
    }

    private static void excluirContato() {

        if (contatoAtual == null) return;

        int resposta = JOptionPane.showConfirmDialog(
                null,
                "Deseja excluir este contato?",
                "Excluir contato",
                JOptionPane.YES_NO_OPTION
        );

        if (resposta != JOptionPane.YES_OPTION) return;

        // remove grupo
        grupos.remove(contatoAtual);

        participantesGrupo.remove(contatoAtual);

        // remove conversa
        conversas.remove(contatoAtual);

        paineisConversas.remove(contatoAtual);

        // remove contato
        todosContatos.remove(contatoAtual);

        // atualiza lista
        modelContatos.clear();

        for (String contato : todosContatos) {
            modelContatos.addElement(contato);
        }

        // seleciona outro contato
        if (!todosContatos.isEmpty()) {

            contatoAtual = todosContatos.get(0);

            listaContatos.setSelectedIndex(0);

        } else {

            contatoAtual = null;

            atualizarMensagens();
        }

        atualizarMensagens();
    }

    private static void enviarArquivo() {

        JFileChooser chooser = new JFileChooser();

        chooser.setDialogTitle("Selecionar imagem");

        // filtro png/jpg/jpeg
        FileNameExtensionFilter filtro = new FileNameExtensionFilter(
                "Imagens PNG e JPG",
                "png",
                "jpg",
                "jpeg"
        );

        chooser.setFileFilter(filtro);

        int resultado = chooser.showOpenDialog(null);

        if (resultado != JFileChooser.APPROVE_OPTION) return;

        File arquivo = chooser.getSelectedFile();

        // pega conversa atual
        StringBuilder conversa = conversas.get(contatoAtual);

        if (conversa == null) return;

        adicionarMensagemTexto("📎 Você enviou:");

        adicionarImagem(arquivo);
    }

    private static String gerarIdContato(String nome) {

        return nome + " (#" + contadorContatos++ + ")";
    }

    private static String horarioAtual() {

        return new SimpleDateFormat("HH:mm").format(new Date());
    }

    private static String formatarMensagem(String texto) {

        // negrito fake
        texto = texto.replaceAll("\\*(.*?)\\*", "【$1】");

        // itálico fake
        texto = texto.replaceAll("_(.*?)_", "/$1/");

        // emojis
        texto = texto.replace(":)", "😊");
        texto = texto.replace(":(", "😢");
        texto = texto.replace(":D", "😄");
        texto = texto.replace("<3", "❤️");
        texto = texto.replace(":fire:", "🔥");
        texto = texto.replace(":ok:", "👌");

        return texto;
    }

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