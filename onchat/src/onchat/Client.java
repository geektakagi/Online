package onchat;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.JSplitPane;
import javax.swing.BoxLayout;
import javax.swing.JTextArea;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.JScrollBar;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JOptionPane;

public class Client extends JFrame {

	private JPanel contentPane;
	private JTextField textField;

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

	
	// Create the frame.
	public Client() {
		String Name = new String("Renet");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 640, 920);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnConnections = new JMenu("Connections");
		menuBar.add(mnConnections);
		
		JMenuItem mntmConnect = new JMenuItem("Connect");
		mnConnections.add(mntmConnect);
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
		
		JLabel label = new JLabel("");
		label.setFont(new Font("Meiryo", Font.PLAIN, 25));
		scrollPane.setViewportView(label);
		
		mntmConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				// connect to the server
				Connection c = new Connection("127.0.0.0");

				String text = textField.getText();
				InputStream is = new ByteArrayInputStream(text.getBytes("UTF-8"));

				JOptionPane.showMessageDialog(null, "Connected to the server");
			}
		});
		
		
		btnSend.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(textField.getText() != ""){
					String str = new String("<html>" + Name + " > "+ textField.getText() + "<br>");
					str += label.getText();
					label.setText(str);
					textField.setText("");
				}	
			}
		});
		
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == e.VK_ENTER){
					if(textField.getText() != ""){
						String str = new String("<html>" + Name + " > "+ textField.getText() + "<br>");
						str += label.getText();
						label.setText(str);
						textField.setText("");
					}
				}
				
			}
		});
		
		
	}
}