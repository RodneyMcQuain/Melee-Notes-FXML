package application;
	
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import model.database.SQLiteUtils;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	public static Stage theStage;
	public static ArrayList<FXMLStringAndController> history = new ArrayList<FXMLStringAndController>();
	
	@Override
	public void start(Stage primaryStage) {
		theStage = primaryStage;
		
		startDatabase();
		
		Scene mainMenuScene = loadMainMenu();
		
		primaryStage.setScene(mainMenuScene);
		primaryStage.show();
	}
	
	public void startDatabase() {
		SQLiteUtils.buildBlankDatabase();
	}
	
	public Scene loadMainMenu() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
		MainMenuController mainMenuController = new MainMenuController();
		loader.setController(mainMenuController);
		
		Parent root = null;
		try {   
			root = (Parent) loader.load();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		return new Scene(root);	
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
