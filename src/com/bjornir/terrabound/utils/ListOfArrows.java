package com.bjornir.terrabound.utils;

import com.bjornir.terrabound.entities.Arrow;

import java.util.ArrayList;


public class ListOfArrows {
	private ArrayList<Arrow> list;
	private static ListOfArrows instance;

	public static ListOfArrows getInstance(){
		if(instance == null){
			instance = new ListOfArrows();
		}
		return instance;
	}

	private ListOfArrows(){
		list = new ArrayList<>();
	}

	public void add(Arrow a){
		list.add(a);
	}

	public ArrayList<Arrow> getAllArrows(){
		return new ArrayList<>(list);
	}

}
