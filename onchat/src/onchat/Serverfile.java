<<<<<<< HEAD

=======
﻿package onchat;
>>>>>>> master
// チャットサーバChatServer.java
// このプログラムは,チャットのサーバプログラムです
// 使い方java ChatServer [ポート番号]
// ポート番号を省略すると,ポート番号6000 番を使います
// 起動の例java ChatServer
// 終了にはコントロールC を入力してください
//2回目
// このサーバへの接続にはTelnet.javaなどを使ってください
// 接続を止めたいときには,行頭でquitと入力してください

// ライブラリの利用
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.Vector;
<<<<<<< HEAD
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Serverfile
{
	public static void main(String[] args)
	{
					System.out.println("サーバーが起動しました");
		Thread Chat = new ChatServer(args[0]);
		Chat.start();
		System.out.println("チャットサーバーが起動しました");
		
		Thread Fileserver = new SocketServer();
		Fileserver.start();
		System.out.println("ファイルサーバーが起動しました");
	}
}


class SocketServer extends Thread 
{
	final static int PORT = 8001;	// 待ちうけポート番号

	public void run() 
	{
		System.out.println("ファイル受信待機");
		String outputFilepath = "f.txt";       // 受信先ファイルの保存先
		byte[] buffer         = new byte[512]; // ふぁいるじゅしんのばっふぁ
		System.out.println("test");
		try {
				//そけっとの準備
				ServerSocket serverSocket = new ServerSocket(PORT);
				Socket       socket       = serverSocket.accept();
				System.out.println("ソケットの準備");
				// ストリームのゆんび
				InputStream  inputStream  = socket.getInputStream();
				OutputStream outputStream = new FileOutputStream(outputFilepath);
				System.out.println("ストリームの準備完了");
				System.out.println("受信");
				// ファイルをストリームにて送信
				int fileLength;
				while ((fileLength = inputStream.read(buffer)) > 0) 
				{
					outputStream.write(buffer, 0, fileLength);
					System.out.println("readtest");
				}

			// I—¹ˆ—
				outputStream.flush();
				outputStream.close();
				inputStream.close();
				socket.close();
				serverSocket.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			System.out.println("ファイル受信完了");
			SocketClient sousinn = new SocketClient();
			sousinn.client();
	}	
}

class SocketClient 
{
	final static String HOST = "10.0.9.13"; // 接続先アドレス
	final static int    PORT = 8002;        // 接続先ポート番号

	public static void client() 
	{
		String filepath = "f.txt";             // 送信するファイルのパス
		File   file     = new File(filepath); // 送信するファイルのオブジェクト
		System.out.println("送信するファイルを認識");
		byte[] buffer   = new byte[512];      // ファイル送信時のバッファ

		try 
		{
			// ソケットの準備
			Socket socket = new Socket(HOST, PORT);
			System.out.println("ソケットの準備完了");
			// ストリームの準備
			InputStream  inputStream  = new FileInputStream(file);
			OutputStream outputStream = socket.getOutputStream();
			System.out.println("ストリームの準備完了");
			// ファイルをストリームで送信
			int fileLength;
			while ((fileLength = inputStream.read(buffer)) > 0) 
			{
				outputStream.write(buffer, 0, fileLength);
			}
			System.out.println("ファイルを送信");
=======
>>>>>>> master


	// mainメソッド
	// サーバソケットを作り,クライアントからの接続を待ち受けます
// ChatServerクラス
public class ChatServer {
	static final int DEFAULT_PORT = 4820;//ポート番号省略時は6000 番を使います
	static ServerSocket serverSocket;
	static Vector<Socket> connections;


	public static void main(String[] arg){

		int port = DEFAULT_PORT ;
		if (arg.length > 0) port = Integer.parseInt(arg[0]) ;
		
		System.err.println("Server Starting...");

		try
		{
			serverSocket = new ServerSocket(port);
		}

		catch (IOException e)
		{
			System.err.println(e);
			System.exit(1);
		}
		
		Thread com = new Thread(new console());
		com.start();
		System.err.println("Server Started");
		
		while (true)
		{
			try
			{
				Socket cs = serverSocket.accept();
				addConnection(cs);
				Thread ct = new Thread(new clientProc(cs));
				ct.start();
			}
			catch (IOException e)
			{
				System.err.println(e);
			}
		}
	}

	// sendAllメソッド
	// 各クライアントにメッセージを送ります
	public static void sendAll(String s)
	{
		if (connections != null)
		{// コネクションがあれば実行します
			for (Enumeration<Socket> e = connections.elements();
					e.hasMoreElements() ;)
			{
				try
				{
					String sendStr= new String(s.getBytes("UTF-8"), "UTF-8");
					PrintWriter pw = new PrintWriter(((Socket) e.nextElement()).getOutputStream());
					pw.println(sendStr);
					pw.flush();
				}
				catch (IOException ex){}
			}
		}
		System.out.println(s);
	}

	// addConnectionメソッド 
	// クライアントとの接続を追加します

	public static void addConnection(Socket s)
	{
		if (connections == null)
		{
			connections = new Vector<Socket>();
		}
		connections.addElement(s);
		
		System.err.println("new connection added");
	}

	// deleteConnectionメソッド
	// あるクライアントとのコネクションを削除します

	public static void deleteConnection(Socket s)
	{
		if (connections != null)
		{
			connections.removeElement(s);
			System.err.println("connection deleted:" + s);
		}
	}

<<<<<<< HEAD
		public static Vector<Socket> getConnections()
=======
	public static Vector<Socket> getConnections()
>>>>>>> master
	{
		return connections;
	}

}

// clientProcクラス
// クライアント処理用スレッドのひな形です

class clientProc implements Runnable
{
	Socket s;
	BufferedReader in;
	PrintWriter out;
	String name = null;
	ChatServer server = null ;

	//コンストラクタ
	public clientProc(Socket s) throws IOException
		{
			this.s = s;
			in = new BufferedReader(new InputStreamReader(
			  s.getInputStream()));
			out = new PrintWriter(s.getOutputStream());
		}

	// スレッドの本体
	// 各クライアントとの接続処理を行います
	public void run()
	{
		try {
			while (name == null)
			{
				String sendStr = "あなたのお名前は？";
				sendStr = new String(sendStr.getBytes("UTF-8"), "UTF-8");
				out.println(sendStr);
				out.flush();
				name = in.readLine();
			}
			
			String sendStr = "welcome " + name;
			sendStr = new String(sendStr.getBytes("UTF-8"), "UTF-8");
			ChatServer.sendAll(sendStr);

			String line = in.readLine();
			while (!"quit".equals(line))
			{
				sendStr = name + "> " + line;
				sendStr = new String(sendStr.getBytes("UTF-8"), "UTF-8");
				ChatServer.sendAll(sendStr);
				line = in.readLine();
			}
			ChatServer.deleteConnection(s);
			s.close();
		}
		catch (IOException e)
		{
			try
			{
				s.close();
			}
			catch (IOException e2){}
		}
	}

/*
	
	private void deleteConnection()
	{
		try 
		{
			ChatServer.deleteConnection(s);
			s.close();
			
		}
		catch (IOException e){}
	}
	
	public static void deleteallconnections(){
		deleteConnection();
	}
	
	
	public static void deleteallconnections()
	{
		if (connections != null)
		{// コネクションがあれば実行します
			for (Enumeration e = connections.elements();
					e.hasMoreElements() ;)
			{
					connections.removeElement(s);
			}
		}
	}
	*/
	

}
//コマンド

class console implements Runnable
	{
		public void run(){
			Scanner scan = new Scanner(System.in);
			String com = scan.next();


			while(!"exit".equals(com))
			{
				switch(com)
				{

					case "list":
					{
						System.out.println("list");
						Vector<Socket> connections = ChatServer.getConnections();
						if (connections != null)
						{ // コネクションがあれば実行します
<<<<<<< HEAD
							for (Enumeration<Socket> e = connections.elements();
									e.hasMoreElements() ;)
							{
								System.out.println(e.nextElement());								
							}
=======
							int connectionCount = 0;
							for (Enumeration<Socket> e = connections.elements();
									e.hasMoreElements() ;)
							{
								System.out.println(e.nextElement());
								connectionCount++;
							}
							System.out.println("Connections : " + connectionCount);
>>>>>>> master
						} 
						else 
						{
							System.out.println("No connection.");
						}		
						break;
					}

					case "help":
					{
						System.out.println("list : display all connections.");	
						System.out.println("help : display this help.");
<<<<<<< HEAD
						System.out.println("chatport : display port");
						break;
					}
					case "chatport":
					{
						System.out.println("用意中");
=======
>>>>>>> master
						break;
					}

					default:
					{
						System.out.println("unknown command");
						System.out.println("show help use \"help\" command ");
					}
					
				}
				
				com=scan.next();
			}
			scan.close();
			//clientProc.deleteallconnections();
			System.out.println("Server Closed.");
			System.exit(0);
		}
<<<<<<< HEAD
}
=======
	}
>>>>>>> master
