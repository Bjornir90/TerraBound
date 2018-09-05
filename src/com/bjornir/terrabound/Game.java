package com.bjornir.terrabound;

import com.bjornir.terrabound.entities.Player;
import com.bjornir.terrabound.utils.MapUtils;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class Game extends BasicGame {
    public static float MAX_SPEED = 5.0f, ACCELERATION = 1.0f, GRAVITY = 0.007f;
    private Player player;
    private TiledMap map;

    public Game() {
        super("TerraBound");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        player = new Player("sprites/Archer(noBow).png", 2);
        player.setInput(container.getInput());
        player.setX(20);
        player.setY(50);
        map = new TiledMap("sprites/arena.tmx");
        MapUtils.setMap(map);
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        player.draw();
        map.render(0, 0);
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        player.update(delta);
    }

}
