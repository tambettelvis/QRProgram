package com.tanilsoo.tambet_telvis;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class PrintScene implements Scenable {

	ComboBox<Integer> amountComboBox;
	
	ListView<String> postTypesListView = new ListView<String>();
	ObservableList<String> postTypes = FXCollections.observableArrayList();
	
	Button printButton;
	CheckBox defaultPrinterCheckBox;
	
	public PrintScene(){
		try {
		postTypes.addAll(OrdersScene.getPostTypeItems());
		} catch(Exception e){
			System.out.println("exception captured");
		}
		postTypesListView.setItems(postTypes);
		postTypesListView.setCellFactory(cell -> {
            return new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                    	String[] split1 = item.split("\\.");
                    	String[] split2 = split1[1].split("/");
                        setText(split1[0] + ". " + "\t \t" + split2[1] + " / " + split2[0] + " / " + split2[2]);

                        setFont(Font.font(20));
                    }
                }
            };
        });
		amountComboBox = new ComboBox<Integer>(OrdersScene.getComboBoxItems(10));
		amountComboBox.getSelectionModel().selectFirst();
	}
	
	@Override
	public Scene createScene() {
		BorderPane mainPanel = new BorderPane();

		VBox rightPanel = new VBox(10);
		rightPanel.setPadding(new Insets(10));
		Label printTitleLabel = new Label("Prindi QR koodid");
		printTitleLabel.setFont(new Font("Calibri", 15));
		HBox printLine1 = new HBox(3);
		HBox printLine2 = new HBox(3);
		HBox printLine3 = new HBox(3);
		
		defaultPrinterCheckBox = new CheckBox();
		defaultPrinterCheckBox.setSelected(true);
		printButton = new Button("PRINDI");
		printButton.setOnAction(event -> onButtonClicked());
		printButton.setPrefSize(200, 40);
		
		printLine1.getChildren().addAll(new Label("Kogus: "), amountComboBox);
		printLine2.getChildren().addAll(new Label("Printer vaikimisi: "), defaultPrinterCheckBox);
		printLine3.getChildren().addAll(printButton);
		rightPanel.getChildren().addAll(printTitleLabel, printLine1, printLine2, printLine3);
		
		mainPanel.setTop(Main.getHeader());
		mainPanel.setRight(rightPanel);
		mainPanel.setCenter(postTypesListView);
		
		Scene scene = new Scene(mainPanel, Main.MAIN_WIDTH, Main.MAIN_HEIGHT);
		return scene;
	}
	
	public void onButtonClicked() {
		String item = postTypesListView.getSelectionModel().getSelectedItem();
		System.out.println(item);
		if(item != null && !item.isEmpty() && 
				amountComboBox.getValue() != null){
			ConnectPrinter printer = new ConnectPrinter();
			int packId = Integer.parseInt(item.split("\\.")[0]);
			System.out.println(packId);
			printer.printToPrinter(MysqlConnector.getFileNameById(packId), defaultPrinterCheckBox.isSelected(), amountComboBox.getValue());
			System.out.println("PRINTING...");
		} else {
		}
	}

}
