package ee.tanilsoo.scene;

import ee.tanilsoo.src.Graph;
import ee.tanilsoo.src.Main;
import ee.tanilsoo.src.MysqlConnector;
import ee.tanilsoo.src.PackManager;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ShiftScene implements Scenable {

	private static int firstShiftStart = 5;
	private static int firstShiftEnd = 15;
	private static int secondShiftStart = 15;
	private static int secondShiftEnd = 24;
	
	int padding = 20;
	private int days = 10;
	
	Button dayButton = new Button("1 Päev");
	Button weekButton = new Button("Nädal");
	Button monthButton = new Button("Kuu"); 
	
	public ShiftScene() {}
	
	public ShiftScene(int days) { this.days = days; }
	
	@Override
	public Scene createScene(){
		BorderPane borderPanel = new BorderPane();
		BorderPane header = Main.getHeader();
		
		VBox mainPanel = new VBox(padding);
		
		Graph firstShiftGraph = new Graph("Vahetus 1", Main.MAIN_WIDTH, (int) (Main.MAIN_HEIGHT/2.5));
		firstShiftGraph.populateData(MysqlConnector.getPackOperationsByAmount(PackManager.OPERATION_IMMUTATUD, days, firstShiftStart, firstShiftEnd));
		
		Graph secondShiftGraph = new Graph("Vahetus 2", Main.MAIN_WIDTH, (int) (Main.MAIN_HEIGHT/2.5));
		secondShiftGraph.populateData(MysqlConnector.getPackOperationsByAmount(PackManager.OPERATION_IMMUTATUD, days, secondShiftStart, secondShiftEnd));
		
		HBox buttonPanel = new HBox(padding);
		buttonPanel.getChildren().addAll(dayButton, weekButton, monthButton);
		
		mainPanel.getChildren().addAll(buttonPanel, firstShiftGraph, secondShiftGraph);
		
		dayButton.setOnAction(e -> SceneBuilder.setNewScene(new ShiftScene(1)));
		weekButton.setOnAction(e -> SceneBuilder.setNewScene(new ShiftScene(7)));
		monthButton.setOnAction(e -> SceneBuilder.setNewScene(new ShiftScene(30)));
		
		dayButton.setPrefSize(100, 20);
		weekButton.setPrefSize(100, 20);
		monthButton.setPrefSize(100, 20);
		
		borderPanel.setTop(header);
		borderPanel.setCenter(mainPanel);
		
		Scene scene = new Scene(borderPanel, Main.MAIN_WIDTH, Main.MAIN_HEIGHT);
		return scene;
	}


	
}
