package com.bjornir.terrabound.utils;

import com.bjornir.terrabound.entities.Arrow;

import java.util.ArrayList;


public class ArrowsList {
	private ArrayList<Arrow> localList, remoteList;
	private static ArrowsList instance;

	public static ArrowsList getInstance(){
		if(instance == null){
			instance = new ArrowsList();
			System.out.println("Instanciated ListOfArrow");
		}
		return instance;
	}

	private ArrowsList(){
		localList = new ArrayList<>();
		remoteList = new ArrayList<>();
	}

	public void addLocal(Arrow a){
		localList.add(a);
	}

	public Arrow isPresent(long netID) throws ElementNotPresentException{
		for(Arrow a : remoteList){
			if(a.getNetworkID() == netID){
				return a;
			}
		}
		throw new ElementNotPresentException("Arrow with netID "+netID+" not found");
	}

	public ArrayList<Arrow> getAllLocalArrows(){
		return new ArrayList<>(localList);
	}

	public ArrayList<Arrow> getAllArrows(){
		ArrayList<Arrow> allArrowsList = new ArrayList<>(localList);
		allArrowsList.addAll(remoteList);
		return allArrowsList;
	}

}
