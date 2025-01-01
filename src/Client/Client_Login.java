package Client;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.awt.event.ActionEvent;

public class Client_Login extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField txtLocalhost;
	private JTextField textField_2;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client_Login frame = new Client_Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Client_Login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Đăng nhập (Client)");
		setBounds(100, 100, 600, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Giao tiếp nhân sự");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(201, 24, 163, 13);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Đăng nhập");
		lblNewLabel_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_1_1.setBounds(201, 47, 163, 15);
		contentPane.add(lblNewLabel_1_1);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(244, 93, 160, 19);
		contentPane.add(textField);
		
		JLabel lblNewLabel = new JLabel("Tên đăng nhập");
		lblNewLabel.setBounds(132, 96, 102, 13);
		contentPane.add(lblNewLabel);
		
		txtLocalhost = new JTextField();
		txtLocalhost.setText("localhost");
		txtLocalhost.setColumns(10);
		txtLocalhost.setBounds(244, 122, 160, 19);
		contentPane.add(txtLocalhost);
		
		JLabel lblNewLabel_2 = new JLabel("Địa chỉ IP");
		lblNewLabel_2.setBounds(132, 125, 102, 13);
		contentPane.add(lblNewLabel_2);
		
		textField_2 = new JTextField();
		textField_2.setText("1234");
		textField_2.setColumns(10);
		textField_2.setBounds(244, 151, 160, 19);
		contentPane.add(textField_2);
		
		JLabel lblNewLabel_3 = new JLabel("Port");
		lblNewLabel_3.setBounds(132, 154, 102, 13);
		contentPane.add(lblNewLabel_3);
		
		JButton btnngNhp = new JButton("ĐĂNG NHẬP");
		btnngNhp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connectToServer();
			}
		});
		btnngNhp.setBounds(201, 199, 143, 21);
		contentPane.add(btnngNhp);
	}
	
	private void connectToServer() {
		if (textField.getText().length() > 0 && txtLocalhost.getText().length() > 0 && textField_2.getText().length() > 0) {
			if (textField.getText().length() <= 15) {
				String username = textField.getText();
				String u = username.replace(" ", "_");
				Client_Home main = new Client_Home();
				main.initFrame(u, txtLocalhost.getText(), Integer.parseInt(textField_2.getText()));
				if (main.isConnected()) {
					main.setLocationRelativeTo(null);
					main.setVisible(true);
					setVisible(false);
				}
			}else {
				JOptionPane.showMessageDialog(this, "Tài khoản phải tối đa 15 ký tự bao gồm [khoảng trắng].!", "Lỗi", JOptionPane.ERROR_MESSAGE);
				
			}
		}else {
			JOptionPane.showMessageDialog(this, "Vui lòng điền đẩy đủ thông tin", "", JOptionPane.ERROR_MESSAGE);
		}
	}
}
