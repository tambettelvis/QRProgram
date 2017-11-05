package ee.tanilsoo.scene;

import ee.tanilsoo.src.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;

public class SceneBuilder {

	public static void setNewScene(Scenable sceneable){
		Scene scene = sceneable.createScene();
		scene.widthProperty().addListener(new ChangeListener<Number>(){ //Every scene gets width and height change listener.
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number newVal) {
				Main.MAIN_WIDTH = newVal.intValue();
			}
		});
		scene.heightProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number newVal) {
				Main.MAIN_HEIGHT = newVal.intValue();
			}
		});
		Main.primaryStage.setScene(scene);
	}
	
}
