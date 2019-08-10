package com.bjornir.terrabound;

import com.bjornir.terrabound.entities.Entity;
import com.bjornir.terrabound.entities.Player;
import com.bjornir.terrabound.networking.ClientEndpoint;
import com.bjornir.terrabound.utils.*;
import engine.lighting.LightingCore;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.*;
import org.newdawn.slick.tiled.TiledMap;

import java.util.ArrayList;

public class Game extends BasicGame {
    public static final float MAX_SPEED = 1.5f, ACCELERATION = 0.015f, GRAVITYSTRENGTH = 0.003f, HOOKRANGE = 350.0f;
    public static Vector GRAVITY;
    public static TiledMap CurrentMap;
    public static final float NATIVE_DISPLAY_WIDTH = 1920, NATIVE_DISPLAY_HEIGHT = 1080;
    private ClientEndpoint endpoint;
    private ArrayList<Entity> entities;
    private Player player;
    public static GameContainer Container;

    static {
        GRAVITY = new Vector(0, GRAVITYSTRENGTH);
    }

    public Game() {
        super("TerraBound");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        container.setTargetFrameRate(60);
        container.getGraphics().setBackground(Color.gray);

        Container = container;

        LightingCore.initLighting();

        CurrentMap = new TiledMap("sprites/arena.tmx");

        endpoint = ClientEndpoint.getInstance();

        entities = new ArrayList<>();

        player = new Player("sprites/Archer(noBow).png", 32, 32);

        player.moveTo(64, 64);

        player.setInput(container.getInput());

        entities.add(player);

    }

    @Override
    public void render(GameContainer container, Graphics g) {

        g.scale((float)container.getWidth()/NATIVE_DISPLAY_WIDTH, (float)container.getHeight()/NATIVE_DISPLAY_HEIGHT);

        //LightingCore.startTexRendering();
        CurrentMap.render(0,0);
        entities.forEach(entity -> entity.draw());
        //LightingCore.endTexRendering();

        g.flush();
    }

    @Override
    public void update(GameContainer container, int delta) {
        entities.forEach(entity -> entity.update(delta));
    }


}
