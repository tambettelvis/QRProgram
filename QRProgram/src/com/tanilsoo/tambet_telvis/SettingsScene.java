package com.tanilsoo.tambet_telvis;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SettingsScene implements Scenable {

	ComboBox<String> postTypes;
	ComboBox<Integer> amountComboBox;
	Label amountOfPacks = new Label("NO VALUE");
	
	Map<String, Integer> tavaPacks;
	Map<String, Integer> immutatudPacks;
	
	Button exportButton = new Button("EXPORT EXCEL");
	
	public SettingsScene() {
		postTypes = new ComboBox<String>(OrdersScene.getPostTypeItems());
		postTypes.valueProperty().addListener(new OnComboBoxValueChange());
		amountComboBox = new ComboBox<Integer>(OrdersScene.getComboBoxItems(100));
		
		
		tavaPacks = MysqlConnector.getLaosTavaPakke();
		immutatudPacks = MysqlConnector.getLaosImmutatudPakke();
	}
	
	@Override
	public Scene createScene() {
		BorderPane mainPanel = new BorderPane();
		BorderPane header = Main.getHeader();
		
		Button saveButton = new Button("Salvesta");
		saveButton.setOnAction(new OnButtonClicked(11));
		
		VBox rows = new VBox(10);
		rows.setPadding(new Insets(20));
		
		HBox packPanel = new HBox(10);
		packPanel.getChildren().addAll(new Label("Tavalisi: "), postTypes,
				new Label("Pakkide arv: "), amountOfPacks, new Label(" | Muuda: "),
				amountComboBox, saveButton);
		
		exportButton.setOnAction(event -> exportExcel());
		rows.getChildren().addAll(packPanel, exportButton);
		
		mainPanel.setTop(header);
		mainPanel.setCenter(rows);
		Scene scene = new Scene(mainPanel, Main.MAIN_WIDTH, Main.MAIN_HEIGHT);
		return scene;
	}
	
	public void exportExcel(){
		Workbook wb = new HSSFWorkbook();
	    CreationHelper createHelper = wb.getCreationHelper();
	    Sheet sheet = wb.createSheet("new sheet");
	    
	    Row row = sheet.createRow((short)0);
	    Cell cell = row.createCell(0);
	    cell.setCellValue(1);
	    row.createCell(2).setCellValue(
	            createHelper.createRichTextString("This is a string"));
	    try {
			FileOutputStream stream = new FileOutputStream("java.xls");
			wb.write(stream);
			stream.close();
			wb.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private class OnButtonClicked implements EventHandler<ActionEvent> {
		
		int action;
		
		public OnButtonClicked(int action){
			this.action = action;
		}

		@Override
		public void handle(ActionEvent arg0) {
			int packId = Integer.parseInt(postTypes.getSelectionModel().getSelectedItem().substring(0, 1));
			System.out.println(packId);
			switch(action){
				case 0:
					//MysqlConnector.updateTavalisi(packId, amount);
					break;
			}
			 
		}
		
	}
	
	private class OnComboBoxValueChange implements ChangeListener<String>{

		@Override
		public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue) {
			String postData = newValue.substring(2).trim();
			for(Map.Entry<String, Integer> entry : tavaPacks.entrySet()){
				if(entry.getKey().equals(postData)){
					amountOfPacks.setText(String.valueOf(entry.getValue()));
				}
			}
		}
		
	}
}