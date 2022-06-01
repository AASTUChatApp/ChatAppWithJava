package com.mycompany.chatapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;

public class Login extends JFrame{
    protected JPanel loginMainPanel;
    protected JPanel loginContainer;
    protected JLabel Title;
    protected JPasswordField passwordField;
    protected JLabel emailLabel;
    protected JLabel pwdLabel;
    protected JTextField emailField;
    protected JButton submit;
    private JButton registerButton;
    protected String JDBC = "jdbc:mysql://localhost:3306/chatapp";
    protected String username = "root";
    protected String password = "";

    public Login(String title){
        super(title);
        add(loginMainPanel);
        setLayout(new GridLayout(0,1,10,10));
        submit.addActionListener(
            (e) -> {
                if(e.getSource() == submit){
                    String email = emailField.getText();
                    String pass = String.valueOf(passwordField.getPassword());
                    ArrayList<String> errors = new ArrayList<>();
                    if(email.isEmpty() || pass.isEmpty()){
                        errors.add("Please fill out all the fields");
                    }
                    String res = authenticate(email,pass);
                    System.out.println(res);
                    if(!res.isEmpty()){
                        loginMainPanel.setVisible(false);
                        this.dispose();
                        Socket socket = null;
                        try {
                            socket = new Socket("localhost",8002);
                            ChatAppUI chatAppUI = new ChatAppUI(socket,email);
//                            chatAppUI.MainPanel.setSize(400,400);
                            chatAppUI.setSize(400,400);
                            chatAppUI.setVisible(true);
                            chatAppUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                            this.setPreferredSize(new Dimension(400,400));
//                            this.setContentPane(chatAppUI.MainPanel);
                            chatAppUI.readMessage();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                    }else{
                        JOptionPane.showMessageDialog(this,email + "\n" + pass + "\nEmail or Password Error","Result",JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        );
        registerButton.addActionListener(
                (e) -> {
                    if(e.getSource() == registerButton){
                        Registor registor = new Registor("Register");
                        this.dispose();
                        registor.setSize(350,320);
                        registor.setVisible(true);
                        registor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    }
                }
        );
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    private String authenticate(String email,String password){
        try{
            Connection con = DriverManager.getConnection(JDBC, username, password);
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM user WHERE email = ? && password = ?");
            stmt.setString(1,email);
            stmt.setString(2,password);
            ResultSet resultSet = stmt.executeQuery();
            System.out.println(resultSet.getFetchSize());
            String name = "";
            while(resultSet.next()){
                System.out.println(resultSet.getString("name") + " " + resultSet.getString("email") + " " +resultSet.getString("password"));
                name = resultSet.getString("name");
            }
            stmt.close();
            con.close();
            return name;
        }catch (SQLException e){
            System.out.println(e);
           setError(e);
        }
        return "";
    }

    private void setError(Exception e) {
        JOptionPane.showMessageDialog(this,e.getMessage(),"Exception",JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args){
        Login login = new Login("Login");
        login.setSize(400,300);
        login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        login.setVisible(true);
    }
}
