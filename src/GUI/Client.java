package GUI;

import org.json.JSONArray;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.io.IOException;
import java.util.Arrays;


public class Client {
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    public String username;
    public static String contResponse;
    public int UID;
    public Boolean sessao = false;

    public Client(Socket socket){
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException e) {
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }

    public Boolean tryLogin(String user, String pass) throws IOException {
        String response;
        bufferedWriter.write( "〗♘†〖" + user + "〖〗†♘" + pass);
        bufferedWriter.newLine();
        bufferedWriter.flush();

        response = bufferedReader.readLine();
        if (response != null && !response.isBlank()){
            if (response.substring(0,8).equals("〗♘†〖true")){
                this.UID = Integer.parseInt(response.substring(8));
                this.username = user;
                this.sessao = true;
                return true;
            }else {
                JOptionPane.showMessageDialog(null, "Usuário ou senha incorretos.");
                this.sessao = false;
                return false;
            }
        }else{
            return false;
        }
    }

    public String Cadastro(String user, String pass1, String pass2) throws IOException {

        if(!user.isBlank() && !pass1.isBlank() && !pass2.isBlank() && user != null && pass1 != null && pass2 != null) {
            if (pass1.equals(pass2)) {
                String response;
                this.bufferedWriter.write("♘〗†〖" + user + "〖〗†♘" + pass1);
                this.bufferedWriter.newLine();
                this.bufferedWriter.flush();

                response = bufferedReader.readLine();
                switch (response){
                    case("♘〗†〖USEREX"):
                        JOptionPane.showMessageDialog(null,"USUÁRIO JÁ EXISTENTE!");
                        break;
                    case("♘〗†〖USERSU"):
                        JOptionPane.showMessageDialog(null,"USUÁRIO CADASTRADO COM SUCESSO");
                        break;
                    case("♘〗†〖USERBLANK"):
                        JOptionPane.showMessageDialog(null,"ERRO DE TRANSMISSÃO: USUÁRIO EM BRANCO OU NULO");
                        break;
                    default:
                        JOptionPane.showMessageDialog(null,"UM ERRO DESCONHECIDO OCORREU!");
                        break;
                }
                AppGUi.trocarTela("login");
                return "passAC";
            }else{
                return "passNE";
            }
        }else{
            return "Blank";
        }
    }

    public void sendMessage(int CID, String messageToSend){

        try {


            bufferedWriter.write("〖††〗♘"+CID + "〖〗†♘" + username + "〖〗†♘" + messageToSend);
            bufferedWriter.newLine();
            bufferedWriter.flush();

        } catch (IOException e) {
            closeEverything(socket,bufferedReader,bufferedWriter);
        }

    }

    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromChat;
                String MsgLimpa[];
                while(socket.isConnected()){
                    try {
                        msgFromChat = bufferedReader.readLine();
                        if(msgFromChat == null){
                            throw new IOException();
                        }
                        //〖†〗༽♘
                        if (msgFromChat != null && msgFromChat.startsWith("〖†〗༽♘")) {
                            MsgLimpa = msgFromChat.substring(5).split("〖〗†♘");
                            ChatUI.adicionarMensagemTexto(MsgLimpa[1] + ": " + MsgLimpa[2], ChatUI.paineisConversas.get(MsgLimpa[3]),ChatUI.horarioAtual(),false,Integer.parseInt(MsgLimpa[0]));
                        }
                        if (msgFromChat.startsWith("〖†⤟〗ㅱ")) {
                            contResponse = msgFromChat;

                        }else if (msgFromChat.startsWith("⤟ㅱㅱ⤟")){

                            ChatUI.conversas.put(msgFromChat.substring(4),new StringBuilder());
                            ChatUI.paineisConversas.put(msgFromChat.substring(4), ChatUI.criarPainelConversa());
                            ChatUI.modelContatos.addElement(msgFromChat.substring(4));
                            ChatUI.todosContatos.add(msgFromChat.substring(4));
                            ChatUI.atualizarMensagens();
                        }

                    } catch (IOException e) {
                        closeEverything(socket,bufferedReader,bufferedWriter);
                        break;
                    }
                }
            }
        }).start();
    }

    public String checkUID(int UID){

        String UsernameRes = "";

        try {

            bufferedWriter.write("〖†♘〗†"+UID);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            UsernameRes = bufferedReader.readLine();
            if (UsernameRes == null) {
                throw new IOException();
            }
            if (UsernameRes.startsWith("〖†♘〗†")) {
                UsernameRes = UsernameRes.substring(5);
                return UsernameRes;
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }

        return "";
    }

    public void closeEverything(Socket socket,BufferedReader bufferedReader,BufferedWriter bufferedWriter){
        try{
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            if(socket != null){
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JSONArray carregarMessages() {
        String chats = "";
        JSONArray jsonArray = null;
        try {
            chats = bufferedReader.readLine();
            if (chats == null) {
                throw new IOException();
            }
            if (chats.startsWith("〱♘⟹")) {
                chats = chats.substring(3);
                jsonArray = new JSONArray(chats);
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }

        return jsonArray;
    }

    public JSONArray carregarChats() {
        String chats = "";
        JSONArray jsonArray = null;
        try {
            chats = bufferedReader.readLine();
            if (chats == null) {
                throw new IOException();
            }
            if (chats.startsWith("⟹♘〱")) {
                chats = chats.substring(3);

                jsonArray = new JSONArray(chats);
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }

        return jsonArray;
    }

    public String[] addContato(String nomeContato) throws IOException, InterruptedException {


        bufferedWriter.write("〖†⤟〗ㅱ"+nomeContato+"ㅱ〗⤟"+username);
        bufferedWriter.newLine();
        bufferedWriter.flush();

        while (contResponse == null || contResponse.isBlank()){
            Thread.sleep(10);
        }

        if(contResponse.startsWith("〖†⤟〗ㅱtrue")){
            String limpo[];
            contResponse = contResponse.substring(9);

            limpo = contResponse.split("ㅱ〗⤟");

            contResponse = "";
            return limpo;
        }else if(contResponse.equals("〖†⤟〗ㅱfalse")){
            contResponse = "";
            JOptionPane.showMessageDialog(null,"Usuário Inexistente!");
        }else{
            contResponse = "";
            System.out.println("UM ERRO DESCONHECIDO OCORREU AO TENTAR ADICIONAR O CONTATO");
            return null;
        }
        return null;
    }

    public static void main(String[] args) throws IOException {

        if (ChatUI.client == null) {
            Socket socket = new Socket("127.0.0.1", 9010);
            ChatUI.client = new Client(socket);
        }

        SwingUtilities.invokeLater(AppGUi::criarTela);
    }

}
