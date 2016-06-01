import java.util.*;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class ChatClient extends JFrame implements Runnable, ActionListener {
	public static void main(String[] args) {
		ChatClient window = new ChatClient();
		window.setSize(800, 600);
		window.setVisible(true);
	}

	//�A�v���P�[�V������
	private static final String APPNAME = "�`���b�g�N���C�A���g";

	//�ڑ���T�[�o�[�̃z�X�g��
	private static final String HOST = "localhost";

	//�ڑ���|�[�g�ԍ�
	private static final int PORT = 2815;

	//���̃A�v���P�[�V�����̃N���C�A���g�\�P�b�g
	private Socket socket;

	//���b�Z�[�W��M�Ď��p�X���b�h
	private Thread thread;

	//���ݓ������̃`���b�g���[����
	private String roomName;

	//�ȉ��A�R���|�[�l���g
	private JList roomList;	//�`���b�g���[���̃��X�g
	private JList userList;	//���ݓ������̃`���b�g���[���̃��[�U�[
	private JTextArea msgTextArea;		//���b�Z�[�W��\������e�L�X�g�G���A
	private JTextField msgTextField;	//���b�Z�[�W���͗p�̈�s�e�L�X�g
	private JTextField nameTextField;	//���[�U�[����`���b�g���[��������͂����s�e�L�X�g
	private JButton submitButton;		//�u���M�v�{�^��
	private JButton renameButton;		//�u���O�̕ύX�v�{�^��
	private JButton addRoomButton;		//�u������ǉ��v�{�^��
	private JButton enterRoomButton;	//�u�����E�ގ��v�{�^��

	public ChatClient() {
		super(APPNAME);

		JPanel topPanel = new JPanel();
		JPanel leftPanel = new JPanel();
		JPanel buttomPanel = new JPanel();

		JPanel roomPanel = new JPanel();
		JPanel userPanel = new JPanel();

		roomList = new JList();
		userList = new JList();
		msgTextArea = new JTextArea();
		msgTextField = new JTextField();
		nameTextField = new JTextField();
		submitButton = new JButton("���M");
		renameButton = new JButton("���O�̕ύX");
		addRoomButton = new JButton("������ǉ�");
		enterRoomButton = new JButton("����");

		submitButton.addActionListener(this);
		submitButton.setActionCommand("submit");

		renameButton.addActionListener(this);
		renameButton.setActionCommand("rename");

		addRoomButton.addActionListener(this);
		addRoomButton.setActionCommand("addRoom");

		enterRoomButton.addActionListener(this);
		enterRoomButton.setActionCommand("enterRoom");

		roomPanel.setLayout(new BorderLayout());
		roomPanel.add(new JLabel("�`���b�g���[��"), BorderLayout.NORTH);
		roomPanel.add(new JScrollPane(roomList), BorderLayout.CENTER);
		roomPanel.add(enterRoomButton, BorderLayout.SOUTH);

		userPanel.setLayout(new BorderLayout());
		userPanel.add(new JLabel("�Q�����[�U�["), BorderLayout.NORTH);
		userPanel.add(new JScrollPane(userList), BorderLayout.CENTER);

		topPanel.setLayout(new FlowLayout());
		topPanel.add(new JLabel("���O"));
		topPanel.add(nameTextField);
		topPanel.add(renameButton);
		topPanel.add(addRoomButton);

		nameTextField.setPreferredSize(new Dimension(200, nameTextField.getPreferredSize().height));

		leftPanel.setLayout(new GridLayout(2, 1));
		leftPanel.add(roomPanel);
		leftPanel.add(userPanel);

		buttomPanel.setLayout(new BorderLayout());
		buttomPanel.add(msgTextField, BorderLayout.CENTER);
		buttomPanel.add(submitButton, BorderLayout.EAST);

		//�e�L�X�g�G���A�̓��b�Z�[�W��\�����邾���Ȃ̂ŕҏW�s�ɐݒ�
		msgTextArea.setEditable(false);

		//�R���|�[�l���g�̏�Ԃ�ގ���Ԃŏ�����
		exitedRoom();

		this.getContentPane().add(new JScrollPane(msgTextArea), BorderLayout.CENTER);
		this.getContentPane().add(topPanel, BorderLayout.NORTH);
		this.getContentPane().add(leftPanel, BorderLayout.WEST);
		this.getContentPane().add(buttomPanel, BorderLayout.SOUTH);

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try { close(); }
				catch(Exception err) { }
			}
		});
		connectServer();

		//���b�Z�[�W��M�Ď��p�̃X���b�h�𐶐����ăX�^�[�g������
		thread = new Thread(this);
		thread.start();

		//���݂̕������擾����
		sendMessage("getRooms");
	}

	//�T�[�o�[�ɐڑ�����
	public void connectServer() {
		try {
			socket = new Socket(HOST, PORT);
			msgTextArea.append(">�T�[�o�[�ɐڑ����܂���\n"); 
		}
		catch(Exception err) {
			msgTextArea.append("ERROR>" + err + "\n"); 
		}
	}

	//�T�[�o�[����ؒf����
	public void close() throws IOException {
		sendMessage("close");
		socket.close();
	}

	//���b�Z�[�W���T�[�o�[�ɑ��M����
	public void sendMessage(String msg) {
		try {
			OutputStream output = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(output);

			writer.println(msg);
			writer.flush();
		}
		catch(Exception err) { msgTextArea.append("ERROR>" + err + "\n"); }
	}

	//�T�[�o�[���瑗���Ă������b�Z�[�W�̏���
	public void reachedMessage(String name, String value) {
		//�`���b�g���[���̃��X�g�ɕύX��������ꂽ
		if (name.equals("rooms")) {
			if (value.equals("")) {
				roomList.setModel(new DefaultListModel());
			}
			else {
				String[] rooms = value.split(" ");
				roomList.setListData(rooms);
			}
		}
		//���[�U�[�����ގ�����
		else if (name.equals("users")) {
			if (value.equals("")) {
				userList.setModel(new DefaultListModel());
			}
			else {
				String[] users = value.split(" ");
				userList.setListData(users);
			}
		}
		//���b�Z�[�W�������Ă���
		else if (name.equals("msg")) {
			msgTextArea.append(value + "\n"); 
		}
		//�����ɐ�������
		else if (name.equals("successful")) {
			if (value.equals("setName")) msgTextArea.append(">���O��ύX���܂���\n"); 
		}
		//�G���[����������
		else if (name.equals("error")) {
			msgTextArea.append("ERROR>" + value + "\n"); 
		}
	}

	//���b�Z�[�W�Ď��p�̃X���b�h
	public void run() {
		try {
			InputStream input = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			while(!socket.isClosed()) {
				String line = reader.readLine();

				String[] msg = line.split(" ", 2);
				String msgName = msg[0];
				String msgValue = (msg.length < 2 ? "" : msg[1]);

				reachedMessage(msgName, msgValue);
			}
		}
		catch(Exception err) { }
	}

	//�{�^���������ꂽ�Ƃ��̃C�x���g����
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();

		if(cmd.equals("submit")) {	//���M
			sendMessage("msg " + msgTextField.getText());
			msgTextField.setText("");
		}
		else if(cmd.equals("rename")) {	//���O�̕ύX
			sendMessage("setName " + nameTextField.getText());
		}
		else if(cmd.equals("addRoom")) {	//�������쐬
			String roomName = nameTextField.getText();
			sendMessage("addRoom " + roomName);
			enteredRoom(roomName);
			sendMessage("getUsers " + roomName);
		}
		else if(cmd.equals("enterRoom")) {	//����
			Object room = roomList.getSelectedValue();
			if (room != null) {
				String roomName = room.toString();
				sendMessage("enterRoom " + roomName);
				enteredRoom(roomName);
			}
		}
		else if(cmd.equals("exitRoom")) {	//�ގ�
			sendMessage("exitRoom " + roomName);
			exitedRoom();
		}
	}

	//�����ɓ������Ă����Ԃ̃R���|�[�l���g�ݒ�
	private void enteredRoom(String roomName) {
		this.roomName = roomName;
		setTitle(APPNAME + " " + roomName);

		msgTextField.setEnabled(true);
		submitButton.setEnabled(true);

		addRoomButton.setEnabled(false);
		enterRoomButton.setText("�ގ�");
		enterRoomButton.setActionCommand("exitRoom");
	}

	//�����ɓ������Ă��Ȃ���Ԃ̃R���|�[�l���g�ݒ�
	private void exitedRoom() {
		roomName = null;
		setTitle(APPNAME);

		msgTextField.setEnabled(false);
		submitButton.setEnabled(false);

		addRoomButton.setEnabled(true);
		enterRoomButton.setText("����");
		enterRoomButton.setActionCommand("enterRoom");
		userList.setModel(new DefaultListModel());
	}
}