package app.com.lsl.imagesend;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/** socket util辅助类
 * Created by M1308_000 on 2017/3/29.
 */

public class ClientSocket {
    private String ip;

    private int port;

    private Socket socket = null;

    DataOutputStream dos = null;

    DataInputStream dis = null;

    public ClientSocket(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    /**
     * 创建socket连接
     */
    public void CreateConnection() throws Exception {
        try {
            socket = new Socket(ip,port);
            // 创建文本输出流对象
            PrintWriter pw = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream(),"utf-8")),true);
            // 发送请求码
            pw.println("request_download");

        } catch (Exception e) {
            e.printStackTrace();
            if (socket != null)
                socket.close();
            throw e;
        } finally {
        }
    }

    public void sendMessage(String sendMessage) throws Exception {
        try {
            dos = new DataOutputStream(socket.getOutputStream());
            if (sendMessage.equals("request0")) {
                dos.writeByte(0x1);
                dos.flush();
                return;
            }
            if (sendMessage.equals("request1")) {
                dos.writeByte(0x2);
                dos.flush();
                return;
            }
            if (sendMessage.equals("request2")) {
                dos.writeByte(0x3);
                dos.flush();
                return;
            } else {
                dos.writeUTF(sendMessage);
                dos.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (dos != null)
                dos.close();
            throw e;
        } finally {
        }
    }

    public DataInputStream getMessageStream() throws Exception {
        try {
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            return dis;
        } catch (Exception e) {
            e.printStackTrace();
            if (dis != null)
                dis.close();
            throw e;
        } finally {
        }
    }

    public void shutDownConn() {
        try {
            if (dos != null)
                dos.close();
            if (dis != null)
                dis.close();
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
