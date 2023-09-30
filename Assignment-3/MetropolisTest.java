import junit.framework.TestCase;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class MetropolisTest extends TestCase {
    private MetropolisDAO mdao;
    @BeforeEach
    public void initDB() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/test_db");
        dataSource.setUsername("nika13");
        dataSource.setPassword("Nikasql123!.");
        try {
            Connection connection = dataSource.getConnection();

            String dropTableSql = "DROP TABLE IF EXISTS TestMetropolis";
            String createTableSql = "CREATE TABLE TestMetropolis (metropolis CHAR(64), continent CHAR(64), population BIGINT)";

            Statement statement = connection.createStatement();
            statement.executeUpdate(dropTableSql);
            statement.executeUpdate(createTableSql);

        } catch (SQLException e) {
            System.out.println(e);
        }
        mdao = new MetropolisDAO(dataSource);
        mdao.setTable("TestMetropolis");
    }
    @Test
    public void testSimple() {
        assertEquals(0, mdao.getAll().size());
        mdao.add("Tbilisi", "Europe", 1500000);
        assertEquals(1, mdao.getAll().size());
        mdao.add("Batumi", "Europe", 500000);
        assertEquals(2, mdao.getAll().size());
        assertEquals(2, mdao.searchMetropolis("", "Europe", -1, true, false).size());
    }

    @Test
    public void testPopulation() {
        mdao.add("Tbilisi", "Europe", 150000);
        mdao.add("Batumi", "Europe", 500000);
        mdao.add("Tokyo", "Asia", 14000000);
        mdao.add("Poti", "Europe", 100000);
        List<Metropolis> lessThan = mdao.searchMetropolis("", "", 1600000, false, false);
        assertEquals(3, lessThan.size());
        for (Metropolis m : lessThan) {
            assertTrue(m.getPopulation() < 1600000);
        }


    }
    @Test
    public void testPopulationMore() {
        mdao.add("Tokyo", "Asia", 14000000);
        mdao.add("Tbilisi", "Europe", 150000);
        mdao.add("NY", "North America", 8000000);
        mdao.add("Moscow", "Europe", 12000000);
        mdao.add("London", "Europe", 8580000);
        List<Metropolis> moreThan = mdao.searchMetropolis("", "", 2000000, true, false);
        assertEquals(4, moreThan.size());
        for (Metropolis m : moreThan) {
            assertTrue(m.getPopulation() > 2000000);
        }
    }

    @Test
    public void testContinent() {
        mdao.add("Tokyo", "Asia", 14000000);
        mdao.add("Tbilisi", "Europe", 150000);
        mdao.add("NY", "North America", 8000000);
        mdao.add("Moscow", "Europe", 12000000);
        mdao.add("London", "Europe", 8580000);
        List<Metropolis> continents = mdao.searchMetropolis("", "Eu", -1, true, false);
        assertEquals(3, continents.size());
        for (Metropolis m : continents) {
            assertTrue(m.getContinent().equals("Europe"));
        }
        List<Metropolis> continents2 = mdao.searchMetropolis("", "Europe", 2000000, true, true);
        assertEquals(2, continents2.size());
        for (Metropolis m : continents2) {
            assertTrue(m.getContinent().equals("Europe"));
        }
    }

    @Test
    public void testSimilars() {
        mdao.add("Memphis", "Africa", 8000000);
        mdao.add("Memphis", "North America", 4000000);
        mdao.add("Melbourne", "Australia", 6000000);
        mdao.add("Melbourne", "North America", 5500000);

        List<Metropolis> metroList = mdao.searchMetropolis("Memphis", "", -1, true, true);
        assertEquals(2, metroList.size());
        for (Metropolis m : metroList) {
            assertTrue(m.getMetropolisName().equals("Memphis"));
        }

        metroList = mdao.searchMetropolis("Memphis", "North America", -1, true, true);
        assertEquals(1, metroList.size());
        for (Metropolis m : metroList) {
            assertTrue(m.getContinent().equals("North America"));
        }
        metroList = mdao.searchMetropolis("Melbourne", "", 5500001, true, true);
        for (Metropolis m : metroList) {
            assertTrue(m.getContinent().equals("Australia"));
        }
        assertEquals(3, Metropolis.getColumnsCount());
    }

    @Test
    public void test() {
        mdao.add("Springfield", "North America", 170000);
        mdao.add("Springfield", "North America", 113000);
        mdao.add("Springfield", "Australia", 80000);
        mdao.add("Springfield", "Europe", 90000);

        List<Metropolis> metroList  = mdao.searchMetropolis("Spring", "", 100000, true, false);
        assertEquals(2, metroList.size());
        for (Metropolis m : metroList) {
            assertTrue(m.getMetropolisName().equals("Springfield"));
        }

        metroList  = mdao.searchMetropolis("Spring", "", 50000, true, false);
        assertEquals(4, metroList.size());
        for (Metropolis m : metroList) {
            assertTrue(m.getMetropolisName().equals("Springfield"));
        }
    }
}
