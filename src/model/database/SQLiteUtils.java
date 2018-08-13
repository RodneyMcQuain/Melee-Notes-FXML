package model.database;
import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.sqlite.SQLiteConfig;

public class SQLiteUtils {
	private static final String DB_FILENAME = "meleeNotes.db";

	
	public static Connection getConnection() {
		Connection c = null;
		
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + DB_FILENAME);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	
		return c;
	}
	
	public static Connection getConnectionWithForeignKeysSupport() {
		Connection c = null;
		
		try {
			SQLiteConfig config = new SQLiteConfig();
			config.enforceForeignKeys(true);
			
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + DB_FILENAME, config.toProperties());
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	
		return c;		
	}
	
	public static void closeQuietly(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void closeQuietly(Statement stmt) {
		try {
			if (stmt != null) {
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void closeQuietly(Connection con) {
		try {
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void createTournamentTable() {
		Statement stmt = null;
        Connection con = getConnection();

		try {
	 		stmt = con.createStatement();
	 		String sql = "CREATE TABLE		IF NOT EXISTS tournaments " +
	 					 "(tournamentID     INTEGER       PRIMARY KEY 		AUTOINCREMENT, " +
	 					 "userID			INTEGER 	  NOT NULL, " +
	 					 "tournamentName    VARCHAR(100)  NOT NULL, " +
	 					 "dateOfTournament  DATE          NOT NULL, " +
	 					 "myPLacing     	INTEGER, " + 
	 					 "state 			VARCHAR(20), " +
	 					 "city              VARCHAR(40), " +
	 					 "notes             LONGTEXT, " + 
	 					 "FOREIGN KEY (userID)		REFERENCES users(userID));";
	 		stmt.executeUpdate(sql);
		} catch (Exception ex) {
			ex.printStackTrace();
 		} finally {
 			SQLiteUtils.closeQuietly(stmt);
 			SQLiteUtils.closeQuietly(con);
 		}
		System.out.println("Tournament table created.");
	}
	
	public static void createMoneyTable() {
		Statement stmt = null;
        Connection con = getConnection();
		
		try {
	 		stmt = con.createStatement();
	 		String sql = "CREATE TABLE		IF NOT EXISTS moneys " +
	 					 "(moneyID		    INTEGER       PRIMARY KEY 		AUTOINCREMENT, " +
	 					 "tournamentID		INTEGER 	  NOT NULL, " +
	 					 "prizeMoney		INTEGER, " +
	 					 "moneyMatch		INTEGER, " +
	 					 "entryFee	     	INTEGER, " + 
	 					 "venueFee 			INTEGER, " +
	 					 "travelExpense     INTEGER, " + 
	 					 "FOREIGN KEY (tournamentID)		REFERENCES tournaments(tournamentID)	ON DELETE CASCADE);";
	 		stmt.executeUpdate(sql);
		} catch (Exception ex) {
			ex.printStackTrace();
 		} finally {
 			SQLiteUtils.closeQuietly(stmt);
 			SQLiteUtils.closeQuietly(con);
 		}
		System.out.println("Money table created.");
	}
	
	public static void createSetTable() {
		Statement stmt = null;
        Connection con = getConnection();
		
		try {
	 		stmt = con.createStatement();
	 		String sql = "CREATE TABLE 		IF NOT EXISTS sets " +
	 					 "(setID	        INTEGER       PRIMARY KEY 		AUTOINCREMENT," +
	 					 "playerID		    INTEGER	      NOT NULL, " +
	 					 "tournamentID     	INTEGER		  NOT NULL, " + 
	 					 "outcome	  		VARCHAR(5)    NOT NULL, " +
	 					 "bracketRound		VARCHAR(25), " +
	 					 "type	 			VARCHAR(20), " +
	 					 "format            VARCHAR(15), " +
	 					 "notes             LONGTEXT, " + 
	 					 "FOREIGN KEY (playerID)		REFERENCES players(playerID), " + 
	 					 "FOREIGN KEY (tournamentID)	REFERENCES tournaments(tournamentID)	ON DELETE CASCADE);";
	 		stmt.executeUpdate(sql);
		} catch (Exception ex) {
			ex.printStackTrace();
 		} finally {
 			SQLiteUtils.closeQuietly(stmt);
 			SQLiteUtils.closeQuietly(con);
 		}
		System.out.println("Set table created.");
	}
	
	public static void createGameTable() {
		Statement stmt = null;
        Connection con = getConnection();
		
		try {
	 		stmt = con.createStatement();
	 		String sql = "CREATE TABLE 			IF NOT EXISTS games " +
	 					 "(gameID     			INTEGER       PRIMARY KEY 		AUTOINCREMENT, " +
	 					 "setID    	  			INTEGER  	  NOT NULL, " +
	 					 "myCharacterID 		INTEGER 	  NOT NULL, " + 
	 					 "opponentCharacterID 	INTEGER 	  NOT NULL, " + 
	 					 "stageID	  			INTEGER       NOT NULL, " +
	 					 "outcome 				VARCHAR(5), " + 
	 					 "FOREIGN KEY (setID)				REFERENCES sets(setID)	ON DELETE CASCADE, " + 
	 					 "FOREIGN KEY (myCharacterID)		REFERENCES characters(characterID), " + 
	 					 "FOREIGN KEY (opponentCharacterID)	REFERENCES characters(characterID), " + 
	 					 "FOREIGN KEY (stageID)				REFERENCES stages(stageID));";
	 		stmt.executeUpdate(sql);
		} catch (Exception ex) {
			ex.printStackTrace();
 		} finally {
 			SQLiteUtils.closeQuietly(stmt);
 			SQLiteUtils.closeQuietly(con);
 		}
		System.out.println("Game table created.");
	}
	
	public static void createUserTable() {
		Statement stmt = null;
        Connection con = getConnection();
		
		try {
	 		stmt = con.createStatement();
	 		String sql = "CREATE TABLE 		IF NOT EXISTS users " +
	 					 "(userID     		INTEGER		  PRIMARY KEY		AUTOINCREMENT, " + 
	 					 "email 			VARCHAR(100)  NOT NULL			UNIQUE, " + 
	 					 "password 			VARCHAR(20)   NOT NULL);";
	 		stmt.executeUpdate(sql);
		} catch (Exception ex) {
			ex.printStackTrace();
 		} finally {
 			SQLiteUtils.closeQuietly(stmt);
 			SQLiteUtils.closeQuietly(con);
 		}
		System.out.println("User table created.");
	}
	
	public static void createPlayerTable() {
		Statement stmt = null;
        Connection con = getConnection();
		
		try {
	 		stmt = con.createStatement();
	 		String sql = "CREATE TABLE 		IF NOT EXISTS players " +
	 					 "(playerID     	INTEGER		  PRIMARY KEY		AUTOINCREMENT, " + 
	 					 "userID			INTEGER		  NOT NULL, " +
	 					 "tag	 			VARCHAR(100)  NOT NULL, " + 
	 					 "notes 			LONG TEXT, " + 
	 					 "FOREIGN KEY (userID)		REFERENCES users(userID));";
	 		stmt.executeUpdate(sql);
		} catch (Exception ex) {
			ex.printStackTrace();
 		} finally {
 			SQLiteUtils.closeQuietly(stmt);
 			SQLiteUtils.closeQuietly(con);
 		}
		System.out.println("Player table created.");
	}
	
	public static void createStageTable() {
		Statement stmt = null;
        Connection con = getConnection();
		
		try {
	 		stmt = con.createStatement();
	 		String sql = "CREATE TABLE 	IF NOT EXISTS stages " +
	 					 "(stageID     	INTEGER		  PRIMARY KEY		AUTOINCREMENT, " + 
	 					 "name	 		VARCHAR(20)   NOT NULL);";
	 		stmt.executeUpdate(sql);
		} catch (Exception ex) {
			ex.printStackTrace();
 		} finally {
 			SQLiteUtils.closeQuietly(stmt);
 			SQLiteUtils.closeQuietly(con);
 		}
		System.out.println("Stage table created.");
	}
	
	public static void createCharacterTable() {
		Statement stmt = null;
        Connection con = getConnection();
		
		try {
	 		stmt = con.createStatement();
	 		String sql = "CREATE TABLE  IF NOT EXISTS characters " +
	 					 "(characterID  INTEGER		  PRIMARY KEY		AUTOINCREMENT, " + 
	 					 "name	 		VARCHAR(20)   NOT NULL);";
	 		stmt.executeUpdate(sql);
		} catch (Exception ex) {
			ex.printStackTrace();
 		} finally {
 			SQLiteUtils.closeQuietly(stmt);
 			SQLiteUtils.closeQuietly(con);
 		}
		System.out.println("Character table created.");
	}
	
	public static void createCharacterNotesTable() {
		Statement stmt = null;
        Connection con = getConnection();
		
		try {
	 		stmt = con.createStatement();
	 		String sql = "CREATE TABLE 			IF NOT EXISTS characterNotes " +
	 					 "(characterNoteID		INTEGER		  PRIMARY KEY		AUTOINCREMENT, " + 
	 					 "characterID	 		INTEGER		  NOT NULL, " + 
	 					 "userID				INTEGER		  NOT NULL, " +
	 					 "subject 				varchar(50)   NOT NULL, " + 
	 					 "notes					LONG TEXT, " +
	 					 "FOREIGN KEY (characterID)		REFERENCES characters(characterID));";
	 		stmt.executeUpdate(sql);
		} catch (Exception ex) {
			ex.printStackTrace();
 		} finally {
 			SQLiteUtils.closeQuietly(stmt);
 			SQLiteUtils.closeQuietly(con);
 		}
		System.out.println("Character notes table created.");
	}
	
	public static void createAnalysisNotesTable() {
		Statement stmt = null;
        Connection con = getConnection();
		
		try {
	 		stmt = con.createStatement();
	 		String sql = "CREATE TABLE 			IF NOT EXISTS analysisNotes " +
	 					 "(analysisNoteID	   	INTEGER       PRIMARY KEY 		AUTOINCREMENT, " +
	 					 "userID				INTEGER		  NOT NULL, " +
	 					 "subject				varChar(50)   NOT NULL, " +
	 					 "focusPoints			varChar(50), " +
	 					 "notes					LONG TEXT, " +
	 					 "FOREIGN KEY (userID)	REFERENCES users(userID));";
	 		stmt.executeUpdate(sql);
		} catch (Exception ex) {
			ex.printStackTrace();
 		} finally {
 			SQLiteUtils.closeQuietly(stmt);
 			SQLiteUtils.closeQuietly(con);
 		}
		System.out.println("Analysis Notes table created.");
	}
	
	public static void createTiltTypeTable() {
		Statement stmt = null;
        Connection con = getConnection();
		
		try {
	 		stmt = con.createStatement();
	 		String sql = "CREATE TABLE 		IF NOT EXISTS tiltTypes " +
	 					 "(tiltTypeID		INTEGER       PRIMARY KEY 		AUTOINCREMENT, " +
	 					 "userID			INTEGER		  NOT NULL, " +
	 					 "type				varChar(50)	  NOT NULL, " +
	 					 "describeProblem	LONG TEXT, " +
	 					 "whyLogical		LONG TEXT, " +
	 					 "logicFlawed		LONG TEXT, " +
	 					 "possibleSolutions	varChar(50), " +
	 					 "whySolutions		LONG TEXT, " +
	 					 "FOREIGN KEY (userID)		REFERENCES users(userID));";
	 		stmt.executeUpdate(sql);
		} catch (Exception ex) {
			ex.printStackTrace();
 		} finally {
 			SQLiteUtils.closeQuietly(stmt);
 			SQLiteUtils.closeQuietly(con);
 		}
		System.out.println("Tilt Type table created.");
	}

	public static void createTiltProgressNotesTable() {
		Statement stmt = null;
        Connection con = getConnection();
		
		try {
	 		stmt = con.createStatement();
	 		String sql = "CREATE TABLE 			IF NOT EXISTS tiltProgressNotes " +
	 					 "(tiltProgressNoteID	INTEGER       PRIMARY KEY 		AUTOINCREMENT, " +
	 					 "tiltTypeID			INTEGER   	  NOT NULL, " +
	 					 "date					DATE   		  NOT NULL, " +
	 					 "notes					LONG TEXT, " +
	 					 "FOREIGN KEY (tiltTypeID)		REFERENCES tiltTypes(tiltTypeID));";
	 		stmt.executeUpdate(sql);
		} catch (Exception ex) {
			ex.printStackTrace();
 		} finally {
 			SQLiteUtils.closeQuietly(stmt);
 			SQLiteUtils.closeQuietly(con);
 		}
		System.out.println("Tilt Progress notes table created.");
	}
		
    public static void createNewDatabase(String DB_FILENAME) {
        String url = "jdbc:sqlite:F:/users/Rodney/Documents/JavaWorkSpace/MeleeNotesFXML/" + DB_FILENAME;
        Connection con = null;
        try {
        	con = DriverManager.getConnection(url);
        	if (con != null) {
                DatabaseMetaData meta = con.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }
		} catch (Exception ex) {
			ex.printStackTrace();
 		} finally {
 			SQLiteUtils.closeQuietly(con);
 		}
    }
    
    public static void buildBlankDatabase() {    	
		File db = new File(DB_FILENAME);
		if (!db.exists()) {
	    	createNewDatabase(DB_FILENAME);
	    	createUserTable();
	    	createTournamentTable();
	    	createMoneyTable();
	    	createPlayerTable();
	    	createSetTable();
	    	createCharacterTable();
	    	createCharacterNotesTable();
	    	createAnalysisNotesTable();
	    	createStageTable();
	    	createGameTable();
		}
    }
}
