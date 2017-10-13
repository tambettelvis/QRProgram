package com.tanilsoo.tambet_telvis;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class AddRemovePacksScene implements Scenable {

	ComboBox packAmount;
	ComboBox postTypes;
	Button lisaButton = new Button("Lisa");
	Button eemaldaButton = new Button("Eemalda");
	CheckBox checkBox = new CheckBox("Immutamata");
	
	public AddRemovePacksScene() {
		packAmount = new ComboBox(OrdersScene.getComboBoxItems(100));
		postTypes = new ComboBox(OrdersScene.getPostTypeItems());
	}
	
	@Override
	public Scene createScene() {
		BorderPane borderPanel = new BorderPane();
		BorderPane header = Main.getHeader();
		
		Label l = new Label("Lisa pakid lattu");
		l.setFont(new Font("Calibri", 16));
		lisaButton.setOnAction(new OnButtonClicked());
		eemaldaButton.setOnAction(new OnButtonClicked());
		HBox lisaPackPanel = new HBox(10);
		lisaPackPanel.getChildren().addAll(postTypes, packAmount, checkBox, lisaButton, eemaldaButton);
		
		VBox addPackPanel = new VBox(10);
		addPackPanel.getChildren().addAll(l, lisaPackPanel);
		
		
		
		
		Scene scene = new Scene(borderPanel, Main.MAIN_WIDTH, Main.MAIN_HEIGHT);
		borderPanel.setCenter(addPackPanel);
		borderPanel.setTop(header);
		return scene;
	}

	private class OnButtonClicked implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent e) {
			if(postTypes.getValue() == null || postTypes.getValue().toString().isEmpty())
				return;
			
			int id = Integer.parseInt(postTypes.getValue().toString().split("\\.")[0]);
			int amt = Integer.parseInt(packAmount.getValue().toString());
			
			
			
			if(e.getSource() == lisaButton){
				if(MysqlConnector.isPackInImmutatud(id)){
					MysqlConnector.incrementLaosImmutatudPack(id, amt);
				} else {
					MysqlConnector.addPackInToLaosImmutatud(id, amt);
				}
				Alert alert = new Alert(AlertType.CONFIRMATION, "Lisasin lattu " + amt + " pakki " + postTypes.getValue().toString(), ButtonType.OK);
				alert.show();
			} else if(e.getSource() == eemaldaButton){
				if(MysqlConnector.isPackInImmutatud(id)){
					MysqlConnector.decrementLaosImmutatudPack(id, amt);
					Alert alert = new Alert(AlertType.CONFIRMATION, "Eemaldasin laost " + amt + " pakki " + postTypes.getValue().toString(), ButtonType.OK);
					alert.show();
				}
			}
		}
		
	}
	
}
