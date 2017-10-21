package com.tanilsoo.tambet_telvis;

import java.util.Map;

import org.apache.poi.util.SystemOutLogger;

import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

public class LaoScene implements Scenable {
	

	@Override
	public Scene createScene(){
		BorderPane borderPanel = new BorderPane();
		BorderPane header = Main.getHeader();
		
		TabPane tabPanel = new TabPane();
		Tab tab = new Tab("Immutatud");
		tab.setClosable(false);
		
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		
		BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis, yAxis);
	
		barChart.setTitle("Immutatud pakid");
		xAxis.tickLabelFontProperty().set(new Font("Calbiri", 15));
		xAxis.setLabel("Postid");
		yAxis.setLabel("Kokku");
		
		//Immutatud
		Map<Integer, Integer> immutatudPacks = MysqlConnector.getLaosImmutatudPakke();
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		for(Map.Entry<Integer, Integer> entry : immutatudPacks.entrySet()){
			Pack p = PackManager.getPackById(entry.getKey());
			XYChart.Data<String, Number> data = new XYChart.Data<String, Number>(p.toString(), entry.getValue().intValue());
			series.getData().add(data);
		}
		
		Scene scene = new Scene(borderPanel, Main.MAIN_WIDTH, Main.MAIN_HEIGHT);
		barChart.getData().add(series);
		tab.setContent(barChart);
		tabPanel.getTabs().add(tab);
		borderPanel.setCenter(tabPanel);
		borderPanel.setTop(header);

		for (XYChart.Series<String, Number> s : barChart.getData()) {
			for (XYChart.Data<String, Number> item : s.getData()) {
				Tooltip tip = new Tooltip(item.getXValue().toString() + "\n" + "Pakke: " + item.getYValue());
				Tooltip.install(item.getNode(), tip);
			
			}
		}
		
		return scene;
	}
	
	
	

}
