package com.imc.test.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.ClientEndpoint;
import javax.websocket.DeploymentException;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import org.glassfish.tyrus.client.ClientManager;

/**
 *
 * @author lysenko
 */
@ClientEndpoint
public class OnOpenPrintClient {

    @OnMessage
    public void onMessage(ByteBuffer message, Session session) throws IOException {
        System.out.println("From server: " + new String(message.array()));
    }

    public static void main(String[] args) throws Exception{
        CountDownLatch latch = new CountDownLatch(1);
        new Thread(() -> {
            try {
                final String ipAndPort = "localhost:8080";
                final String url = "ws://" + ipAndPort + "/server/on_open_print";
                ClientManager client = ClientManager.createClient();
                for (int i = 0; i < 10; i++) {
                    Session server = client.connectToServer(OnOpenPrintClient.class, new URI(url));
                    server.getBasicRemote().sendBinary(ByteBuffer.wrap("Works".getBytes()));
                    Thread.sleep(1000);
                    server.close();
                    System.out.println("i: " + i);
                }
                latch.countDown();
            } catch (URISyntaxException | IOException | InterruptedException |DeploymentException ex) {
                Logger.getLogger(NewWSEndpoint.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
        latch.await();
    }

}