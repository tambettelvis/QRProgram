package ee.tanilsoo.src;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import ee.tanilsoo.scene.EmployeeScene;
import ee.tanilsoo.scene.JobsScene;
import ee.tanilsoo.scene.LaoScene;
import ee.tanilsoo.scene.MainScene;
import ee.tanilsoo.scene.OrdersScene;
import ee.tanilsoo.scene.PrintScene;
import ee.tanilsoo.scene.RawDataScene;
import ee.tanilsoo.scene.SceneBuilder;
import ee.tanilsoo.scene.SettingsScene;
import ee.tanilsoo.scene.ShiftScene;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

	private static final long serialVersionUID = 1L;
	public static int MAIN_WIDTH = 1280;
	public static int MAIN_HEIGHT = 800;
	
	static MysqlConnector mysqlConnector;
	
	public static Stage primaryStage;
	Scene mainScene;
	static BorderPane header;
	
	public static void main(String[] args){
		mysqlConnector = new MysqlConnector();
		
		if(!MysqlConnector.isConnected()){
			
		}
		
		ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(3);
		executor.scheduleAtFixedRate(new CheckPrinter(), 5000, 15000, TimeUnit.MILLISECONDS);
		initialize();
		
		launch(args);
	}
	

	@Override
	public void start(Stage primaryStage) throws Exception {
		Main.primaryStage = primaryStage;
		primaryStage.setTitle("Title");
		
		header = createHeader();
		SceneBuilder.setNewScene(new MainScene());
		//SceneBuilder.setNewScene(new PrintScene());
		Main.primaryStage.setOnCloseRequest(e -> closeApplication());
		Main.primaryStage.show();
	}
	
	public static void initialize(){
		PackManager.refreshPacksList();
	}
	
	private void closeApplication() {
		Platform.exit();
		System.exit(0);
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
				new HeaderButton("PRINDI", new PrintScene()),
				new HeaderButton("LADU", new LaoScene()),
				new HeaderButton("VAHETUS", new ShiftScene()),
				new HeaderButton("TELLIMUSED", new OrdersScene()),
				new HeaderButton("T÷÷DE NIMEKIRI", new JobsScene()),
				new HeaderButton("SEADED", new SettingsScene()),
				new HeaderButton("RAW DATA", new RawDataScene())
				);
		
		
		 headerBox.getChildren().addAll(
				new HeaderButton("PRINDI", new PrintScene())
				);
		 
		
		HBox imgPanel = new HBox();
		imgPanel.setPadding(new Insets(10));
		ImageView img = new ImageView(new Image("file:images/woodmaster.png"));
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
