import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MetropolisFrame extends JFrame{
    private JTextField metropolisField;
    private JTextField continentField;
    private JTextField populationField;
    private JComboBox populationC;
    private JComboBox matchC;
    private JButton addButton;
    private JButton searchButton;
    private JScrollPane scroll;

    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;

    private MetropolisDAO dao;
    private MetropolisesTable metropolisesTable;
    private static final String POPULAR_LARGER = "Population Larger Than";
    private static final String EXACT = "Exact Match";

    public MetropolisFrame(MetropolisDAO dao, MetropolisesTable metropolisesTable) {
        this.dao = dao;
        this.metropolisesTable = metropolisesTable;
        setSize(WIDTH, HEIGHT);
        initNorth();
        initEast();
        initTable(metropolisesTable);
        eventListeners();
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void initTable(MetropolisesTable metropolisesTable) {
        JTable table = new JTable(metropolisesTable);
        scroll = new JScrollPane(table);
        add(scroll);
    }

    public void initNorth() {
        int colSize = 12;
        String space = "   ";
        metropolisField = new JTextField(colSize);
        continentField = new JTextField(colSize);
        populationField = new JTextField(colSize);

        JPanel northPanel = new JPanel();
        northPanel.add(new JLabel("Metropolis: "));
        northPanel.add(metropolisField);
        northPanel.add(new JLabel(space));
        northPanel.add(new JLabel("Continent: "));
        northPanel.add(continentField);
        northPanel.add(new JLabel(space));
        northPanel.add(new JLabel("Population: "));
        northPanel.add(populationField);

        add(northPanel, BorderLayout.NORTH);
    }

    public void initEast() {
        JPanel eastPanel = new JPanel(new GridLayout(6,1));
        Box box = Box.createVerticalBox();
        box.add(new JLabel("     "));
        addButton = new JButton("Add");
        box.add(addButton);
        searchButton = new JButton("Search");
        box.add(searchButton);
        eastPanel.add(box);
        JPanel little = new JPanel(new GridLayout(2, 1));
        little.setBorder(new TitledBorder("Search Options"));
        populationC = new JComboBox();
        populationC.addItem("Population Larger Than");
        populationC.addItem("Population Less Than");

        matchC = new JComboBox();
        matchC.addItem("Exact Match");
        matchC.addItem("Partial Match");
        little.add(populationC);
        little.add(matchC);
        eastPanel.add(little);

        add(eastPanel, BorderLayout.EAST);
    }

    private void eventListeners() {
        setAddButton();
        setSearchButton();
    }

    private void setSearchButton() {
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String metropolisName = metropolisField.getText();
                String continent = continentField.getText();
                String popTxt = populationField.getText();
                int population = popTxt.isEmpty() ? -1 : Integer.parseInt(popTxt);
                String largerThan = (String) populationC.getSelectedItem();
                String exact = (String) matchC.getSelectedItem();
                emptyFields();
                boolean isLarger = (largerThan.equals(POPULAR_LARGER));
                boolean isExact = (exact.equals(EXACT));
                searchMetropolis(metropolisName, continent, population, isLarger, isExact);
            }
        });
    }


    private void setAddButton() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String metropolisName = metropolisField.getText();
                String continent = continentField.getText();
                String popTxt = populationField.getText();
                int population = popTxt.isEmpty() ? -1 : Integer.parseInt(popTxt);
                emptyFields();
                addMetropolis(metropolisName, continent, population);
            }
        });
    }

    public void searchMetropolis(String metropolistName, String continent, int population, boolean isLarger, boolean isExact) {
        if (!metropolistName.isEmpty() || !continent.isEmpty() || population != -1) {
            List<Metropolis> newList = dao.searchMetropolis(metropolistName, continent, population, isLarger, isExact);
            metropolisesTable.update(newList);
            metropolisesTable.fireTableDataChanged();
        } else if (metropolistName.isEmpty() && continent.isEmpty() && population == -1) {
            metropolisesTable.update(dao.getAll());
            metropolisesTable.fireTableDataChanged();
        }
    }

    private void emptyFields() {
        metropolisField.setText("");
        continentField.setText("");
        populationField.setText("");
    }

    private void addMetropolis(String metropolisName, String continent, int population) {
        if (isValidMetropolis(metropolisName, continent, population)) {
            List<Metropolis> newList = dao.add(metropolisName, continent, population);
            metropolisesTable.update(newList);
            metropolisesTable.fireTableDataChanged();
        }
    }

    private boolean isValidMetropolis(String metropolisName, String continent, int population) {
        return metropolisName != "" && continent != "" && population != -1;
    }
}
