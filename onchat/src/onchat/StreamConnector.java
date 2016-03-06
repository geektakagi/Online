package onchat;

import java.net.*;
import java.io.*;

// StreamConnectorクラス
// ストリームを受け取り,両者を結合してデータを受け渡します
// StreamConnectorクラスはスレッドを構成するためのクラスです
class StreamConnector implements Runnable {
	InputStream src = null;
	OutputStream dist = null;

	// コンストラクタ入出力ストリームを受け取ります
	public StreamConnector(InputStream in, OutputStream out){
		src = in;
		dist = out;
	}
	// 処理の本体
	// ストリームの読み書きを無限に繰り返します
	public void run(){
		byte[] buff = new byte[1024];
		while (true) {
			try {
				int n = src.read(buff);
				if (n > 0)
					dist.write(buff, 0, n);
			}
			catch(Exception e){
				e.printStackTrace();
				System.err.print(e);
				System.exit(1);
			}
		}
	}
}