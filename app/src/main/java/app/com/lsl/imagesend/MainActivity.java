package app.com.lsl.imagesend;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private Button but0,but1;
    public static ImageView iv;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        but0 = (Button) findViewById(R.id.but_send);
        but1 = (Button) findViewById(R.id.but_re);
        iv = (ImageView) findViewById(R.id.iv);
        tv = (TextView) findViewById(R.id.tv);

        but0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(networkTask).start();
            }
        });

        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new ClientTest();
                    }
                }).start();

            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            iv.setImageBitmap((Bitmap) msg.obj);
            Bundle data = msg.getData();
            String str = data.getString("value");
            tv.setText("请求结果为：" + str);
        }
    };


    Socket socket = null;

    Runnable networkTask = new Runnable() {
        @Override
        public void run() {

            try {
                socket = new Socket("192.168.1.100",54321);

                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);

                PrintWriter pw = new PrintWriter(
                        new BufferedWriter(
                                new OutputStreamWriter(socket.getOutputStream(),"utf-8")),true);
                pw.println("request_commit");

                Message msg = new Message();
                Bundle data = new Bundle();
                data.putString("value","请求成功！！！");
                msg.setData(data);
                msg.obj = bitmap;
                handler.sendMessage(msg);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                //
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte [] bytes = baos.toByteArray();

                dos.write(bytes);

                Log.e("socket:====", String.valueOf(socket));

                dos.close();
                socket.close();

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

}
