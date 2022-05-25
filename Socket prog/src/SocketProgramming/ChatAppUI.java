package SocketProgramming;

import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ChatAppUI extends JFrame {
    public JPanel MainPanel;
    public JPanel titlebar;
    public JPanel sidebar;
    public JPanel chatbox;
    public JPanel footer;
    public JPanel writeMsgPanel;
    public JScrollPane scrollPane;
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

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Socket socket = new Socket("localhost",8002);
        ChatAppUI chatAppUI = new ChatAppUI(socket);
        chatAppUI.setVisible(true);
        chatAppUI.setSize(400,400);
        chatAppUI.readMessage();
    }

    public ChatAppUI(Socket socket){

        try {
            this.socket = socket;

            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));


        MainPanel = new JPanel();
        titlebar = new JPanel();
        sidebar = new JPanel();
        chatbox = new JPanel();
        footer = new JPanel();
        //
        msgHistory = new JTextArea(100,100);
        msgHistory.setBackground(new Color(255, 153, 255));
        msgHistory.setFont(new Font(Font.DIALOG_INPUT,Font.ITALIC,15));
        msgHistory.setEditable(false);
        float align = msgHistory.getAlignmentX();
        System.out.println(align);
        msgHistory.setMargin(new Insets(15,10,15,5));
//        msgHistory.setAlignmentX(4);
        writeMsgPanel = new JPanel();
        readMsgPanel = new JPanel();
        scrollPane = new JScrollPane(msgHistory,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sendButton = new JButton("Send");
        OnlineFriends = new JButton[10];
        recievedMsg = new JLabel();
        sentMessage = new JLabel();
        GroupName = new JLabel("Group 4");
        search = new JTextField("Search");
        writeMessage = new JTextField("Write message",15);
        Username =new JTextField("Enter Username here");
        Username.addActionListener(
                e -> {
                    if(e.getSource() == Username){
                        Username.setEditable(false);
                        try {
                            String txt = Username.getText();
                            bufferedWriter.write(txt);
                            bufferedWriter.newLine();
                            bufferedWriter.flush();
                            String greating = bufferedReader.readLine();
                            msgHistory.append(greating + "\n");
                            String names = bufferedReader.readLine();
                            msgHistory.append(names + "\n");

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


        for (int i = 1; i < OnlineFriends.length; i++){
            OnlineFriends[i] = new JButton("User " + i);
        }

        add(MainPanel);

        sidebar.setLayout(new GridLayout(OnlineFriends.length,1,10,5));
        sidebar.setPreferredSize(new Dimension(120,200));
        sidebar.setBackground(new Color(255, 128, 128));

        for (int i = 1; i < OnlineFriends.length; i++){
            OnlineFriends[i].setBounds(0 ,0, 70,50);
        }
        for (int i = 1; i < OnlineFriends.length; i++){
            OnlineFriends[i].setBorder(BorderFactory.createEmptyBorder(7,10,5,10));
        }
        for (int i = 1; i < OnlineFriends.length; i++){
            OnlineFriends[i].setFont(new Font(Font.MONOSPACED,Font.ITALIC,20));
        }

        for (int i = 1; i < OnlineFriends.length; i++){
            OnlineFriends[i].setFont(new Font(Font.MONOSPACED,Font.ITALIC,20));
        }

        for (int i = 1; i < OnlineFriends.length; i++){
            sidebar.add(OnlineFriends[i]);
        }
        MainPanel.add(sidebar,BorderLayout.WEST);


        chatbox.setBackground(new Color(255, 179, 179));
        chatbox.setLayout(new BorderLayout(5,10));
        readMsgPanel.setBackground(new Color(255, 153, 255));
        readMsgPanel.setPreferredSize(new Dimension(100,200));
        readMsgPanel.setLayout(new GridLayout(0,1));
        readMsgPanel.add(scrollPane);

//        writeMsgPanel.setLayout(null);
        writeMsgPanel.setPreferredSize(new Dimension(100,40));
        writeMessage.setBounds(10,5,150,40);
        sendButton.addActionListener(
                (e) -> {
                    if(e.getSource() == sendButton){
                        if(readMsgPanel.getBackground() != Color.GREEN){
                            msgHistory.setBackground(Color.GREEN);
                        }else{
                            msgHistory.setBackground(new Color(255, 153, 255));
                        }
                        String msg = writeMessage.getText();
//                        JLabel MSG = new JLabel();
//                        MSG.setVisible(true);
//                        MSG.setText(msg);
//                        Username.setText(msg);
                          msgHistory.append(msg + "\n");
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

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
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
                        System.out.println(messageFromClients);
                        msgHistory.append(messageFromClients + "\n");
                    } catch (IOException e) {
                        closeEverything(socket,bufferedReader,bufferedWriter);
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
