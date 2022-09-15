package thread;

import com.example.talk.DemoApplication;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class LivingThread implements Runnable {
    @Override
    public void run() {
        try {
            sleep(1000);
            Socket s = new Socket(DemoApplication.serviceIP, 23432);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
