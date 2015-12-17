package com.imc.test.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import javax.websocket.EndpointConfig;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author lysenko
 */

@ServerEndpoint("/on_open_print")
public class OnOpenPrintEndpoint {
    @OnOpen
    public void onOpen(Session session, EndpointConfig conf) {
        System.out.println("open");
    }

    @OnMessage
    public void onMessage(ByteBuffer message, Session session) throws IOException {
        System.out.println("from client: " + new String(message.array()));
        session.getBasicRemote().sendBinary(message);
    }

}
