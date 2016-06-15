package onchat;

import java.io.*;

// Transmitter
class ChatStringTransmitter implements Runnable {
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
				
				if(sendStr.length() > 0){
					InputStream src = new ByteArrayInputStream(sendStr.getBytes("utf-8"));
					int strLen = src.read(buff);
					dist.write(buff, 0, strLen);
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


// receiver
class ChatStringReceiver implements Runnable {
	InputStream src = null;

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
					String receiveStr = new String(buff, 0, n, "UTF-8");
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