import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;

/**
 * Created by 桢 on 2017/2/27.
 */
public class ChatClient extends Frame {
    // this is written on GitHub Websit. I want to check Branches function.
    TextField tfTxt = new TextField();
    TextArea taContent = new TextArea();
    String str_of_ta = "";
    Socket s = null;
    DataOutputStream dos = null;
    boolean beConnected = false;


    static final int WIDTH = 300;
    static final int HEIGHT = 300;

    public static void main(String[] args) {
        ChatClient chatClient = new ChatClient();
        chatClient.launchFrame();
        chatClient.receive();
    }

    public void launchFrame() {
        setLocation(400, 300);
        this.setSize(WIDTH, HEIGHT);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
                disconnect();
            }
        });
        add(tfTxt, BorderLayout.SOUTH);
        add(taContent, BorderLayout.NORTH);
        pack();
        tfTxt.addActionListener(new TFListener());
        setVisible(true);
        connect();
    }

    public void receive() {
        ClientReceiver clientReceiver = new ClientReceiver(s);
        new Thread(clientReceiver).start();
    }

    public void connect() {
        try {
            s = new Socket("127.0.0.1", 8888);
            System.out.println("connected");
            beConnected = true;
            dos = new DataOutputStream(s.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String string) {
        try {
            dos.writeUTF(string);
            dos.flush();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            dos.close();
            s.close();
            beConnected = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class TFListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String str = tfTxt.getText().trim();
//            str_of_ta=str_of_ta+str+'\n';
//            taContent.setText(str_of_ta);
            send(str);
            tfTxt.setText("");
        }
    }

    private class ClientReceiver implements Runnable {
        Socket s;
        DataInputStream dis = null;
        String str = null;

        public ClientReceiver(Socket s) {
            this.s = s;
        }

        @Override

        public void run() {
            try {
                dis = new DataInputStream(s.getInputStream());
                while (beConnected) {
                    str = dis.readUTF();
                    str_of_ta = str_of_ta + str + '\n';
                    taContent.setText(str_of_ta);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
