import java.net.*;
import java.util.*;
import java.io.*;

public class ChatServer {
	public static void main(String[] args) {
		ChatServer application = ChatServer.getInstance();
		application.start();
	}

	//�T�[�o�[�̓V���O���g���݌v�B�B��̃C���X�^���X
	private static ChatServer instance;
	public static ChatServer getInstance() {
		if (instance == null) {
			instance = new ChatServer();
		}
		return instance;
	}

	//�T�[�o�[�\�P�b�g
	private ServerSocket server;

	//���݊J���Ă��镔���I�u�W�F�N�g�̓��I�z��
	private ArrayList<ChatRoom> roomList;

	//���݃`���b�g�ɎQ�����Ă���S���[�U�[�̓��I�z��
	private ArrayList<ChatClientUser> userList;

	private ChatServer() {
		roomList = new ArrayList<ChatRoom>();
		userList = new ArrayList<ChatClientUser>();
	}

	//main ���\�b�h����Ăяo�����
	public void start() {
		try {
			server = new ServerSocket(2815);

			while(!server.isClosed()) {
				//�V�����N���C�A���g�̐ڑ���҂�
				Socket client = server.accept();

				//���[�U�[�I�u�W�F�N�g�𐶐�����
				ChatClientUser user = new ChatClientUser(client);
				addUser(user);
			}
		}
		catch(Exception err) {
			err.printStackTrace();
		}
	}

	//�`���b�g���[����ǉ�����
	public void addChatRoom(ChatRoom room) {
		if (roomList.contains(room)) return;

		roomList.add(room);
System.out.println("addRoom=[" + room + "]");

		//���ׂẴ��[�U�[�Ƀ`���b�g���[�����X�V���ꂽ���Ƃ�ʒm����
		for(int i = 0 ; i < userList.size() ; i++) {
			userList.get(i).reachedMessage("getRooms" , "");
		}
	}
	//�w�肵�����O�̃`���b�g���[�����擾����
	public ChatRoom getChatRoom(String name) {
		for(int i = 0 ; i < roomList.size() ; i++) {
			ChatRoom room = roomList.get(i);
			if (name.equals(room.getName())) return room;
		}
		return null;
	}
	//�`���b�g���[���̔z����擾����
	public ChatRoom[] getChatRooms() {
		ChatRoom[] result = new ChatRoom[roomList.size()];

		//���݊J����Ă��镔���̃��X�g��z��Ɉڂ�
		for(int i = 0 ; i < roomList.size() ; i++) {
			result[i] = roomList.get(i);
		}
		return result;
	}
	//�`���b�g���[�����폜����
	public void removeChatRoom(ChatRoom room) {
		roomList.remove(room);
System.out.println("removeRoom=[" + room + "]");

		//���ׂẴ��[�U�[�Ƀ`���b�g���[�����X�V���ꂽ���Ƃ�ʒm����
		for(int i = 0 ; i < userList.size() ; i++) {
			userList.get(i).reachedMessage("getRooms" , "");
		}
	}
	//���ׂẴ`���b�g���[�����폜����
	public void clearChatRoom() { 
		roomList.clear();

		//���ׂẴ��[�U�[�Ƀ`���b�g���[�����X�V���ꂽ���Ƃ�ʒm����
		for(int i = 0 ; i < userList.size() ; i++) {
			userList.get(i).reachedMessage("getRooms" , "");
		}
	}

	//���[�U�[��ǉ�����
	public void addUser(ChatClientUser user) {
		if (userList.contains(user)) return;

		userList.add(user);
System.out.println("addUser=[" + user + "]");
	}

	//�w�肵�����O�̃��[�U�[���擾����
	public ChatClientUser getUser(String name) {
		for(int i = 0 ; i < userList.size() ; i++) {
			ChatClientUser user = userList.get(i);
			if (user.getName().equals(name)) return user;
		}
		return null;
	}

	//���ׂẴ��[�U�[��Ԃ�
	public ChatClientUser[] getUsers() {
		ChatClientUser[] users = new ChatClientUser[userList.size()];
		userList.toArray(users);
		return users;
	}

	//���[�U�[���폜����
	public void removeUser(ChatClientUser user) {
		userList.remove(user);
System.out.println("removeUser=[" + user + "]");

		//�T�[�o�[���烆�[�U�[����������ꍇ�A���Ȃ킿���[�U�[�̓��O�A�E�g����
		//���ׂĂ̕����𒲂ׁA���[�U�[�𕔉�����ސȂ�����
		for(int i = 0 ; i < roomList.size() ; i++) {
			if (roomList.get(i).containsUser(user)) roomList.get(i).removeUser(user);
		}
	}

	//���ׂẴ��[�U�[���폜����
	public void clearUser() { userList.clear(); }

	//�T�[�o�[����Đؒf����
	public void close() throws IOException {
		server.close();
	}
}

//�`���b�g���[���I�u�W�F�N�g
class ChatRoom implements MessageListener {
	//�`���b�g���[���̖��O
	private String name;

	//���̃`���b�g���[���̊Ǘ������������[�U�[
	private ChatClientUser hostUser;

	//���̃`���b�g���[���ɎQ�����Ă���S�Ẵ��[�U�[�̓��I�z��
	//���̔z��ɂ� hostUser ���܂�
	private ArrayList<ChatClientUser> roomUsers;

	public ChatRoom(String name, ChatClientUser hostUser) {
		roomUsers = new ArrayList<ChatClientUser>();

		this.name = name;
		this.hostUser = hostUser;

		addUser(hostUser);
	}

	//���̕����̖��O
	public String getName() {
		return name;
	}

	//���̕������쐬���������̂���N���C�A���g
	public ChatClientUser getHostUser() {
		return hostUser;
	}

	//���̕����Ƀ��[�U�[��ǉ��i�����j����
	public void addUser(ChatClientUser user) {
		user.addMessageListener(this);
		roomUsers.add(user);
		for(int i = 0 ; i < roomUsers.size() ; i++) {
			roomUsers.get(i).reachedMessage("getUsers", name);
			roomUsers.get(i).sendMessage("msg >" + user.getName() + " ���񂪓������܂���");
		}
	}

	//�w�肵�����[�U�[�����̕����ɂ��邩�ǂ���
	public boolean containsUser(ChatClientUser user) {
		return roomUsers.contains(user);
	}

	//���̕����̃��[�U�[�S�����擾����
	public ChatClientUser[] getUsers() {
		ChatClientUser[] users = new ChatClientUser[roomUsers.size()];
		roomUsers.toArray(users);
		return users;
	}

	//�w�肵�����[�U�[���`���b�g���[������ގ�������
	public void removeUser(ChatClientUser user) {
		user.removeMessageListener(this);
		roomUsers.remove(user);
		for(int i = 0 ; i < roomUsers.size() ; i++) {
			roomUsers.get(i).reachedMessage("getUsers", name);
			roomUsers.get(i).sendMessage("msg >" + user.getName() + " ���񂪑ގ����܂���");
		}

		//���[�U�[�����Ȃ��Ȃ����̂ŕ������폜����
		if (roomUsers.size() == 0) {
			ChatServer.getInstance().removeChatRoom(this);
		}
	}

	//���̃`���b�g���[���̃��[�U�[�����b�Z�[�W����������
	public void messageThrow(MessageEvent e) {
		ChatClientUser source = e.getUser();

		//���[�U�[����������
		if (e.getName().equals("msg")) {
			for(int i = 0 ; i < roomUsers.size() ; i++) {
				String message = e.getName() + " " + source.getName() + ">" + e.getValue();
				roomUsers.get(i).sendMessage(message);
			}
		}
		//���[�U�[�����O��ύX����
		else if(e.getName().equals("setName")) {
			for(int i = 0 ; i < roomUsers.size() ; i++) {
				roomUsers.get(i).reachedMessage("getUsers", name);
			}
		}
	}
}

class MessageEvent extends EventObject {
	private ChatClientUser source;
	private String name;
	private String value;

	public MessageEvent(ChatClientUser source, String name, String value) {
		super(source);
		this.source = source;
		this.name = name;
		this.value = value;
	}

	//�C�x���g�𔭐����������[�U�[
	public ChatClientUser getUser() { return source; }

	//���̃C�x���g�̃R�}���h����Ԃ�
	public String getName() { return this.name; }

	//���̃C�x���g��
	public String getValue() { return this.value; }
}

interface MessageListener extends EventListener {
	void messageThrow(MessageEvent e);
}

//�`���b�g���[�U�[�I�u�W�F�N�g
class ChatClientUser implements Runnable, MessageListener {
	//�\�P�b�g
	private Socket socket;

	//���[�U�[�̖��O
	private String name;

	//�`���b�g�T�[�o�[
	private ChatServer server = ChatServer.getInstance();

	//���b�Z�[�W���X�i�̓��I�z��
	//���b�Z�[�W���X�i�͂��̃��[�U�[�����������Ƃ��ɌĂяo�����C�x���g
	private ArrayList<MessageListener> messageListeners;

	public ChatClientUser(Socket socket) {
		messageListeners = new ArrayList<MessageListener>();
		this.socket = socket;

		addMessageListener(this);

		Thread thread = new Thread(this);
		thread.start();
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}

	public void run() {
		try {
			//���[�U�[�̏����擾����
			InputStream input = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));

			//���[�U�[�̃��b�Z�[�W���M���m�F
			while(!socket.isClosed()) {
				String line = reader.readLine();
System.out.println("INPUT=" + line);

				String[] msg = line.split(" ", 2);
				String msgName = msg[0];
				String msgValue = (msg.length < 2 ? "" : msg[1]);

				reachedMessage(msgName, msgValue);
			}
		}
		catch(Exception err) { err.printStackTrace(); }
	}

	//���̃��[�U�[�����M���Ă������b�Z�[�W�C�x���g���󂯂�
	public void messageThrow(MessageEvent e) {
		String msgType = e.getName();
		String msgValue = e.getValue();

		//�ؒf����
		if (msgType.equals("close")) {
			try { close(); }
			catch(IOException err) { err.printStackTrace(); }
		}
		//���O���X�V����
		else if(msgType.equals("setName")) {
			String name = msgValue;

			//���p�����͎g���Ȃ��L��
			if (name.indexOf(" ") == -1) {
				String before = getName();
				setName(name);
				sendMessage("successful setName");
				reachedMessage("msg", before + " ���� " + name + " �ɖ��O��ύX���܂���");
			}
			else {
				sendMessage("error ���O�ɔ��p�󔒕������g�����Ƃ͂ł��܂���");
			}
		}
		//�V����������ǉ�����
		else if(msgType.equals("addRoom")) {
			String name = msgValue;

			//���p�����͎g���Ȃ��L��
			if (name.indexOf(" ") == -1) {
				ChatRoom room = new ChatRoom(name , this);
				server.addChatRoom(room);
				sendMessage("successful addRoom");
			}
			else sendMessage("error ���O�ɔ��p�󔒕������g�����Ƃ͂ł��܂���");
		}
		//���ݑ��݂��镔����Ԃ�
		else if(msgType.equals("getRooms")) {
			String result = "";
			ChatRoom[] rooms = server.getChatRooms();
			for(int i = 0 ; i < rooms.length ; i++) {
				result += rooms[i].getName() + " ";
			}
			sendMessage("rooms " + result);
		}
		//�����ɓ���
		else if(msgType.equals("enterRoom")) {
			ChatRoom room = server.getChatRoom(msgValue);
			if (room != null) {
				room.addUser(this);
				sendMessage("successful enterRoom");
			}
			else sendMessage("error \"" + msgValue + "\" ��������܂���");
		}
		//��������o��
		else if(msgType.equals("exitRoom")) {
			ChatRoom room = server.getChatRoom(msgValue);
			if (room != null) {
				room.removeUser(this);
				sendMessage("successful exitRoom");
			}
			else sendMessage("error \"" + msgValue + "\" ��������܂���");
		}
		//�w�肵�������̃��[�U�[�̃��X�g��Ԃ�
		else if(msgType.equals("getUsers")) {
			ChatRoom room = server.getChatRoom(msgValue);
			if (room != null) {
				String result = "";
				ChatClientUser[] users = room.getUsers();
				for(int i = 0 ; i < users.length ; i++) {
					result += users[i].getName() + " ";
				}
				sendMessage("users " + result);
			}
		}
	}

	public String toString() {
		return "NAME=" + getName();
	}

	public void close() throws IOException {
		server.removeUser(this);
		messageListeners.clear();
		socket.close();
	}

	//���̃��[�U�[�Ɏw�肳�ꂽ���b�Z�[�W�𑗐M����
	public void sendMessage(String message) {
		try {
			OutputStream output = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(output);

			//���b�Z�[�W�̑��M
			writer.println(message);

			writer.flush();
		}
		catch(Exception err) {
		}
	}
	//���̃��[�U�[���󂯎�������b�Z�[�W����������
	public void reachedMessage(String name, String value) {
		MessageEvent event = new MessageEvent(this, name, value);
		for(int i = 0 ; i < messageListeners.size() ; i++ ) {
			messageListeners.get(i).messageThrow(event);
		}
	}

	//���̃I�u�W�F�N�g�Ƀ��b�Z�[�W���X�i��o�^����
	public void addMessageListener(MessageListener l) {
		messageListeners.add(l);
	}

	//�w�肵�����b�Z�[�W���X�i�����̃I�u�W�F�N�g�����������
	public void removeMessageListener(MessageListener l) {
		messageListeners.remove(l);
	}

	//���̃I�u�W�F�N�g�ɓo�^����Ă��郁�b�Z�[�W���X�i�̔z���Ԃ�
	public MessageListener[] getMessageListeners() {
		MessageListener[] listeners = new MessageListener[messageListeners.size()];
		messageListeners.toArray(listeners);
		return listeners;
	}
}