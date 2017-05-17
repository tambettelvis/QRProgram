package com.tanilsoo.tambet_telvis;

import javafx.scene.Scene;

public class SceneBuilder {

	public static void setNewScene(Scenable sceneable){
		Scene scene = sceneable.createScene();
		Main.primaryStage.setScene(scene);
	}
	
}
