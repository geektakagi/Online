import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketClient {
	final static String HOST = "10.28.232.238"; // �ڑ���A�h���X
	final static int    PORT = 8001;        // �ڑ���|�[�g�ԍ�

	public static void main(String[] args) {
		String filepath = "a.txt";             // ���M����t�@�C���̃p�X
		File   file     = new File(filepath); // ���M����t�@�C���̃I�u�W�F�N�g
		System.out.println("���M����t�@�C����F��");
		byte[] buffer   = new byte[512];      // �t�@�C�����M���̃o�b�t�@

		try {
			// �\�P�b�g�̏���
			Socket socket = new Socket(HOST, PORT);
			System.out.println("�\�P�b�g�̏�������");
			// �X�g���[���̏���
			InputStream  inputStream  = new FileInputStream(file);
			OutputStream outputStream = socket.getOutputStream();
			System.out.println("�X�g���[���̏�������");
			// �t�@�C�����X�g���[���ő��M
			int fileLength;
			while ((fileLength = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, fileLength);
			}
			System.out.println("�t�@�C���𑗐M");

			// �I������
			outputStream.flush();
			outputStream.close();
			inputStream.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}