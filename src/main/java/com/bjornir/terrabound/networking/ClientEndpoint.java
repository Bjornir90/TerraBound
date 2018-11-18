package com.bjornir.terrabound.networking;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ClientEndpoint {
    private WebSocket ws;
    private String server = "ws://127.0.0.1:8080";
    private static ClientEndpoint instance;

    private ClientEndpoint(){
        try {
            ws = new WebSocketFactory().setConnectionTimeout(5000).createSocket(server).addListener(new WebSocketAdapter(){
                @Override
                public void onTextMessage(WebSocket webSocket, String message){
                    System.out.println("Received message : " + message);
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
                System.out.println("Error connecting to server : ");
                System.out.println(e.getMessage());
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
