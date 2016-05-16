package onchat;

import java.net.*;
import java.io.*;

// Transmitter
class ChatStringTransmitter implements Runnable {
	// InputStream src = null;
	OutputStream dist = null;

	// コンストラクタで受け取る
	public ChatStringTransmitter(OutputStream out){
		dist = out;
	}

	public void run(){
		byte[] buff = new byte[1024];
		while (true) {
			try {
				// int strLen = src.read(buff);
				String sendStr = Client.getTextFieldStrings();
				buff = sendStr.getBytes();
				int strLen = sendStr.getBytes().length;
				
				if(sendStr.length() > 0){
					Client.chatWrite("send message to the server. sendStr = " + sendStr);
					Client.chatWrite("strLen = " + strLen);
				}

				if (strLen > 0)
					dist.write(buff, 0, strLen);
				
			}
			catch(Exception e){
				e.printStackTrace();
				System.err.print(e);
				System.exit(1);
			}
		}
	}
}


// receiver
class ChatStringReceiver implements Runnable {
	InputStream src = null;
	// OutputStream dist = null;

	// コンストラクタで受け取る
	public ChatStringReceiver(InputStream in){
		src = in;
	}

	public void run(){
		byte[] buff = new byte[1024];
		while (true) {
			try {
				int n = src.read(buff);
				if (n > 0){
					String receiveStr = new String(buff, "UTF-8");
					Client.chatWrite(receiveStr);
				}
			}
			catch(Exception e){
				e.printStackTrace();
				System.err.print(e);
				System.exit(1);
			}
		}
	}
}