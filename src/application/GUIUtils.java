package application;

import javafx.scene.control.ComboBox;
import model.database.PlayerDao;
import model.database.PlayerDaoImpl;

public class GUIUtils {
	public static void populateCharacterComboBox(ComboBox<String> characterComboBox) {
		characterComboBox.getItems().clear();
		characterComboBox.getItems().addAll("Fox", "Falco", "Marth", "Sheik", "Jigglypuff", "Peach", "Ice Climbers", "Cptn. Falcon", "Pikachu", 
											"Samus", "Dr. Mario", "Yoshi", "Luigi", "Gannon", "Mario", "Young Link", "Donkey Kong", "Link", 
											"Mr. Game & Watch", "Roy", "Mewtwo", "Zelda", "Ness", "Pichu", "Bowser", "Kirby");
	}
	
	public static void populateCharacterComboBoxStats(ComboBox<String> characterComboBox) {
		characterComboBox.getItems().clear();
		characterComboBox.getItems().addAll("All Characters", "Fox", "Falco", "Marth", "Sheik", "Jigglypuff", "Peach", "Ice Climbers", "Cptn. Falcon", "Pikachu", 
											"Samus", "Dr. Mario", "Yoshi", "Luigi", "Gannon", "Mario", "Young Link", "Donkey Kong", "Link", 
											"Mr. Game & Watch", "Roy", "Mewtwo", "Zelda", "Ness", "Pichu", "Bowser", "Kirby");
	}
	
	public static void populateStageComboBox(ComboBox<String> stageComboBox) {
		stageComboBox.getItems().clear();
		stageComboBox.getItems().addAll("Battlefield", "Dreamland", "Yoshi's Story", "Fountain of Dreams", "Pokemon Stadium", "Final Destination");
	}
	
	public static void populatePlayerComboBox(ComboBox<String> playerComboBox) {
		PlayerDao playerDao = new PlayerDaoImpl();
		int userID = 1; //placeholder
    	playerDao.populatePlayerComboBox(playerComboBox, userID);
	}
	
	public static void populateOutcomeComboBox(ComboBox<String> outcomeComboBox) {
		outcomeComboBox.getItems().clear();
		outcomeComboBox.getItems().addAll("Won", "Lost");
	}
	
	public static void populateTypeComboBox(ComboBox<String> typeComboBox) {
		typeComboBox.getItems().clear();
		typeComboBox.getItems().addAll("Tournament", "Money Match", "Friendly");
	}
	
	public static void populateFormatComboBox(ComboBox<String> formatComboBox) {
		formatComboBox.getItems().clear();
		formatComboBox.getItems().addAll("Bo3", "Bo5");
	}
}
