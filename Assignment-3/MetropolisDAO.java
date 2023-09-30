import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MetropolisDAO {
    private final BasicDataSource dataSource;
    private String table = "metropolises";
    public MetropolisDAO() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/MetropolisesDB");
        dataSource.setUsername("");
        dataSource.setPassword("");
        this.dataSource = dataSource;
    }

    public MetropolisDAO(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public List<Metropolis> add(String metropolisName, String continent, int population) {
        ArrayList metropolises = new ArrayList();
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            String query = "INSERT INTO " + table + " (metropolis, continent, population) ";
            query += "VALUES ('" + metropolisName + "', ";
            query += "'" + continent + "', " + Integer.toString(population);
            query += ");";

            statement.executeUpdate(query);
            ResultSet result = statement.executeQuery("SELECT * FROM " + table);

            while (result.next()) {
                Metropolis m = new Metropolis();
                m.setMetropolisName(result.getString(1));
                m.setContinent(result.getString(2));
                m.setPopulation(result.getInt(3));
                metropolises.add(m);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return metropolises;
    }

    public List<Metropolis> searchMetropolis(String metropolisName,
                                             String continent,
                                             int population,
                                             boolean popLarger,
                                             boolean exact) {
        ArrayList<Metropolis> metropolises = new ArrayList<>();
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM " + table;
            query = filter(metropolisName, exact, query, "metropolis");
            query = filter(continent, exact, query, "continent");
            query = filterPopulation(population, popLarger, query);
            query += ";";
            ResultSet result = statement.executeQuery(query);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            while (result.next()) {
                Metropolis m = new Metropolis();
                m.setMetropolisName(result.getString(1));
                m.setContinent(result.getString(2));
                m.setPopulation(result.getInt(3));
                metropolises.add(m);
            }
        } catch (SQLException e) {
        }
        return metropolises;
    }

    public List<Metropolis> getAll() {
        ArrayList<Metropolis> metropolises = new ArrayList<>();
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM " + table + ";");
            while (result.next()) {
                Metropolis m = new Metropolis();
                m.setMetropolisName(result.getString(1));
                m.setContinent(result.getString(2));
                m.setPopulation(result.getInt(3));
                metropolises.add(m);
            }
        } catch (SQLException e) {
            Throwable throwables = new Throwable();
            throwables.printStackTrace();
        }
        return metropolises;
    }

    private String filterPopulation(int population, boolean popLarger,  String query) {
        if (population == -1) return query;
        String operator = popLarger ? ">" : "<=";
        if (!query.contains("WHERE"))
            query += " WHERE population " + operator + " " + Integer.toString(population);
        else
            query += " AND population " + operator + " " + Integer.toString(population);
        return  query;
    }

    private String filter(String name, boolean exact, String query, String col) {
        if (!name.isEmpty() && exact) {
            if (!query.contains("WHERE"))
                query += " WHERE " + col + " = " + "'" + name + "'";
            else
                query += " AND " + col + " = " + "'" + name + "'";
        } else if (!name.isEmpty() && !exact) {
            if (!query.contains("WHERE"))
                query += " WHERE " + col + " LIKE " + "'%" + name + "%'";
            else
                query += " AND " + col + " LIKE " + "'%" + name + "%'";
        }
        return query;
    }
}
