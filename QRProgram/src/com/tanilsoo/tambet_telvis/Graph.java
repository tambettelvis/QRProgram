package com.tanilsoo.tambet_telvis;

import java.util.Map;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.FlowPane;

public class Graph extends FlowPane {

	CategoryAxis axisX = new CategoryAxis();
	NumberAxis axisY = new NumberAxis();
	LineChart<String, Number> chart;
	XYChart.Series<String, Number> series;
	
	public  Graph(){
		chart = new LineChart<>(axisX, axisY);
		chart.setPrefWidth(900);
		series = new XYChart.Series<>();
		series.setName("Immutatud pakid(viimased 30 päeva)");
		getChildren().add(chart);
	}
	
	public void clearGraph(){
		chart.getData().clear();
	}
	
	public void populateData(Map<String, Number> data){
		clearGraph();
		for(Map.Entry<String, Number> entry : data.entrySet()){
			series.getData().add(new XYChart.Data<String, Number>(entry.getKey(), entry.getValue()));
		}
		chart.getData().add(series);
	}
	
}
