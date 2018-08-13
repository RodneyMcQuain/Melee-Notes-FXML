package application;

import model.database.GameDao;
import model.database.GameDaoImpl;
import model.database.SetDao;
import model.database.SetDaoImpl;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.Game;
import model.GeneralUtils;
import model.Set;

public class AddGameController {
	private int setID;
	@FXML public ComboBox<String> cbOutcome;
	@FXML public ComboBox<String> cbMyCharacter;
	@FXML public ComboBox<String> cbOpponentCharacter;
	@FXML public ComboBox<String> cbStage;
	@FXML public Button btAddGame;
	
	public AddGameController(int setID) {
		this.setID = setID;
	}
	
	@FXML
	public void initialize() {
		populateComboBoxes();
		
		addAddGameToHistory();
	}
	
	private void populateComboBoxes() {
		GUIUtils.populateOutcomeComboBox(cbOutcome);
		GUIUtils.populateCharacterComboBox(cbMyCharacter);
		GUIUtils.populateCharacterComboBox(cbOpponentCharacter);
		GUIUtils.populateStageComboBox(cbStage);		
	}
	
	private void addAddGameToHistory() {
		String fxmlString = "AddGame.fxml";
		AddGameController controller = new AddGameController(setID);
		GeneralUtils.addToHistory(new FXMLStringAndController(fxmlString, controller));
	}
	
	@FXML
	public void onClick_btAddGame() {
		String gameOutcome = cbOutcome.getValue();
		if (gameOutcome == null || gameOutcome.isEmpty()) {
	    	Alert alNoOutcome = new Alert(AlertType.ERROR);
	    	alNoOutcome.setTitle("No Outcome Selected");
	    	alNoOutcome.setHeaderText(null);
	    	alNoOutcome.setContentText("Please select an outcome.");
	    	alNoOutcome.showAndWait();	
	    	return;
		}
		String myCharacter = cbMyCharacter.getValue();
		if (myCharacter == null || myCharacter.isEmpty()) {
	    	Alert alNoMyCharacter = new Alert(AlertType.ERROR);
	    	alNoMyCharacter.setTitle("My Character Not Selected");
	    	alNoMyCharacter.setHeaderText(null);
	    	alNoMyCharacter.setContentText("Please select my character.");
	    	alNoMyCharacter.showAndWait();	
	    	return;
		}
		String opponentCharacter = cbOpponentCharacter.getValue();
		if (opponentCharacter == null || opponentCharacter.isEmpty()) {
	    	Alert alNoOpponentCharacter= new Alert(AlertType.ERROR);
	    	alNoOpponentCharacter.setTitle("Opponent Character Not Selected");
	    	alNoOpponentCharacter.setHeaderText(null);
	    	alNoOpponentCharacter.setContentText("Please select opponent character.");
	    	alNoOpponentCharacter.showAndWait();	
	    	return;
		}
		String stage = cbStage.getValue();
		if (stage == null || stage.isEmpty()) {
	    	Alert alNoStage= new Alert(AlertType.ERROR);
	    	alNoStage.setTitle("No Stage Selected");
	    	alNoStage.setHeaderText(null);
	    	alNoStage.setContentText("Please select a stage.");
	    	alNoStage.showAndWait();	
	    	return;
		}
		
 		if (!isValidGame(gameOutcome))
 			return;
		
 		int myCharacterID = GeneralUtils.getCharacterID(myCharacter);
 		int opponentCharacterID = GeneralUtils.getCharacterID(opponentCharacter);
 		int stageID = GeneralUtils.getStageID(stage);

		GameDao gameDao = new GameDaoImpl();
 		Game game = new Game(setID, myCharacterID, opponentCharacterID, stageID, gameOutcome);
 		gameDao.insertGame(game);
		
    	Alert alAddGame = new Alert(AlertType.INFORMATION);
    	alAddGame.setTitle("Add Game");
    	alAddGame.setHeaderText(null);
    	alAddGame.setContentText("The game was added.");
    	alAddGame.showAndWait();	
    	
    	setFieldsBlank();
	}
	
	private boolean isValidGame(String gameOutcome) {
		final int WIN_CONDITION_BO3 = 2;
		final int WIN_CONDITION_BO5 = 3;
		final int INVALID_COUNT_B03 = 1;
		final int INVALID_COUNT_B05 = 2;
		boolean isValid = true;
		boolean isSetComplete = false;
		boolean isInvalidCount = false;
		String setOutcome = null;
		String format = null;

		GameDao gameDao = new GameDaoImpl();
		int gameWon = gameDao.calculateGamesWon(setID);
		int gameLost = gameDao.calculateGamesLost(setID);

		if (gameOutcome.equals("Won")) {
			gameWon++;
		} else {
			gameLost++;
		}
		
		SetDao setDao = new SetDaoImpl();
		Set set = setDao.getSetById(setID);
	 		
		format = set.getFormat();
 		setOutcome = set.getOutcome();
 		if (setOutcome.equals("Won") && format.equals("Bo3")) {
 			if (gameLost > INVALID_COUNT_B03) {
 				isInvalidCount = true;
 			}
 			if (gameWon > WIN_CONDITION_BO3) {
 				isSetComplete = true;
 			}
 		} else if (setOutcome.equals("Lost") && format.equals("Bo3")) {
 			if (gameWon > INVALID_COUNT_B03) {
 				isInvalidCount = true;
 			}
 			if (gameLost > WIN_CONDITION_BO3) {
 				isSetComplete = true;
 			}
 		} else if (setOutcome.equals("Won") && format.equals("Bo5")) {
 			if (gameLost > INVALID_COUNT_B05) {
 				isInvalidCount = true;
 			}
 			if (gameWon > WIN_CONDITION_BO5) {
 				isSetComplete = true;
 			}
 		} else if (setOutcome.equals("Lost") && format.equals("Bo5")) {
 			if (gameWon > INVALID_COUNT_B05) {
 				isInvalidCount = true;
 			}
 			if (gameLost > WIN_CONDITION_BO5) {
 				isSetComplete = true;
 			}
 		}
	
 		if (isSetComplete) {
	    	Alert alSetComplete = new Alert(AlertType.ERROR);
	    	alSetComplete.setTitle("Set Complete");
	    	alSetComplete.setHeaderText(null);
	    	alSetComplete.setContentText("The set is complete, remove or edit a game.");
	    	alSetComplete.showAndWait();	
	    	goToSpecifiedSetScene();
	    	isValid = false;
 		}
 		if (isInvalidCount) {
	    	Alert alInvalidCount = new Alert(AlertType.ERROR);
	    	alInvalidCount.setTitle("Invalid Count");
	    	alInvalidCount.setHeaderText(null);
	    	alInvalidCount.setContentText("The count of the set is invalid, make sure the set outcome and format are correct.");
	    	alInvalidCount.showAndWait();	
	    	goToSpecifiedSetScene();
	    	isValid = false;
 		}
 		
 		return isValid;
	}
	
	private void setFieldsBlank() {
    	cbOutcome.setValue("");
		cbMyCharacter.setValue("");
		cbOpponentCharacter.setValue("");
		cbStage.setValue("");
	}
	
	private void goToSpecifiedSetScene() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("SpecifiedSet.fxml"));
	        
			SpecifiedSetController specifiedSetController = new SpecifiedSetController(setID);
			loader.setController(specifiedSetController);
			Parent root = (Parent) loader.load();
			
			Stage currentStage = (Stage) btAddGame.getScene().getWindow();
	        currentStage.setScene(new Scene(root));
	        currentStage.show();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}		
	}
}
