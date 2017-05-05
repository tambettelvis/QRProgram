package com.tanilsoo.tambet_telvis;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class JobsScene implements Scenable {

	public static ObservableList<Order> orders = FXCollections.observableArrayList();
	
	Main main;
	
	TextField companyName = new TextField();
	TextField priceField = new TextField();
	TextField additionalInfoField = new TextField();
	ComboBox packAmount;
	ComboBox postTypes;
	
	TableView tableView = new TableView();
	TableView jobTableView = new TableView();
	Button addNewOrderButton = new Button("Lisa");
	
	public JobsScene(Main main) {
		this.main = main;
		packAmount = new ComboBox(getComboBoxItems(100));
		postTypes = new ComboBox(getPostTypeItems());
		updateOrderList();
	}
	
	@Override
	public void setScene() {
		BorderPane borderPanel = new BorderPane();
		HBox headerBox = Main.getMainHeaderBox();
		Button backBtn = new Button("Tagasi");
		backBtn.setOnAction(e -> main.getPrimaryStage().setScene(main.getMainScene()));
		backBtn.setPrefSize(100, 20);
		headerBox.getChildren().add(backBtn);
		Main.addLogoToHeaderBox(headerBox);
		
		VBox centerPanel = new VBox();
		centerPanel.setPadding(new Insets(20));
		Label title = new Label("Lisa tellimus");
		title.setFont(new Font("Calibri", 20));
		centerPanel.getChildren().add(title);
		
		//Tellimuse Paneel.
		HBox orderPanelInput = new HBox();
		orderPanelInput.setPadding(new Insets(10));
		
		priceField.setMaxWidth(50);
		addNewOrderButton.setPrefSize(100, 20);
		addNewOrderButton.setOnAction(new OnButtonClicked());
		
		orderPanelInput.setSpacing(10);
		orderPanelInput.getChildren().add(new Label("Firma nimi:"));
		orderPanelInput.getChildren().add(companyName);
		orderPanelInput.getChildren().add(new Label("Posti m��dud:"));
		orderPanelInput.getChildren().add(postTypes);
		orderPanelInput.getChildren().add(new Label("Pakkide arv:"));
		orderPanelInput.getChildren().add(packAmount);
		orderPanelInput.getChildren().add(new Label("Hind:"));
		orderPanelInput.getChildren().add(priceField);
		orderPanelInput.getChildren().add(new Label("Lisa info:"));
		orderPanelInput.getChildren().add(additionalInfoField);
		orderPanelInput.getChildren().add(addNewOrderButton);
		
		//T��de nimekiri
		HBox jobsPanelInput = new HBox();
		//TODO------
		createOrderTableView();

		FlowPane tableViewsPanel = new FlowPane();
		tableViewsPanel.getChildren().addAll(tableView);
		
		centerPanel.getChildren().addAll(orderPanelInput, tableView);
		
		
		borderPanel.setTop(headerBox);
		borderPanel.setCenter(centerPanel);
		Scene scene = new Scene(borderPanel, Main.MAIN_WIDTH, Main.MAIN_HEIGHT);
		main.getPrimaryStage().setScene(scene);
	}
	
	private void createOrderTableView() {
		TableColumn nameColumn = new TableColumn("Tellija");
		TableColumn lengthColumn = new TableColumn("Pikkus");
		TableColumn diameterColumn = new TableColumn("Diameeter");
		TableColumn woodTypeColumn = new TableColumn("Puu");
		TableColumn amtPacksColumn = new TableColumn("Kogus");
		TableColumn priceColumn = new TableColumn("Hind");
		TableColumn additionalInfoColumn = new TableColumn("Lisa info");
		TableColumn packsDoneColumn = new TableColumn("Pakke tehtud");
		tableView.getColumns().addAll(nameColumn, lengthColumn, diameterColumn, woodTypeColumn, amtPacksColumn, priceColumn, packsDoneColumn, additionalInfoColumn);
		
		nameColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("name"));
		lengthColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("length"));
		diameterColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("diameter"));
		woodTypeColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("woodType"));
		amtPacksColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("amtOfPacks"));
		priceColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("price"));
		additionalInfoColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("additionalInfo"));
		packsDoneColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("packsDone"));
	}
	
	private ObservableList<String> getPostTypeItems(){
		ObservableList<String> items = FXCollections.observableArrayList();
		List<String> mysqlData = MysqlConnector.getPostTypes();
		for(String data : mysqlData){
			items.add(data);
		}
		return items;
	}

	public void updateOrderList(){
		orders.clear();
		List<List<String>> packMysqlData = MysqlConnector.getPackOrder();
		for(List<String> data : packMysqlData){
			//name(str), length(int), diameter(int), puu(char), amt_packs(int), price(int), additional_info(str)
			orders.add(new Order(data.get(0), Integer.parseInt(data.get(1)), Integer.parseInt(data.get(2)), data.get(3), Integer.parseInt(data.get(4)),
					Integer.parseInt(data.get(5)), data.get(6), Integer.parseInt(data.get(7))));
		}
		tableView.setItems(orders);
	}
	
	private ObservableList<Integer> getComboBoxItems(int amount){
		ObservableList<Integer> items = FXCollections.observableArrayList();
		for(int i = 1; i <= amount; i++){
			items.add(new Integer(i));
		}
		return items;
	}
	
	
	private class OnButtonClicked implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent e) {
			if(e.getSource() == addNewOrderButton){
				if(postTypes.getValue() != null && !postTypes.getValue().toString().isEmpty()){
					MysqlConnector.insertOrder(companyName.getText(), Integer.parseInt(postTypes.getValue().toString().split("\\.")[0]),
							Integer.parseInt(packAmount.getValue().toString()), Integer.parseInt(priceField.getText()), additionalInfoField.getText());
					
					//TODO Display message...
				}
			}
		}
		
	}

}
