package model.database;
import java.util.List;

import model.Tournament;

public interface TournamentDao {
	public List<Tournament> getAllTournaments(int userID);
	public List<Tournament> getTournamentsByDate(int userID, String startDate, String endDate);
	public Tournament getTournamentById(int tournamentID);
	public int getMostRecentTournamentId();
	public void updateTournament(Tournament tournament);
	public void insertTournament(Tournament tournament);
	public void deleteTournamentById(int tournamentID);
}
