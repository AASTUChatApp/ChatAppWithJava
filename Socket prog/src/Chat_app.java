import javax.swing.*;
import java.awt.*;

public class Chat_app extends JFrame{
    private JPanel panel1;
    private JPanel client;
    private JButton search;
    private JTextField textField1;
    private JLabel chatter;

    private void createUIComponents() {
        // TODO: place custom component creation code here
        client = new JPanel();
        search = new JButton("search");
        textField1 = new JTextField(30);
        this.setContentPane(panel1);
        search.setPreferredSize(new Dimension(12,5));
        client.setLayout(new FlowLayout());
        client.add(search);
        client.add(textField1);
        this.add(panel1);
    }
    public static  void  main(String[] args){
        Chat_app chat_app = new Chat_app();
        chat_app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chat_app.setVisible(true);
        chat_app.setSize(400,500);
    }
}
