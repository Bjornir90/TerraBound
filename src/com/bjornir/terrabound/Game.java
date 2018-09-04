package com.bjornir.terrabound;

import com.bjornir.terrabound.entities.Player;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class Game extends BasicGame {
    public static float MAX_SPEED = 5.0f, ACCELERATION = 1.0f;
    private Player player;

    public Game() {
        super("TerraBound");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        player = new Player("sprites/Archer(noBow).png");
        player.setInput(container.getInput());
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        player.draw();
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        player.update(delta);
    }

}
