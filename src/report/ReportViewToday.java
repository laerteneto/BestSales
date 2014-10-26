package report;

import system.SystemWindow;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ReportViewToday extends JInternalFrame {

    private JPanel mainPainel;
    private JLabel labelTitle, labelSumToday, labelSumValue, labelSumSelected;
    private JTable tableCustomer;
    private DefaultTableModel tableModel;
    private JButton buttomSave;

    public ReportViewToday() {
        mainPainel = new JPanel(null);

        /*TITULO*/
        labelTitle = new JLabel("REPORT TODAY");
        labelTitle.setSize(300, 50);
        labelTitle.setLocation(10, 5);
        labelTitle.setFont(new Font("Verdana", Font.BOLD, 20));

        labelSumToday = new JLabel("TODAY'S BALANCE $ :");
        labelSumToday.setSize(210, 30);
        labelSumToday.setLocation(10, 50);

        labelSumValue = new JLabel();
        labelSumValue.setSize(100, 30);
        labelSumValue.setLocation(220, 50);
        labelSumValue.setForeground(Color.blue);


        /* Money format */
        Locale BR = new Locale("pt", "BR");
        DecimalFormatSymbols R = new DecimalFormatSymbols(BR);
        DecimalFormat DR = new DecimalFormat("###,###,##0.00", R);

        SystemWindow j0 = new SystemWindow(); // RECUPERAR A DATA
        ReportDao reportSum = new ReportDao();
        reportSum.sumToday(j0.dateString);
        try {
            while (reportSum.list.next()) {
                double valor = reportSum.list.getDouble("soma");
                String str = String.valueOf(valor);
                labelSumValue.setText(DR.format(Double.parseDouble(str)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReportViewToday.class.getName()).log(Level.SEVERE, null, ex);
        }

        /* ADD IN PAINEL */

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


        buttomSave = new JButton("SELECTED ORDER");
        buttomSave.setSize(200, 30);
        buttomSave.setLocation(10, 630);

        labelSumSelected = new JLabel();
        labelSumSelected.setSize(100, 30);
        labelSumSelected.setLocation(250, 630);
        labelSumSelected.setForeground(Color.blue);

        mainPainel.add(labelTitle);
        mainPainel.add(scrollTabelaCliente);
        mainPainel.add(labelSumToday);
        mainPainel.add(labelSumValue);
        mainPainel.add(buttomSave);
        mainPainel.add(labelSumSelected);

        setTitle("Report today");
        setSize(800, 600);

        getContentPane().add(mainPainel);

        this.Events();
        this.search();



    }

    public void Events() {
        buttomSave.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        double sum = 0;
                        for (int i = 0; i < tableCustomer.getRowCount(); i++) {
                            if (tableCustomer.isCellSelected(i, 1)) {
                                String removeComma = tableModel.getValueAt(i, 1).toString();
                                removeComma = removeComma.replace(".", " ");
                                removeComma = removeComma.replace(" ", "");
                                removeComma = removeComma.replace(",", ".");
                                sum = sum + Double.parseDouble(removeComma);
                            }
                        }
                        String Sum = String.valueOf(sum);

                        /* OBTENDO FORMATO CORRETO DO DINHEIRO */
                        Locale BR = new Locale("pt", "BR");
                        DecimalFormatSymbols R = new DecimalFormatSymbols(BR);
                        DecimalFormat DR = new DecimalFormat("###,###,##0.00", R);


                        labelSumSelected.setText(DR.format(Double.parseDouble(Sum)));

                    }
                });

    }

    public void search() {

        /*---------------  ADICIONAR VALORES A LISTA  ----------------*/

        SystemWindow j = new SystemWindow();
        ReportDao reportDao = new ReportDao();
        reportDao.listPurchaseToday(j.dateString);

        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }

        /* OBTENDO FORMATO CORRETO DO DINHEIRO */
        Locale US = new Locale("en", "UK");
        DecimalFormatSymbols REAL = new DecimalFormatSymbols(US);
        DecimalFormat DinheiroReal = new DecimalFormat("###,###,##0.00", REAL);

        /* OBTENDO FORMATO CORRETO PARA DATA */
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        try {
            while (reportDao.list.next()) {
                String strNameCustomer = reportDao.list.getString("nome");
                String strValue = reportDao.list.getString("valor");
                Date strDate = reportDao.list.getDate("data_compra");
                tableModel.addRow(new Object[]{strNameCustomer, DinheiroReal.format(Double.parseDouble(strValue)), sdf.format(strDate)});
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReportViewToday.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
