package app.com.lsl.imagesend;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;

/** 请求服务器数据
 * Created by M1308_000 on 2017/3/29.
 */

public class ClientTest {
    private ClientSocket cs = null;

    private String ip = "192.168.1.100";

    private int port = 54321;

    private String sendMsg = "request0";

    public ClientTest(){
        try {
            if (createConn()) {
                sendMessage();
                getMessage();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage () {
        if (cs == null)
            return;
        try {
            cs.sendMessage(sendMsg);
        } catch (Exception e) {
            Log.e("Err:","消息发送失败！");
        }
    }

    private void getMessage() {
        if (cs == null)
            return;
        DataInputStream inputStream = null;
        try {
            inputStream = cs.getMessageStream();
        } catch (Exception e) {
            Log.e("Err:", "接收消息缓存错误");
            return;
        }
        try {
            // 保存本地路径。文件名会自动继承服务器端而来
            String savePath = "storage/emulated/0/Images/";
            int bufferSize = 8192;
            byte [] buf = new byte[bufferSize];
            int passedlen = 0;
            long len = 0;

            savePath +=  inputStream.readUTF();
            DataOutputStream dos = new DataOutputStream(
                    new BufferedOutputStream(
                            new BufferedOutputStream(
                                    new FileOutputStream(savePath))));
            len = inputStream.readLong();
            Log.e("fileLen:", "" + len);
            Log.e("Accept....", "start");

            while (true) {
                int read = 0;
                if (inputStream != null) {
                    read = inputStream.read(buf);
                }
                passedlen += read;
                if (read == -1)
                    break;

                Log.e("AcceptIng....", "" + passedlen * 100 / len + "%");

                dos.write(buf, 0, read);
            }
            Log.e("Accept....", "Success文件存为：" + savePath);

            dos.close();

        } catch (Exception e) {
            Log.e("Err:", "接收消息错误！！！" + e.getMessage());
            return;
        }
    }

    private boolean createConn() {
        cs = new ClientSocket(ip, port);
        try {
            cs.CreateConnection();
            Log.e("Conn...","Success");
            return true;
        } catch (Exception e) {
            Log.e("Conn...","Err");
            return false;
        }
    }
}
