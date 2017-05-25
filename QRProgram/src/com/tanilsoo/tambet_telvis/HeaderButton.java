package com.tanilsoo.tambet_telvis;

import java.util.ArrayList;
import java.util.List;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class HeaderButton extends Label {
	
	
	static List<HeaderButton> headerButtons = new ArrayList<HeaderButton>();
	
	Color defaultColor = new Color(0.51, 0.26, 0.0, 1);
	
	public HeaderButton(String text, Scenable scenable){
		super(text);
		Font font = new Font("Calibri", 20);
		setTextFill(defaultColor);
		setFont(font);
		
		if(headerButtons.isEmpty()){
			focus();
		}
		
		setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				SceneBuilder.setNewScene(scenable);
				setAllUnfocused();
				focus();
			}
		});
		headerButtons.add(this);
	}
	
	private void setAllUnfocused(){
		for(HeaderButton button : headerButtons){
			button.unfocus();
		}
	}
	
	private void unfocus(){
		setTextFill(defaultColor);
		Effect glow = new Glow(0);
		setEffect(glow);
	}
	
	private void focus(){
		setTextFill(Color.WHITE);
		Effect glow = new Glow(1.0);
		setEffect(glow);
	}
	

}
