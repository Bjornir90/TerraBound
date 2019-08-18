package com.bjornir.terrabound;

import com.bjornir.terrabound.entities.Entity;
import com.bjornir.terrabound.entities.Player;
import com.bjornir.terrabound.networking.ClientEndpoint;
import com.bjornir.terrabound.utils.*;
import engine.lighting.LightSource;
import engine.lighting.LightingCore;
import org.newdawn.slick.*;
import org.newdawn.slick.tiled.TiledMap;

import java.util.ArrayList;
import java.util.HashMap;

public class Game extends BasicGame {
    public static final float MAX_SPEED = 1.5f, ACCELERATION = 0.015f, GRAVITYSTRENGTH = 0.003f;
    public static Vector GRAVITY;
    public static TiledMap CurrentMap;
    public static final float NATIVE_DISPLAY_WIDTH = 1920, NATIVE_DISPLAY_HEIGHT = 1080;
    private static Game instance;
    private ClientEndpoint endpoint;
    public ArrayList<Entity> entities;
    public HashMap<Long, Entity> remoteEntities;
    private Player player;
    private LightSource cursorLightSource;
    public GameContainer Container;

    static {
        GRAVITY = new Vector(0, GRAVITYSTRENGTH);
    }

    public static Game getInstance(){
        return instance;
    }

    public Game() {
        super("TerraBound");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        instance = this;

        HashMap<String, Sprite> spriteMap = new HashMap<>();

        Sprite playerS = new Sprite("Archer(noBow).png");
        Sprite arrowS = new Sprite("Arrow.png");

        spriteMap.put("archer", playerS);
        spriteMap.put("arrow", arrowS);

        Sprite.LoadedSprites = spriteMap;

        container.getGraphics().setBackground(Color.gray);

        Container = container;

        LightingCore.initLighting();

        CurrentMap = new TiledMap("sprites/arena.tmx");

        endpoint = ClientEndpoint.getInstance();

        entities = new ArrayList<>();
        remoteEntities = new HashMap<>();

        player = new Player(32, 32);

        player.moveTo(64, 64);

        player.setInput(container.getInput());

        entities.add(player);

        new LightSource(Color.white, 400, 1920.0f/2.0f, 1080.0f/2.0f).turnOn();

        cursorLightSource = new LightSource(Color.cyan, 200, 0.0f, 0.0f);
        cursorLightSource.setFalloff(6.0f);
        cursorLightSource.turnOn();

        container.getInput().addMouseListener(new MouseListener() {
            @Override
            public void mouseWheelMoved(int i) {

            }

            @Override
            public void mouseClicked(int i, int i1, int i2, int i3) {

            }

            @Override
            public void mousePressed(int i, int i1, int i2) {

            }

            @Override
            public void mouseReleased(int i, int i1, int i2) {

            }

            @Override
            public void mouseMoved(int i, int i1, int i2, int i3) {
                //Reposition the mouse click in Native coordinates (1920x1080)
                float x = Math.round(i2*(Game.NATIVE_DISPLAY_WIDTH/Game.getInstance().Container.getScreenWidth()));
                float y = Math.round(i3*(Game.NATIVE_DISPLAY_HEIGHT/Game.getInstance().Container.getScreenHeight()));
                cursorLightSource.setPosition(x, y);
            }

            @Override
            public void mouseDragged(int i, int i1, int i2, int i3) {

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

        g.scale((float)container.getWidth()/NATIVE_DISPLAY_WIDTH, (float)container.getHeight()/NATIVE_DISPLAY_HEIGHT);

        LightingCore.startTexRendering();
        CurrentMap.render(0,0);
        entities.forEach(entity -> entity.draw());

        ClientEndpoint.getInstance().getCreatedEntities().forEach(entity -> remoteEntities.put(entity.getNetworkID(), entity));
        ClientEndpoint.getInstance().clearCreatedEntities();
        remoteEntities.values().forEach(entity -> entity.draw());

        LightingCore.endTexRendering();

        g.setLineWidth(3.0f);
        entities.forEach(entity -> entity.drawBounds(g));
    }

    @Override
    public void update(GameContainer container, int delta) {
        entities.forEach(entity -> entity.update(delta));
        remoteEntities.values().forEach(entity -> entity.update(delta));
    }


}
