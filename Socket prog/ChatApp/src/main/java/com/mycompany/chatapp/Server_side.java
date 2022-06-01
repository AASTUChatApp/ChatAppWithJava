package com.mycompany.chatapp;
import java.io.IOException;
import java.net.*;

public class Server_side {
    private ServerSocket serverSocket;
    public Server_side(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public  void startServer(){
        try{
            while (!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                System.out.println("A new client has conneted");
                ClientHandler clientHandler = new ClientHandler(socket);

                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void closeServerSocket(){
        try{
            if(serverSocket != null){
                serverSocket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public  static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8002);
        Server_side server = new Server_side(serverSocket);
        server.startServer();
    }
}
