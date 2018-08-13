package application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class HeaderController {
	@FXML public Button btBack;
	@FXML public Button btMainMenu;

	@FXML
	public void onClick_btBack() {
		int lastIndexNum = Main.history.size() - 1;
		
		lastIndexNum = validateList(lastIndexNum);
				
		FXMLStringAndController fxmlStringAndController = Main.history.get(lastIndexNum);
		Object controller = fxmlStringAndController.getController();
		String fxmlString = fxmlStringAndController.getFxmlString();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlString));
        
		loader.setController(controller);
		Parent root = null;
		try {
			root = (Parent) loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        Main.theStage.setScene(new Scene(root));
        Main.theStage.show();
	}
	
	/**
	 * If last element and 2nd to last element are equal to each other get rid of both
	 * to avoid endless looping. If not remove just the last element like normal.
	 * 
	 * @param lastIndexNum - Current last index number.
	 * @return lastIndexNum - Updated last index number, reflecting new list size.
	*/
	private int validateList(int lastIndexNum) {
		if (lastIndexNum >= 1) {
			if (!(Main.history.get(lastIndexNum).equals(Main.history.get(lastIndexNum - 1)))) {
				lastIndexNum = removeLastElementAndDecrement(lastIndexNum);
			} else {
				lastIndexNum = removeLastElementAndDecrement(lastIndexNum);
				lastIndexNum = removeLastElementAndDecrement(lastIndexNum);
			}
		}
		
		return lastIndexNum;
	}
	
	private int removeLastElementAndDecrement(int lastIndexNum) {
		Main.history.remove(lastIndexNum);
		lastIndexNum--;
		
		return lastIndexNum;
	}
	
	@FXML
	public void onClick_btMainMenu() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
	        
			MainMenuController mainMenuController = new MainMenuController();
			loader.setController(mainMenuController);
			Parent root = (Parent) loader.load();
		
			Stage currentStage = (Stage) btMainMenu.getScene().getWindow();
			currentStage.setScene(new Scene(root));
			currentStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
