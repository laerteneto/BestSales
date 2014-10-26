package product;

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

public class ProductView extends JInternalFrame {

    private JPanel mainPainel;
    private JLabel labelTitle, labelName, labelObs;
    private JTextField textName;
    private JTable tableCustomer;
    private JButton buttomSearch, buttomRemoveRows;
    private DefaultTableModel tableModel;
    
    public ProductView() {
        mainPainel = new JPanel(null);

        labelTitle = new JLabel("PRODUCT LIST");
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

        tableCustomer = new JTable(tableModel);
        tableModel.addColumn("PRODUCT CODE");
        tableModel.addColumn("NAME");
        tableModel.addColumn("PURCHASE PRICE R$");
        tableModel.addColumn("SALE PRICE R$");
        tableModel.addColumn("DESCRIPTION");


        JScrollPane scrollTabelaCliente = new JScrollPane(tableCustomer);
        scrollTabelaCliente.setSize(990, 400);
        scrollTabelaCliente.setLocation(10, 110);

        buttomRemoveRows = new JButton("REMOVE LINE(S) SELECTED");
        buttomRemoveRows.setSize(350, 30);
        buttomRemoveRows.setLocation(300, 515);

        mainPainel.add(labelTitle);
        mainPainel.add(labelName);
        mainPainel.add(textName);

        mainPainel.add(buttomSearch);
        mainPainel.add(buttomRemoveRows);

        mainPainel.add(scrollTabelaCliente);

        /*Bottome Notion*/
        labelObs = new JLabel("Double-click the record to edit it !");
        labelObs.setSize(350, 15);
        labelObs.setLocation(10, 525);
        labelObs.setFont(new Font("arial", Font.BOLD, 12));
        labelObs.setForeground(Color.gray);

        mainPainel.add(labelObs);


        setTitle("Product List");
        setSize(800, 600);

        getContentPane().add(mainPainel);

        this.Events();
        this.search();

    }

    public void Events() {
    	ProductView productView = this;
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
                        /*Verify if something is selected*/
                        if (tableCustomer.getRowCount() == 0) {
                            JOptionPane.showMessageDialog(null, "Select the rows that you want to remove", null, JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        int result = JOptionPane.showConfirmDialog(null, "You really want to remove the selected rows?", "ATTENTION", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                        if (result == 1) {
                            return;  // STOP IF THE USER PRESS NO !
                        }

                        for (int i = 0; i < tableModel.getRowCount(); i++) {
                            if (tableCustomer.isCellSelected(i, 0)) {
                                Object id = tableCustomer.getValueAt(i, 0);
                                System.out.println(id);
                                ProductDao removeFromProducts = new ProductDao();
                                removeFromProducts.remove(id);
                            }
                        }
                        search();

                    }
                });

        tableCustomer.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
            	
                if (e.getClickCount() == 2) {
                    Integer id = null;
                    String name = null;
                    String price_purchase = null;
                    String price_sale = null;
                    String description = null;

                    int i = tableCustomer.getSelectedRow();

                    if (tableCustomer.isCellSelected(i, 0)) {
                        id = Integer.parseInt(String.valueOf(tableCustomer.getValueAt(i, 0)));
                        name = tableCustomer.getValueAt(i, 1).toString();
                        price_purchase = tableCustomer.getValueAt(i, 2).toString();
                        price_sale = tableCustomer.getValueAt(i, 3).toString();
                        if (tableCustomer.getValueAt(i, 4) == null) {
                            description = "";
                        } else {
                            description = tableCustomer.getValueAt(i, 4).toString();
                        }
                    }

                    ProductEdit editionProduct = new ProductEdit(productView, id, name, price_purchase, price_sale, description);
                    editionProduct.setVisible(true);

                }
            }
        });

    }

    public void search() {
        ///// COPY THE BUTTON TO SEE ( SEE LATER )
        String name = textName.getText();
        ProductDao pdao = new ProductDao();
        pdao.search(name);

        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }

        /* MONEY FORMAT */
        Locale BRAZIL = new Locale("pt", "BR");
        DecimalFormatSymbols REAL = new DecimalFormatSymbols(BRAZIL);
        DecimalFormat DinheiroReal = new DecimalFormat("###,###,##0.00", REAL);

        /* DATE FORMAT */
        //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {
            while (pdao.list.next()) {
                int strCode = pdao.list.getInt("id");
                String strName = pdao.list.getString("nome");
                String strPurchase = pdao.list.getString("preco_compra");
                String strSale = pdao.list.getString("preco_venda");
                String strDes = pdao.list.getString("descricao");
                tableModel.addRow(new Object[]{strCode, strName, DinheiroReal.format(Double.parseDouble(strPurchase)), DinheiroReal.format(Double.parseDouble(strSale)), strDes});
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}