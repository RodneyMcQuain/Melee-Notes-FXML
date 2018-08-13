package application;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.GeneralUtils;
import model.Set;
import model.Tournament;
import model.database.SetDao;
import model.database.SetDaoImpl;
import model.database.TournamentDao;
import model.database.TournamentDaoImpl;

public class SpecifiedTournamentController {
	private int tournamentID;
	@FXML public Label lblSpecifiedTournament;
	@FXML public TextField tfTournamentName;
	@FXML public DatePicker dpDate;
	@FXML public TextField tfMyPlacing;
	@FXML public TextField tfState;
	@FXML public TextField tfCity;
	@FXML public TextArea taNotes;
	@FXML public Button btRemoveTournament;
	@FXML public Button btAddSet;
	@FXML public VBox setScrollPaneRoot;
	@FXML public ScrollPane setScrollPane;
	
	public SpecifiedTournamentController(int tournamentID) {
		this.tournamentID = tournamentID;
	}
	
	@FXML
	public void initialize() {
		TournamentDao tournamentDao = new TournamentDaoImpl();
		Tournament tournament = tournamentDao.getTournamentById(tournamentID);
		
		printFields(tournament);
		
		printSets();
		
		addSpecifiedTournamentToHistory();
	}
	
	private void printFields(Tournament tournament) {
		tournamentID = tournament.getTournamentID();
		String tournamentName = tournament.getTournamentName();
		String date = tournament.getDateOfTournament();
		int myPlacing = tournament.getMyPlacing();
		String state = tournament.getState();
		String city = tournament.getCity();
		String notes = tournament.getNotes();
		
    	lblSpecifiedTournament.setText(tournamentName);
    	tfTournamentName.setText(tournamentName);
		tfMyPlacing.setText(String.valueOf(myPlacing));
		dpDate.setValue(LocalDate.parse(date));
		tfState.setText(state);
		tfCity.setText(city);
		taNotes.setText(notes);
	}
	
	private void printSets() {
		setScrollPaneRoot.getChildren().clear();
		
    	SetDao setDao = new SetDaoImpl();
    	List<Set> sets = setDao.getAllSetsByTournamentId(tournamentID);
    	int rowCount = sets.size();
    	
    	if (rowCount != 0) {
			Hyperlink[] hlSets = new Hyperlink[rowCount];
	 		for (int i = 0; i < rowCount; i++) {
	 			Set set = sets.get(i);
		 		
		 		String setString = "Set " + (i + 1) + ": " + set.toString();
	 			hlSets[i] = new Hyperlink(); 
	 			hlSets[i].setText(setString);
				setScrollPaneRoot.getChildren().addAll(hlSets[i]);
				
				hlSets[i].setOnAction(e -> {			    	
			    	goToSpecifiedSet(set);
				});
    		} 
    	} else {
    		setScrollPaneRoot.getChildren().addAll(new Label("No sets were found"));
		}

    	setScrollPane.setContent(setScrollPaneRoot);
	}
	
	private void addSpecifiedTournamentToHistory() {
		String fxmlString = "SpecifiedTournament.fxml";
		SpecifiedTournamentController controller = new SpecifiedTournamentController(tournamentID);
		GeneralUtils.addToHistory(new FXMLStringAndController(fxmlString, controller));
	}
	
	@FXML
	public void onClick_btApplyChanges() {
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
		if (!tfMyPlacing.getText().trim().equals("")) {
			try {
				myPlacing = Integer.parseInt(myPlacingString);
			} catch (NumberFormatException nfe) {
		    	Alert alNotNumberMyPlacing = new Alert(AlertType.ERROR);
		    	alNotNumberMyPlacing.setTitle("My Placing");
		    	alNotNumberMyPlacing.setHeaderText(null);
		    	alNotNumberMyPlacing.setContentText("Please enter an integer for the place you recieved.");
		    	alNotNumberMyPlacing.showAndWait();
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
		Tournament tournament = new Tournament(tournamentID, userID, tournamentName, dateOfTournament.toString(), myPlacing, state, city, notes);
		TournamentDao tournamentDao = new TournamentDaoImpl();
		tournamentDao.updateTournament(tournament);
		
		lblSpecifiedTournament.setText(tournamentName);
		
    	Alert alApplyChanges = new Alert(AlertType.INFORMATION);
    	alApplyChanges.setTitle("Apply Changes");
    	alApplyChanges.setHeaderText(null);
    	alApplyChanges.setContentText("The tournament was updated.");
    	alApplyChanges.showAndWait();
	}
	
	@FXML
	public void onClick_btRemoveTournament() {
    	Alert alRemoveTournament = new Alert(AlertType.CONFIRMATION);
    	alRemoveTournament.setTitle("Remove Tournament");
    	alRemoveTournament.setHeaderText(null);
    	alRemoveTournament.setContentText("Are you sure you want to remove the tournament? This action will also delete the sets and games corresponding with the tournament");
		Optional<ButtonType> optionSelected = alRemoveTournament.showAndWait();
		if (optionSelected.get() == ButtonType.OK) {
			TournamentDao tournamentDoa = new TournamentDaoImpl();
			tournamentDoa.deleteTournamentById(tournamentID);
				
			goToMainMenu();
		} else {
			return;
		}
	}
	
	private void goToMainMenu() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
			Stage currentStage = (Stage) btRemoveTournament.getScene().getWindow();
			
			currentStage.setScene(new Scene(root));
			currentStage.show();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	@FXML
	public void onClick_btAddSet() {
		try {		
			FXMLLoader loader = new FXMLLoader(getClass().getResource("AddSet.fxml"));
			
			AddSetController addSetController = new AddSetController(tournamentID);
			loader.setController(addSetController);
			Parent root = (Parent) loader.load();
			
			Stage currentStage = (Stage) setScrollPaneRoot.getScene().getWindow();
	        currentStage.setScene(new Scene(root));
	        currentStage.show();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}	
	}
	
	private void goToSpecifiedSet(Set set) {
		try {		
			FXMLLoader loader = new FXMLLoader(getClass().getResource("SpecifiedSet.fxml"));
	        
			SpecifiedSetController specifiedSetController = new SpecifiedSetController(set.getSetID());
			loader.setController(specifiedSetController);
			Parent root = (Parent) loader.load();
			
			Stage currentStage = (Stage) setScrollPaneRoot.getScene().getWindow();
	        currentStage.setScene(new Scene(root));
	        currentStage.show();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}	
	}
}
