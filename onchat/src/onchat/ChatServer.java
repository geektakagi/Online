package onchat;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer 
{
	static final int DEFAULT_PORT = 10000;
	static Vector connections;

	static Hashtable userTable = null;
	static random = null;

	// クライアントとの接続をVectorオブジェクトconnectionsに登録
	public static void addConnection(Socket sock){
		if (connections == null) {
			connections = new Vector();	
		}
		connections.addElement(sock);
	}

	// クライアントとの接続をconnectionsから削除
	public static void deleteConnection(Socket sock) {
		if (connections != null) {
			connections.removeElement(sock);
		}
	}

	public static void loginUser(String name) {
		if (userTable == null) {
			userTable = new Hashtable();
		}

	}

	// クライアントにメッセージを送る
	public static void sendAll(String str) {
		if (connections != null ) {
			// すべてのconnectionに対して出力
			for (Enumeration enu = connections.elements(); enu.hasMoreElements() ; ) {
				try {
					PrintWriter out = new PrintWriter(((Socket) enu.nextElement()).getOutputStream());
					out.println(str);
					out.flush();
				} catch (IOException ex) {	}
			}	
		}
		System.out.println(str);
	}

	// サーバソケットの作成とクライアント接続の処理
	public static void main(String[] args) {
		int port = DEFAULT_PORT;
		if (args.length > 0) 
			port = Integer.parseInt(args[0]);

		try {
			serverSocket = new ServerSocket(DEFAULT_PORT);
		} catch (IOException e)	{
			System.err.println("Server Socketを作成できませんでした");
			System.exit(1);
		}

		while (true) {
			try {
				// コネクションの登録
				Socket cs = serverSocket.accept();
				addConnection(cs);

				// クライアントの処理スレッド
				Thread ct = new Thread(new ClientProc(cs));
				ct.start();

			} catch (IOException e) {
				System.err.println("クライアントの接続エラーです。")
			}
		}
	}
}

class ClientProc implements Runnable {
	Socket sock;		// クライアント接続用ソケット
	BufferdReader in;	// 入力
	PrintWriter out;	// 出力
	String name = null;	// クライアントの名前

	// コンストラクタ
	public clientProc(Socket sock) throws IOException {
		this.sock = sock;
		in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		out = new PrintWriter(sock.getOutputStream());
	}

	// メイン処理
	public void run(){
		try {
			while (name == null){
				out.print("お名前は？: ");
				out.flush();
				name = in.readLine();
			}
			String line = in.readLine();
			while (!"quit".equals(line)){
				ChatServer.sendAll(name + "> " +line);
				line = in.readLine();
			}
			ChatServer.deleteConnection(sock);
			sock.close();
		}catch (IOException e){
			try {
				sock.close();
			}catch (IOException e2){}
		}
	}
}