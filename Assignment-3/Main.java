import org.apache.commons.dbcp2.BasicDataSource;

public class Main {
    public static void main(String[] args) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/MetropolisesDB");
        dataSource.setUsername("nika13");
        dataSource.setPassword("Nikasql123!.");

        MetropolisDAO dao = new MetropolisDAO(dataSource);
        MetropolisesTable metropolisesTable = new MetropolisesTable(dao.getAll());
        MetropolisFrame frame = new MetropolisFrame(dao, metropolisesTable);
    }

}
