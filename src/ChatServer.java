import com.sun.security.ntlm.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 桢 on 2017/3/7.
 */
public class ChatServer {
    ServerSocket ss = null;
    boolean started = false;

    List<MyClient> clients = new ArrayList<>();

    public static void main(String[] args) {
        new ChatServer().start();
    }

    public void start() {
        started = true;
        DataInputStream dis = null;
        try {
            ss = new ServerSocket(8888);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            while (started) {
                Socket socket = ss.accept();
                MyClient client = new MyClient(socket);
                new Thread(client).start();
                clients.add(client);
            }
        } catch (Exception e) {
            System.out.println("Client closed");
        } finally {
            try {
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class MyClient implements Runnable {
        private Socket s;
        private DataInputStream dis = null;
        private DataOutputStream dos = null;
        boolean beconnect = false;

        public MyClient(Socket s) {
            this.s = s;
        }

        @Override
        public void run() {

            beconnect = true;
            System.out.println("a client connected");
            try {
                dis = new DataInputStream(s.getInputStream());
                dos = new DataOutputStream(s.getOutputStream());
                beconnect = true;

                while (beconnect) {
                    String str = dis.readUTF();
                    System.out.println(str);
                    for (int i = 0; i < clients.size(); i++) {
                        MyClient c = clients.get(i);

                        c.send(str);
                    }
                }
            } catch (Exception e) {

                System.out.println("Client closed");
            } finally {
                try {
                    if (dis != null) dis.close();
                    if (dos != null) dos.close();
                    if (s != null) s.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }


        public void send(String str) {
            try {
                dos.writeUTF(str);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
