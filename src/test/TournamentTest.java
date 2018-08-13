package test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import model.Game;
import model.Money;
import model.Set;
import model.database.GameDao;
import model.database.GameDaoImpl;
import model.database.MoneyDao;
import model.database.MoneyDaoImpl;
import model.database.SetDao;
import model.database.SetDaoImpl;
import model.database.TournamentDao;
import model.database.TournamentDaoImpl;

class TournamentTest {
	@Test
	public void cascadeDeleteTournamentSetTest() {
		SQLiteTestData.deleteDatabaseFile();
		SQLiteTestData.buildEntireDatabase();
		
		int tournamentID = 1;
		
		TournamentDao tournamentDao = new TournamentDaoImpl();
		tournamentDao.deleteTournamentById(tournamentID);
		
		SetDao setDao = new SetDaoImpl();
		List<Set> sets = setDao.getAllSetsByTournamentId(tournamentID);
		
		assertEquals(0, sets.size());
		SQLiteTestData.deleteDatabaseFile();
	}
	
	@Test
	public void cascadeDeleteTournamentGameTest() {
		SQLiteTestData.deleteDatabaseFile();
		SQLiteTestData.buildEntireDatabase();
		
		int tournamentID = 1;
		int setID = 1;
		
		TournamentDao tournamentDao = new TournamentDaoImpl();
		tournamentDao.deleteTournamentById(tournamentID);
		
		GameDao gameDao = new GameDaoImpl();
		List<Game> games = gameDao.getAllGamesBySetId(setID);
		
		assertEquals(0, games.size());
		SQLiteTestData.deleteDatabaseFile();
	}
	
	@Test
	public void cascadeDeleteTournamentMoneyTest() {
		SQLiteTestData.deleteDatabaseFile();
		SQLiteTestData.buildEntireDatabase();
		
		int tournamentID = 1;
		
		TournamentDao tournamentDao = new TournamentDaoImpl();
		tournamentDao.deleteTournamentById(tournamentID);
		
		MoneyDao moneyDao = new MoneyDaoImpl();
		List<Money> moneys = moneyDao.getAllMoneysByTournamentId(tournamentID);
		
		assertEquals(0, moneys.size());
		SQLiteTestData.deleteDatabaseFile();
	}

}
