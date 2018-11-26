package com.bjornir.terrabound.networking;

import com.neovisionaries.ws.client.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ClientEndpoint {
    private WebSocket ws;
    private String server = "ws://127.0.0.1:8080";
    private static ClientEndpoint instance;

    private ClientEndpoint(){
        initConnection();
    }

    private void initConnection(){
        try {
            ws = new WebSocketFactory().setConnectionTimeout(5000).createSocket(server).addListener(new WebSocketAdapter(){
                @Override
                public void onTextMessage(WebSocket webSocket, String message){
                    System.out.println("Received message : " + message);
                    int separatorIndex = message.indexOf(';');
                    long networkID = Long.parseLong(message.substring(0, separatorIndex));
                    System.out.println("networkID = " + networkID);
                }

                @Override
                public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                    System.out.println("Connected to server : " + server);
                    websocket.sendText("This is the client");
                    websocket.flush();
                }
            });
            try {
                ws.connect();
            } catch (WebSocketException e) {
                WebSocketError error = e.getError();
                System.err.println("Could not connect to server : "+error.name());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ClientEndpoint getInstance(){
        if(instance == null){
            instance = new ClientEndpoint();
        }
        return instance;
    }

    public void send(String data){
        ws.sendText(data);
        ws.flush();
    }
}
