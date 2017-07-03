package com.tanilsoo.tambet_telvis;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
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
	
	Button addPackButton;
	Button addNewEmployeeBtn;
	
	
	TextField packageLength = new TextField();
	TextField packageDiameter = new TextField();
	TextField employeeNameField = new TextField();
	TextField uniqueFileField = new TextField();
	
	ToggleGroup radioGroup;
	String selectedRadioButton = "Mänd";
	
	Label lengthLabel = new Label("Paki pikkus: ");
	Label diameterLabel = new Label("Paki diameeter: ");
	
	static Label errorMessage = new Label();
	
	

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
		FlowPane packLine4 = new FlowPane();
		
		Label packageTitleLabel = new Label("Lisa uus paki tüüp:");
		Label uniqueFileLabel = new Label("Faili nimi:");
		packageTitleLabel.setFont(new Font("Calibri", 18));//TODO add font family.
		
		addPackButton = new Button("Lisa");
		addPackButton.setOnAction(new OnButtonClicked());
		addPackButton.setPrefSize(100, 20);
		
		packageLength.setMaxWidth(50);
		packageDiameter.setMaxWidth(50);
		uniqueFileField.setMaxWidth(50);
		
		radioGroup = new ToggleGroup();
		RadioButton mRadioBtn = new RadioButton("Mänd");
		RadioButton kRadioBtn = new RadioButton("Kuusk");
		mRadioBtn.setUserData("M");
		kRadioBtn.setUserData("K");
		mRadioBtn.setSelected(true);
		mRadioBtn.setToggleGroup(radioGroup);
		kRadioBtn.setToggleGroup(radioGroup);
		Label woodTypeLabel = new Label("Puu liik ");
		
		packLine1.getChildren().addAll(lengthLabel, packageLength);
		packLine2.getChildren().addAll(diameterLabel, packageDiameter);
		packLine3.getChildren().addAll(woodTypeLabel, mRadioBtn, kRadioBtn);
		packLine4.getChildren().addAll(uniqueFileLabel, uniqueFileField);
		
		
		
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
		
		Graph graphPanel = new Graph();
		graphPanel.populateData(MysqlConnector.getPackOperationsByAmount(2, 30));
		
		centerPanel.getChildren().add(graphPanel);
		
		//Print paneel
		
		
		VBox leftPanel = new VBox();
		leftPanel.setSpacing(5);
		leftPanel.getChildren().addAll(packAddGroup, employeeAddGroup, errorMessage);
		
		// Add components to...
		packAddGroup.getChildren().addAll(packageTitleLabel, packLine1, packLine2, packLine3, packLine4, addPackButton);
		employeeAddGroup.getChildren().addAll(addNewEmployeeTitleLabel, employeeLine1, addNewEmployeeBtn);
		
		
		mainPanel.setLeft(leftPanel);
		mainPanel.setTop(header);
		mainPanel.setCenter(centerPanel);
		
		Scene scene = new Scene(mainPanel, Main.MAIN_WIDTH, Main.MAIN_HEIGHT);
		return scene;
	}
	
	public static void showErrorMessage(String message){
		errorMessage.setTextFill(Color.RED);
		errorMessage.setText(message);
	}
	
	public static void showSucessMessage(String message){
		errorMessage.setTextFill(Color.GREEN);
		errorMessage.setText(message);
	}
	
	private class OnButtonClicked implements EventHandler<ActionEvent> {
		
		
		
		@Override
		public void handle(ActionEvent e) {
			if(e.getSource() == addPackButton){
				if(packageLength.getText().equals("") || packageDiameter.getText().equals("") || uniqueFileField.getText().equals("")){
					showErrorMessage("Sisesta tekst!");
					return;
				}
				
				//Cheking that unique file doesn't exist...
				List<String> uniqueFiles = MysqlConnector.getUniqueFiles();
				for(String fileName : uniqueFiles){
					if(fileName.equals(uniqueFileField.getText())){
						showErrorMessage("Fail ei ole unikaalne!");
						//Display error
						return;
					}
				}
				
				int packLengthInt = Integer.parseInt(packageLength.getText());
				int packDiameterInt = Integer.parseInt(packageDiameter.getText());
				
				MysqlConnector.insertPactType(packLengthInt, packDiameterInt, radioGroup.getSelectedToggle().getUserData().toString(), uniqueFileField.getText());
				showSucessMessage("Pakk lisatud!");
				//System.out.println("Button");
				//updatePackagePanel();
				/*
				if(!checkIfPackageExists()){
					updatePackagePanel();
				}*/
				
				
			} else if(e.getSource() == addNewEmployeeBtn){
				
				if(employeeNameField.getText().trim().equals("")){
					showErrorMessage("Sisesta tekst...");
					return;
				}
				
				List<String> employeeNames = MysqlConnector.getEmployeeNames();
				for(String name : employeeNames){
					if(name.equals(employeeNameField.getText())){
						showErrorMessage("Töötaja on juba lisatud!");
						//Display error;
						return;
					}
				}
				
				MysqlConnector.insertEmployee(employeeNameField.getText());
				showSucessMessage("Töötaja lisatud!");
			} 
			
			Main.initialize();
		}

	
		
	}
	
	

}
