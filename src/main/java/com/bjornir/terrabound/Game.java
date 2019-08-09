package com.bjornir.terrabound;

import com.bjornir.terrabound.entities.Entity;
import com.bjornir.terrabound.entities.Player;
import com.bjornir.terrabound.networking.ClientEndpoint;
import com.bjornir.terrabound.utils.*;
import engine.lighting.LightingCore;
import org.newdawn.slick.*;
import org.newdawn.slick.tiled.TiledMap;

import java.util.ArrayList;

public class Game extends BasicGame {
    public static final float MAX_SPEED = 1.5f, ACCELERATION = 0.015f, GRAVITYSTRENGTH = 0.003f, HOOKRANGE = 350.0f;
    public static Vector GRAVITY;
    public static TiledMap CurrentMap;
    private ClientEndpoint endpoint;
    private ArrayList<Entity> entities;
    private Player player;

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

        LightingCore.initLighting();

        CurrentMap = new TiledMap("sprites/arena.tmx");

        endpoint = ClientEndpoint.getInstance();

        entities = new ArrayList<>();

        player = new Player("sprites/Archer(noBow).png", 32, 32);

        player.moveTo(64, 64);

        player.setInput(container.getInput());

    }

    @Override
    public void render(GameContainer container, Graphics g) {

        LightingCore.startTexRendering();
        entities.forEach(entity -> entity.draw());
        player.draw();
        CurrentMap.render(0,0);
        LightingCore.endTexRendering();

    }

    @Override
    public void update(GameContainer container, int delta) {
        entities.forEach(entity -> entity.update(delta));
        player.update(delta);
    }


}
