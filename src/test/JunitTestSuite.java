package test;

import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({ 
	TournamentTest.class,
})

public class JunitTestSuite {
    
	@BeforeClass
    public static void init() {
		SQLiteTestData.deleteDatabaseFile();
		SQLiteTestData.buildEntireDatabase();
	}
}
