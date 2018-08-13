package model;

import application.FXMLStringAndController;
import application.Main;

public class GeneralUtils {
	//index 0 is empty to make comparing to the auto incremented database id intuitive
	public static final String[] CHARACTERS = {"", "Fox", "Falco", "Marth", "Sheik", "Jigglypuff", "Peach", "Ice Climbers", "Cptn. Falcon", "Pikachu", 
			   "Samus", "Dr. Mario", "Yoshi", "Luigi", "Gannon", "Mario", "Young Link", "Donkey Kong", "Link", 
			   "Mr. Game & Watch", "Roy", "Mewtwo", "Zelda", "Ness", "Pichu", "Bowser", "Kirby"};
	
	public static final String[] STAGES = {"", "Battlefield", "Dreamland", "Yoshis Story", "Fountain of Dreams", "Final Destination", "Pokemon Stadium"};
    
	public static String getCharacterNameByID(int characterID) {
		return CHARACTERS[characterID];
	}
	
	public static String getStageNameByID(int stageID) {
		return STAGES[stageID];
	}
	
	public static String formatGameHeader(int recordNum, String outcome) {
		return "Game " + (recordNum) + " - " + outcome;
	}
	
	public static String formatDate(String date) {
 		String[] splitDate = date.split("-");
 		String formattedDate = splitDate[1] + "/" + splitDate[2] + "/" + splitDate[0];
 		
		return formattedDate;
	}
	
	public static int getCharacterID(String character) {
		for (int i = 1; i < CHARACTERS.length; i++) {
			if (character.equals(CHARACTERS[i])) {
				int characterID = i;
				
				return characterID;
			}
		}
		
		return 0;
	}
	
	public static int getStageID(String stage) {
		for (int i = 1; i < STAGES.length; i++) {
			if (stage.equals(STAGES[i])) {
				int stageID = i;
				
				return stageID;
			}
		}
		
		return 0;
	}
	
	public static void addToHistory(FXMLStringAndController fxmlStringAndController) {
		Main.history.add(fxmlStringAndController);
	}
}
