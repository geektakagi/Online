package onchat;

import java.net.*;
import java.io.*;

public class Connection {

	Socket serverSocket;//接続用ソケット
	public OutputStream serverOutput;//ネットワーク出力用ストリーム
	public BufferedInputStream serverInput;// 同入力用ストリーム
	String host;// 接続先サーバアドレス
	int port; // 接続先サーバポート番号

	static final int DEFAULT_PORT = 4820;

	// コンストラクタ（１）アドレスとポートの指定がある場合
	public Connection(String host, int port){
		this.host = host;
		this.port = port;
	}

	// コンストラクタ（２）アドレスの指定のみの場合
	public Connection(String host){
		this(host, DEFAULT_PORT); // デフォルトポートで接続
	}

	// TCP コネクションを開いて処理を開始します
	public static void main(String[] args){
		try {
			Connection c = null;
			// 引数の個数によってコンストラクタが異なります
			switch (args.length){
			case 1:// サーバアドレスのみの指定
				c = new Connection(args[0]);
				break;
			case 2:// アドレスとポートの指定
				c = new Connection(args[0], Integer.parseInt(args[1]));
				break;
			default:// 使い方が間違っている場合
				System.out.println(
					"usage: java Connection <host name> {<port number>}");
				return;
			}
			c.openConnection();
			c.main_proc();
		}catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
	}

	public InputStream getServerInputStream() {
		return serverInput;
	}

	public OutputStream getServerOutputStream() {
		return serverOutput;
	}

	// openConnectionメソッド
	//アドレスとポート番号からソケットを作りストリームを作成します
	public void openConnection()
		throws IOException,UnknownHostException
	{
		serverSocket = new Socket(host, port);
		serverOutput = serverSocket.getOutputStream();
		serverInput = new
			BufferedInputStream(serverSocket.getInputStream());
	}

	// main_procメソッド
	// ネットワークとのやりとりをするスレッドをスタートさせます
	public void main_proc()
		throws IOException
	{
		try {			
			ChatStringTransmitter textfieldToSocket 
				= new ChatStringTransmitter(serverOutput);
			ChatStringReceiver socketToTextfield
				= new ChatStringReceiver(serverInput);			

			Thread input_thread = new Thread(textfieldToSocket);
			Thread output_thread = new Thread(socketToTextfield);
			// スレッドを起動します
			input_thread.start();
			output_thread.start();
		}
		catch(Exception e){
			System.err.print(e);
			System.exit(1);
		}
	}	
}
