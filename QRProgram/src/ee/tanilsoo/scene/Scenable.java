package ee.tanilsoo.scene;

import javafx.scene.Scene;
/**Every scene must implement interface.*/
public interface Scenable {

	/**Creates scene object with components and returns it.*/
	public Scene createScene();

}
