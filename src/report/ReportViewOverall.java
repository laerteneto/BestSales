package report;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class ReportViewOverall extends JInternalFrame {

    private JPanel mainPainel;
    private JLabel labelTitle, labelSumOverall, labelSumValeu, labelFilter, labelSearch, labelSumSelected;
    private JTable tableCustomer;
    private DefaultTableModel tableModel;
    private JTextField textFilter;
    private JComboBox comboFilter;
    private JButton buttomSearch, buttomSum;

    public ReportViewOverall() {
        mainPainel = new JPanel(null);

        /*TITULO*/
        labelTitle = new JLabel("GLOBAL REPORT");
        labelTitle.setSize(300, 50);
        labelTitle.setLocation(10, 5);
        labelTitle.setFont(new Font("Verdana", Font.BOLD, 20));

        labelSumOverall = new JLabel("FINANCE - TOTAL $ :");
        labelSumOverall.setSize(210, 30);
        labelSumOverall.setLocation(10, 50);

        labelSumValeu = new JLabel();
        labelSumValeu.setSize(100, 30);
        labelSumValeu.setLocation(180, 50);
        labelSumValeu.setForeground(Color.blue);

        /* ADD ON PAINEL */

        /*EDITION IS NOT ALLOWED*/
        tableModel = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };

        tableCustomer = new JTable(tableModel);
        tableModel.addColumn("CUSTOMER");
        tableModel.addColumn("VALUE $");
        tableModel.addColumn("DATE OF PURCHASE");


        JScrollPane scrollTabelaCliente = new JScrollPane(tableCustomer);
        scrollTabelaCliente.setSize(860, 500);
        scrollTabelaCliente.setLocation(10, 110);


        /*FILTROS*/

        labelSearch = new JLabel("SEARCH");
        labelSearch.setSize(100, 30);
        labelSearch.setLocation(635, 40);
        labelSearch.setForeground(Color.gray);

        labelFilter = new JLabel("by :");
        labelFilter.setSize(40, 20);
        labelFilter.setLocation(420, 70);

        comboFilter = new JComboBox();
        comboFilter.setSize(130, 20);
        comboFilter.setLocation(470, 70);
        comboFilter.addItem("");
        comboFilter.addItem("name");
        comboFilter.addItem("date of purchase");

        textFilter = new JTextField();
        textFilter.setSize(120, 20);
        textFilter.setLocation(615, 70);

        buttomSearch = new JButton("SEARCH");
        buttomSearch.setSize(120, 20);
        buttomSearch.setLocation(750, 70);

        buttomSum = new JButton("SELECTED SOMAR");
        buttomSum.setSize(200, 30);
        buttomSum.setLocation(10, 630);

        labelSumSelected = new JLabel();
        labelSumSelected.setSize(100, 30);
        labelSumSelected.setLocation(250, 630);
        labelSumSelected.setForeground(Color.blue);


        mainPainel.add(labelTitle);
        mainPainel.add(scrollTabelaCliente);
        mainPainel.add(labelSumOverall);
        mainPainel.add(labelSumValeu);
        mainPainel.add(comboFilter);
        mainPainel.add(textFilter);
        mainPainel.add(labelFilter);
        mainPainel.add(labelSearch);
        mainPainel.add(buttomSearch);
        mainPainel.add(buttomSum);
        mainPainel.add(labelSumSelected);

        setTitle("Global Report");
        setSize(800, 600);

        getContentPane().add(mainPainel);

        this.Events();
        this.searchInDatabase();

    }

    public void search() {
        ReportDao reportFiltered = new ReportDao();
        String filter = comboFilter.getSelectedItem().toString();
        String valueFilter = textFilter.getText();

        /*Verifying if something is coming null*/
        if (filter == "" || valueFilter == "") {
            JOptionPane.showMessageDialog(null, "FILL ALL FIELDS TO COMPLETE THE SURVEY!");
            return;
        }


        reportFiltered.filter(filter, valueFilter);

        /*Verifying database errors*/
        if (reportFiltered.errorFilter) {
            JOptionPane.showMessageDialog(null, "VERIFY THE DATAS!", null, JOptionPane.WARNING_MESSAGE);
            return;
        }

        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }

        /* MONEY FORMAT */
        Locale US = new Locale("en", "UK");
        DecimalFormatSymbols REAL = new DecimalFormatSymbols(US);
        DecimalFormat DR = new DecimalFormat("###,###,##0.00", REAL);

        /* DATE FORMAT */
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {
            while (reportFiltered.list.next()) {
                String strNameCustomer = reportFiltered.list.getString("nome");
                String strValue = reportFiltered.list.getString("valor");
                Date strDate = reportFiltered.list.getDate("data_compra");
                tableModel.addRow(new Object[]{strNameCustomer, DR.format(Double.parseDouble(strValue)), sdf.format(strDate)});
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReportViewOverall.class.getName()).log(Level.SEVERE, null, ex);
        }


        double sum = 0;
        for (int i = 0; i < tableCustomer.getRowCount(); i++) {
            String removeComma = tableModel.getValueAt(i, 1).toString();
            sum = sum + Double.parseDouble(removeComma);

        }
        String Sum = String.valueOf(sum);

        /* Money format */

        labelSumValeu.setText(DR.format(Double.parseDouble(Sum)));
    }

    public void Events() {

        textFilter.addActionListener(new ActionListener() {

                    public void keyReleased(KeyEvent e) {
                        search();
                    }

                    public void actionPerformed(ActionEvent e) {
                        search();
                    }
                });


        buttomSearch.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        search();

                    }
                });

        buttomSum.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        double sum = 0;
                        for (int i = 0; i < tableCustomer.getRowCount(); i++) {
                            if (tableCustomer.isCellSelected(i, 1)) {
                                String revenue = tableModel.getValueAt(i, 1).toString();
                                sum = sum + Double.parseDouble(revenue);
                            }
                        }
                        String Sum = String.valueOf(sum);

                        /* Money Format */
                        Locale US = new Locale("en", "UK");
                        DecimalFormatSymbols REAL = new DecimalFormatSymbols(US);
                        DecimalFormat DR = new DecimalFormat("###,###,##0.00", REAL);


                        labelSumSelected.setText(DR.format(Double.parseDouble(Sum)));

                    }
                });



    }

    public void searchInDatabase() {

        /* Money format */

        Locale US = new Locale("en", "UK");
        DecimalFormatSymbols REAL = new DecimalFormatSymbols(US);
        DecimalFormat DR = new DecimalFormat("###,###,##0.00", REAL);

        /*SOMANDO*/
        ReportDao reportSum = new ReportDao();
        reportSum.sumOverall();
        try {
            while (reportSum.list.next()) {
                double value = reportSum.list.getDouble("soma");
                String str = String.valueOf(value);
                labelSumValeu.setText(DR.format(Double.parseDouble(str)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReportViewToday.class.getName()).log(Level.SEVERE, null, ex);
        }


        /*---------------  ADD VALUES IN LIST  ----------------*/
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        /* Money format */
        /* Date format */
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        ReportDao reportOverall = new ReportDao();
        reportOverall.listPurchaseOverall();
        try {
            while (reportOverall.list.next()) {
                String strNameCustomer = reportOverall.list.getString("nome");
                String strValue = reportOverall.list.getString("valor");
                Date strDate = reportOverall.list.getDate("data_compra");
                tableModel.addRow(new Object[]{strNameCustomer, DR.format(Double.parseDouble(strValue)), sdf.format(strDate)});
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReportViewOverall.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}