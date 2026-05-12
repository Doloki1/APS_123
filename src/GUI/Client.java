package GUI;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    public String username;
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
            if (response.equals("〗♘†〖true")){
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

    public void sendMessage(int CID, String messageToSend){

        try {


            bufferedWriter.write(CID + "〖〗†♘" + username + "〖〗†♘" + messageToSend);
            System.out.println(messageToSend);
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
                while(socket.isConnected()){
                    try {
                        msgFromChat = bufferedReader.readLine();
                        if(msgFromChat == null){
                            throw new IOException();
                        }
                        System.out.println(msgFromChat);
                    } catch (IOException e) {
                        closeEverything(socket,bufferedReader,bufferedWriter);
                        break;
                    }
                }
            }
        }).start();
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AppGUi::criarTela);
    }

}
