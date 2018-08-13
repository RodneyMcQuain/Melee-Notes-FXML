package application;

import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.GeneralUtils;
import model.Tournament;
import model.database.TournamentDao;
import model.database.TournamentDaoImpl;

public class MainMenuController {
	@FXML public Button btAddTournament;
	@FXML public ScrollPane tournamentScrollPane;
	@FXML public VBox tournamentScrollPaneRoot;
	
	@FXML
	public void initialize() {
		printTournaments();
		
		addMainMenuToHistory();
	}
	
	private void printTournaments() {
		tournamentScrollPaneRoot.getChildren().clear();
		
		int userID = 1; //placeholder
 		TournamentDao tournamentDao = new TournamentDaoImpl();
 		List<Tournament> tournaments = tournamentDao.getAllTournaments(userID);
 		int rowCount = 0;
 		if (tournaments != null)
 			rowCount = tournaments.size();
 		
    	if (rowCount != 0) {
			Hyperlink[] hlTournaments = new Hyperlink[tournaments.size()];
	 		for (int i = 0; i < rowCount; i++) {
		        Tournament tournament = tournaments.get(i);	        		
		 		
		 		hlTournaments[i] = new Hyperlink(); 
		 		hlTournaments[i].setText((i + 1) + ". " + tournament.toString());
		 		tournamentScrollPaneRoot.getChildren().addAll(hlTournaments[i]);
					    	 	
		        hlTournaments[i].setOnAction(e -> {
		        	goToSpecifiedTournament(tournament);
				});
	 		}
    	} else {
    		tournamentScrollPaneRoot.getChildren().addAll(new Label("No tournaments were found"));
    	}
    	
    	tournamentScrollPane.setContent(tournamentScrollPaneRoot);
	}
	
	private void addMainMenuToHistory() {
		String fxmlString = "MainMenu.fxml";
		MainMenuController controller = new MainMenuController();
		GeneralUtils.addToHistory(new FXMLStringAndController(fxmlString, controller));		
	}
	
	private void goToSpecifiedTournament(Tournament tournament) {
		try {		
			FXMLLoader loader = new FXMLLoader(getClass().getResource("SpecifiedTournament.fxml"));
	        
			SpecifiedTournamentController specifiedTournamentController = new SpecifiedTournamentController(tournament.getTournamentID());
			loader.setController(specifiedTournamentController);
			Parent root = (Parent) loader.load();

			Stage currentStage = (Stage) tournamentScrollPaneRoot.getScene().getWindow();
	        currentStage.setScene(new Scene(root));
	        currentStage.show();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}		
	}
	
	@FXML
	public void onClick_btAddTournament(ActionEvent e) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("AddTournament.fxml"));
	        Stage currentStage = (Stage) btAddTournament.getScene().getWindow();
	        
	        currentStage.setScene(new Scene(root));
	        currentStage.show();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}		
	}
}
