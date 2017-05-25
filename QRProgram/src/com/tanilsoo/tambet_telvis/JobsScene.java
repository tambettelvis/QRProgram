package com.tanilsoo.tambet_telvis;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;

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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class JobsScene implements Scenable {

	TableView<Job> jobsTable = new TableView<Job>();
	ObservableList<Job> jobsList = FXCollections.observableArrayList();
	
	Button moveDown = new Button("Alla");
	Button moveUp = new Button("Ülesse");
	Button addNewJobButton = new Button("Lisa");
	Button removeItemButton = new Button("Eemalda");
	
	ComboBox<Integer> priorityComboBox = new ComboBox<Integer>();
	
	TextField descriptionTextField = new TextField();
	
	public JobsScene(){
		ObservableList<Integer> comboBoxItems = OrdersScene.getComboBoxItems(MysqlConnector.getHighestPriority() + 1);
		priorityComboBox.setItems(comboBoxItems);
		createTableColumns();
	}
	
	private void init(){
		Map<String, Integer> jobData = MysqlConnector.getJobData();
		List<Job> temp = new ArrayList<>();
		for(Map.Entry<String, Integer> entry : jobData.entrySet()){
			temp.add(new Job(entry.getKey(), entry.getValue()));
		}
		
		Job.jobs = temp;
		jobsList.setAll(Job.jobs);
		jobsTable.setItems(jobsList);
	}
	
	@Override
	public Scene createScene() {
		init();
		BorderPane mainPanel = new BorderPane();
		BorderPane header = Main.getHeader();
		
		BorderPane centerPanel = new BorderPane();
		System.out.println(descriptionTextField.getWidth());
		moveDown.setPrefSize(149, 20);
		moveUp.setPrefSize(149, 20);
		removeItemButton.setPrefSize(149, 20);
		moveDown.setOnAction(new OnButtonClicked());
		moveUp.setOnAction(new OnButtonClicked());
		addNewJobButton.setOnAction(new OnButtonClicked());
		removeItemButton.setOnAction(new OnButtonClicked());
		
		VBox newJobPanel = new VBox(3);
		Label label = new Label();
		
		label.setFont(new Font("Calibri", 17));
		newJobPanel.getChildren().addAll(new Label("Kirjeldus:"), descriptionTextField,
				new Label("Tähtsus: "), priorityComboBox, addNewJobButton);
		
		VBox rightSidePanel = new VBox(3);
		rightSidePanel.getChildren().addAll(moveUp, moveDown, removeItemButton, label, newJobPanel);
		
		centerPanel.setCenter(jobsTable);
		centerPanel.setRight(rightSidePanel);
		mainPanel.setCenter(centerPanel);
		mainPanel.setTop(header);
		Scene scene = new Scene(mainPanel, Main.MAIN_WIDTH, Main.MAIN_HEIGHT);
		return scene;
	}
	
	private void createTableColumns(){
		TableColumn<Job, Integer> priorityColumn = new TableColumn<Job, Integer>("Tähtsus");
		TableColumn<Job, String> descriptionColumn = new TableColumn<Job, String>("Kirjeldus");
		priorityColumn.setCellValueFactory(new PropertyValueFactory<Job, Integer>("priority"));
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<Job, String>("description"));
		priorityColumn.setSortType(TableColumn.SortType.ASCENDING);
		
		jobsTable.getColumns().addAll(priorityColumn, descriptionColumn);
		jobsTable.getSortOrder().add(priorityColumn);
	}
	
	private void refresh(){
		SceneBuilder.setNewScene(new JobsScene());
	}
	
	private class OnButtonClicked implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent e) {
			Job job = jobsTable.getSelectionModel().getSelectedItem();
			if(e.getSource() == moveUp){
				if(job != null){
					job.updatePriority(true);
					refresh();
				}
			} else if(e.getSource() == moveDown){
				if(job != null){
					job.updatePriority(false);
					refresh();
				} else {
				}
			} else if(e.getSource() == addNewJobButton){
				if(descriptionTextField.getText().isEmpty() || priorityComboBox.getSelectionModel().getSelectedItem() == null){
					//TODO error...
					return;
				}
				
				MysqlConnector.insertNewJob(descriptionTextField.getText(), priorityComboBox.getSelectionModel().getSelectedItem());
				refresh();
			} else if(e.getSource() == removeItemButton){
				if(job != null){
					MysqlConnector.deleteJob(job.getDescription(), job.getPriority());
					refresh();
				} else {
					System.out.println("Job == null");
				}
			}
		}
		
	}

}
