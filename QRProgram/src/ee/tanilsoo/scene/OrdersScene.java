package ee.tanilsoo.scene;

import java.util.List;

import ee.tanilsoo.src.Main;
import ee.tanilsoo.src.MysqlConnector;
import ee.tanilsoo.src.Order;
import ee.tanilsoo.src.Pack;
import ee.tanilsoo.src.PackManager;
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

public class OrdersScene implements Scenable {

	public static ObservableList<Order> orders = FXCollections.observableArrayList();
	
	
	TextField companyName = new TextField();
	TextField priceField = new TextField();
	TextField additionalInfoField = new TextField();
	ComboBox<Integer> packAmount;
	ComboBox<Pack> postTypes;
	
	TableView<Order> tableView = new TableView<Order>();
	Button addNewOrderButton = new Button("Lisa");
	Button removeOrderButton = new Button("Eemalda");
	
	public OrdersScene() {
		packAmount = new ComboBox<>(getComboBoxItems(100));
		postTypes = PackManager.getPostTypesComboBox(); // TODO add postypes.
		createOrderTableView();
	}
	
	private void init(){
		updateOrderList();
	}
	
	@Override
	public Scene createScene() {
		init();
		BorderPane borderPanel = new BorderPane();
		BorderPane header = Main.getHeader();
		
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
		orderPanelInput.getChildren().add(new Label("Posti mõõdud:"));
		orderPanelInput.getChildren().add(postTypes);
		orderPanelInput.getChildren().add(new Label("Pakkide arv:"));
		orderPanelInput.getChildren().add(packAmount);
		orderPanelInput.getChildren().add(new Label("Hind:"));
		orderPanelInput.getChildren().add(priceField);
		orderPanelInput.getChildren().add(new Label("Lisa info:"));
		orderPanelInput.getChildren().add(additionalInfoField);
		orderPanelInput.getChildren().add(addNewOrderButton);

		FlowPane tableViewsPanel = new FlowPane();
		tableViewsPanel.getChildren().addAll(tableView);
		
		
		removeOrderButton.setPrefSize(140, 35);
		removeOrderButton.setOnAction(new OnButtonClicked());
		
		centerPanel.getChildren().addAll(orderPanelInput, tableView, removeOrderButton);
		
		
		borderPanel.setTop(header);
		borderPanel.setCenter(centerPanel);
		Scene scene = new Scene(borderPanel, Main.MAIN_WIDTH, Main.MAIN_HEIGHT);
		return scene;
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
	

	public void updateOrderList(){
		orders.clear();
		List<List<String>> packMysqlData = MysqlConnector.getPackOrder();
		for(List<String> data : packMysqlData){
			//name(str), length(int), diameter(int), puu(char), amt_packs(int), price(int), additional_info(str)
			orders.add(new Order(Integer.parseInt(data.get(8)), data.get(0), Integer.parseInt(data.get(1)), Integer.parseInt(data.get(2)), data.get(3), Integer.parseInt(data.get(4)),
					Integer.parseInt(data.get(5)), data.get(6), Integer.parseInt(data.get(7))));
		}
		tableView.setItems(orders);
	}
	
	public static ObservableList<Integer> getComboBoxItems(int amount){
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
					updateOrderList();
				}
			} else if(e.getSource() == removeOrderButton){
				Order orderLine = tableView.getSelectionModel().getSelectedItem();
				if(orderLine != null){
					MysqlConnector.deleteOrder(orderLine.getId());
					System.out.println(orderLine.getId());
					updateOrderList();
					System.out.println("DELETED.. Updated");
				}
			}
		}
		
	}

}
