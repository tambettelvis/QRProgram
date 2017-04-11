package com.tanilsoo.tambet_telvis;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
import javafx.stage.Stage;

public class Main extends Application {

	private static final long serialVersionUID = 1L;
	public static final int MAIN_WIDTH = 1120;
	public static final int MAIN_HEIGHT = 680;
	
	public static MysqlConnector mysqlConnector;
	
	private Stage primaryStage;
	
	ObservableList employeeData = FXCollections.observableArrayList();
	ObservableList employeeInfoData = FXCollections.observableArrayList();
	ObservableList lastOperationData = FXCollections.observableArrayList();
	
	Button laoButton;
	Button addPackButton;
	Button addNewEmployeeBtn;
	Button employeeChartButton;
	
	TextField packageLength = new TextField();
	TextField packageDiameter = new TextField();
	TextField employeeNameField = new TextField();
	TextField uniqueFileField = new TextField();
	
	ToggleGroup radioGroup;
	String selectedRadioButton = "Mänd";
	
	Label lengthLabel = new Label("Paki pikkus: ");
	Label diameterLabel = new Label("Paki diameeter: ");
	
	Label employeeAmtOfImmutamata;
	Label employeeAmtOfImmutatud;
	Label employeeAmtOfLaaditud;
	
	
	static Label errorMessage = new Label();
	
	Scene mainScene;
	
	public static void main(String[] args){
		mysqlConnector = new MysqlConnector();
		//Main frame = new Main();
		
		launch(args);
		
		if(!MysqlConnector.isConnected()){
			/*String[] str = {"Jah", "Ei"};
			JOptionPane.showOptionDialog(frame, "Ei saanud ühendust MySQL serveriga. Kas ühendan uuesti?", "Error", JOptionPane.YES_NO_OPTION,
					JOptionPane.ERROR_MESSAGE,null, str, "Jah");*/
		}
		
		ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(3);
		//executor.scheduleAtFixedRate(new CheckPrinter(mysqlConnector), 0L, 10000, TimeUnit.MILLISECONDS);
		
		
	}
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("Title");
		BorderPane mainPanel = new BorderPane();
		mainPanel.setStyle("-fx-background-color: #f2e6d9;");//f9f2ec
		
		//Header
		HBox headerBox = getMainHeaderBox();
		laoButton = new Button("Ladu");
		laoButton.setPrefSize(100, 20);
		laoButton.setOnAction(new OnButtonClicked(this));
		employeeChartButton = new Button("Töötjad");
		employeeChartButton.setPrefSize(100, 20);
		employeeChartButton.setOnAction(new OnButtonClicked(this));
		headerBox.getChildren().add(laoButton);
		headerBox.getChildren().add(employeeChartButton);
		
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
		addPackButton.setOnAction(new OnButtonClicked(this));
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
		addNewEmployeeBtn.setOnAction(new OnButtonClicked(this));
		addNewEmployeeBtn.setPrefSize(100, 20);
		
		Label addEmployeeLabel = new Label("Töötaja nimi:");
		employeeLine1.getChildren().addAll(addEmployeeLabel, employeeNameField);
		
		
		//CENTER SIDE...
		
		//Packtype informations...
		
		FlowPane employeeInfoPanel = new FlowPane();
		employeeInfoPanel.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		employeeInfoPanel.setPadding(new Insets(10));
		
		ListView employeeListView = new ListView(employeeData);
		employeeListView.setPrefHeight(200);
		ListView employeeDataListView = new ListView(employeeInfoData);
		employeeDataListView.setPrefHeight(200);
		employeeDataListView.setPrefWidth(400);
		employeeListView.getSelectionModel().selectedItemProperty().addListener(new OnListViewItemChange(this));
		
		VBox additionalEmployeePanel = new VBox();
		additionalEmployeePanel.setSpacing(30);
		employeeAmtOfImmutamata = new Label("Pakke lattu tootnud(7 päeva): ");
		employeeAmtOfImmutatud = new Label("Pakke immutatud    (7 päeva): ");
		employeeAmtOfLaaditud = new Label("Pakke laaditud     (7 päeva): ");
		employeeAmtOfImmutamata.setFont(new Font("Calibri", 15));
		employeeAmtOfImmutatud.setFont(new Font("Calibri", 15));
		employeeAmtOfLaaditud.setFont(new Font("Calibri", 15));
		additionalEmployeePanel.getChildren().addAll(employeeAmtOfImmutamata, employeeAmtOfImmutatud, employeeAmtOfLaaditud);
		
		employeeInfoPanel.getChildren().addAll(employeeListView, employeeDataListView, additionalEmployeePanel);
		
		//Last actions info
		FlowPane lastActionsPanel = new FlowPane();
		ListView lastActionsListView = new ListView(lastOperationData);
		lastActionsPanel.getChildren().add(lastActionsListView);
		
		
		//Add components to...
		packAddGroup.getChildren().addAll(packageTitleLabel, packLine1, packLine2, packLine3, packLine4, addPackButton);
		employeeAddGroup.getChildren().addAll(addNewEmployeeTitleLabel, employeeLine1, addNewEmployeeBtn);
		
		VBox leftPanel = new VBox();
		leftPanel.setSpacing(5);
		leftPanel.getChildren().addAll(packAddGroup, employeeAddGroup, errorMessage);
		
		VBox centerPanel = new VBox();
		centerPanel.setSpacing(10);
		centerPanel.getChildren().addAll(employeeInfoPanel, lastActionsListView);
		
		
		mainPanel.setLeft(leftPanel);
		mainPanel.setTop(headerBox);
		mainPanel.setCenter(centerPanel);
		
		
		//LaoScene laoScene = new LaoScene(scene2);
		
		mainScene = new Scene(mainPanel, MAIN_WIDTH, MAIN_HEIGHT);
		primaryStage.setScene(mainScene);
		primaryStage.setOnCloseRequest(e -> Platform.exit());
		primaryStage.show();
		
		initialize();
		List<String> employeeNames = MysqlConnector.getEmployeeNames();
		for(String s : employeeNames){
			System.out.println(s);
		}
		
	}
	
	public void initialize(){
		updateEmployeePanel();
		updateLastActionPanel();
	}
	
	public static HBox getMainHeaderBox(){
		HBox headerBox = new HBox();
		headerBox.setPadding(new Insets(20));
		headerBox.setSpacing(10);
		headerBox.setStyle("-fx-background-color: #d9b38c;");
		return headerBox;
	}
	
	
	public Main(){}
	
	private void updateEmployeePanel(){
		employeeData.clear();
		List<String> packages = MysqlConnector.getEmployeeNames();
		
		for(String s : packages){
			employeeData.add(s);
		}

	}
	
	private void updateLastActionPanel(){
		lastOperationData.clear();
		List<String> lastActionData = MysqlConnector.getLastOperationsData();
		for(String s : lastActionData){
			lastOperationData.add(s);
		}
	}
	
	private void updateEmployeeDataPanel(String employeeName){
		employeeInfoData.clear();
		List<String> employeeInfo = MysqlConnector.getPackOperationDataByEmployee(employeeName);
		for(String s : employeeInfo){
			employeeInfoData.add(s);
		}
		
	}
	
	
	public Scene getMainScene(){
		return mainScene;
	}
	
	public Stage getPrimaryStage(){
		return primaryStage;
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
		
		Main main;
		
		public OnButtonClicked(Main main) {
			this.main = main;
		}
		
		@Override
		public void handle(ActionEvent e) {
			if(e.getSource() == laoButton){
				//TODO Show graphs... or something
				LaoScene laoScene = new LaoScene(main);
			} else if(e.getSource() == employeeChartButton){
				EmployeeScene employeeScene = new EmployeeScene(main);
			} else if(e.getSource() == addPackButton){
				if(packageLength.getText().equals("") || packageDiameter.getText().equals("") || uniqueFileField.getText().equals("")){
					Main.showErrorMessage("Sisesta tekst!");
					return;
				}
				
				//Cheking that unique file doesn't exist...
				List<String> uniqueFiles = MysqlConnector.getUniqueFiles();
				for(String fileName : uniqueFiles){
					if(fileName.equals(uniqueFileField.getText())){
						Main.showErrorMessage("Fail ei ole unikaalne!");
						//Display error
						return;
					}
				}
				
				int packLengthInt = Integer.parseInt(packageLength.getText());
				int packDiameterInt = Integer.parseInt(packageDiameter.getText());
				
				MysqlConnector.insertPactType(packLengthInt, packDiameterInt, radioGroup.getSelectedToggle().getUserData().toString(), uniqueFileField.getText());
				Main.showSucessMessage("Pakk lisatud!");
				//System.out.println("Button");
				//updatePackagePanel();
				/*
				if(!checkIfPackageExists()){
					updatePackagePanel();
				}*/
				
				
			} else if(e.getSource() == addNewEmployeeBtn){
				
				if(employeeNameField.getText().trim().equals("")){
					Main.showErrorMessage("Sisesta tekst...");
					return;
				}
				
				List<String> employeeNames = MysqlConnector.getEmployeeNames();
				for(String name : employeeNames){
					if(name.equals(employeeNameField.getText())){
						Main.showErrorMessage("Töötaja on juba lisatud!");
						//Display error;
						return;
					}
				}
				
				MysqlConnector.insertEmployee(employeeNameField.getText());
				Main.showSucessMessage("Töötaja lisatud!");
			}
			
			main.initialize();
		}

	
		
	}
	
	private class OnListViewItemChange implements ChangeListener<String> {

		Main main;
		public OnListViewItemChange(Main main) {
			this.main = main;
		}
		
		@Override
		public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue) {
			main.updateEmployeeDataPanel(newValue);
			employeeAmtOfImmutamata.setText("Pakke lattu tootnud(7 päeva): " + MysqlConnector.getPackImmutamatAmountByEmployee(newValue, 7));
			employeeAmtOfImmutatud.setText("Pakke immutatud    (7 päeva): " + MysqlConnector.getPackImmutatudAmountByEmployee(newValue, 7));
			employeeAmtOfLaaditud.setText("Pakke laaditud     (7 päeva): " + MysqlConnector.getPackLaaditudAmountByEmployee(newValue, 7));
		}
		
	}
	

}
