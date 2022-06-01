package com.mycompany.chatapp;

import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.util.Scanner;

public class ChatAppUI extends JFrame {
    public JPanel MainPanel;
    public JPanel titlebar;
    public JPanel sidebar;
    public JPanel chatbox;
    public JPanel footer;
    public JPanel writeMsgPanel;
    public JScrollPane scrollPane;
    JScrollPane scrollPane2;
    public JPanel readMsgPanel;
    public JButton sendButton;
    public JButton[] OnlineFriends;
    public JLabel recievedMsg;
    public JLabel sentMessage;
    public JLabel GroupName;
    public JTextField search;
    public JTextField writeMessage;
    public JTextField Username;
    public JTextArea msgHistory;
    public JTextPane textPane;

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    private String JDBC = "jdbc:mysql://localhost:3306/chatapp";
    private String dbusername = "root";
    private String password = "";
    PreparedStatement statement;

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Socket socket = new Socket("localhost",8002);
        ChatAppUI chatAppUI = new ChatAppUI(socket,"");
        chatAppUI.setVisible(true);
        chatAppUI.setSize(400,400);
        chatAppUI.readMessage();
    }

    public ChatAppUI(Socket socket,String userEmail){

        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        Connection connection = DriverManager.getConnection(JDBC,dbusername,password);
        String sql = "SELECT * FROM user WHERE email = ?";
        statement = connection.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
        statement.setString(1,userEmail);
        ResultSet user = statement.executeQuery();
        ResultSetMetaData metadata = user.getMetaData();

        sql = "UPDATE user SET acount_state = ? WHERE email = ?";
        statement = connection.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
        statement.setString(1, "online");
        statement.setString(2, userEmail);
        statement.executeUpdate();

        if(user.next()){
            this.username = user.getString("name");
            bufferedWriter.write(userEmail);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }

        MainPanel = new JPanel();
        titlebar = new JPanel();
        sidebar = new JPanel();
        chatbox = new JPanel();
        footer = new JPanel();
        //
        msgHistory = new JTextArea(100,100);
        msgHistory.setBackground(Color.GREEN);
        msgHistory.setFont(new Font(Font.DIALOG_INPUT,Font.ITALIC,15));
        msgHistory.setEditable(false);
        float align = msgHistory.getAlignmentX();
//      System.out.println(align);
        msgHistory.setMargin(new Insets(15,10,15,5));
        msgHistory.setAlignmentX(4);
        writeMsgPanel = new JPanel();
        readMsgPanel = new JPanel(new GridLayout(0,1));
        scrollPane = new JScrollPane(msgHistory,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
     //scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sendButton = new JButton("Send");

        //we will assign this array of buttons with the array size of the num of persons who are online available and the content of each
        // button will be replaced with the name of the user

        recievedMsg = new JLabel();
        sentMessage = new JLabel();
        GroupName = new JLabel("Group 4");
        search = new JTextField("Search");
        writeMessage = new JTextField("Write message",15);
        Username =new JTextField(username);
        Username.setEditable(false);
        textPane = new JTextPane();
        textPane.setPreferredSize(new Dimension(150,50));
        Username.addActionListener(
                e -> {
                    if(e.getSource() == Username){
                        Username.setEditable(false);
                        try {
                            String txt = Username.getText();
                            bufferedWriter.write(txt);
                            bufferedWriter.newLine();
                            bufferedWriter.flush();
//                            String greating = bufferedReader.readLine();
//                            textPane.setText(greating);
//                            readMsgPanel.add(textPane);
//                            textPane.setAlignmentX(LEFT_ALIGNMENT);
//                            msgHistory.append(greating + "\n");
                          //  String names = bufferedReader.readLine();
//                            msgHistory.append(names + "\n");

                        }catch (IOException ioException){
                            closeEverything(socket,bufferedReader,bufferedWriter);
                        }
                    }
                }
        );


        setLayout(new BorderLayout());

        MainPanel.setBackground(new Color(0, 102, 255));
        MainPanel.setLayout(new BorderLayout(10,10));
        titlebar.setLayout(null);
        titlebar.setPreferredSize(new Dimension(200,50));
//        search.setHorizontalAlignment(0);
        search.setBounds(20,10,150,30);
        search.setMargin(new Insets(5,5,5,5));
        titlebar.add(search);
//        GroupName.setHorizontalAlignment(1);
        GroupName.setBounds(200,10,100,20);
        GroupName.setFont(new Font("serif",Font.BOLD,20));
        titlebar.add(GroupName);
        MainPanel.add(titlebar,BorderLayout.NORTH);

        scrollPane2 = new JScrollPane(sidebar,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        MainPanel.add(scrollPane2, BorderLayout.WEST);
        setOnlineFriends(getOnlines(connection));

        chatbox.setBackground(new Color(255, 179, 179));
        chatbox.setLayout(new BorderLayout(5,10));
        readMsgPanel.setBackground(Color.GRAY);
        readMsgPanel.setPreferredSize(new Dimension(100,200));
        readMsgPanel.add(scrollPane);
     //   readMsgPanel.setLayout(new GridLayout(0,1));
        //scrollPane.setViewportView(readMsgPanel);

//        writeMsgPanel.setLayout(null);
        writeMsgPanel.setPreferredSize(new Dimension(100,40));
        writeMessage.setBounds(10,5,150,40);
        sendButton.addActionListener(
                (e) -> {
                    if(e.getSource() == sendButton){
//                        if(readMsgPanel.getBackground() != Color.GREEN){
//                            msgHistory.setBackground(Color.GREEN);
//                        }else{
//                            msgHistory.setBackground(new Color(255, 153, 255));
//                        }
                        String msg = writeMessage.getText();
//                        JLabel MSG = new JLabel();
//                        MSG.setVisible(true);
//                        MSG.setText(msg);
//                        Username.setText(msg);
                          msgHistory.append(msg + "\n");
//                        textPane.setText(msg);
//                        readMsgPanel.add(textPane,"wrap, al right, w ::80%");
//                        textPane.setAlignmentX(Component.RIGHT_ALIGNMENT);
                        writeMessage.setText("");
                        try{
                            bufferedWriter.write(Username.getText() + " : " + msg);
                            bufferedWriter.newLine();
                            bufferedWriter.flush();
                        }catch (IOException ioException){
                            closeEverything(socket,bufferedReader,bufferedWriter);
                        }

                    }
                }
        );

      //  sendButton.setBounds(20,10,150,30);
        writeMsgPanel.add(writeMessage);
        writeMsgPanel.add(sendButton);
        chatbox.add(readMsgPanel,BorderLayout.NORTH);
        chatbox.add(writeMsgPanel,BorderLayout.SOUTH);
        MainPanel.add(chatbox);


        //footer
        footer.setLayout(new FlowLayout());
        footer.setPreferredSize(new Dimension(100,50));
        footer.setBackground(new Color(179, 255, 179));
        Username.setFont(new Font(Font.SANS_SERIF,Font.PLAIN, 15));
        footer.add(Username);
        MainPanel.add(footer,BorderLayout.SOUTH);

        // adding the main panel to the  frame
        add(MainPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setOnlineFriends(ResultSet onlines) {
        try{
            MainPanel.remove(scrollPane2);
            sidebar = new JPanel();
            ResultSet online = onlines;
            online.last();
            int rowCount = online.getRow();
            System.out.println("online " + rowCount);
            OnlineFriends = new JButton[rowCount - 1];

            sidebar.setLayout(new GridLayout(OnlineFriends.length,1,10,5));
            sidebar.setPreferredSize(new Dimension(120,200));
            sidebar.setBackground(new Color(255, 128, 128));

            online.first();
            for (int i = 0; i < OnlineFriends.length; i++){
                String name = String.valueOf(online.getString("name").trim());
                if( name.equals(username)){
                    System.out.println("this name not displayed " + username);
                    i--;
                    online.next();
                    continue;
                }
                OnlineFriends[i] = new JButton( online.getString("name"));
                System.out.println("on name " + name + " " +  username.trim() + " " + i);
                online.next();
            }

            for (int i = 0; i < OnlineFriends.length; i++){
                OnlineFriends[i].setBounds(0 ,0, 50,30);
                OnlineFriends[i].setPreferredSize(new Dimension(50,30));
            }
            for (int i = 0; i < OnlineFriends.length; i++){
                OnlineFriends[i].setBorder(BorderFactory.createEmptyBorder(7,10,5,10));
            }
            for (int i = 0; i < OnlineFriends.length; i++){
                OnlineFriends[i].setFont(new Font(Font.MONOSPACED,Font.ITALIC,20));
            }

            for (int i = 0; i < OnlineFriends.length; i++){
                OnlineFriends[i].setFont(new Font(Font.MONOSPACED,Font.ITALIC,20));
            }

            for (int i = 0; i < OnlineFriends.length; i++){
                sidebar.add(OnlineFriends[i]);
            }

            scrollPane2 = new JScrollPane(sidebar,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            MainPanel.add(scrollPane2, BorderLayout.WEST);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ResultSet getOnlines(Connection connection) {
        try{
            String sql = "SELECT * FROM user WHERE acount_state = ?";
            statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            statement.setString(1, "online");
            ResultSet online = statement.executeQuery();
            return online;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void sendMessage(String message){
        try{
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

//          Scanner scanner = new Scanner(System.in);
            while(socket.isConnected()){
//                String message = scanner.nextLine();
                bufferedWriter.write(username + " : " + message);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }

        }catch (IOException e){
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }

    public void readMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromClients;
                while (socket.isConnected()){
                    try {
                        messageFromClients = bufferedReader.readLine();
                        if(messageFromClients.startsWith("SERVER")){
                            System.out.println("From Server");
                            setOnlineFriends(getOnlines(DriverManager.getConnection(JDBC,dbusername,password)));
                        }
//                        System.out.println(messageFromClients);
//                        textPane.setText(messageFromClients);
//                        readMsgPanel.add(textPane,"wrap, w ::80%");
//                        textPane.setAlignmentX(Component.LEFT_ALIGNMENT);
                        msgHistory.append(messageFromClients + "\n");
                    } catch (IOException e) {
                        closeEverything(socket,bufferedReader,bufferedWriter);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
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
        }catch (IOException e){
            e.printStackTrace();
        }
    }




}
