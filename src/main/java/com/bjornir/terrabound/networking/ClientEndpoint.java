package com.bjornir.terrabound.networking;

import com.bjornir.terrabound.Game;
import com.bjornir.terrabound.entities.Arrow;
import com.bjornir.terrabound.entities.Entity;
import com.neovisionaries.ws.client.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientEndpoint {
    private WebSocket ws;
    private String server;
    private static ClientEndpoint instance;

    private ClientEndpoint(){
        initConnection();
    }

    private void readServerAddress(){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("properties.conf"));
        } catch (FileNotFoundException e) {
            System.err.println("Error : could not locate config file\nPlease download properties.conf again.");
            System.exit(1);
        }
        try {
            String currentLine;
            int delimiterIndex;
            String propertyName;
            do {
                currentLine = reader.readLine();
                delimiterIndex = currentLine.indexOf(':');
                propertyName = currentLine.substring(0, delimiterIndex);
            } while (!propertyName.equals("serveraddress"));
            String value = currentLine.substring(delimiterIndex+1);
            server = "ws://"+value;

        } catch (IOException e) {
            System.err.println("Could not read config file.\nPlease restart the game.\nIf this persists, try reinstalling the game.");
            System.exit(1);
        }
    }

    private void initConnection(){
        readServerAddress();
        try {
            ws = new WebSocketFactory().setConnectionTimeout(5000).createSocket(server).addListener(new WebSocketAdapter(){
                @Override
                public void onTextMessage(WebSocket webSocket, String message){

                    HashMap<Long, Entity> remote = Game.getInstance().remoteEntities;
                    int separatorIndex = message.indexOf(';');
                    long networkID = Long.parseLong(message.substring(0, separatorIndex));

                    if(remote.containsKey(networkID)){
                        ((Arrow) remote.get(networkID) ).updateFromString(message);
                    } else {
                        remote.put(networkID, new Arrow(message));
                    }

                    Game.getInstance().remoteEntities = remote;

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
    }
}
