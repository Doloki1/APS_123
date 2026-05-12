package GUI;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private String clientUsername;
    private Database db;
    public Boolean sessao = false;

    public ClientHandler(Socket socket){
        try{
            this.socket = socket;
            this.db = new Database();

            String chamada;
            String[] chamadaLimpa;


            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            do {
                chamada = bufferedReader.readLine();
                chamada = chamada.substring(4);
                chamadaLimpa = chamada.split("〖〗†♘");
                if (db.login(chamadaLimpa[0],chamadaLimpa[1])){
                    this.sessao = true;
                    bufferedWriter.write( "〗♘†〖true");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    System.out.println("Sessão iniciada");
                }else{
                    bufferedWriter.write( "〗♘†〖false");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    System.out.println("Sessão Falhada");
                }
            }while(!this.sessao);

            this.clientUsername = chamadaLimpa[0];
            clientHandlers.add(this);
            broadcastMessage("SERVER: " + clientUsername + " entrou no chat.");
        } catch (IOException e) {
            closeEverything(socket,bufferedReader,bufferedWriter);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        String messageFromClient;
        String[] MsgLimpa;
        while(socket.isConnected()){
            try{
                messageFromClient = bufferedReader.readLine();
                if (messageFromClient != null) {
                    MsgLimpa = messageFromClient.split("〖〗†♘");
                db.salvaMensagem(MsgLimpa[2],MsgLimpa[1],Integer.parseInt(MsgLimpa[0]));
                //broadcastMessage(messageFromClient);
                System.out.println(Arrays.toString(MsgLimpa));
                }
            } catch (IOException e) {
                closeEverything(socket,bufferedReader,bufferedWriter);
                break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void broadcastMessage(String messageToSend){
        for (ClientHandler clientHandler : clientHandlers){
            try {
                if (!clientHandler.clientUsername.equals(clientUsername)) {
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }

            } catch (IOException e){
                closeEverything(socket,bufferedReader,bufferedWriter);
            }
        }
    }

    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadcastMessage("SERVER: " + clientUsername + " saiu do chat.");
    }

    public void closeEverything(Socket socket,BufferedReader bufferedReader ,BufferedWriter bufferedWriter){

        removeClientHandler();
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

}
