package application;

import model.database.SetDao;
import model.database.SetDaoImpl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import model.database.GameDao;
import model.database.GameDaoImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Game;
import model.GeneralUtils;
import model.Set;
import model.database.PlayerDao;
import model.database.PlayerDaoImpl;

public class SpecifiedSetController {
	private int setID;
	private int tournamentID;
	@FXML public Label lblSpecifiedSet;
	@FXML public ComboBox<String> cbPlayer;
	@FXML public ComboBox<String> cbOutcome;
	@FXML public ComboBox<String> cbType;
	@FXML public ComboBox<String> cbFormat;
	@FXML public TextField tfBracketRound;
	@FXML public TextArea taNotes;
	@FXML public ScrollPane gameScrollPane;
	@FXML public VBox gameScrollPaneRoot;
	@FXML public Button btRemoveSet;
	@FXML public Button btAddPlayer;
	@FXML public Button btAddGame;
	
	public SpecifiedSetController(int setID) {
		this.setID = setID;
	}
	
	@FXML
	public void initialize() {
		SetDao setDao = new SetDaoImpl();
		Set set = setDao.getSetById(setID);
		
		printFields(set);
		
		printGames();
		
		populateComboBoxes();
		
		addSpecifiedSetToHistory();
	}
	
	private void printFields(Set set) {
		setID = set.getSetID();
		tournamentID = set.getTournamentID();
		String outcome = set.getOutcome();
		String type = set.getType();
		String format = set.getFormat();
		String bracketRound = set.getBracketRound();
		String notes = set.getNotes();

		PlayerDao playerDao = new PlayerDaoImpl();
		String tag = playerDao.getPlayerTagById(set.getPlayerID());
		
		lblSpecifiedSet.setText(set.toString());
		cbPlayer.setValue(tag);
		cbOutcome.setValue(outcome);
		cbType.setValue(type);
		cbFormat.setValue(format);
		tfBracketRound.setText(bracketRound);
		taNotes.setText(notes);
	}
	
	public void printGames() {
    	GameDao gameDao = new GameDaoImpl();
    	List<Game> games = gameDao.getAllGamesBySetId(setID);
    	int rowCount = games.size();
    		 		
    	if (rowCount != 0) {
			Hyperlink[] hlGames = new Hyperlink[rowCount];
	 		for (int i = 0; i < rowCount; i++) {
	 			Game game = games.get(i);
	 			String outcome = game.getOutcome();
	 			
	 			String gameString = GeneralUtils.formatGameHeader(i + 1, outcome);
		 		hlGames[i] = new Hyperlink(); 
	 			hlGames[i].setText(gameString);
	 			gameScrollPaneRoot.getChildren().addAll(hlGames[i]);

        		hlGames[i].setOnAction(e -> {
        			goToSpecifiedGame(game, gameString);
				});
	 		}
    	} else {
			gameScrollPaneRoot.getChildren().addAll(new Label("No games were found"));
		}
		
    	gameScrollPane.setContent(gameScrollPaneRoot);
	}
	
	private void populateComboBoxes() {
		GUIUtils.populatePlayerComboBox(cbPlayer);
		GUIUtils.populateOutcomeComboBox(cbOutcome);
		GUIUtils.populateFormatComboBox(cbFormat);
		GUIUtils.populateTypeComboBox(cbType);		
	}
	
	private void addSpecifiedSetToHistory() {
		String fxmlString = "SpecifiedSet.fxml";
		SpecifiedSetController controller = new SpecifiedSetController(setID);
		GeneralUtils.addToHistory(new FXMLStringAndController(fxmlString, controller));		
	}
	
	@FXML
	public void onClick_btApplyChanges() {
		String tag = cbPlayer.getValue();
		if (tag == null || tag.isEmpty()) {
	    	Alert allNoPlayer = new Alert(AlertType.ERROR);
	    	allNoPlayer.setTitle("Add Set");
	    	allNoPlayer.setHeaderText(null);
	    	allNoPlayer.setContentText("Please select a player.");
	    	allNoPlayer.showAndWait();	
	    	return;
		}
		PlayerDao playerDao = new PlayerDaoImpl();
		int playerID = playerDao.getPlayerIdByTag(tag);
		String outcome = cbOutcome.getValue();
		if (outcome == null || outcome.isEmpty()) {
	    	Alert alNoOutcome = new Alert(AlertType.ERROR);
	    	alNoOutcome.setTitle("Add Set");
	    	alNoOutcome.setHeaderText(null);
	    	alNoOutcome.setContentText("Please select an outcome.");
	    	alNoOutcome.showAndWait();	
	    	return;
		}
		String bracketRound = tfBracketRound.getText();
		String type =  cbType.getValue();
		String format = cbFormat.getValue();
		String notes = taNotes.getText();
		
		Set set = new Set(setID, tournamentID, playerID, outcome, bracketRound, type, format, notes);
		SetDao setDao = new SetDaoImpl();
		setDao.updateSet(set);
		
    	Alert alApplyChanges = new Alert(AlertType.INFORMATION);
    	alApplyChanges.setTitle("Apply Changes");
    	alApplyChanges.setHeaderText(null);
    	alApplyChanges.setContentText("The set was updated.");
    	alApplyChanges.showAndWait();
	}
	
	@FXML
	public void onClick_btRemoveSet() {
    	Alert alRemoveSet = new Alert(AlertType.CONFIRMATION);
    	alRemoveSet.setTitle("Remove Set");
    	alRemoveSet.setHeaderText(null);
    	alRemoveSet.setContentText("Are you sure you want to remove the game? This action will also delete the games corresponding with the set.");
		Optional<ButtonType> optionSelected = alRemoveSet.showAndWait();
		if (optionSelected.get() == ButtonType.OK) {
			SetDao setDao = new SetDaoImpl();
			setDao.deleteSetById(setID);
			
			goToSpecifiedTournament();
		} else {
			return;
		}
	}

	private void goToSpecifiedTournament() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("SpecifiedTournament.fxml"));

			SpecifiedTournamentController specifiedTournamentController = new SpecifiedTournamentController(tournamentID);
			loader.setController(specifiedTournamentController);
			Parent root = loader.load();
			
	        Stage currentStage = (Stage) btRemoveSet.getScene().getWindow();

	        currentStage.setScene(new Scene(root));
	        currentStage.show();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}		
	}
	
	@FXML
	public void onClick_btAddPlayer() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("AddPlayer.fxml"));
	        
	        AddPlayerController addPlayerController = new AddPlayerController();
			loader.setController(addPlayerController);
			Parent root = (Parent) loader.load();
			
	        Stage currentStage = (Stage) btAddPlayer.getScene().getWindow();	       	      
	        currentStage.setScene(new Scene(root));
	        currentStage.show();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}		
	}
	
	@FXML
	public void onClick_btAddGame() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("AddGame.fxml"));
	        
	        AddGameController addGameController = new AddGameController(setID);
			loader.setController(addGameController);
			Parent root = (Parent) loader.load();
			
	        Stage currentStage = (Stage) btAddGame.getScene().getWindow();	       	      
	        currentStage.setScene(new Scene(root));
	        currentStage.show();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}		
	}
	
	public void goToSpecifiedGame(Game game, String gameString) {
		try {		
			FXMLLoader loader = new FXMLLoader(getClass().getResource("SpecifiedGame.fxml"));
	        
			SpecifiedGameController specifiedGameController = new SpecifiedGameController(game.getGameID(), gameString);
			loader.setController(specifiedGameController);
			Parent root = (Parent) loader.load();
			
			Stage currentStage = (Stage) gameScrollPaneRoot.getScene().getWindow();
	        currentStage.setScene(new Scene(root));
	        currentStage.show();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}	
	}
}
