package com.bjornir.terrabound;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class Main {
    public static void main(String[] args) throws SlickException {
        new AppGameContainer(new Game(), 960, 540, false).start();
    }
}
