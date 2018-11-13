package com.bjornir.terrabound;

import com.bjornir.terrabound.entities.Player;
import com.bjornir.terrabound.utils.MapUtils;
import com.bjornir.terrabound.utils.RayCaster;
import com.bjornir.terrabound.utils.Vector;
import org.newdawn.slick.*;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.tiled.TiledMap;

public class Game extends BasicGame {
    public static float MAX_SPEED = 1.2f, ACCELERATION = 0.015f, GRAVITYSTRENGTH = 0.003f;
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
        container.setVSync(true);
        player = new Player("sprites/Archer(noBow).png", 2);
        player.setInput(container.getInput());
        player.setX(50);
        player.setY(50);
        player.setG(container.getGraphics());
        map = new TiledMap("sprites/arena.tmx");
        MapUtils.setMap(map);
        tf = new TextField(container, container.getDefaultFont(), 15, 15, 250, 75);
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
        player.draw();
        map.render(0, 0);
        tf.render(container, g);
        player.drawBounds();
    }

    @Override
    public void update(GameContainer container, int delta) {
        Vector speed = player.update(delta);
        tf.setText("Time to update : "+ delta + "ms\n"+speed+"\n"+player.getAcceleration());
    }


}
