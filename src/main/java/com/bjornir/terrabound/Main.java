package com.bjornir.terrabound;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.AppletGameContainer;
import org.newdawn.slick.SlickException;

public class Main {
    public static void main(String[] args) throws SlickException {

        AppGameContainer agc = new AppGameContainer(new Game());
        agc.setDisplayMode(agc.getScreenWidth(), agc.getScreenHeight(), true);
        agc.start();
    }

}
