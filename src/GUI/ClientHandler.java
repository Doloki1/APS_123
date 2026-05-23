package GUI;

import java.io.*;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONObject;
import java.sql.ResultSetMetaData;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private String chatMessagesload;
    private Socket socket;
    private String chatload;
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
            String cabecario;
            String[] chamadaLimpa;


            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            do {
                chamada = bufferedReader.readLine();
                cabecario = chamada.substring(0 ,4);
                chamada = chamada.substring(4);
                chamadaLimpa = chamada.split("〖〗†♘");
                System.out.println(cabecario);
                if(cabecario.equals("〗♘†〖")){
                    System.out.println("Login");
                    int UID = db.login(chamadaLimpa[0], chamadaLimpa[1]);
                    if (UID > 0) {
                        this.sessao = true;
                        bufferedWriter.write("〗♘†〖true" + UID);
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                        System.out.println("Sessão iniciada");
                    } else {
                        bufferedWriter.write("〗♘†〖false");
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                        System.out.println("Sessão Falhada");
                    }
                }else if(cabecario.equals("♘〗†〖")){
                    System.out.println("Cadastro");

                    switch(db.salvaUsuario(chamadaLimpa[0],chamadaLimpa[1])){
                        case "USEREX":
                            bufferedWriter.write("♘〗†〖USEREX");
                            bufferedWriter.newLine();
                            bufferedWriter.flush();
                            break;
                        case "USERSU":
                            bufferedWriter.write("♘〗†〖USERSU");
                            bufferedWriter.newLine();
                            bufferedWriter.flush();
                            break;
                        case "USERBLANK":
                            bufferedWriter.write("♘〗†〖USERBLANK");
                            bufferedWriter.newLine();
                            bufferedWriter.flush();
                            break;
                        default:
                            bufferedWriter.write("♘〗†〖ERROR");
                            bufferedWriter.newLine();
                            bufferedWriter.flush();
                            break;
                    }
                }
            }while(!this.sessao);
            this.clientUsername = chamadaLimpa[0];
            clientHandlers.add(this);

            this.chatload = checkChats(this.clientUsername);
            this.chatMessagesload = checkMsgs(this.clientUsername);

            bufferedWriter.write(this.chatload);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            bufferedWriter.write(this.chatMessagesload);
            bufferedWriter.newLine();
            bufferedWriter.flush();

        } catch (IOException e) {
            closeEverything(socket,bufferedReader,bufferedWriter);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
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
                if (messageFromClient != null && messageFromClient.startsWith("〖††〗♘")) {
                    MsgLimpa = messageFromClient.substring(5).split("〖〗†♘");
                db.salvaMensagem(MsgLimpa[2],MsgLimpa[1],Integer.parseInt(MsgLimpa[0]));

                String nomeChat = db.checkCID(MsgLimpa[0]);
                broadcastMessage("〖†〗༽♘"+messageFromClient.substring(5)+"〖〗†♘"+nomeChat);


                System.out.println(Arrays.toString(MsgLimpa));
                } else if (messageFromClient != null && messageFromClient.startsWith("〖†♘〗†")) {

                    String res = db.checkUID(messageFromClient.substring(5));
                    bufferedWriter.write("〖†♘〗†"+res);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                } else if (messageFromClient != null && messageFromClient.startsWith("〖†⤟〗ㅱ")) {
                    String limpo[] = messageFromClient.substring(5).split("ㅱ〗⤟");
                    int CID;
                    if(db.checkUserExist(limpo[0])){

                        CID = db.criaChat(limpo[0],limpo[1],limpo[0]+" e "+limpo[1]);

                        bufferedWriter.write("〖†⤟〗ㅱtrueㅱ〗⤟"+CID+"ㅱ〗⤟"+limpo[0]+" e "+limpo[1]);
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    }else{
                        bufferedWriter.write("〖†⤟〗ㅱfalse");
                        bufferedWriter.newLine();
                        bufferedWriter.flush();
                    }

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

    public String checkMsgs(String User) throws Exception {
        String sql = "SELECT UID FROM Users WHERE(Username ='"+User+"');";
        JSONArray result = new JSONArray();
        JSONArray tmp;
        int CID = 0;
        int UID = 0;
        ResultSet rs;
        ResultSet rs2;

        rs = db.st.executeQuery(sql);
        while(rs.next()){
            UID = rs.getInt("UID");
        }
        rs.close();
        sql = "Select CID from Chatlink Where UID = "+UID+";";

        rs = db.st.executeQuery(sql);
        while(rs.next()){
            CID = rs.getInt("CID");
            sql = "Select * from Messages Where CID = "+CID+";";
            rs2 = db.st.executeQuery(sql);
            tmp = convert(rs2);
            for (int i = 0; i < tmp.length(); i++) {
                result.put(tmp.get(i));
            }
        }

        //result = convert(rs);

        return "〱♘⟹" + result;
    }

    public String checkChats(String User) throws Exception {

        String sql = "SELECT UID FROM Users WHERE(Username ='"+User+"');";
        JSONArray result = new JSONArray();
        JSONArray tmp;
        int CID = 0;
        int UID = 0;
        ResultSet rs;
        ResultSet rs2;

        rs = db.st.executeQuery(sql);
        while(rs.next()){
            UID = rs.getInt("UID");
        }
        rs.close();
        sql = "Select * from Chatlink Where UID = "+UID+";";

        rs = db.st.executeQuery(sql);

        while(rs.next()){
            CID = rs.getInt("CID");
            sql = "Select * from Chats Where CID = "+CID+";";
            rs2 = db.st.executeQuery(sql);
            tmp = convert(rs2);
            for (int i = 0; i < tmp.length(); i++) {
                result.put(tmp.get(i));
            }
        }

        return "⟹♘〱" + result;
    }

    public JSONArray convert(ResultSet rs) throws Exception {
        JSONArray json = new JSONArray();
        ResultSetMetaData rsmd = rs.getMetaData();
        while(rs.next()) {
            int numColumns = rsmd.getColumnCount();
            JSONObject obj = new JSONObject();
            for (int i = 1; i <= numColumns; i++) {
                String column_name = rsmd.getColumnName(i);
                obj.put(column_name, rs.getObject(column_name));
            }
            json.put(obj);
        }
        return json;
    }

}
