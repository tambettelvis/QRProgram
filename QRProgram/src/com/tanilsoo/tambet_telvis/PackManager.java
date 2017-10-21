package com.tanilsoo.tambet_telvis;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class PackManager {

	public static List<Pack> packs = new ArrayList<Pack>();
	
	public static void addPack(Pack p){
		packs.add(p);
	}
	
	public static void refreshPacksList(){
		packs.clear();
		packs.addAll(MysqlConnector.getPostTypes());
	}
	
	public static Pack getPackById(int id){
		for(Pack p : packs){
			if(p.getId() == id){
				return p;
			}
		}
		return null;
	}
	
	public static Pack getPackByUniqueFile(String uniqueFile){
		for(Pack p : packs){
			if(p.getUniqueFile().equals(uniqueFile)){
				return p;
			}
		}
		return null;
	}
	
	public static ComboBox<Pack> getPostTypesComboBox(){
		ObservableList<Pack> observableList = FXCollections.observableArrayList(packs);
		ComboBox<Pack> postTypes = new ComboBox<>(observableList);
		
		Callback<ListView<Pack>,ListCell<Pack>> cellFactory = new Callback<ListView<Pack>,ListCell<Pack>>(){
		    @Override
		    public ListCell<Pack> call(ListView<Pack> l){
		        return new ListCell<Pack>(){
		            @Override
		            protected void updateItem(Pack item, boolean empty) {
		                super.updateItem(item, empty);
		                if (item == null || empty) {
		                    setGraphic(null);
		                } else {
		                	setText(String.format("%d / %d / %s - %s", item.getDiameter(), item.getLength(), item.getPuu(), item.getAdditionInformation()));
		                }
		            }
		        } ;
		    }
		};
		
		postTypes.setButtonCell(cellFactory.call(null));
		postTypes.setCellFactory(cellFactory);
		
		return postTypes;
	}
	
	
}
