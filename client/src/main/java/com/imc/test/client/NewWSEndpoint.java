package com.imc.test.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
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
public class NewWSEndpoint {

    @OnMessage
    public void onMessage(ByteBuffer message, Session session) throws IOException {
        System.out.println("Hello from server: " + message);
        session.close();
        System.exit(0);
    }

    public static void main(String[] args) throws Exception{
        new Thread(() -> {
            try {
                ClientManager client = ClientManager.createClient();
                final String ipAndPort = "localhost:8080";
                final String url = "ws://" + ipAndPort + "/server/endpoint";
                Session server = client.connectToServer(NewWSEndpoint.class, new URI(url));
                server.getBasicRemote().sendBinary(ByteBuffer.wrap(new byte[0]));
            } catch (URISyntaxException | IOException | DeploymentException ex) {
                Logger.getLogger(NewWSEndpoint.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
        Thread.sleep(10000);
    }

}
