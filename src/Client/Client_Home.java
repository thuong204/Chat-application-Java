package Client;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.Color;
import java.awt.Component;
import javax.swing.ScrollPaneConstants;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;

public class Client_Home extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextArea textArea;
	private JTextField textField;
	private JButton btnngNhp;
	private JTextPane textPane;
	String name, ip;
	int port;
	Socket socket;
	DataOutputStream dos;
	private boolean isConnected = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client_Home frame = new Client_Home();
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
	public Client_Home() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Giao diện chính (Client)");
		setBounds(100, 100, 600, 310);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel_1_1 = new JLabel("Danh sách online");
		lblNewLabel_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_1_1.setBounds(443, 32, 133, 15);
		contentPane.add(lblNewLabel_1_1);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 586, 22);
		contentPane.add(menuBar);
		
		JMenu mnNewMenu = new JMenu("Tài khoản");
		menuBar.add(mnNewMenu);
		
		textArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(10, 32, 400, 200);
		contentPane.add(scrollPane);
		
		textField = new JTextField();
		textField.setBounds(10, 239, 400, 22);
		contentPane.add(textField);
		textField.setColumns(10);
		JScrollPane scrollPane_1 = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane_1.setBounds(443, 57, 133, 165);
		contentPane.add(scrollPane_1);
		
		textPane = new JTextPane();
		scrollPane_1.setViewportView(textPane);
		
		btnngNhp = new JButton("GỬI");
		btnngNhp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!textField.getText().equals("")) {
					try {
			            String content = name+" "+ textField.getText();
			            dos.writeUTF("CMD_CHATALL "+ content);
			            appendMyMessage(" "+textField.getText(), name);
			            textField.setText("");
			        } catch (IOException ex) {
			            appendMessage("Không thể gửi tin nhắn đi bây giờ, máy chủ hiện đang đóng hoặc lỗi, vui lòng khởi động lại", "Lỗi", Color.RED, Color.RED);
			        }
				}
			}
		});
		btnngNhp.setBounds(443, 239, 133, 22);
		contentPane.add(btnngNhp);
	}

	public void initFrame(String name, String ip, int port) {
		this.name = name;
		this.ip = ip;
		this.port = port;
		setTitle("Tài khoản tên: "+name);
		connect();
	}
	
	public void connect() {
		appendMessage("Đang kết nối...", "Trạng thái", Color.PINK, Color.PINK);
        try {
            socket = new Socket(ip, port);
            dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF("CMD_JOIN "+ name);
            appendMessage(" Đã kết nối máy chủ", "Trạng thái", Color.PINK, Color.PINK);
            appendMessage(" Giao tiếp", "Trạng thái", Color.PINK, Color.PINK);
            
            // Khởi động Client Thread 
            new Thread(new Client_Thread(socket, this)).start();
            btnngNhp.setEnabled(true);
            isConnected = true;
            
        }
        catch(IOException e) {
            isConnected = false;
            JOptionPane.showMessageDialog(this, "Máy chủ chỉ mở từ 7:00 đến 17:00","Kết nối thất bại",JOptionPane.ERROR_MESSAGE);
            appendMessage("[IOException]: "+ e.getMessage(), "Lỗi", Color.RED, Color.RED);
        }
	}

	public boolean isConnected(){
        return this.isConnected;
    }
	
	public void appendMessage(String msg, String header, Color headerColor, Color contentColor) {
		textArea.setEditable(true);
		getMsgHeader(header, headerColor);
		getMsgContent(msg, contentColor);
		textArea.setEditable(false);
	}
	
	public void appendMyMessage(String msg, String header){
        textArea.setEditable(true);
        getMsgHeader(header, Color.GREEN);
        getMsgContent(msg, Color.BLACK);
        textArea.setEditable(false);
    }
	
	public void getMsgHeader(String header, Color color){
        int len = textArea.getDocument().getLength();
        textArea.setCaretPosition(len);
        textArea.replaceSelection(header+":");
    }
	
	public void getMsgContent(String msg, Color color){
        int len = textArea.getDocument().getLength();
        textArea.setCaretPosition(len);
        textArea.replaceSelection(msg +"\n\n");
    }
	public void showOnLineList(Vector list){
        try {
            textPane.setEditable(true);
            textPane.setContentType("text/html");
            StringBuilder sb = new StringBuilder();
            Iterator it = list.iterator();
            sb.append("<html><table>");
            while(it.hasNext()){
                Object e = it.next();
                sb.append("<tr><td><b>></b></td><td>").append(e).append("</td></tr>");
                System.out.println("Online: "+ e);
            }
            sb.append("</table></body></html>");
            textPane.removeAll();
            textPane.setText(sb.toString());
            textPane.setEditable(false);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
	
	public void appendOnlineList(Vector list){
        sampleOnlineList(list); 
    }
		
	private void sampleOnlineList(Vector list){
        textPane.setEditable(true);
        textPane.removeAll();
        textPane.setText("");
        Iterator i = list.iterator();
        while(i.hasNext()){
            Object e = i.next();
            JPanel panel = new JPanel();
            panel.setLayout(new FlowLayout(FlowLayout.LEFT));
            panel.setBackground(Color.white);
            JLabel label = new JLabel();
            label.setText(" "+ e);
            panel.add(label);
            int len = textPane.getDocument().getLength();
            textPane.setCaretPosition(len);
            textPane.insertComponent(panel);
            sampleAppend();
        }
        textPane.setEditable(false);
    }
    private void sampleAppend(){
        int len = textPane.getDocument().getLength();
        textPane.setCaretPosition(len);
        textPane.replaceSelection("\n");
    }
}
