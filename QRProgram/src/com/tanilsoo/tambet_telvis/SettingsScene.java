package com.tanilsoo.tambet_telvis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class SettingsScene implements Scenable {


	ComboBox<Integer> packAmount;
	ComboBox<Pack> postTypes1;
	Button lisaButton = new Button("Lisa");
	Button eemaldaButton = new Button("Eemalda");
	
	ComboBox<Pack> postTypes2;
	ComboBox<Integer> amountComboBox;
	Label amountOfPacks = new Label("NO VALUE");
	
	Map<Integer, Integer> immutatudPacks;
	
	Button saveButton = new Button("Salvesta");
	Button exportButton = new Button("EXPORT EXCEL");
	
	public SettingsScene() {
		packAmount = new ComboBox<>(OrdersScene.getComboBoxItems(100));
		postTypes1 = PackManager.getPostTypesComboBox();
		
		postTypes2 = PackManager.getPostTypesComboBox();
		postTypes2.valueProperty().addListener(new OnComboBoxValueChange());
		amountComboBox = new ComboBox<Integer>(OrdersScene.getComboBoxItems(100));
		
		immutatudPacks = MysqlConnector.getLaosImmutatudPakke();
	}
	
	@Override
	public Scene createScene() {
		BorderPane mainPanel = new BorderPane();
		BorderPane header = Main.getHeader();
		
		Label l = new Label("Pakkide haldus");
		l.setFont(new Font("Calibri", 18));
		lisaButton.setOnAction(new OnButtonClicked());
		eemaldaButton.setOnAction(new OnButtonClicked());
		HBox lisaPackPanel = new HBox(10);
		lisaPackPanel.getChildren().addAll(new Label("Lisa/eemalda pakke: "), postTypes1, packAmount, lisaButton, eemaldaButton);
		
		VBox addPackPanel = new VBox(10);
		addPackPanel.getChildren().addAll(l, lisaPackPanel);
		
		
		saveButton.setOnAction(new OnButtonClicked());
		
		VBox rows = new VBox(10);
		rows.setPadding(new Insets(20));
		
		HBox packPanel = new HBox(10);
		packPanel.getChildren().addAll(new Label("Immutatud: "), postTypes2,
				new Label("Pakkide arv: "), amountOfPacks, new Label(" | Muuda: "),
				amountComboBox, saveButton);
		
		HBox exportPanel = new HBox(10);
		exportButton.setOnAction(event -> exportExcel());
		exportPanel.getChildren().addAll(new Label("Ekspordi pakkide arv: "), exportButton);
		rows.getChildren().addAll(addPackPanel, packPanel, exportPanel);
		
		mainPanel.setTop(header);
		mainPanel.setCenter(rows);
		Scene scene = new Scene(mainPanel, Main.MAIN_WIDTH, Main.MAIN_HEIGHT);
		return scene;
	}
	
	public void exportExcel(){
		Workbook wb = new HSSFWorkbook();
	    CreationHelper createHelper = wb.getCreationHelper();
	    Sheet sheet = wb.createSheet("new sheet");
	    
	    Map<Integer, Integer> immutatud = MysqlConnector.getLaosImmutatudPakke();
	    

	    int startRow = 3;
	    
	    Row row = sheet.createRow(startRow);
	    row.createCell(0).setCellValue(createHelper.createRichTextString("Immutatud pakid"));
	    startRow++;
	    Row titles = sheet.createRow(startRow);
	    titles.createCell(0).setCellValue(createHelper.createRichTextString("Post Type"));
	    titles.createCell(1).setCellValue(createHelper.createRichTextString("Amount"));
	    startRow++;
	    
	    /*Iterator<Map.Entry<String, Integer>> it2= immutatud.entrySet().iterator();
	    while (it2.hasNext()) {
	    	Map.Entry<String, Integer> entry = it2.next();
	    	Row r = sheet.createRow(startRow);
	    	r.createCell(0).setCellValue(createHelper.createRichTextString(entry.getKey()));
		    r.createCell(1).setCellValue(entry.getValue());
		    startRow += 1;
	    }*/
	    
	    FileChooser fileChooser = new FileChooser();
	    fileChooser.setTitle("Salvesta fail");
	    FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("Excel failid", "*.xls", "*.xlsx", "*.xlt");
	    fileChooser.getExtensionFilters().add(fileExtensions);
	    
	    File file = fileChooser.showSaveDialog(Main.primaryStage);
	    if(file!= null){
		    try {
				FileOutputStream stream = new FileOutputStream(file);
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
	}
	
	private class OnButtonClicked implements EventHandler<ActionEvent> {
	

		@Override
		public void handle(ActionEvent e) {
			
			int id = postTypes1.getValue().getId();
			int amt = Integer.parseInt(packAmount.getValue().toString());
			
			
			
			if(e.getSource() == lisaButton){
				if(postTypes1.getValue() == null)
					return;
				if(MysqlConnector.isPackInImmutatud(id)){
					MysqlConnector.incrementLaosImmutatudPack(id, amt);
				} else {
					MysqlConnector.addPackInToLaosImmutatud(id, amt);
				}
				Alert alert = new Alert(AlertType.CONFIRMATION, "Lisasin lattu " + amt + " pakki " + postTypes1.getValue().toString(), ButtonType.OK);
				alert.show();
			} else if(e.getSource() == eemaldaButton){
				if(postTypes1.getValue() == null)
					return;
				if(MysqlConnector.isPackInImmutatud(id)){
					MysqlConnector.decrementLaosImmutatudPack(id, amt);
					Alert alert = new Alert(AlertType.CONFIRMATION, "Eemaldasin laost " + amt + " pakki " + postTypes1.getValue().toString(), ButtonType.OK);
					alert.show();
				}
			} else if(e.getSource() == saveButton){
				if(immutatudPacks.containsKey(postTypes2.getValue().getId())){
					MysqlConnector.updateImmutatud(postTypes2.getValue().getId(), amountComboBox.getValue());
				} 
			} else if(e.getSource() == exportButton){
				
			}
			 
		}
		
	}
	
	private class OnComboBoxValueChange implements ChangeListener<Pack>{

		@Override
		public void changed(ObservableValue<? extends Pack> ov, Pack oldValue, Pack newValue) {
			
			for(Map.Entry<Integer, Integer> entry : immutatudPacks.entrySet()){
				if(entry.getKey().equals(newValue.getId())){
					amountOfPacks.setText(String.valueOf(entry.getValue()));
					return;
				}
			}
			amountOfPacks.setText("0");
		}
		
	}
	
	
}
