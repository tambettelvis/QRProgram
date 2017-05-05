package com.tanilsoo.tambet_telvis;

import java.util.Map;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EmployeeScene implements Scenable {
	
	Button dayButton;
	Button weekButton;
	Button monthButton;
	
	XYChart.Series tavalineSeries;
	XYChart.Series immutatudSeries;
	XYChart.Series laaditudSeries;
	
	BarChart<String, Number> barChart;
	
	Main main;

	public EmployeeScene(Main main){
		this.main = main;
	}
	
	/** Default 1 day scene. */
	public void setScene(){
		setScene(1);
	}
	
	public void setScene(int days){
		
		BorderPane borderPanel = new BorderPane();
		
		VBox leftPanel = new VBox();
		leftPanel.setPadding(new Insets(10));
		leftPanel.setSpacing(10);
		
		HBox headerBox = Main.getMainHeaderBox();
		Button backButton = new Button("Tagasi");
		backButton.setPrefSize(100, 20);
		backButton.setOnAction(e -> main.getPrimaryStage().setScene(main.getMainScene()));
		headerBox.getChildren().add(backButton);
		Main.addLogoToHeaderBox(headerBox);
		
		Button dayButton = new Button("1 Päev");
		Button weekButton = new Button("Nädal");
		Button monthButton = new Button("Kuu");
		dayButton.setOnAction(e -> setScene(1));
		weekButton.setOnAction(e -> setScene(7));
		monthButton.setOnAction(e -> setScene(30));
		dayButton.setPrefSize(100, 20);
		weekButton.setPrefSize(100, 20);
		monthButton.setPrefSize(100, 20);
		
		leftPanel.getChildren().addAll(dayButton, weekButton, monthButton);
		
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Töötaja nimi");
		yAxis.setLabel("Kogus");
		
		barChart = new BarChart<String, Number>(xAxis, yAxis);
		tavalineSeries = new XYChart.Series();
		immutatudSeries = new XYChart.Series();
		laaditudSeries = new XYChart.Series();
		tavalineSeries.setName("Immutamata pakid");
		immutatudSeries.setName("Immutatud pakid");
		laaditudSeries.setName("Laaditud pakid");
		
		barChart.getData().add(tavalineSeries);
		barChart.getData().add(immutatudSeries);
		barChart.getData().add(laaditudSeries);
		
		//Tavaline
		for(Map.Entry<String, Integer> entry : MysqlConnector.getEmployeeOperationsAmount(2, days).entrySet()){
			tavalineSeries.getData().add(new XYChart.Data(entry.getKey(), entry.getValue().intValue()));
		}
		
		//Tavaline
		for(Map.Entry<String, Integer> entry : MysqlConnector.getEmployeeOperationsAmount(3, days).entrySet()){
			immutatudSeries.getData().add(new XYChart.Data(entry.getKey(), entry.getValue().intValue()));
		}
		
		//Tavaline
		for(Map.Entry<String, Integer> entry : MysqlConnector.getEmployeeOperationsAmount(4, days).entrySet()){
			laaditudSeries.getData().add(new XYChart.Data(entry.getKey(), entry.getValue().intValue()));
		}
		
		borderPanel.setCenter(barChart);
		borderPanel.setLeft(leftPanel);
		borderPanel.setTop(headerBox);
		Scene scene = new Scene(borderPanel, Main.MAIN_WIDTH, Main.MAIN_HEIGHT);
		main.getPrimaryStage().setScene(scene);
		
	}


	
}
