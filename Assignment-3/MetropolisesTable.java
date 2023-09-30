import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class MetropolisesTable extends AbstractTableModel {
    private List<Metropolis> metropolises; // Data structure where all Metropolis objects are saved can be viewed as 2-d array
    private final String[] columnNames = { "metropolis", "continent", "population"}; // Array of colum names in metropolis table

    /**
     * This constructor does not takes any parameters
     */
    public MetropolisesTable() {
        metropolises = new ArrayList<>();
    }

    /**
     * Second construcor which takes already created arguments
     * @param metropolises List of Metropolis Objects
     */
    public MetropolisesTable(List<Metropolis> metropolises) {

        update(metropolises);
    }

    /**
     * This method assigns passed argument to instance variable
     * this is used for changing and managing data information about table
     * @param metropolises List of Metropolis Objects
     */
    public void update(List<Metropolis> metropolises) {
        this.metropolises = metropolises;
    }

    /**
     * Returns the number of rows in the model. A JTable uses this method to determine how many rows it should display. This method should be quick, as it is called frequently during rendering.
     * Returns:
     * the number of rows in the model
     */
    @Override
    public int getRowCount() {
        return metropolises.size();
    }

    /**
     * Returns the number of columns in the model. A JTable uses this method to determine how many columns it should create and display by default.
     * Returns:
     * the number of columns in the model
     */
    @Override
    public int getColumnCount() {
        return Metropolis.getColumnsCount();
    }

    /**
     * Returns the value for the cell at columnIndex and rowIndex.
     * Params:
     * @param rowIndex – the row whose value is to be queried
     * @param columnIndex – the column whose value is to be queried
     * @return Object which can be any type that can be store in sql
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return metropolises.get(rowIndex).getMetropolisName();
        } else if (columnIndex == 1) {
            return metropolises.get(rowIndex).getContinent();
        } else if (columnIndex == 2) {
            return metropolises.get(rowIndex).getPopulation();
        }
        return null;
    }

    /**
     * Returns a default name for the column using spreadsheet conventions: A, B, C, ... Z, AA, AB, etc. If column cannot be found, returns an empty string.
     * Params:
     * column – the column being queried
     * Returns:
     * a string containing the default name of column
     * @param index  the column being queried
     * @return string that is name of the indexed column
     */
    @Override
    public String getColumnName(int index) {
        return columnNames[index];
    }
}
