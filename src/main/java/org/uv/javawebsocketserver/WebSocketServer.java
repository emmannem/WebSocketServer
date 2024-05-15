/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.uv.javawebsocketserver;

import java.io.IOException;
import java.util.logging.Logger;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author JMRes
 */
@ServerEndpoint("/websocket")
public class WebSocketServer {

    private static final Logger log = Logger.getLogger(WebSocketServer.class.getName());
    private int totalPoints = 100; // Puntos iniciales

    // abrimos la sesión y obtenemos el id del cliente
    @OnOpen
    public void onOpen(Session session) {
        log.info("WebSocket opened: " + session.getId());
        try {
            session.getBasicRemote().sendText("Current points: " + totalPoints);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        try {
            int pointsToSubtract = Integer.parseInt(message);
            totalPoints -= pointsToSubtract;
            log.info("Subtracted " + pointsToSubtract + " points.");
            session.getBasicRemote().sendText("Points subtracted: " + pointsToSubtract + ", Remaining points: " + totalPoints);
        } catch (NumberFormatException e) {
            session.getBasicRemote().sendText("Invalid input. Please send a valid integer.");
        }
    }

    @OnClose
    // Tambien puedo saber porque se cerro la sesión
    public void onClose(Session session, CloseReason reason) {
        log.info("WebSocket closed: " + session.getId()
                + "Close reason: " + reason);
    }

    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }
}
