package application;

import model.database.PlayerDao;
import model.database.PlayerDaoImpl;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.GeneralUtils;
import model.Set;

public class AddSetController {
	private int tournamentID;
	@FXML public ComboBox<String> cbPlayer;
	@FXML public Button btAddPlayer;
	@FXML public ComboBox<String> cbOutcome;
	@FXML public ComboBox<String> cbType;
	@FXML public TextField tfBracketRound;
	@FXML public ComboBox<String> cbFormat;
	@FXML public TextArea taNotes;
	@FXML public Button btAddSet;
	
	public AddSetController(int tournamentID) {
		this.tournamentID = tournamentID;
	}
	
	@FXML
	public void initialize() {
		populateComboBoxes();
		
		addAddSetToHistory();
	}
	
	private void populateComboBoxes() {
		GUIUtils.populatePlayerComboBox(cbPlayer);
		GUIUtils.populateOutcomeComboBox(cbOutcome);
		GUIUtils.populateFormatComboBox(cbFormat);
		GUIUtils.populateTypeComboBox(cbType);		
	}
	
	private void addAddSetToHistory() {
		String fxmlString = "AddSet.fxml";
		AddSetController controller = new AddSetController(tournamentID);
		GeneralUtils.addToHistory(new FXMLStringAndController(fxmlString, controller));
	}
	
	@FXML
	public void onClick_btAddSet() {
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
		String format = cbFormat.getValue();
		String type = cbType.getValue();
		String notes = taNotes.getText();
		
		Set set = new Set(tournamentID, playerID, outcome, bracketRound, type, format, notes);
		SetDao setDao = new SetDaoImpl();
		setDao.insertSet(set);
		
    	Alert alAddSet = new Alert(AlertType.INFORMATION);
    	alAddSet.setTitle("Add Set");
    	alAddSet.setHeaderText(null);
    	alAddSet.setContentText("The set was added.");
    	alAddSet.showAndWait();	

    	setFieldsBlank();
	}
	
	private void setFieldsBlank() {
    	cbPlayer.setValue("");
    	tfBracketRound.setText("");
    	cbOutcome.setValue("");
    	cbType.setValue("");
    	cbFormat.setValue("");
    	taNotes.setText("");
	}
	
	@FXML
	public void onClick_btAddPlayer() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("AddPlayer.fxml"));
	        Stage currentStage = (Stage) btAddPlayer.getScene().getWindow();
	        
	        currentStage.setScene(new Scene(root));
	        currentStage.show();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}		
	}
}
