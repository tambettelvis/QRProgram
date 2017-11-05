package ee.tanilsoo.scene;

import java.util.Map;

import ee.tanilsoo.src.Main;
import ee.tanilsoo.src.MysqlConnector;
import ee.tanilsoo.src.Pack;
import ee.tanilsoo.src.PackManager;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

public class LaoScene implements Scenable {
	
	int changeX = 0;
	int changeY = 0;

	@Override
	public Scene createScene(){
		BorderPane borderPanel = new BorderPane();
		BorderPane header = Main.getHeader();
		
		TabPane tabPanel = new TabPane();
		Tab tab = new Tab("Immutatud");
		tab.setClosable(false);
		
		
		NumberAxis xAxis = new NumberAxis();
		CategoryAxis yAxis = new CategoryAxis();
		
		StackedBarChart<Number, String> barChart = new StackedBarChart<Number, String>(xAxis, yAxis);
		barChart.setTitle("Immutatud pakid");
		xAxis.tickLabelFontProperty().set(new Font("Calbiri", 15));
		xAxis.setLabel("Postid");
		yAxis.setLabel("Kokku");
		
		//Immutatud
		Map<Integer, Integer> immutatudPacks = MysqlConnector.getLaosImmutatudPakke();
		XYChart.Series<Number, String> series = new XYChart.Series<>();
		for(Map.Entry<Integer, Integer> entry : immutatudPacks.entrySet()){
			Pack p = PackManager.getPackById(entry.getKey());
			XYChart.Data<Number, String> data = new XYChart.Data<Number, String>(entry.getValue().intValue(), p.toString() + " - " + entry.getValue().intValue());
			series.getData().add(data);
		}
		
		Scene scene = new Scene(borderPanel, Main.MAIN_WIDTH, Main.MAIN_HEIGHT);
		barChart.getData().add(series);
		tab.setContent(barChart);
		tabPanel.getTabs().add(tab);
		borderPanel.setCenter(tabPanel);
		borderPanel.setTop(header);

		for (XYChart.Series<Number, String> s : barChart.getData()) {
			for (XYChart.Data<Number, String> item : s.getData()) {
				Tooltip tip = new Tooltip("Pakke: " + item.getXValue().toString() + "\n" + item.getYValue());
				Tooltip.install(item.getNode(), tip);
			
			}
		}
		
		barChart.setOnMouseDragged(mouseEventHandler);
		barChart.setOnScroll(scrollEventHandler);
		
		return scene;
	}
	
	EventHandler<MouseEvent> mouseEventHandler = new EventHandler<MouseEvent>(){

		@Override
		public void handle(MouseEvent event) {
			
			if(event.getEventType() == MouseEvent.MOUSE_PRESSED){
				
			} else if(event.getEventType() == MouseEvent.MOUSE_DRAGGED){
				
			} else if(event.getEventType() == MouseEvent.MOUSE_RELEASED){
				
			} 
			
		}
		
	};
	
	EventHandler<ScrollEvent> scrollEventHandler = new EventHandler<ScrollEvent>(){

		@Override
		public void handle(ScrollEvent event) {
		}
		
	};
	
	

}
