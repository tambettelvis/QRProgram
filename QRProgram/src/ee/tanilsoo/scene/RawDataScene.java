package ee.tanilsoo.scene;

import java.util.List;

import ee.tanilsoo.src.Main;
import ee.tanilsoo.src.MysqlConnector;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class RawDataScene implements Scenable {

	public static ObservableList<String> employeeData = FXCollections.observableArrayList();
	public static ObservableList<String> employeeInfoData = FXCollections.observableArrayList();
	public static ObservableList<String> lastOperationData = FXCollections.observableArrayList();
	
	Label employeeAmtOfImmutamata;
	Label employeeAmtOfImmutatud;
	Label employeeAmtOfLaaditud;
	
	private void init(){
		updateEmployeePanel();
		updateLastActionPanel();
	}
	
	@Override
	public Scene createScene() {
		init();
		BorderPane mainPanel = new BorderPane();
		
		
		// Packtype informations...
		FlowPane employeeInfoPanel = new FlowPane();
		employeeInfoPanel.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		employeeInfoPanel.setPadding(new Insets(10));

		ListView<String> employeeListView = new ListView<String>(employeeData);
		employeeListView.setPrefHeight(200);
		ListView<String> employeeDataListView = new ListView<String>(employeeInfoData);
		employeeDataListView.setPrefHeight(200);
		employeeDataListView.setPrefWidth(400);
		employeeListView.getSelectionModel().selectedItemProperty().addListener(new OnListViewItemChange());

		VBox additionalEmployeePanel = new VBox();
		additionalEmployeePanel.setSpacing(30);
		employeeAmtOfImmutamata = new Label("Pakke lattu tootnud(7 päeva): ");
		employeeAmtOfImmutatud = new Label("Pakke immutatud    (7 päeva): ");
		employeeAmtOfLaaditud = new Label("Pakke laaditud     (7 päeva): ");
		employeeAmtOfImmutamata.setFont(new Font("Calibri", 15));
		employeeAmtOfImmutatud.setFont(new Font("Calibri", 15));
		employeeAmtOfLaaditud.setFont(new Font("Calibri", 15));
		additionalEmployeePanel.getChildren().addAll(employeeAmtOfImmutamata, employeeAmtOfImmutatud,
				employeeAmtOfLaaditud);

		employeeInfoPanel.getChildren().addAll(employeeListView, employeeDataListView, additionalEmployeePanel);

		// Last actions info
		FlowPane lastActionsPanel = new FlowPane();
		ListView<String> lastActionsListView = new ListView<String>(lastOperationData);
		lastActionsPanel.getChildren().add(lastActionsListView);

		VBox centerPanel = new VBox();
		centerPanel.setSpacing(10);
		centerPanel.getChildren().addAll(employeeInfoPanel, lastActionsListView);

		
		mainPanel.setTop(Main.getHeader());
		mainPanel.setCenter(centerPanel);
		Scene scene = new Scene(mainPanel, Main.MAIN_WIDTH, Main.MAIN_HEIGHT);
		return scene;
	}
	
	private static void updateEmployeePanel(){
		employeeData.clear();
		List<String> packages = MysqlConnector.getEmployeeNames();
		
		for(String s : packages){
			employeeData.add(s);
		}

	}
	
	private static void updateLastActionPanel(){
		lastOperationData.clear();
		List<String> lastActionData = MysqlConnector.getLastOperationsData();
		for(String s : lastActionData){
			lastOperationData.add(s);
		}
	}
	
	private static void updateEmployeeDataPanel(String employeeName){
		employeeInfoData.clear();
		List<String> employeeInfo = MysqlConnector.getPackOperationDataByEmployee(employeeName);
		for(String s : employeeInfo){
			employeeInfoData.add(s);
		}
		
	}
	
	private class OnListViewItemChange implements ChangeListener<String> {

		@Override
		public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue) {
			updateEmployeeDataPanel(newValue);
			employeeAmtOfImmutamata.setText(
					"Pakke lattu tootnud(7 päeva): " + MysqlConnector.getPackImmutamatAmountByEmployee(newValue, 7));
			employeeAmtOfImmutatud.setText(
					"Pakke immutatud    (7 päeva): " + MysqlConnector.getPackImmutatudAmountByEmployee(newValue, 7));
			employeeAmtOfLaaditud.setText(
					"Pakke laaditud     (7 päeva): " + MysqlConnector.getPackLaaditudAmountByEmployee(newValue, 7));
		}

	}

}
