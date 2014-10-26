package product;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ProductEdit extends JDialog {

    private JPanel mainPainel;
    private JLabel labelTitle, labelName, labelPricePurchase, labelPriceSale, labelDescription, labelId;
    private JTextField textoName, textPurchase, textSale, textId, textDescription;
    private JButton buttomCancel, buttomUpdate;
    private int id;
    final ProductView productView;

    public ProductEdit(ProductView productView, int id, String nome, String preco_compra, String preco_venda, String descricao) {
        this.id = id;
        this.productView = productView;
        mainPainel = new JPanel(null);

        /*TITULO*/
        labelTitle = new JLabel("EDITING PRODUCTS");
        labelTitle.setSize(300, 50);
        labelTitle.setLocation(10, 5);
        labelTitle.setFont(new Font("Verdana", Font.BOLD, 20));
        mainPainel.add(labelTitle);


        labelId = new JLabel("PRODUCT CODE: ");
        labelId.setSize(230, 30);
        labelId.setLocation(10, 65);
        labelId.setFont(new Font("Verdana", Font.BOLD, 15));

        mainPainel.add(labelId);

        textId = new JTextField(String.valueOf(id));
        textId.setSize(30, 30);
        textId.setLocation(210, 65);
        textId.setEditable(false);
        textId.setFont(new Font("Verdana", Font.BOLD, 15));
        textId.setBorder(null);

        mainPainel.add(textId);


        labelName = new JLabel("NAME :");
        labelName.setSize(100, 30);
        labelName.setLocation(10, 100);

        labelPricePurchase = new JLabel("PURCHASE PRICE $ :");
        labelPricePurchase.setSize(180, 30);
        labelPricePurchase.setLocation(10, 135);

        labelPriceSale = new JLabel("SALE PRICE $ :");
        labelPriceSale.setSize(180, 30);
        labelPriceSale.setLocation(10, 170);

        labelDescription = new JLabel("DESCRIPTION :");
        labelDescription.setSize(120, 30);
        labelDescription.setLocation(10, 205);

        mainPainel.add(labelName);
        mainPainel.add(labelPricePurchase);
        mainPainel.add(labelPriceSale);
        mainPainel.add(labelDescription);


        /*Changing , by . to put in database*/
        preco_compra = preco_compra.replace(',', '.');
        preco_venda = preco_venda.replace(',', '.');

        textoName = new JTextField(nome);
        textoName.setSize(230, 30);
        textoName.setLocation(200, 100);

        textPurchase = new JTextField(preco_compra);
        textPurchase.setSize(230, 30);
        textPurchase.setLocation(200, 135);

        textSale = new JTextField(preco_venda);
        textSale.setSize(230, 30);
        textSale.setLocation(200, 170);

        textDescription = new JTextField(descricao);
        textDescription.setSize(600, 30);
        textDescription.setLocation(200, 205);

        mainPainel.add(textoName);
        mainPainel.add(textPurchase);
        mainPainel.add(textSale);
        mainPainel.add(textDescription);

        buttomCancel = new JButton("CANCEL");
        buttomUpdate = new JButton("UPDATE");

        buttomCancel.setSize(110, 30);
        buttomCancel.setLocation(570, 275);

        buttomUpdate.setSize(110, 30);
        buttomUpdate.setLocation(690, 275);


        mainPainel.add(buttomCancel);
        mainPainel.add(buttomUpdate);

        this.getContentPane().add(mainPainel);

        this.setTitle("Editing Products");
        this.setSize(850, 600);
        this.setLocationRelativeTo(null);

        this.Events();
    }

    public void Events() {
        buttomUpdate.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String name = textoName.getText();
                        String purchase = textPurchase.getText();
                        String sale = textSale.getText();
                        String description = textDescription.getText();
                        purchase = purchase.replace(',', '.');
                        sale = sale.replace(',', '.');


                        String sql = "";

                        sql += "UPDATE produtos SET nome = '" + name + "', preco_compra ='" + purchase + "' , preco_venda = '" + sale + "' ,descricao ='" + description + "' WHERE id = " + id + " ;";


                        ProductDao pdao = new ProductDao();
                        pdao.edit(sql);

                        if (!pdao.editaded) {
                            return;
                        } else {
                            setVisible(false);
                            productView.search();
                        }

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
