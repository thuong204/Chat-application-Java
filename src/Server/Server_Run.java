package Server;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import java.awt.ScrollPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Vector;
import java.awt.event.ActionEvent;

public class Server_Run extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextArea textArea;
	SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
	Thread t;
	Server_Thread serverThread;
	public Vector socketList = new Vector();
    public Vector clientList = new Vector();
    public Vector clientFileSharingUsername = new Vector();
    public Vector clientFileSharingSocket = new Vector();
    ServerSocket server;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Server_Run frame = new Server_Run();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the frame.
	 */
	public Server_Run() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Máy chủ (Server)");
		setBounds(100, 100, 700, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Port");
		lblNewLabel.setBounds(80, 426, 45, 13);
		contentPane.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setText("1234");
		textField.setBounds(115, 423, 96, 19);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("Khởi động");
		btnNewButton.setBounds(234, 422, 143, 21);
		contentPane.add(btnNewButton);
		
		JButton btnDngMyCh = new JButton("Dừng");
		btnDngMyCh.setEnabled(false);
		btnDngMyCh.setBounds(387, 422, 143, 21);
		contentPane.add(btnDngMyCh);
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!textField.getText().equals("")) {
					JButton1ActionPerformed(e);
					btnNewButton.setEnabled(false);
					btnDngMyCh.setEnabled(true);
				}else {
					JOptionPane.showMessageDialog(null, "Không được để trống mã số cổng(Port)", "Thông báo", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		
		btnDngMyCh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton2ActionPerformed(e);
			}
		});
		
		textArea = new JTextArea();
//		textArea.setBounds(25, 10, 480, 384);
//		contentPane.add(textArea);
		
		JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(48, 81, 587, 304);
		contentPane.add(scrollPane);
		
		JLabel lblNewLabel_1 = new JLabel("Giao tiếp nhân sự");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(251, 24, 163, 13);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("bảo dưỡng xe cơ giới");
		lblNewLabel_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_1_1.setBounds(251, 47, 163, 15);
		contentPane.add(lblNewLabel_1_1);
		
	}
	
	private void JButton1ActionPerformed(java.awt.event.ActionEvent evt) {
		int port = Integer.valueOf(textField.getText());
        serverThread = new Server_Thread(port, this);
        t = new Thread(serverThread);
        t.start();
        new Thread(new Online_List_Thread(this)).start();
	}
	
	private void JButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int confirm = JOptionPane.showConfirmDialog(null, "Thao tác này sẽ đóng máy chủ!", "", JOptionPane.YES_NO_OPTION);
        if(confirm == 0){
            serverThread.stop();
            t.run();
        }
    }
	
	public void appendMessage(String msg) {
		java.util.Date date = new java.util.Date();
        textArea.append(sdf.format(date) +": "+ msg +"\n");
        textArea.setCaretPosition(textArea.getText().length() - 1);
	}
	
	public void setSocketList(Socket socket){
        try {
            socketList.add(socket);
            appendMessage("[setSocketList]: Được thêm");
        } catch (Exception e) { appendMessage("[setSocketList]: "+ e.getMessage()); }
    }
    public void setClientList(String client){
        try {
            clientList.add(client);
            appendMessage("[setClientList]: Được thêm");
        } catch (Exception e) { appendMessage("[setClientList]: "+ e.getMessage()); }
    }
    public void setClientFileSharingUsername(String user){
        try {
            clientFileSharingUsername.add(user);
        } catch (Exception e) { }
    }
    
    public void setClientFileSharingSocket(Socket soc){
        try {
            clientFileSharingSocket.add(soc);
        } catch (Exception e) { }
    }
    
    public Socket getClientList(String client){
        Socket tsoc = null;
        for(int x=0; x < clientList.size(); x++){
            if(clientList.get(x).equals(client)){
                tsoc = (Socket) socketList.get(x);
                break;
            }
        }
        return tsoc;
    }
    public void removeFromTheList(String client){
        try {
            for(int x=0; x < clientList.size(); x++){
                if(clientList.elementAt(x).equals(client)){
                    clientList.removeElementAt(x);
                    socketList.removeElementAt(x);
                    appendMessage("[Removed]: "+ client);
                    break;
                }
            }
        } catch (Exception e) {
            appendMessage("[RemovedException]: "+ e.getMessage());
        }
    }
    
    public Socket getClientFileSharingSocket(String username){
        Socket tsoc = null;
        for(int x=0; x < clientFileSharingUsername.size(); x++){
            if(clientFileSharingUsername.elementAt(x).equals(username)){
                tsoc = (Socket) clientFileSharingSocket.elementAt(x);
                break;
            }
        }
        return tsoc;
    }
        public void removeClientFileSharing(String username){
        for(int x=0; x < clientFileSharingUsername.size(); x++){
            if(clientFileSharingUsername.elementAt(x).equals(username)){
                try {
                    Socket rSock = getClientFileSharingSocket(username);
                    if(rSock != null){
                        rSock.close();
                    }
                    clientFileSharingUsername.removeElementAt(x);
                    clientFileSharingSocket.removeElementAt(x);
                    appendMessage("[FileSharing]: Hủy bỏ "+ username);
                } catch (IOException e) {
                    appendMessage("[FileSharing]: "+ e.getMessage());
                    appendMessage("[FileSharing]: Không thể hủy bỏ "+ username);
                }
                break;
            }
        }
    }
}
