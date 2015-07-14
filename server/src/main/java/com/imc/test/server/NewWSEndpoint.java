package com.imc.test.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author lysenko
 */
@ServerEndpoint("/endpoint")
public class NewWSEndpoint {

    private final byte[] bigMessage = new byte[5000000]; //more than default buffer size

    @OnMessage
    public void onMessage(ByteBuffer msg, Session session) throws IOException {
        System.out.println("Hello from client: " + msg);
        sendByByteBuffer(bigMessage, session);
    }

    public static void sendByByteBuffer(byte[] responseBytes, Session wsSession) throws IOException {
        int bufsize = 1048576;
        int pos = 0;
//        wsSession.getAsyncRemote().sendBinary(ByteBuffer.wrap(responseBytes), new SendHandler() {
//
//            @Override
//            public void onResult(SendResult result) {
//                if (!result.isOK()) {
//                    LOG.log(Level.SEVERE, null, result.getException());
//                }
//            }
//        });
        while (pos + bufsize <= responseBytes.length) {
            boolean endFlag = false;
            if (pos + bufsize == responseBytes.length) {
                endFlag = true;
            }
            wsSession.getBasicRemote().sendBinary(ByteBuffer.wrap(Arrays.copyOfRange(responseBytes, pos, pos + bufsize)), endFlag);
            pos += bufsize;
        }
        if (pos < responseBytes.length) {
            wsSession.getBasicRemote().sendBinary(ByteBuffer.wrap(Arrays.copyOfRange(responseBytes, pos, responseBytes.length)), true);
        }
    }

}
