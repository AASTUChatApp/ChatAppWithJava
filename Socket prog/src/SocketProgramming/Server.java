/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SocketProgramming;

import java.awt.BorderLayout;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author mernig
 */
public class Server extends JFrame {
// Text area for displaying contents

    private JTextArea jta = new JTextArea();

    public static void main(String[] args) {
        new Server();
    }

    public Server() {
// Place text area on the frame
        setLayout(new BorderLayout());
        add(new JScrollPane(jta), BorderLayout.CENTER);
        setTitle("Server");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        try {
            // Create a server socket
            ServerSocket serverSocket = new ServerSocket(8000);

            // Listen for a connection request
            Socket socket = serverSocket.accept();
            // Create data input and output streams
            InetAddress inet = socket.getInetAddress();
            jta.append(inet.getHostName()+" Server running on " +inet.getHostAddress() + new Date() + '\n' );

            DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
            DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());
            while (true) {
                // Receive radius from the client
                double radius = inputFromClient.readDouble();
                double area = radius * radius * Math.PI; // Compute area
                // Send area back to the client
                outputToClient.writeDouble(area);
                jta.append("Radius received from client: " + radius + '\n');
                jta.append("Calculated area : " + area + '\n');

            }

        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
}

