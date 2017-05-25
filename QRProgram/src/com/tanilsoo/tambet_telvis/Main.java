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
	static BorderPane header;
	
	public static void main(String[] args){
		mysqlConnector = new MysqlConnector();
		//Main frame = new Main();'
		
		ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(3);
		//executor.scheduleAtFixedRate(new CheckPrinter(), 0L, 10000, TimeUnit.MILLISECONDS);
		
		header = createHeader();
		launch(args);
		
		if(!MysqlConnector.isConnected()){
			/*String[] str = {"Jah", "Ei"};
			JOptionPane.showOptionDialog(frame, "Ei saanud �hendust MySQL serveriga. Kas �hendan uuesti?", "Error", JOptionPane.YES_NO_OPTION,
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
		
	}
	
	
	
	public Scene getMainScene(){
		return mainScene;
	}
	
	public static void setNewScene(Pane mainPanel){
		Scene scene = new Scene(mainPanel, Main.MAIN_WIDTH, Main.MAIN_HEIGHT);
		primaryStage.setScene(scene);
	}
	
	private static BorderPane createHeader(){
		BorderPane header = new BorderPane();
		header.setStyle("-fx-background-color: #d9b38c;");
		
		HBox headerBox = new HBox();
		headerBox.setPadding(new Insets(20));
		headerBox.setSpacing(20);
		headerBox.getChildren().addAll(
				new HeaderButton("HOME", new MainScene()),
				new HeaderButton("LADU", new LaoScene()),
				new HeaderButton("T��TAJAD", new EmployeeScene()),
				new HeaderButton("TELLIMUSED", new OrdersScene()),
				new HeaderButton("T��DE NIMEKIRI", new JobsScene()),
				new HeaderButton("SEADED", new SettingsScene()),
				new HeaderButton("RAW DATA", new RawDataScene())
				);
		
		HBox imgPanel = new HBox();
		imgPanel.setPadding(new Insets(10));
		ImageView img = new ImageView(new Image("file:woodmaster.png"));
		img.setFitWidth(85);
		img.setFitHeight(43);
		imgPanel.getChildren().add(img);
		
		
		header.setLeft(headerBox);
		header.setRight(imgPanel);
		
		return header;
	}
	
	
	public static BorderPane getHeader(){
		return header;
	}
	
	
	

}
