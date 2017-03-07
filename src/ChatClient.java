import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by 桢 on 2017/2/27.
 */
public class ChatClient extends Frame{

    static final int WIDTH=600;
    static final int HEIGHT=600;
    public static void main(String[] args){
        new ChatClient().launchFrame();
    }

    public void launchFrame(){
        setLocation(400,300);
        this.setSize(WIDTH,HEIGHT);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setVisible(true);
    }
}
