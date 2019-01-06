package com.bjornir.terrabound.utils;

import com.bjornir.terrabound.Game;
import com.bjornir.terrabound.entities.HookTarget;
import com.bjornir.terrabound.entities.Player;

import java.util.ArrayList;

public class HookTargetWatcher {
    private ArrayList<HookTarget> targetArrayList;
    private static HookTargetWatcher instance;
    private Player player;

    public static HookTargetWatcher getInstance(){
        if(instance == null){
            instance = new HookTargetWatcher();
        }
        return instance;
    }

    private HookTargetWatcher(){
        targetArrayList = new ArrayList<>();
    }

    public void init(Player p){
        player = p;
    }

    public void addWatched(HookTarget t){
        targetArrayList.add(t);
    }

    public void onUpdate(){
        float closestDistance = 100000.0f;
        HookTarget closestTarget = null;
        for(HookTarget t : targetArrayList){
            Vector playerToTarget = new Vector(t.getX()-player.getX(), t.getY()-player.getY());
            float distancePlayerToTarget = playerToTarget.norm();
            if(distancePlayerToTarget < Game.HOOKRANGE){
                t.setInRange(true);
                if(closestDistance > distancePlayerToTarget){
                    closestDistance = distancePlayerToTarget;
                    closestTarget = t;
                }
            } else {
                t.setInRange(false);
            }
        }
        player.setClosestTarget(closestTarget);
    }
}
