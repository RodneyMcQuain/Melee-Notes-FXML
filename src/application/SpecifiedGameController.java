package application;

import model.database.GameDao;
import model.database.GameDaoImpl;

import java.io.IOException;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.Game;
import model.GeneralUtils;

public class SpecifiedGameController {
	private int gameID;
	private int setID;
	private String gameHeader;
	@FXML public Label lblSpecifiedGame;
	@FXML public ComboBox<String> cbOutcome;
	@FXML public ComboBox<String> cbMyCharacter;
	@FXML public ComboBox<String> cbOpponentCharacter;
	@FXML public ComboBox<String> cbStage;
	@FXML public Button btRemoveGame;
	
	public SpecifiedGameController(int gameID, String gameHeader) {
		this.gameID = gameID;
		this.gameHeader = gameHeader;
	}
	
	@FXML
	public void initialize() {		
		GameDao gameDao = new GameDaoImpl();
		Game game = gameDao.getGameById(gameID);
		
		printFields(game, gameHeader);
		
		populateComboBoxes();

		addSpecifiedGameToHistory();
	}
	
	private void printFields(Game game, String gameHeader) {
		gameID = game.getGameID();
		setID = game.getSetID();
		String outcome = game.getOutcome();
 		String myCharacter = GeneralUtils.getCharacterNameByID(game.getMyCharacterID());
 		String opponentCharacter = GeneralUtils.getCharacterNameByID(game.getOpponentCharacterID());
 		String stage = GeneralUtils.getStageNameByID(game.getStageID());
 		
 		lblSpecifiedGame.setText(gameHeader);
 		cbOutcome.setValue(outcome);
		cbMyCharacter.setValue(myCharacter);
		cbOpponentCharacter.setValue(opponentCharacter);
		cbStage.setValue(stage);
	}
	
	private void populateComboBoxes() {
		GUIUtils.populateOutcomeComboBox(cbOutcome);
		GUIUtils.populateCharacterComboBox(cbMyCharacter);
		GUIUtils.populateCharacterComboBox(cbOpponentCharacter);
		GUIUtils.populateStageComboBox(cbStage);		
	}
	
	private void addSpecifiedGameToHistory() {
		String fxmlString = "SpecifiedGame.fxml";
		SpecifiedGameController controller = new SpecifiedGameController(gameID, gameHeader);
		GeneralUtils.addToHistory(new FXMLStringAndController(fxmlString, controller));
	}
	
	@FXML
	public void onClick_btApplyChanges() {
		String outcome = cbOutcome.getValue();
		String myCharacter = cbMyCharacter.getValue();
		String opponentCharacter =  cbOpponentCharacter.getValue();
		String stage =  cbStage.getValue();
		int myCharacterID = GeneralUtils.getCharacterID(myCharacter);
		int opponentCharacterID = GeneralUtils.getCharacterID(opponentCharacter);
		int stageID = GeneralUtils.getStageID(stage);
		
		GameDao gameDao = new GameDaoImpl();
		Game game = new Game(gameID, setID, myCharacterID, opponentCharacterID, stageID, outcome);
		gameDao.updateGame(game);

		Alert alApplyChanges = new Alert(AlertType.INFORMATION);
    	alApplyChanges.setTitle("Apply Changes");
    	alApplyChanges.setHeaderText(null);
    	alApplyChanges.setContentText("The game was updated.");
    	alApplyChanges.showAndWait();
	}
	
	@FXML
	public void onClick_btRemoveGame() {
    	Alert alRemoveGame = new Alert(AlertType.CONFIRMATION);
    	alRemoveGame.setTitle("Remove Game");
    	alRemoveGame.setHeaderText(null);
    	alRemoveGame.setContentText("Are you sure you want to remove the game?");
		Optional<ButtonType> optionSelected = alRemoveGame.showAndWait();
		if (optionSelected.get() == ButtonType.OK) {
			GameDao gameDao = new GameDaoImpl();
			gameDao.deleteGameById(gameID);		
			
			goToSpecifiedSet();
		} else {
			return;
		}
	}
	
	private void goToSpecifiedSet() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("SpecifiedSet.fxml"));
	        
			SpecifiedSetController specifiedSetController = new SpecifiedSetController(setID);
			loader.setController(specifiedSetController);
			Parent root = (Parent) loader.load();
		
			Stage currentStage = (Stage) btRemoveGame.getScene().getWindow();
	        currentStage.setScene(new Scene(root));
	        currentStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
