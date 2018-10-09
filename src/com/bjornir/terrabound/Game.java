package com.bjornir.terrabound;

import com.bjornir.terrabound.entities.Player;
import com.bjornir.terrabound.utils.MapUtils;
import com.bjornir.terrabound.utils.RayCaster;
import com.bjornir.terrabound.utils.Vector;
import org.newdawn.slick.*;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.tiled.TiledMap;

public class Game extends BasicGame {
    public static float MAX_SPEED = 1.0f, ACCELERATION = 0.2f, GRAVITY = 0.05f;
    private Player player;
    private TiledMap map;
    private TextField tf;

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
        tf = new TextField(container, container.getDefaultFont(), 50, 50, 250, 50);
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
        tf.setText(speed.toString());
    }

}
