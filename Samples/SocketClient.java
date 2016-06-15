import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketClient {
	final static String HOST = "10.28.232.238"; // 接続先アドレス
	final static int    PORT = 8001;        // 接続先ポート番号

	public static void main(String[] args) {
		String filepath = "a.txt";             // 送信するファイルのパス
		File   file     = new File(filepath); // 送信するファイルのオブジェクト
		System.out.println("送信するファイルを認識");
		byte[] buffer   = new byte[512];      // ファイル送信時のバッファ

		try {
			// ソケットの準備
			Socket socket = new Socket(HOST, PORT);
			System.out.println("ソケットの準備完了");
			// ストリームの準備
			InputStream  inputStream  = new FileInputStream(file);
			OutputStream outputStream = socket.getOutputStream();
			System.out.println("ストリームの準備完了");
			// ファイルをストリームで送信
			int fileLength;
			while ((fileLength = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, fileLength);
			}
			System.out.println("ファイルを送信");

			// 終了処理
			outputStream.flush();
			outputStream.close();
			inputStream.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}