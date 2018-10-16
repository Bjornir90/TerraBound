package com.bjornir.terrabound;

import com.bjornir.terrabound.entities.Player;
import com.bjornir.terrabound.utils.MapUtils;
import com.bjornir.terrabound.utils.RayCaster;
import com.bjornir.terrabound.utils.Vector;
import org.newdawn.slick.*;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.tiled.TiledMap;

public class Game extends BasicGame {
    public static float MAX_SPEED = 1.2f, ACCELERATION = 0.6f, GRAVITYSTRENGTH = 0.003f;
    public static Vector GRAVITY;
    private Player player;
    private TiledMap map;
    private TextField tf;

    static {
        GRAVITY = new Vector(0, GRAVITYSTRENGTH);
    }

    public Game() {
        super("TerraBound");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        player = new Player("sprites/Archer(noBow).png", 2);
        player.setInput(container.getInput());
        player.setX(20);
        player.setY(50);
        player.setG(container.getGraphics());
        map = new TiledMap("sprites/arena.tmx");
        MapUtils.setMap(map);
        tf = new TextField(container, container.getDefaultFont(), 15, 15, 250, 75);
    }

    @Override
    public void render(GameContainer container, Graphics g) {
        player.draw();
        RayCaster.drawRays(g);
        map.render(0, 0);
        tf.render(container, g);
    }

    @Override
    public void update(GameContainer container, int delta) {
        Vector speed = player.update(delta);
        tf.setText("Time to update : "+ delta + "ms\n"+speed+"\n"+player.getAcceleration());
    }

}
