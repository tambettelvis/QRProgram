package ee.tanilsoo.scene;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import ee.tanilsoo.src.FileHandler;
import ee.tanilsoo.src.Main;
import ee.tanilsoo.src.MysqlConnector;
import ee.tanilsoo.src.Pack;
import ee.tanilsoo.src.PackManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class SettingsScene implements Scenable {

	Button exportButton = new Button("EXPORT EXCEL");
	Button changeButton = new Button("Muuda");
	
	ComboBox<Integer> comboBox;
	
	ObservableList<Pack> packs = FXCollections.observableArrayList();
	TableView<Pack> tableView;
	
	public SettingsScene() {
		comboBox = new ComboBox<>(OrdersScene.getComboBoxItems(100));
	}
	
	@Override
	public Scene createScene() {
		BorderPane mainPanel = new BorderPane();
		BorderPane header = Main.getHeader();
		
		changeButton.setOnAction(new OnButtonClicked());
		
		tableView = new TableView<>();
		createTable();
		updateTableView();
		
		Label l = new Label("Pakkide haldus");
		l.setFont(new Font("Calibri", 18));
		
		VBox rows = new VBox(10);
		
		HBox exportPanel = new HBox(10);
		exportButton.setOnAction(event -> FileHandler.exportExcel());
		exportPanel.getChildren().addAll(new Label("Ekspordi pakkide arv: "), exportButton);
		rows.getChildren().addAll(exportPanel, l, new HBox(10, comboBox, changeButton));
		
		mainPanel.setTop(header);
		mainPanel.setRight(rows);
		mainPanel.setCenter(tableView);
		Scene scene = new Scene(mainPanel, Main.MAIN_WIDTH, Main.MAIN_HEIGHT);
		return scene;
	}
	
	
	private void createTable(){
		TableColumn column1 = new TableColumn("Diameeter");
		column1.setCellValueFactory(new PropertyValueFactory<Pack, Integer>("diameter"));
		TableColumn column2 = new TableColumn("Pikkus");
		column2.setCellValueFactory(new PropertyValueFactory<Pack, Integer>("length"));
		TableColumn column3 = new TableColumn("Type");
		column3.setCellValueFactory(new PropertyValueFactory<Pack, Integer>("puu"));
		TableColumn column4 = new TableColumn("Lisainfo");
		column4.setCellValueFactory(new PropertyValueFactory<Pack, Integer>("additionInformation"));
		TableColumn column5 = new TableColumn("Kogus");
		column5.setCellValueFactory(new PropertyValueFactory<Pack, Integer>("amount"));
		
		tableView.getColumns().addAll(column1, column2, column3, column4, column5);
		
		tableView.getSelectionModel().selectedItemProperty().addListener((list, oldVal, newVal) -> {
			if(newVal != null)
				comboBox.setValue(newVal.getAmount());
		});
	}
	
	private void updateTableView(){
		
		List<Pack> packList = PackManager.getPacks();
		
		List<Pack> emptyPacks = new ArrayList<>();
		List<Pack> nonEmptyPacks = new ArrayList<>();
		for(Pack p : packList){
			if(p.getAmount() > 0)
				nonEmptyPacks.add(p);
			else
				emptyPacks.add(p);
		}
		
		emptyPacks.stream().sorted(Comparator.comparing(Pack::getDiameter).thenComparing(Pack::getLength));
		nonEmptyPacks.stream().sorted(Comparator.comparing(Pack::getDiameter).thenComparing(Pack::getLength));
		
		nonEmptyPacks.addAll(emptyPacks);
		packs.setAll(nonEmptyPacks);
		
		tableView.setItems(packs);
	}
	
	
	
	private class OnButtonClicked implements EventHandler<ActionEvent> {
	
		@Override
		public void handle(ActionEvent e) {
			if(e.getSource() == changeButton){
				Pack p = tableView.getSelectionModel().getSelectedItem();
				if(comboBox.getValue() == null || p == null){
					MainScene.displayErrorMessage("Vali pakk"); 
					return;
				}
				System.out.println(p.getId());
				MysqlConnector.setPackAmount(p.getId(), comboBox.getValue());
				updateTableView();
				MainScene.displayConfirmationMessage("Uuendatud!");
			}
		}
		
	}
	
}
