import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends JFrame{
    Socket socket;

    BufferedReader read;
    PrintWriter out;

    private JLabel heading = new JLabel("Client App");
    private JTextArea messageBox = new JTextArea();
    private JTextField message = new JTextField();
    private Font font = new Font("Roboto",Font.PLAIN , 20 );

    public Client(){
        try{
            System.out.println("Sending request to server.");
            socket = new Socket("127.0.0.1",7777);
            System.out.println("Connection is established.");

            read = new BufferedReader(new InputStreamReader( socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            createGUI();
            handleEvent();
            startReading();
            //startWriting();
        }catch (Exception ob){
            System.out.println("Some Error occurred "+ ob);
        }
    }

    private void createGUI(){
        this.setTitle("Client Message:-");
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        heading.setFont(font);
        messageBox.setFont(font);
        message.setFont(font);
        // load the image to a imageIcon
        ImageIcon imageIcon = new ImageIcon("logo.png");
        Image image = imageIcon.getImage(); // transform it
        Image newImg = image.getScaledInstance(60, 60,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        heading.setIcon(new ImageIcon(newImg));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBackground(Color.DARK_GRAY);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageBox.setEditable(false);
        message.setHorizontalAlignment(SwingConstants.CENTER);

        this.setLayout(new BorderLayout());

        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageBox);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(message,BorderLayout.SOUTH);
        this.setVisible(true);
    }

    private void handleEvent(){
        message.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                //System.out.println("The Key Pressed "+e.getKeyCode());
                if(e.getKeyCode() == 10){
                    //System.out.println("You have pressed enter Button.");
                    String content = message.getText();
                    messageBox.append("Me:"+content+"\n");
                    out.println(content);
                    out.flush();
                    message.setText("");
                    message.requestFocus();
                }
            }
        });
    }

    public void startReading(){
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                System.out.println(" Reader Thread has started..");
                while(true){
                    try{
                        String str = read.readLine();
                        if(str.equals("exit")){
                            System.out.println("Server has stopped..");
                            JOptionPane.showMessageDialog(null, "Server Terminated the chat");
                            message.setEnabled(false);
                            socket.close();
                            break;
                        }

                        //System.out.println("Server : " + str);
                        messageBox.append("Server: "+str+"\n");
                    }catch (Exception ob){
                        System.out.println("Some error occurred while reading the message just restart the server and cli" +
                                "ent once again");
                    }
                }
            }
        };
        new Thread(r1).start();
    }

    public void startWriting(){
        Runnable r2 =  new Runnable(){
            @Override
            public void run() {
                System.out.println("Writer Thread has Started...");
                while(true){
                    try {
                        BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                        String content = br1.readLine();
                        out.println(content);
                        out.flush();
                    }catch (Exception ob){
                        ob.printStackTrace();
                    }
                }
            }
        };
        new Thread(r2).start();
    }


    public static void main(String[] args) {
        System.out.println("This is client app..");
        new Client();
    }
}
