package application;

import model.database.PlayerDao;
import model.database.PlayerDaoImpl;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.GeneralUtils;
import model.Player;

public class AddPlayerController {
	@FXML public TextField tfTag;
	@FXML public TextArea taNotes;
	
	@FXML
	public void initialize() {
		addAddPlayerToHistory();
	}
	
	private void addAddPlayerToHistory() {
		String fxmlString = "AddPlayer.fxml";
		AddPlayerController controller = new AddPlayerController();
		GeneralUtils.addToHistory(new FXMLStringAndController(fxmlString, controller));
	}
	
	@FXML
	public void onClick_btAddPlayer() {
		String tag = tfTag.getText();		
		if (tag.isEmpty()) {
	    	Alert alNotNullFields = new Alert(AlertType.ERROR);
	    	alNotNullFields.setTitle("Add Player");
	    	alNotNullFields.setHeaderText(null);
	    	alNotNullFields.setContentText("Please enter information into the tag field.");
	    	alNotNullFields.showAndWait();	
			return;
		}
		
		String notes = taNotes.getText();
		
		int userID = 1; //placeholder
		Player player = new Player(userID, tag, notes);
		PlayerDao playerDao = new PlayerDaoImpl();
		playerDao.insertPlayer(player);
	
    	Alert alAddTournament = new Alert(AlertType.INFORMATION);
    	alAddTournament.setTitle("Add Player");
    	alAddTournament.setHeaderText(null);
    	alAddTournament.setContentText("Player has been added.");
    	alAddTournament.showAndWait();
    	
    	setFieldsBlank();
	}
	
	private void setFieldsBlank() {
    	tfTag.setText("");
		taNotes.setText("");
	}
}
