package application;

import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.GeneralUtils;
import model.Tournament;
import model.database.MoneyDao;
import model.database.MoneyDaoImpl;
import model.database.TournamentDao;
import model.database.TournamentDaoImpl;
import javafx.scene.control.Alert.AlertType;

public class AddTournamentController {
	@FXML public TextField tfTournamentName;
	@FXML public DatePicker dpDate;
	@FXML public TextField tfMyPlacing;
	@FXML public TextField tfState;
	@FXML public TextField tfCity;
	@FXML public TextArea taNotes;
	@FXML public Button btAddTournament;
	
	@FXML
	public void initialize() {
		addAddTournamentToHistory();
	}
	
	private void addAddTournamentToHistory() {
		String fxmlString = "AddTournament.fxml";
		AddTournamentController controller = new AddTournamentController();
		GeneralUtils.addToHistory(new FXMLStringAndController(fxmlString, controller));
	}
	
	@FXML
	public void onClick_btAddTournament() {
		String tournamentName = tfTournamentName.getText();
		LocalDate dateOfTournament = dpDate.getValue();
		if (dateOfTournament == null) {
	    	Alert alNoDate = new Alert(AlertType.ERROR);
	    	alNoDate.setTitle("Date of Tournament");
	    	alNoDate.setHeaderText(null);
	    	alNoDate.setContentText("Please enter information into the date field.");
	    	alNoDate.showAndWait();	
			return;
		}
		if (tournamentName.isEmpty()) {
	    	Alert alNoTournamentName = new Alert(AlertType.ERROR);
	    	alNoTournamentName.setTitle("Tournament Name");
	    	alNoTournamentName.setHeaderText(null);
	    	alNoTournamentName.setContentText("Please enter information into the tounament name field.");
	    	alNoTournamentName.showAndWait();	
			return;
		}
		String myPlacingString = tfMyPlacing.getText();
		int myPlacing = 0;
		if (!myPlacingString.trim().equals("")) {
			try {
				myPlacing = Integer.parseInt(myPlacingString);
			} catch (NumberFormatException nfe) {
		    	Alert alNoMyPlacing = new Alert(AlertType.ERROR);
		    	alNoMyPlacing.setTitle("My Placing");
		    	alNoMyPlacing.setHeaderText(null);
		    	alNoMyPlacing.setContentText("Please enter an integer for the place you recieved.");
		    	alNoMyPlacing.showAndWait();
		    	return;
			}
			if (myPlacing < 0) {
		    	Alert alMyPlacingLessThan = new Alert(AlertType.ERROR);
		    	alMyPlacingLessThan.setTitle("My Placing");
		    	alMyPlacingLessThan.setHeaderText(null);
		    	alMyPlacingLessThan.setContentText("Please enter a non-negative number.");
		    	alMyPlacingLessThan.showAndWait();
		    	return;
		    }
		}
		String state = tfState.getText();
		String city = tfCity.getText();
		String notes = taNotes.getText();
	
		int userID = 1; //placeholder
		TournamentDao tournamentDao = new TournamentDaoImpl();
		Tournament tournament = new Tournament(userID, tournamentName, dateOfTournament.toString(), myPlacing, state, city, notes);
		tournamentDao.insertTournament(tournament);
		int tournamentID = tournamentDao.getMostRecentTournamentId();
		
		MoneyDao moneyDao = new MoneyDaoImpl();
		moneyDao.insertMoneyByTournamentId(tournamentID);
		
    	Alert alAddTournament = new Alert(AlertType.INFORMATION);
    	alAddTournament.setTitle("Add Tournament");
    	alAddTournament.setHeaderText(null);
    	alAddTournament.setContentText("Tournament has been added.");
    	alAddTournament.showAndWait();

    	setFieldsBlank();
	}
	
	private void setFieldsBlank() {
    	tfTournamentName.setText("");
		dpDate.setValue(null);
		tfMyPlacing.setText("");
		tfState.setText("");
		tfCity.setText("");
		taNotes.setText("");
	}
}
