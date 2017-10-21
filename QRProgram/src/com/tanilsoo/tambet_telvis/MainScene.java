package com.tanilsoo.tambet_telvis;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MainScene implements Scenable {
	
	private static final int GRAPH_WIDTH = 900;
	private static final int GRAPH_HEIGHT = 600;
	
	Button addPackButton;
	Button addNewEmployeeBtn;
	
	
	TextField packageLength = new TextField();
	TextField packageDiameter = new TextField();
	TextField employeeNameField = new TextField();
	TextField additionalInfo = new TextField();
	
	Label lengthLabel = new Label("Paki pikkus: ");
	Label diameterLabel = new Label("Paki diameeter: ");
	

	@Override
	public Scene createScene() {
		
		BorderPane mainPanel = new BorderPane();
		mainPanel.setStyle("-fx-background-color: #f2e6d9;");//f9f2ec
		
		//Header
		BorderPane header = Main.getHeader();
		//Add new packageType
		VBox packAddGroup = new VBox();
		packAddGroup.setPadding(new Insets(10));
		packAddGroup.setSpacing(8);
		packAddGroup.setMaxWidth(100);
		packAddGroup.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		packAddGroup.setLayoutX(20);//Margin
		packAddGroup.setLayoutY(30);//Margin
		
		FlowPane packLine1 = new FlowPane();
		FlowPane packLine2 = new FlowPane();
		FlowPane packLine3 = new FlowPane();
		
		Label packageTitleLabel = new Label("Lisa uus paki tüüp:");
		packageTitleLabel.setFont(new Font("Calibri", 18));//TODO add font family.
		
		addPackButton = new Button("Lisa");
		addPackButton.setOnAction(new OnButtonClicked());
		addPackButton.setPrefSize(100, 20);
		
		packageLength.setMaxWidth(50);
		packageDiameter.setMaxWidth(50);
		additionalInfo.setMaxWidth(50);
		
		packLine1.getChildren().addAll(lengthLabel, packageLength);
		packLine2.getChildren().addAll(diameterLabel, packageDiameter);
		packLine3.getChildren().addAll(new Label("Lisa info"), additionalInfo);
		
		
		//Employee...
		VBox employeeAddGroup = new VBox();
		employeeAddGroup.setPadding(new Insets(10));
		employeeAddGroup.setSpacing(8);
		employeeAddGroup.setMaxWidth(100);
		employeeAddGroup.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		employeeAddGroup.setLayoutX(20);//Margin
		employeeAddGroup.setLayoutY(30);//Margin
		
		FlowPane employeeLine1 = new FlowPane();
		
		Label addNewEmployeeTitleLabel = new Label("Lisa uus töötaja");
		addNewEmployeeTitleLabel.setFont(new Font("Calibri", 18));
		
		addNewEmployeeBtn = new Button("Lisa");
		addNewEmployeeBtn.setOnAction(new OnButtonClicked());
		addNewEmployeeBtn.setPrefSize(100, 20);
		
		Label addEmployeeLabel = new Label("Töötaja nimi:");
		employeeLine1.getChildren().addAll(addEmployeeLabel, employeeNameField);
		
		
		//CENTER SIDE...
		VBox centerPanel = new VBox();
		
		Graph graphPanel = new Graph("Immutatud pakid(viimased 30 päeva)", GRAPH_WIDTH, GRAPH_HEIGHT);
		graphPanel.populateData(MysqlConnector.getPackOperationsByAmount(3, 30));
		
		
		
		centerPanel.getChildren().addAll(graphPanel);
		
		//Print paneel
		
		
		VBox leftPanel = new VBox();
		leftPanel.setSpacing(5);
		leftPanel.getChildren().addAll(packAddGroup, employeeAddGroup);
		
		// Add components to...
		packAddGroup.getChildren().addAll(packageTitleLabel, packLine1, packLine2, packLine3, addPackButton);
		employeeAddGroup.getChildren().addAll(addNewEmployeeTitleLabel, employeeLine1, addNewEmployeeBtn);
		
		
		mainPanel.setLeft(leftPanel);
		mainPanel.setTop(header);
		mainPanel.setCenter(centerPanel);
		
		Scene scene = new Scene(mainPanel, Main.MAIN_WIDTH, Main.MAIN_HEIGHT);
		return scene;
	}
	
	public void displayErrorMessage(String message){
		Alert alert = new Alert(AlertType.ERROR, message, ButtonType.OK);
		alert.show();
	}
	
	public void displayConfirmationMessage(String message){
		Alert alert = new Alert(AlertType.CONFIRMATION, message, ButtonType.OK);
		alert.show();
	}
	
	
	private class OnButtonClicked implements EventHandler<ActionEvent> {
		
		
		public String generateUniqueFile(){
			String lastUniqueFile = PackManager.packs.get(PackManager.packs.size()-1).getUniqueFile();
			int newUniqueFile = Integer.parseInt(lastUniqueFile.substring(2)) + 1;
			for(Pack p : PackManager.packs){
				System.out.println(p.getUniqueFile());
			}
			for(int i = 0; i < PackManager.packs.size(); i++){
				if(PackManager.packs.get(i).getUniqueFile().equals("qr" + newUniqueFile)){
					System.out.println(newUniqueFile);
					newUniqueFile++;
					i = -1;
				}
			}
			System.out.println("Final QR " + newUniqueFile);
			return "qr" + newUniqueFile;
		}
		
		
		@Override
		public void handle(ActionEvent e) {
			if(e.getSource() == addPackButton){
				if(packageLength.getText().equals("") || packageDiameter.getText().equals("")){
					displayErrorMessage("Kontrolli väljasid.");
					return;
				}
				
				int packLengthInt = Integer.parseInt(packageLength.getText());
				int packDiameterInt = Integer.parseInt(packageDiameter.getText());
				
				PackManager.refreshPacksList();
				
				if(additionalInfo.getText().trim().equals("") || additionalInfo.getText() == null){
					MysqlConnector.insertPactType(packLengthInt, packDiameterInt, "M", generateUniqueFile(), null);
					MysqlConnector.insertPactType(packLengthInt, packDiameterInt, "K", generateUniqueFile(), null);
					
				} else {
					MysqlConnector.insertPactType(packLengthInt, packDiameterInt, "M", generateUniqueFile(), additionalInfo.getText().trim());
					MysqlConnector.insertPactType(packLengthInt, packDiameterInt, "K", generateUniqueFile(), additionalInfo.getText().trim());
				}
				
				
				displayConfirmationMessage("Pakk lisatud!");
				//System.out.println("Button");
				//updatePackagePanel();
				/*
				if(!checkIfPackageExists()){
					updatePackagePanel();
				}*/
				
				
			} else if(e.getSource() == addNewEmployeeBtn){
				
				if(employeeNameField.getText().trim().equals("")){
					displayErrorMessage("Sisesta tekst...");
					return;
				}
				
				List<String> employeeNames = MysqlConnector.getEmployeeNames();
				for(String name : employeeNames){
					if(name.equals(employeeNameField.getText())){
						displayErrorMessage("Töötaja on juba lisatud!");
						//Display error;
						return;
					}
				}
				
				MysqlConnector.insertEmployee(employeeNameField.getText());
				displayConfirmationMessage("Töötaja lisatud!");
			} 
			
		}

	
		
	}
	
	

}
