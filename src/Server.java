import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.sql.SQLOutput;

public class Server {
    BufferedReader read;
    ServerSocket server;
    Socket socket;

    PrintWriter out;

    public Server(){
        try {
            server = new ServerSocket(7777);
            System.out.println("Server is ready to accept the connection.");
            System.out.println("waiting..");
            socket = server.accept();
            try{
                read = new BufferedReader(new InputStreamReader( socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream());
                startReading();
                startWriting();
            }catch (Exception ob){
                System.out.println("The Input Stream is Empty...");
            }

        }catch (Exception e){
            System.out.println("Some error occurred..");
        }
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
                            System.out.println("Client has stopped..");
                            socket.close();
                            break;
                        }

                        System.out.println("Client : " + str);
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
        System.out.println("This is server... going to Start soon");
        new Server();
    }
}
