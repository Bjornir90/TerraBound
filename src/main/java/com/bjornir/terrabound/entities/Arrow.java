package com.bjornir.terrabound.entities;

import com.bjornir.terrabound.networking.ClientEndpoint;
import com.bjornir.terrabound.utils.Side;
import com.bjornir.terrabound.utils.Vector;

import java.util.Objects;

public class Arrow extends Entity {
    private static final int millisecondsOfFlyingStraight = 250;
    private int timeFromCreation;
    private boolean isLanded;

    public Arrow(){
        super();
        generateNetworkID();
    }

    public Arrow(int width, int height) {
        super("arrow", width, height);
        timeFromCreation = 0;
        isPhysical = false;
        isLanded = false;
        generateNetworkID();
    }

    public Arrow(String data){
        this(32, 32);
        int separatorIndex = data.indexOf(';');
        long networkID = Long.parseLong(data.substring(0, separatorIndex));
        this.updateFromString(data);
        this.networkID = networkID;
        this.isLocal = false;
    }


    @Override
    protected void onTerrainCollision(Side side){
        speed = new Vector();
        isPhysical = false;
        isLanded = true;
    }

    @Override
    public void update(int delta){
        super.update(delta);
        timeFromCreation += delta;
        if(timeFromCreation >= millisecondsOfFlyingStraight && !isLanded)
            isPhysical = true;


        if(!speed.isNullVector()){
            setAngle(speed.getAngle());
        }

        //first update of the object
        if(timeFromCreation == delta && isLocal){
            ClientEndpoint.getInstance().send(networkID+";"+this.formatForSending());
            System.out.println("Send object creation");
        }
    }

    /**
     * Returns a String that contains all necessary data to draw the arrow on the other computer.
     * The String is formatted to be used by the method @see updateFromRemote
     * @return a specially formatted string
     */
    public String formatForSending(){
        return position.getX()+":"+position.getY()+":"+speed.getX()+":"+speed.getY()+":"+angle;
    }

    /**
     * Update this arrow attributes with data contained in the parameter.
     * The string must be formatted by @see formatForSending
     * @param data a specially formatted string containing the data relevant for drawing the arrow
     */
    public void updateFromString(String data){

        final int expectedNumberOfData = 5;
        int currentEndIndex, currentDataIndex = 0;
        float[] dataNumbers = new float[expectedNumberOfData];

        int separatorIndex = data.indexOf(';');
        data = data.substring(separatorIndex+1);



        for(; currentDataIndex<expectedNumberOfData; currentDataIndex++){

            currentEndIndex = data.indexOf(':');

            if(currentEndIndex == -1) {
                dataNumbers[currentDataIndex] = Float.parseFloat(data);
                break;
            }

            dataNumbers[currentDataIndex] = Float.parseFloat(data.substring(0, currentEndIndex));

            data = data.substring(currentEndIndex+1);
        }

        position.setX(dataNumbers[0]);
        position.setY(dataNumbers[1]);
        speed.setX(dataNumbers[2]);
        speed.setY(dataNumbers[3]);
        setAngle(dataNumbers[4]);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Arrow arrow = (Arrow) o;
        return timeFromCreation == arrow.timeFromCreation &&
                isLanded == arrow.isLanded;
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeFromCreation, isLanded);
    }


    @Override
    public String toString() {
        return "Arrow{" +
                "timeFromCreation=" + timeFromCreation +
                ", isLanded=" + isLanded +
                ", networkID=" + networkID +
                ", position=" + position +
                ", speed=" + speed +
                ", angle=" + angle +
                ", isPhysical=" + isPhysical +
                '}';
    }
}
