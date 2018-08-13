package model;
import java.util.ArrayList;

public class Tournament {
	private int tournamentID;
	private int userID;
	private String tournamentName;
	private String dateOfTournament;
	private int myPlacing;
	private String state;
	private String city;
	private String notes;
	
	public Tournament(int tournamentID, int userID, String tournamentName, String dateOfTournament, int myPlacing, String state, String city, String notes) {
		this.tournamentID = tournamentID;
		this.userID = userID;
		this.tournamentName = tournamentName;
		this.dateOfTournament = dateOfTournament;
		this.myPlacing = myPlacing;
		this.state = state;
		this.city = city;
		this.notes = notes;
	}
	
	public Tournament (int userID, String tournamentName, String dateOfTournament, int myPlacing, String state, String city, String notes) {
		this.userID = userID;
		this.tournamentName = tournamentName;
		this.dateOfTournament = dateOfTournament;
		this.myPlacing = myPlacing;
		this.state = state;
		this.city = city;
		this.notes = notes;
	}
	
	@Override
	public String toString() {
		String formattedDate = GeneralUtils.formatDate(dateOfTournament);
		
		return tournamentName + " - " + formattedDate;
	}

	public int getTournamentID() {
		return tournamentID;
	}

	public void setTournamentID(int tournamentID) {
		this.tournamentID = tournamentID;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getTournamentName() {
		return tournamentName;
	}

	public void setTournamentName(String tournamentName) {
		this.tournamentName = tournamentName;
	}

	public String getDateOfTournament() {
		return dateOfTournament;
	}

	public void setDateOfTournament(String dateOfTournament) {
		this.dateOfTournament = dateOfTournament;
	}

	public int getMyPlacing() {
		return myPlacing;
	}

	public void setMyPlacing(int myPlacing) {
		this.myPlacing = myPlacing;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}
