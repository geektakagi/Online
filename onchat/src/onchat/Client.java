package onchat;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.awt.event.KeyEvent;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

public class Client extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	private JPanel contentPane;
	private JTextField textField;
	private static JLabel label = new JLabel("");
	private static String sendStr = "";
	private static String Name;
	private static Connection connection = null;

	// Launch the application.
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client frame = new Client();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
	}

	public static String getTextFieldStrings() {
		String returnStr = sendStr;
		sendStr = "";
		
		return returnStr;
	}

	public static void chatWrite(String receiveStr){
		String str = new String("<html>" + receiveStr + "<br>");
		str += label.getText();
		label.setText(str);
	}
	
	// Create the frame.
	public Client() {
		Name = new String("Renet");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 640, 920);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnConnections = new JMenu("Connections");
		menuBar.add(mnConnections);
		
		JMenuItem mntmConnect = new JMenuItem("Connect");
		mnConnections.add(mntmConnect);
		
		JMenuItem mntmConnectionInfo = new JMenuItem("Connection info");
		mnConnections.add(mntmConnectionInfo);
		
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 40));		
		textField.setBounds(17, 17, 486, 45);
		contentPane.add(textField);
		textField.setColumns(30);
		
		JButton btnSend = new JButton("Send");		
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});

		btnSend.setBounds(520, 32, 75, 31);
		contentPane.add(btnSend);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(17, 81, 578, 756);
		contentPane.add(scrollPane);
		
		label = new JLabel("");
		label.setVerticalAlignment(SwingConstants.TOP);
		label.setFont(new Font("Meiryo", Font.PLAIN, 25));
		scrollPane.setViewportView(label);
		
		mntmConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				// connect to the server							
				try {
					Connection c = new Connection("localhost");
					c.openConnection();
					c.main_proc();
					
					Client.setConnection(c);
					
					Client.chatWrite("Connected to the server");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					System.err.print(e);
				}
				
			}
		});
		
		
		btnSend.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(textField.getText() != ""){
					Client.connection.sendMessage(textField.getText());
					textField.setText("");
				}	
			}
		});
		
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					if(textField.getText() != ""){
						Client.connection.sendMessage(textField.getText());
						textField.setText("");

					}
				}
				
			}
		});
		
		
	}

	protected static void setConnection(Connection c) {
		connection = c;		
	}
}