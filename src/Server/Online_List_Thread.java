package Server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Online_List_Thread implements Runnable{
	Server_Run main;
	
	public Online_List_Thread(Server_Run main) {
		this.main = main;
	}
	
	@Override
    public void run() {
        try {
            while(!Thread.interrupted()){
                String msg = "";
                for(int x=0; x < main.clientList.size(); x++){
                    msg = msg+" "+ main.clientList.elementAt(x);
                }
                
                for(int x=0; x < main.socketList.size(); x++){
                    Socket tsoc = (Socket) main.socketList.elementAt(x);
                    DataOutputStream dos = new DataOutputStream(tsoc.getOutputStream());
                    if(msg.length() > 0){
                        dos.writeUTF("CMD_ONLINE "+ msg);
                    }
                }
                
                Thread.sleep(1900);
            }
        } catch(InterruptedException e){
            main.appendMessage("[InterruptedException]: "+ e.getMessage());
        } catch (IOException e) {
            main.appendMessage("[IOException]: "+ e.getMessage());
        }
    }
}
