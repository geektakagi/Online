import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {
	final static int PORT = 8001;	// 待ちうけポート番号

	public static void main(String[] args) {
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
			while ((fileLength = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, fileLength);
			System.out.println("readtest");
			}

			// I—¹ˆ—
			outputStream.flush();
			outputStream.close();
			inputStream.close();
			socket.close();
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}