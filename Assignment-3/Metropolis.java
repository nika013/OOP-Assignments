public class Metropolis {
    private String metropolisName;
    private String continent;
    private int population;
    private static final int columnsCount = 3;

    public Metropolis() {

    }

//    public Metropolis(String metropolisName, String continent, int population) {
//        this.metropolisName = metropolisName;
//        this.continent = continent;
//        this.population = population;
//    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public void setMetropolisName(String metropolisName) {
        this.metropolisName = metropolisName;
    }

    public int getPopulation() {
        return population;
    }

    public String getMetropolisName() {
        return metropolisName;
    }

    public String getContinent() {
        return continent;
    }

    public static int getColumnsCount() {
        return columnsCount;
    }
}
