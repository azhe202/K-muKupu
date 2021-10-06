package application;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class RewardScreen extends GamesModule{

	private Stage stage;
	private Scene scene;
	private Parent root;

	
	public void switchToSummary(MouseEvent event) throws IOException { 
			root = FXMLLoader.load(getClass().getResource("Summary.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
	}
	
	
	
}
