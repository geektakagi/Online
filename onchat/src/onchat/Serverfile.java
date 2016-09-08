
// チャットサーバChatServer.java
// このプログラムは,チャットのサーバプログラムです
// 使い方java ChatServer [ポート番号]
// ポート番号を省略すると,ポート番号6000 番を使います
// 起動の例java ChatServer
// 終了にはコントロールC を入力してください

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
		if (args.size() != 0) {
			Thread Chat = new ChatServer(args[0]);
		}
		else {
			Thread Chat = new ChatServer();
		}
		
		Chat.start();
		
		Thread Fileserver = new SocketServer();
		Fileserver.start();
	}
}


class SocketServer extends Thread 
{
	final static int PORT = 8001;	// 待ちうけポート番号

	public static void main(String[] args) 
	{
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
	}
}

class SocketClient 
{
	final static String HOST = "10.28.232.238"; // 接続先アドレス
	final static int    PORT = 8001;        // 接続先ポート番号

	public static void main(String[] args) 
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

			// 終了処理
			outputStream.flush();
			outputStream.close();
			inputStream.close();
			socket.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}

class ChatServer extends Thread 
{
	static final int DEFAULT_PORT = 6000;//ポート番号省略時は6000 番を使います
	static ServerSocket serverSocket;
	static Vector connections;

	String arg;

	ChatServer(String argsa)
	{
		arg = argsa;
	}

	public void run()
	{
		int port = DEFAULT_PORT ;
		port = Integer.parseInt(arg) ;

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
			for (Enumeration e = connections.elements();
					e.hasMoreElements() ;)
			{
				try
				{
					PrintWriter pw = new PrintWriter(((Socket) e.nextElement()).getOutputStream());
					pw.println(s);
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
			connections = new Vector();
		}
		connections.addElement(s);
	}

	// deleteConnectionメソッド
	// あるクライアントとのコネクションを削除します

	public static void deleteConnection(Socket s)
	{
		if (connections != null)
		{
			connections.removeElement(s);
		}
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
				out.print("お名前は？: ");
				out.flush();
				name = in.readLine();
			}

			String line = in.readLine();
			while (!"quit".equals(line))
			{
				ChatServer.sendAll(name + "> " +line);
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
	
	
	private void deleteConnection()
	{
		try 
		{
			ChatServer.deleteConnection(s);
			s.close();
		}
		catch (IOException e){}
	}
/*
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
					}

					
				}
				
				com=scan.next();
			}
			//clientProc.deleteallconnections();
			System.exit(0);
		}
	}
