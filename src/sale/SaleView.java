package sale;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
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
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class SaleView extends JInternalFrame {

    private JPanel mainPainel;
    private JLabel labelTitle, labelName;
    private JTextField textName;
    private JTable tableCustomer;
    private JButton buttomSearch;
    private DefaultTableModel tableModel;

    public SaleView() {
        mainPainel = new JPanel(null);

        /*TITULO*/
        labelTitle = new JLabel("CUSTOMER ORDERS");
        labelTitle.setSize(300, 50);
        labelTitle.setLocation(10, 5);
        labelTitle.setFont(new Font("Verdana", Font.BOLD, 20));

        /* ADICIONANDO AO PAINEL */


        labelName = new JLabel("NAME :");
        labelName.setSize(50, 30);
        labelName.setLocation(10, 65);


        textName = new JTextField();
        textName.setSize(730, 30);
        textName.setLocation(100, 65);


        buttomSearch = new JButton("SEARCH");
        buttomSearch.setSize(150, 30);
        buttomSearch.setLocation(848, 65);

        /*EDIÇÃO DAS CELULAS NÃO PODE ACONTECER*/
        tableModel = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };

        tableCustomer = new JTable(tableModel);
        tableModel.addColumn("ORDER ID");
        tableModel.addColumn("CUSTOMER NAME");
        tableModel.addColumn("VALUE R$");
        tableModel.addColumn("DATE OF PURCHASE");


        JScrollPane scrollTabelaCliente = new JScrollPane(tableCustomer);
        scrollTabelaCliente.setSize(990, 400);
        scrollTabelaCliente.setLocation(10, 110);

        mainPainel.add(labelTitle);
        mainPainel.add(labelName);
        mainPainel.add(textName);

        mainPainel.add(buttomSearch);

        mainPainel.add(scrollTabelaCliente);


        setTitle("Shopping list");
        setSize(800, 600);

        getContentPane().add(mainPainel);

        this.Events();
        this.search();

    }

    public void Events() {

        buttomSearch.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                search();
            }
        });

        textName.addKeyListener(
                new KeyAdapter() {

                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            search();
                        }
                    }
                });

    }

    public void search() {
        String name = textName.getText();
        SaleDao purchase = new SaleDao();
        purchase.listByName(name);

        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }

        /* Money format */
        Locale BRAZIL = new Locale("pt", "BR");
        DecimalFormatSymbols REAL = new DecimalFormatSymbols(BRAZIL);
        DecimalFormat DinheiroReal = new DecimalFormat("###,###,##0.00", REAL);

        /* data format */
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        try {
            while (purchase.list.next()) {
                int strCode = purchase.list.getInt("id");
                String strNane = purchase.list.getString("nome");
                String strValue = purchase.list.getString("valor");
                Date strDate = purchase.list.getDate("data_compra");
                tableModel.addRow(new Object[]{strCode, strNane, DinheiroReal.format(Double.parseDouble(strValue)), sdf.format(strDate)});
            }
        } catch (SQLException ex) {
            Logger.getLogger(SaleView.class.getName()).log(Level.SEVERE, null, ex);
        }

        tableCustomer.transferFocus();
    }
}
