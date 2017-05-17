package com.tanilsoo.tambet_telvis;


import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

public class JobsScene implements Scenable {

	TableView<Job> jobsTable = new TableView<Job>();
	
	@Override
	public Scene createScene() {
		BorderPane mainPanel = new BorderPane();
		Button backBtn = new Button("Tagasi");
		backBtn.setOnAction(e -> SceneBuilder.setNewScene(new MainScene()));
		BorderPane header = MainScene.createHeader(backBtn);
		
		createTableColumns();
		mainPanel.setCenter(jobsTable);
		mainPanel.setTop(header);
		Scene scene = new Scene(mainPanel, Main.MAIN_WIDTH, Main.MAIN_HEIGHT);
		return scene;
	}
	
	private void createTableColumns(){
		TableColumn<Job, Integer> priorityColumn = new TableColumn<Job, Integer>("Tähtsus");
		TableColumn<Job, String> descriptionColumn = new TableColumn<Job, String>("Kirjeldus");
		priorityColumn.setCellValueFactory(new PropertyValueFactory<Job, Integer>("priority"));
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<Job, String>("description"));
		jobsTable.getColumns().addAll(priorityColumn, descriptionColumn);
	}

}
