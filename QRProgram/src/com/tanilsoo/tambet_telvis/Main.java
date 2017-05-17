package com.tanilsoo.tambet_telvis;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

	private static final long serialVersionUID = 1L;
	public static final int MAIN_WIDTH = 1120;
	public static final int MAIN_HEIGHT = 680;
	
	public static MysqlConnector mysqlConnector;
	
	public static Stage primaryStage;
	Scene mainScene;
	
	public static void main(String[] args){
		mysqlConnector = new MysqlConnector();
		//Main frame = new Main();'
		
		ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(3);
		//executor.scheduleAtFixedRate(new CheckPrinter(), 0L, 10000, TimeUnit.MILLISECONDS);
		
		launch(args);
		
		if(!MysqlConnector.isConnected()){
			/*String[] str = {"Jah", "Ei"};
			JOptionPane.showOptionDialog(frame, "Ei saanud ühendust MySQL serveriga. Kas ühendan uuesti?", "Error", JOptionPane.YES_NO_OPTION,
					JOptionPane.ERROR_MESSAGE,null, str, "Jah");*/
		}
		
	}
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		Main.primaryStage = primaryStage;
		primaryStage.setTitle("Title");
		
		
		
		
		//LaoScene laoScene = new LaoScene(scene2);
		
		SceneBuilder.setNewScene(new MainScene());
		Main.primaryStage.setOnCloseRequest(e -> closeApplication());
		Main.primaryStage.show();
		
		initialize();
		
	}
	
	private void closeApplication() {
		Platform.exit();
		System.exit(0);
	}


	public static void initialize(){
		updateEmployeePanel();
		updateLastActionPanel();
	}
	
	
	
	public Scene getMainScene(){
		return mainScene;
	}
	
	public static void setNewScene(Pane mainPanel){
		Scene scene = new Scene(mainPanel, Main.MAIN_WIDTH, Main.MAIN_HEIGHT);
		primaryStage.setScene(scene);
	}
	
	private static void updateEmployeePanel(){
		MainScene.employeeData.clear();
		List<String> packages = MysqlConnector.getEmployeeNames();
		
		for(String s : packages){
			MainScene.employeeData.add(s);
		}

	}
	
	public static void updateLastActionPanel(){
		MainScene.lastOperationData.clear();
		List<String> lastActionData = MysqlConnector.getLastOperationsData();
		for(String s : lastActionData){
			MainScene.lastOperationData.add(s);
		}
	}
	
	public static void updateEmployeeDataPanel(String employeeName){
		MainScene.employeeInfoData.clear();
		List<String> employeeInfo = MysqlConnector.getPackOperationDataByEmployee(employeeName);
		for(String s : employeeInfo){
			MainScene.employeeInfoData.add(s);
		}
		
	
	}
	

}
