package inventory;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class InventoryView extends JInternalFrame {

    private JPanel mainPainel;
    private JLabel labelTitle, labelName, labelObs;
    private JTextField textName;
    private JTable tableInventory;
    private JButton buttomSearch, buttomRemoveRows;
    private DefaultTableModel tableModel;

    public InventoryView() {
        mainPainel = new JPanel(null);

        labelTitle = new JLabel("STOCK OF PRODUCTS");
        labelTitle.setSize(300, 50);
        labelTitle.setLocation(10, 5);
        labelTitle.setFont(new Font("Verdana", Font.BOLD, 20));

        
        labelName = new JLabel("NAME :");
        labelName.setSize(50, 30);
        labelName.setLocation(10, 65);


        textName = new JTextField();
        textName.setSize(730, 30);
        textName.setLocation(100, 65);


        buttomSearch = new JButton("SEARCH");
        buttomSearch.setSize(150, 30);
        buttomSearch.setLocation(848, 65);

        /*EDITION IS NOT ALLOWED*/
        tableModel = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };

        tableInventory = new JTable(tableModel);
        tableModel.addColumn("id");
        tableModel.addColumn("PRODUCT");
        tableModel.addColumn("QUANTITY");
        tableModel.addColumn("VALUE $ / UNIT");
        tableModel.addColumn("DATE OF STOCK");


        JScrollPane scrollTabelaCliente = new JScrollPane(tableInventory);
        scrollTabelaCliente.setSize(990, 400);
        scrollTabelaCliente.setLocation(10, 110);

        buttomRemoveRows = new JButton("REMOVE SELECT LINE(s)");
        buttomRemoveRows.setSize(350, 30);
        buttomRemoveRows.setLocation(300, 515);


        mainPainel.add(labelTitle);
        mainPainel.add(labelName);
        mainPainel.add(textName);

        mainPainel.add(buttomSearch);
        mainPainel.add(buttomRemoveRows);

        mainPainel.add(scrollTabelaCliente);

        /*OBS*/
        labelObs = new JLabel("Double-click in the record to edit it!");
        labelObs.setSize(350, 15);
        labelObs.setLocation(10, 525);
        labelObs.setFont(new Font("arial", Font.BOLD, 12));
        labelObs.setForeground(Color.gray);

        mainPainel.add(labelObs);


        setTitle("Products in stock");
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
                buttomRemoveRows.transferFocus();

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


        buttomRemoveRows.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {

                        /*VERIFYING IF IS SELECTED*/
                        if (tableInventory.getRowCount() == 0) {
                            JOptionPane.showMessageDialog(null, "Select the rows that you want to remove", null, JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        int result = JOptionPane.showConfirmDialog(null, "You really want to remove the selected rows?", "ATTENTION", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                        if (result == 1) {
                            return;  // STOP WITH NO
                        }

                        for (int i = 0; i < tableModel.getRowCount(); i++) {
                            if (tableInventory.isCellSelected(i, 0)) {
                                Object id = tableInventory.getValueAt(i, 0);
                                System.out.println(id);
                                InventoryDao removeFromInventory = new InventoryDao();
                                removeFromInventory.remove(id);
                            }
                        }
                        search();
                    }
                });

        tableInventory.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Integer id = null;
                    String name = null;
                    Integer quantity = null;
                    String date = null;
                    int i = tableInventory.getSelectedRow();

                    if (tableInventory.isCellSelected(i, 0)) {
                        id = Integer.parseInt(String.valueOf(tableInventory.getValueAt(i, 0)));
                        name = tableInventory.getValueAt(i, 1).toString();
                        quantity = Integer.parseInt(String.valueOf(tableInventory.getValueAt(i, 2)));
                        date = tableInventory.getValueAt(i, 4).toString();
                    }

                    InventoryEdit inventoryEdition = new InventoryEdit(id, name, quantity, date);
                    inventoryEdition.setVisible(true);

                }
            }
        });

    }

    public void search() {
        String name = textName.getText();
        InventoryDao inventory = new InventoryDao();
        inventory.listInventory(name);

        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }

        /* MONEY FORMAT */
        Locale BRAZIL = new Locale("pt", "BR");
        DecimalFormatSymbols REAL = new DecimalFormatSymbols(BRAZIL);
        DecimalFormat DinheiroReal = new DecimalFormat("###,###,##0.00", REAL);

        /* DATA FORMAT */
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        try {
            while (inventory.list.next()) {
                int id = inventory.list.getInt("id");
                String strName = inventory.list.getString("nome");
                int Intquantity = inventory.list.getInt("quantidade");
                String strValue = inventory.list.getString("preco_venda");
                Date strDate = inventory.list.getDate("data_estoque");
                tableModel.addRow(new Object[]{id, strName, Intquantity, DinheiroReal.format(Double.parseDouble(strValue)), sdf.format(strDate)});
            }
        } catch (SQLException ex) {
            Logger.getLogger(InventoryView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
