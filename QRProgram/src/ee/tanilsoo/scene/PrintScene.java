package ee.tanilsoo.scene;


import ee.tanilsoo.src.ConnectPrinter;
import ee.tanilsoo.src.Main;
import ee.tanilsoo.src.Pack;
import ee.tanilsoo.src.PackManager;
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
	
	ListView<Pack> postTypesListView = new ListView<Pack>();
	ObservableList<Pack> postTypes = FXCollections.observableArrayList();
	
	Button printButton;
	CheckBox defaultPrinterCheckBox;
	
	public PrintScene(){
		PackManager.refreshPacksList();
		postTypes.addAll(PackManager.packs);
		postTypesListView.setItems(postTypes);
		postTypesListView.setCellFactory(cell -> {
            return new ListCell<Pack>() {
                @Override
                protected void updateItem(Pack item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                    	if(item.getAdditionInformation() != null){
                    		setText(String.format("%d / %d / %s - %s", item.getDiameter(), item.getLength(), item.getPuu(), item.getAdditionInformation()));
                    	} else {
                    		setText(String.format("%d / %d / %s", item.getDiameter(), item.getLength(), item.getPuu()));
                    	}
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
		Pack item = postTypesListView.getSelectionModel().getSelectedItem();
		if(item != null && amountComboBox.getValue() != null){
			ConnectPrinter printer = new ConnectPrinter();
			printer.printToPrinter(item.getUniqueFile(), defaultPrinterCheckBox.isSelected(), amountComboBox.getValue());
			System.out.println(item);
			System.out.println("PRINTING...");
		} else {
		}
	}

}
