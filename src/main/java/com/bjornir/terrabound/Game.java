package com.bjornir.terrabound;

import com.bjornir.terrabound.entities.Arrow;
import com.bjornir.terrabound.entities.Entity;
import com.bjornir.terrabound.entities.Player;
import com.bjornir.terrabound.entities.Torch;
import com.bjornir.terrabound.networking.ClientEndpoint;
import com.bjornir.terrabound.utils.*;
import engine.lighting.LightSource;
import engine.lighting.LightingCore;
import org.newdawn.slick.*;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.tiled.TiledMap;

import java.util.ArrayList;

public class Game extends BasicGame {
    public static final float MAX_SPEED = 1.5f, ACCELERATION = 0.015f, GRAVITYSTRENGTH = 0.003f, HOOKRANGE = 350.0f;
    public static Vector GRAVITY;
    private Player player;
    private TiledMap map;
    private TextField tf, vectortf;
    private ArrowsList arrowsList;
    private ArrayList<Torch> torches;
    private ClientEndpoint endpoint;

    static {
        GRAVITY = new Vector(0, GRAVITYSTRENGTH);
    }

    public Game() {
        super("TerraBound");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        container.setVSync(true);
        container.setTargetFrameRate(60);
        container.getGraphics().setBackground(Color.gray);

        player = new Player( 2);
        player.loadSprite("sprites/Archer(noBow).png");
        player.setInput(container.getInput());
        player.setX(50);
        player.setY(50);
        player.setG(container.getGraphics());
        EntitiesList.getInstance().add(player);
        torches = new ArrayList<>();

        LightingCore.initLighting();
        for(int i = 0; i<10; i++){
            Torch torch = new Torch(1.0f, 0.0f, i*190+80, 150.0f);
            torches.add(torch);
        }


        map = new TiledMap("sprites/arena.tmx");
        MapUtils.setMap(map);

        arrowsList = ArrowsList.getInstance();

        vectortf = new TextField(container, container.getDefaultFont(), 400, 20, 800, 20);
        tf = new TextField(container, container.getDefaultFont(), 15, 15, 300, 75);

        endpoint = ClientEndpoint.getInstance();

        container.getInput().addMouseListener(player);
        container.getInput().addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(int i, char c) {
                if(i == Input.KEY_P){
                    if(!container.isPaused())
                        container.pause();
                    else
                        container.resume();
                } else if(i == Input.KEY_ESCAPE){
                    container.exit();
                } else if(i == Input.KEY_O){
                    System.out.println("Sending packet to websocket server");
                    endpoint.send("5325;Test data : player pressed O");
                }
            }

            @Override
            public void keyReleased(int i, char c) {

            }

            @Override
            public void setInput(Input input) {

            }

            @Override
            public boolean isAcceptingInput() {
                return true;
            }

            @Override
            public void inputEnded() {

            }

            @Override
            public void inputStarted() {

            }
        });

    }

    @Override
    public void render(GameContainer container, Graphics g) {
        player.drawBounds();
        tf.render(container, g);
        vectortf.render(container, g);
        LightingCore.startTexRendering();
        ArrayList<Entity> entities = EntitiesList.getInstance().getAllEntities();
        for(Entity e : entities){
            e.draw();
        }
        for(Torch t : torches){
            t.draw(g);
        }
        map.render(0, 0);
        for(Arrow a : arrowsList.getAllArrows()){
            a.draw();
            //a.drawBounds();
        }
        LightingCore.endTexRendering();
    }

    @Override
    public void update(GameContainer container, int delta) {
        for(Arrow a : arrowsList.getAllLocalArrows()){
            a.update(delta);
        }
        ArrayList<Entity> entities = EntitiesList.getInstance().getAllEntities();
        for(Entity e : entities){
            e.update(delta);
        }
        for(Torch t : torches){
            t.onUpdate(delta);
        }
        tf.setText("Time to update : "+ delta + "ms\n Speed : "+player.getSpeed()+"\n Accel : "+player.getAcceleration());
        vectortf.setText("Vector to mouse " + player.mouseVector());
    }


}
