package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server_Thread implements Runnable{
	ServerSocket server;
    Server_Run main;
    boolean keepGoing = true;
    
    public Server_Thread(int port, Server_Run main){
        main.appendMessage("[Server]: Máy Chủ hiện đang khởi động ở port "+ port);
        try {
            this.main = main;
            server = new ServerSocket(port);
            main.appendMessage("[Server]: Máy Chủ đã khởi động.!");
        } 
        catch (IOException e) { main.appendMessage("[IOException]: "+ e.getMessage()); }
    }

    @Override
    public void run() {
        try {
            while(keepGoing){
                Socket socket = server.accept();

                /** Socket thread **/
                new Thread(new Socket_Thread(socket, main)).start();
            }
        } catch (IOException e) {
            main.appendMessage("[ServerThreadIOException]: "+ e.getMessage());
        }
    }
    
    
    public void stop(){
        try {
            server.close();
            keepGoing = false;
//            System.out.println("Máy Chủ bị đóng..!");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
