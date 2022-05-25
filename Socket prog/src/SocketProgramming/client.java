/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SocketProgramming;

/**
 *
 * @author mernig
 */
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class client extends JFrame {

    // Text field for receiving radius
    JTextField jtf = new JTextField();
    // Text area to display contents
    JTextArea jta = new JTextArea();
    // IO streams
    DataOutputStream toServer;
    DataInputStream fromServer;
    BufferedOutputStream bufferedOutputStream;

    public static void main(String[] args) {
        new client();
    }

    public client() {
        // Panel p to hold the label and text field
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        p.add(new JLabel("Enter radius"), BorderLayout.WEST);
        p.add(jtf, BorderLayout.CENTER);
        jtf.setHorizontalAlignment(JTextField.RIGHT);
        setLayout(new BorderLayout());
        add(p, BorderLayout.NORTH);
        add(new JScrollPane(jta), BorderLayout.CENTER);
        jtf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    // Get the radius from the text field
                    double radius = Double.parseDouble(jtf.getText().trim());
                    // Send the radius to the server
                    toServer.writeDouble(radius);
                    toServer.flush();
                    // Get area from the server
                    double area = fromServer.readDouble();
                    // Display to the text area
                    jta.append("Radius is " + radius + "\n");
                    jta.append("Area received from the server is" + area + '\n');
                    jtf.setText("");
                } catch (IOException ex) {
                    System.err.println(ex);
                }
            }
        });
        setTitle("Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true); // It is necessary to show the frame here!
        try {
            // Create a socket to connect to the server
            Socket socket = new Socket("localhost", 8000);
            // Socket socket = new Socket("130.254.204.33", 8000);
            // Create an input stream to receive data from the server
            fromServer = new DataInputStream(socket.getInputStream());
            // Create an output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            jta.append(ex.toString() + '\n');
        }
    }
}
