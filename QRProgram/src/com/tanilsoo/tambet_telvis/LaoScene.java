package com.tanilsoo.tambet_telvis;

import java.util.Map;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class LaoScene implements Scenable {
	
	Main main;
	
	public LaoScene(Main main){
		this.main = main;
	}

	@Override
	public void setScene(){
		BorderPane borderPanel = new BorderPane();
		
		HBox headerBox = Main.getMainHeaderBox();
		Button backBtn = new Button("Tagasi");
		backBtn.setOnAction(e -> main.getPrimaryStage().setScene(main.getMainScene()));
		backBtn.setPrefSize(100, 20);
		//backBtn.setOnAction(new OnButtonClicked());
		headerBox.getChildren().add(backBtn);
		
		TabPane tabPanel = new TabPane();
		Tab tab1 = new Tab("Tavaline");
		Tab tab2 = new Tab("Immutatud");
		tab1.setClosable(false);
		tab2.setClosable(false);
		
		CategoryAxis xAxis1 = new CategoryAxis();
		NumberAxis yAxis1 = new NumberAxis();
		
		CategoryAxis xAxis2 = new CategoryAxis();
		NumberAxis yAxis2 = new NumberAxis();
		
		BarChart<String, Number> barChart1 = new BarChart<String, Number>(xAxis1, yAxis1);
		barChart1.setTitle("Immutamata pakid");
		BarChart<String, Number> barChart2 = new BarChart<String, Number>(xAxis2, yAxis2);
		barChart2.setTitle("Immutatud pakid");
		xAxis1.setLabel("Postid");
		yAxis1.setLabel("Kokku");
		xAxis2.setLabel("Postid");
		yAxis2.setLabel("Kokku");
		
		//Immutamata
		Map<String, Integer> tavaPacks = MysqlConnector.getLaosTavaPakke();
		XYChart.Series series1 = new XYChart.Series();
		for(Map.Entry<String, Integer> entry : tavaPacks.entrySet()){
			series1.getData().add(new XYChart.Data(entry.getKey(), entry.getValue().intValue()));
		}
        
		
		
		//Immutatud
		Map<String, Integer> immutatudPacks = MysqlConnector.getLaosImmutatudPakke();
		XYChart.Series series2 = new XYChart.Series();
		for(Map.Entry<String, Integer> entry : immutatudPacks.entrySet()){
			series2.getData().add(new XYChart.Data(entry.getKey(), entry.getValue().intValue()));
		}
        
		Scene scene = new Scene(borderPanel, Main.MAIN_WIDTH, Main.MAIN_HEIGHT);
		barChart1.getData().add(series1);
		barChart2.getData().add(series2);
		tab1.setContent(barChart1);
		tab2.setContent(barChart2);
		tabPanel.getTabs().addAll(tab1, tab2);
		borderPanel.setCenter(tabPanel);
		borderPanel.setTop(headerBox);

		main.getPrimaryStage().setScene(scene);
	}
	
	
	

}
