package com.bjornir.terrabound.utils;

import com.bjornir.terrabound.entities.Entity;

import java.util.ArrayList;

public class EntitiesList {
    private ArrayList<Entity> list;
    private static EntitiesList instance;

    public static EntitiesList getInstance(){
        if(instance == null){
            instance = new EntitiesList();
            System.out.println("Instanciated ListOfArrow");
        }
        return instance;
    }

    private EntitiesList(){
        list = new ArrayList<>();
    }

    public void add(Entity e){
        list.add(e);
    }

    public ArrayList<Entity> getAllEntities(){
        return new ArrayList<>(list);
    }
}
