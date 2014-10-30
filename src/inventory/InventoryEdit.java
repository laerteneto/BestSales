package inventory;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class InventoryEdit extends JDialog {
     
    private JPanel mainPainel;
    private JLabel labelTitle, labelName, labelQuantity, labelDate;
    private JTextField TextQuantity, TextDate, TextProduct;
    private JButton buttomCancel, buttomSave;
    private int id;
    final InventoryView view;

    public InventoryEdit(InventoryView view, Integer id, String produto, Integer quantidade, String data) {
    	this.view = view;
        mainPainel = new JPanel(null);
        this.id = id;

        labelTitle = new JLabel("EDITION OF THE AMOUNT OF PRODUCT IN STOCK");
        labelTitle.setSize(650, 50);
        labelTitle.setLocation(10, 5);
        labelTitle.setFont(new Font("Verdana", Font.BOLD, 20));


        labelName = new JLabel("PRODUCT :");
        labelName.setSize(100, 30);
        labelName.setLocation(10, 60);


        TextProduct = new JTextField(produto);
        TextProduct.setSize(300, 30);
        TextProduct.setLocation(170, 60);
        TextProduct.setEditable(false);

        labelQuantity = new JLabel("QUANTITY :");
        labelQuantity.setSize(100, 30);
        labelQuantity.setLocation(500, 60);


        TextQuantity = new JTextField(quantidade.toString());
        TextQuantity.setSize(60, 30);
        TextQuantity.setLocation(605, 60);

        labelDate = new JLabel("DATE OF STOCK :");
        labelDate.setSize(150, 30);
        labelDate.setLocation(10, 100);

        TextDate = new JTextField(data);
        TextDate.setSize(80, 30);
        TextDate.setLocation(170, 100);


        buttomCancel = new JButton("CANCEL");
        buttomSave = new JButton("UPDATE");

        buttomCancel.setSize(110, 30);
        buttomCancel.setLocation(445, 140);

        buttomSave.setSize(110, 30);
        buttomSave.setLocation(570, 140);


        mainPainel.add(labelTitle);

        mainPainel.add(TextProduct);

        mainPainel.add(labelName);
        mainPainel.add(labelQuantity);
        mainPainel.add(labelDate);

        mainPainel.add(TextQuantity);
        mainPainel.add(TextDate);

        mainPainel.add(buttomCancel);
        mainPainel.add(buttomSave);


        this.getContentPane().add(mainPainel);

        setTitle("EDIT PRODUCT IN STOCK");
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);

        this.Events();
    }

    public void Events() {

        buttomSave.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        String quantity = TextQuantity.getText();
                        String date_register = TextDate.getText();

                        String sql = "";

                        sql += "UPDATE estoque SET quantidade = '" + quantity + "', data_estoque='" + date_register + "'  WHERE id = " + id + " ;";


                        InventoryDao editar = new InventoryDao();
                        editar.edit(sql);
                        
                        if (editar.edited) {

                            setVisible(false);
                        }
                        view.search();

                    }
                });


        buttomCancel.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        int result = JOptionPane.showConfirmDialog(null, "Want to cancel the changes?", "Confirmation", JOptionPane.YES_NO_OPTION);
                        if (result == JOptionPane.YES_OPTION) {
                            setVisible(false);
                        }
                    }
                });


    }
}
