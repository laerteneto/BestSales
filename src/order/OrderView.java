package order;

/* IMPORTAÇÃO DAS BIBLIOTECAS */
import system.SystemWindow;
import customer.CustomerDao;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
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

public class OrderView extends JInternalFrame {

    private JPanel mainPainel;
    private JLabel labelTitle, labelQuantity, labelProduct, labelTotal, labelDate, labelCustomerName;
    private JTextField textQuantity, textTotal, TextDate;
    private JTable tableCustomer;
    private JButton buttomAdd, buttomPrint, buttomRemoveItem, buttomFinishOrder;
    private DefaultTableModel tableModel;
    private JComboBox comboCustomers, comboNameProduct;
    private ArrayList<Order> orderList;
    private boolean finalized = false;
    private boolean finalizedPrint = false;
    //GENERATION OF NAME FILE
    Timestamp time = null;
    String timeImprimir;
    ////////// ESCOLHENDO O CAMINHO DO ARQUIVO PARA IMPRIMIR///////////////////
    String localSaved;
    //VERIFICANDO SISTEMA OPERACIONAL e nome do usuário no sistema
    String so = System.getProperty("os.name");//Sistema Operacional do usuário
    String user = System.getProperty("user.name");//Nome do usuário.
//    String versaoSo = System.getProperty("os.version");//Pega a versão do SO.
//    String javaVers = System.getProperty("java.version");//Vesão do java.
//    String usuarioHome = System.getProperty("user.home");//Pasta do usuário.
//    String usuarioDir = System.getProperty("user.dir");//P
    ////////////////////////////////////////////////////////////////////////////

    public OrderView() {
        System.out.println(so);
        System.out.println(user);
        mainPainel = new JPanel(null);

        /*TITULO*/
        labelTitle = new JLabel("REQUEST - BUDGET");
        labelTitle.setSize(300, 50);
        labelTitle.setLocation(10, 5);
        labelTitle.setFont(new Font("Verdana", Font.BOLD, 20));

        /* ADICIONANDO AO PAINEL */


        labelCustomerName = new JLabel("CUSTOMER :");
        labelCustomerName.setSize(100, 30);
        labelCustomerName.setLocation(10, 65);


        comboCustomers = new JComboBox();
        comboCustomers.setSize(300, 30);
        comboCustomers.setLocation(110, 65);
        comboCustomers.transferFocus();

        comboCustomers.addItem("");

        /***************** Getting customers to put in the combo box *******************/
        OrderDao clientes = new OrderDao();
        clientes.listAllCustomers();
        try {
            while (clientes.list.next()) {
                comboCustomers.addItem(clientes.list.getArray("nome"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderView.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            clientes.stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(OrderView.class.getName()).log(Level.SEVERE, null, ex);
        }


        labelDate = new JLabel("DATE OF REQUEST :");
        labelDate.setSize(150, 30);
        labelDate.setLocation(450, 65);

        SystemWindow j = new SystemWindow();
        TextDate = new JTextField(j.dateString);
        TextDate.setSize(80, 30);
        TextDate.setLocation(600, 65);


        /************************************************************************************/
        labelProduct = new JLabel("PRODUCT :");
        labelProduct.setSize(150, 30);
        labelProduct.setLocation(10, 110);

        comboNameProduct = new JComboBox();
        comboNameProduct.setSize(300, 30);
        comboNameProduct.setLocation(110, 110);
        //comboNameProduct.setEditable(true);


        comboNameProduct.addItem("");
        /***************** Getting products in inventory to put in a combo box *******************/
        OrderDao produtos = new OrderDao();
        produtos.listAllProducts();
        try {
            while (produtos.list.next()) {
                comboNameProduct.addItem(produtos.list.getArray("nome"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderView.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            clientes.stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(OrderView.class.getName()).log(Level.SEVERE, null, ex);
        }

        labelQuantity = new JLabel("QUANTITY :");
        labelQuantity.setSize(100, 25);
        labelQuantity.setLocation(450, 110);


        textQuantity = new JTextField();
        textQuantity.setSize(60, 30);
        textQuantity.setLocation(600, 110);


        buttomAdd = new JButton("ADD");
        buttomAdd.setSize(150, 30);
        buttomAdd.setLocation(720, 110);

        //Edition is not allowed
        tableModel = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };

        tableCustomer = new JTable(tableModel);
        tableModel.addColumn("id");
        tableModel.addColumn("PRODUCT");
        tableModel.addColumn("QUANTITY");
        tableModel.addColumn("$/UNIT");
        tableModel.addColumn("TOTAL");




        JScrollPane scrollTabelaCliente = new JScrollPane(tableCustomer);
        scrollTabelaCliente.setSize(860, 400);
        scrollTabelaCliente.setLocation(10, 180);


        labelTotal = new JLabel("TOTAL $ :");
        labelTotal.setSize(80, 30);
        labelTotal.setLocation(685, 595);


        textTotal = new JTextField();
        textTotal.setSize(100, 30);
        textTotal.setLocation(770, 595);
        textTotal.setText("0.0");

        buttomPrint = new JButton("PRINT");
        buttomPrint.setSize(110, 30);
        buttomPrint.setLocation(890, 340);
        buttomPrint.setBorderPainted(true);
        buttomPrint.setForeground(Color.blue);


        buttomRemoveItem = new JButton("REMOVE ITEM");
        buttomRemoveItem.setSize(150, 29);
        buttomRemoveItem.setLocation(10, 595);

        buttomFinishOrder = new JButton("FINALIZE ORDER");
        buttomFinishOrder.setSize(400, 29);
        buttomFinishOrder.setLocation(250, 595);


        mainPainel.add(labelTitle);
        mainPainel.add(comboNameProduct);
        mainPainel.add(textQuantity);
        mainPainel.add(comboCustomers);
        mainPainel.add(labelQuantity);
        mainPainel.add(labelProduct);
        mainPainel.add(labelTotal);
        mainPainel.add(textTotal);
        mainPainel.add(buttomPrint);
        mainPainel.add(buttomAdd);
        mainPainel.add(scrollTabelaCliente);
        mainPainel.add(labelDate);
        mainPainel.add(TextDate);
        mainPainel.add(labelCustomerName);
        mainPainel.add(buttomRemoveItem);
        mainPainel.add(buttomFinishOrder);


        setTitle("REQUEST - BUDGET");
        setSize(800, 600);

        getContentPane().add(mainPainel);

        orderList = new ArrayList<Order>();

        this.Events();

    }
    
    public void Events() {


        buttomRemoveItem.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        int row = tableCustomer.getSelectedRow();
                        Order OrderSub = orderList.get(row);
                        double aux = OrderSub.getValue() * OrderSub.getQuantity(); /*TO DECREMENT THE TOTAL*/

                        /* - of total */
                        String temp = textTotal.getText();
                        textTotal.setText("");
                        aux = Double.parseDouble(temp) - aux;
                        String s = String.valueOf(aux);
                        textTotal.setText(s);


                        orderList.remove(row);


                        while (tableModel.getRowCount() > 0) {
                            tableModel.removeRow(0);
                        }

                        for (int i = 0; i < orderList.size(); i++) {
                            Order ps = orderList.get(i);
                            tableModel.addRow(new Object[]{ps.getId(), ps.getProduct(), ps.getQuantity(), ps.getValue(), (ps.getValue() * ps.getQuantity())});
                        }

                    }
                });

        textQuantity.addKeyListener(
                new KeyAdapter() {

                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            add();
                        }
                    }
                });

        buttomAdd.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        add();
                    }
                });

        final JTextField textoNome = (JTextField) comboNameProduct.getEditor().getEditorComponent();
        textoNome.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    String name = textoNome.getText();
                    comboNameProduct.removeAllItems();
                    OrderDao orderName = new OrderDao();
                    orderName.productsComboBox(name);

                    while (orderName.list.next()) {
                        comboNameProduct.addItem(orderName.list.getString("nome"));
                    }

                    textoNome.setText(name);
                    comboNameProduct.showPopup();

                    orderName.stm.close();

                } catch (SQLException ex) {
                    Logger.getLogger(OrderView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        buttomFinishOrder.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {

                        if (finalized) {
                            JOptionPane.showMessageDialog(null, "The order already was finalized ! Click in SYSTEM and after in NEW ORDER to do a new one", "Information", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        //Verificando se a tabela não está nula
                        if (tableCustomer.getRowCount() == 0) {
                            JOptionPane.showMessageDialog(null, "THERE ISN'T REQUEST IN THE TABLE !", "ATTENTION", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        int result = JOptionPane.showConfirmDialog(null, "Complete the order?", "Confirmation", JOptionPane.YES_NO_OPTION);
                        if (result == JOptionPane.NO_OPTION) {
                            return;
                        }


                        OrderDao inventory = new OrderDao();
                        /*Obtendo cliente*/
                        customer.CustomerDao customer = new CustomerDao();

                        if (comboCustomers.getSelectedItem() == "") {
                            JOptionPane.showMessageDialog(null, "CUSTOMER MUST BE COMPLETED!", "ATTENTION", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        customer.search(comboCustomers.getSelectedItem().toString());
                        int customerId = 0;
                        try {
                            while (customer.list.next()) {
                                customerId = customer.list.getInt("id");
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(OrderView.class.getName()).log(Level.SEVERE, null, ex);
                            return;
                        }
                        String finalValue = textTotal.getText();
                        String date = TextDate.getText();

                        inventory.InsertCompras(customerId, finalValue, date);


                        for (int i = 0; i < orderList.size(); i++) {
                            Order ps = orderList.get(i);

                            inventory.readUniqueProduct(ps.getProduct());

                            try {
                                while (inventory.list.next()) {
                                    /**UPDATE IN DATABASE**/
                                    int quantidade = inventory.list.getInt("quantidade") - ps.getQuantity();
                                    inventory.UpdateInventory(quantidade, ps.getId());

                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(OrderView.class.getName()).log(Level.SEVERE, null, ex);
                                return;
                            }

                        }
                        finalized = true;
                        buttomPrint.doClick();
                    }
                });

        buttomPrint.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        File dir = null;
                        File dir2 = null;
                        // Creating directory
                        if (so.equals("Linux")) {
                            dir = new File("/home/" + user + "/Documents/BestSales/requests");
                            dir2 = new File("/home/" + user + "/Documents/BestSales/orders");
                        }
                        if (so.equals("Windows 7")) {
                            dir = new File("C:/Users/" + user + "/Documents/BestSales/requests");
                            dir2 = new File("C:/Users/" + user + "/Documents/BestSales/orders");
                        }

                        if (dir.mkdirs() || dir2.mkdirs()) {
                            System.out.println("New directory created in: " + dir.getAbsolutePath());
                            System.out.println("New directory created in: " + dir2.getAbsolutePath());
                        } else {
                            System.out.println("Directory already exist");
                        }


                        ///////////////Permitindo que seja impresso apenas uma impressão quando o pedido for finalizado

                        if (finalized && finalizedPrint) {
                            JOptionPane.showMessageDialog(null, "The order already was finalized and printed , go to the folder (Documents/BestSales/orders) e check the file, or make a new order.", "Message of print", JOptionPane.WARNING_MESSAGE);
                            return;
                        }


                        //////////////Começando para gerar pdf//////////////
                        Document document = new Document();
                        try {
                            //Verificando se a tabela não está nula
                            if (tableCustomer.getRowCount() == 0) {
                                JOptionPane.showMessageDialog(null, "THERE ISN'T REQUEST IN THE TABLE !", "ATTENTION", JOptionPane.WARNING_MESSAGE);
                                return;
                            }


                            Timestamp time = new Timestamp(System.currentTimeMillis());
                            timeImprimir = time.toString();

                            // Dividing by folder of requests and finalized orders
                            // Verifing the OS
                            if (finalized) { // vai pra pedido
                                if (so.equals("Linux")) {
                                    localSaved = "/home/" + user + "/Documents/BestSales/orders/" + comboCustomers.getSelectedItem() + "_" + timeImprimir + ".pdf";
                                }
                                if (so.equals("Windows 7")) {
                                    localSaved = "C:/Users/" + user + "/Documents/BestSales/orders/" + comboCustomers.getSelectedItem() + ".pdf";
                                }
                                finalizedPrint = true;
                            } else { // vai pra orcamento
                                if (so.equals("Linux")) {
                                    localSaved = "/home/" + user + "/Documents/BestSales/requests/" + comboCustomers.getSelectedItem() + "_" + timeImprimir + ".pdf";
                                }
                                if (so.equals("Windows 7")) {
                                    localSaved = "C:/Users/" + user + "/Documents/BestSales/requests/" + comboCustomers.getSelectedItem() + ".pdf";
                                }

                            }

                            PdfWriter pdf = PdfWriter.getInstance(document, new FileOutputStream(localSaved));
                            document.open();

                            Paragraph titulo = new Paragraph("BestSales");
                            titulo.setAlignment(Paragraph.ALIGN_LEFT);
                            document.add(titulo);

                            document.add(new Paragraph(" "));

                            //VERIFICANDO SE É PEDIDO OU ORÇAMENTO
                            if (finalized) {
                                Paragraph orcamento = new Paragraph("ORDER");
                                orcamento.setAlignment(Paragraph.ALIGN_CENTER);
                                document.add(orcamento);
                            } else {
                                Paragraph orcamento = new Paragraph("REQUEST");
                                orcamento.setAlignment(Paragraph.ALIGN_CENTER);
                                document.add(orcamento);
                            }


                            Paragraph date = new Paragraph("Date : " + TextDate.getText() + "");
                            date.setAlignment(Paragraph.ALIGN_RIGHT);
                            document.add(date);

                            document.add(new Paragraph(" "));

                            Paragraph customer = new Paragraph("CUSTOMER : " + comboCustomers.getSelectedItem().toString());
                            customer.setAlignment(Paragraph.ALIGN_CENTER);
                            document.add(customer);
                            document.add(new Paragraph(" "));

                            PdfPTable table = new PdfPTable(5);

                            /*ADICINANDO CABEÇARIO - POG WARNING*/
                            table.addCell("ID");
                            table.addCell("PRODUCT");
                            table.addCell("QUANTITY");
                            table.addCell("VALUE");
                            table.addCell("TOTAL $");

                            for (int i = 0; i < orderList.size(); i++) {
                                Order pedidoPdf = orderList.get(i);
                                table.addCell(String.valueOf(pedidoPdf.getId()));
                                table.addCell(pedidoPdf.getProduct().toUpperCase());
                                table.addCell(String.valueOf(pedidoPdf.getQuantity()));
                                table.addCell(String.valueOf(pedidoPdf.getValue()));
                                table.addCell(String.valueOf(pedidoPdf.getQuantity() * pedidoPdf.getValue()));
                            }

                            document.add(table);

                            document.add(new Paragraph(" "));

                            Paragraph total = new Paragraph("TOTAL : " + textTotal.getText());
                            total.setAlignment(Paragraph.ALIGN_RIGHT);
                            document.add(total);

                            document.add(new Paragraph(" "));
                            document.add(new Paragraph(" "));

                            /*POG WARNING*/
                            Paragraph assinatura = new Paragraph("__________________________                                                   __________________________");
                            document.add(assinatura);

                            Paragraph textoAssinatura = new Paragraph("          Customer signature                                                                     Signature of employee");
                            document.add(textoAssinatura);

                            pdf.pause();
                            pdf.resume();
                            JOptionPane.showMessageDialog(null, "PDF successfully generated!");

                            /*ESPAÇO PARA GERAR A MENSAGEM DE RODAPÉ*/
                            document.add(new Paragraph(" "));
                            document.add(new Paragraph(" "));
                            document.add(new Paragraph(" "));

                        } catch (DocumentException ex) {
                            Logger.getLogger(OrderView.class.getName()).log(Level.SEVERE, null, ex);
                            JOptionPane.showMessageDialog(null, "Error to create the file !", null, JOptionPane.WARNING_MESSAGE);
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(OrderView.class.getName()).log(Level.SEVERE, null, ex);
                            JOptionPane.showMessageDialog(null, "Folder not found !");
                        }

                        /*NAO POSSUI VALOR FISCAL*/
                        Paragraph info = new Paragraph("This document has no fiscal value");
                        info.setAlignment(Paragraph.ALIGN_CENTER);
                        info.setSpacingAfter(TOP_ALIGNMENT);
                        try {
                            document.add(info);
                        } catch (DocumentException ex) {
                            Logger.getLogger(OrderView.class.getName()).log(Level.SEVERE, null, ex);
                        }


                        document.close();


                        /*ABRIR PDF APOS SER GERADO !*/
                        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
                        try {
                            // resgatando por pasta de pedidos e orçamento
                            desktop.open(new File(localSaved));

                        } catch (IOException ex) {
                            Logger.getLogger(OrderView.class.getName()).log(Level.SEVERE, null, ex);

                        }
                    }
                });
    }

    public void add() {
        double aux = 0; /*To sum all*/


        Order p = new Order();
        p.setProduct(comboNameProduct.getSelectedItem().toString());

        //null product
        if (comboNameProduct.getSelectedItem() == "") {
            JOptionPane.showMessageDialog(null, "CHOOSE A PRODUCT", "ATTENTION", JOptionPane.WARNING_MESSAGE);
            return;
        }

        //null quantity
        if ("".equals(textQuantity.getText())) {
            JOptionPane.showMessageDialog(null, "QUANTITY IS EMPTY", "ATTENTION", JOptionPane.WARNING_MESSAGE);
            return;
        }


        /*allowing spaces in quantity*/
        String q = String.valueOf(textQuantity.getText());
        q = q.replace(" ", "");
        p.setQuantity(Integer.valueOf(q));

        //***************************Getting in database********************************//

        OrderDao pdao = new OrderDao();
        pdao.readUniqueProduct(p.getProduct());


        /*******************************checking if the product is null************************/
        if (p.getProduct() == null ? "" == null : p.getProduct().equals("")) {
            JOptionPane.showMessageDialog(null, "PRODUCT IS EMPTY", null, JOptionPane.WARNING_MESSAGE);
            return;
        }

        /****************Verifying if the quantity is OK ***********/
        try {
            while (pdao.list.next()) {

                /*VERIFICANDO*/
                int checking = (pdao.list.getInt("quantidade") - p.getQuantity());
                if (!(checking >= 0)) {
                    int result = JOptionPane.showConfirmDialog(null, "Posted Overcomes the amount of stock in (" + checking * -1 + ") units! Add anyway?", "ATTENTION", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (result == 1) {
                        return;  // STOP ALL IF THE OPERATOR PUT NO !
                    }
                }
                /****************/
                p.setId(pdao.list.getInt("id"));
                p.setValue(pdao.list.getDouble("preco_venda"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderView.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "ERROR ADD, CHECK DATA !", null, JOptionPane.WARNING_MESSAGE);
        }

        // checking if the product already is in the list
        for (int i = 0; i < orderList.size(); i++) {
            Order ps = orderList.get(i);
            if (p.getId() == ps.getId()) {
                JOptionPane.showMessageDialog(null, "PRODUCT IS ALREADY IN THE LIST !", null, JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        //Product no registered --- this will be fixed later together with the function "KeyRelease"
        try {
            Boolean thereIs = true; // this line is getting corrected the error so far
            OrderDao pedido = new OrderDao();
            pedido.readUniqueProduct(p.getProduct());
            while (pedido.list.next()) {
                if (p.getId() == pedido.list.getInt("produtos_id")) {
                    thereIs = true;
                    continue;
                }
            }
            if (!thereIs) {
                JOptionPane.showMessageDialog(null, "PRODUCT DOES NOT EXIST!", "ATTENTION", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderView.class.getName()).log(Level.SEVERE, null, ex);
        }


        orderList.add(p);

        /* Money format */
        Locale BRAZIL = new Locale("pt", "BR");
        DecimalFormatSymbols REAL = new DecimalFormatSymbols(BRAZIL);
        DecimalFormat DinheiroReal = new DecimalFormat("###,###,##0.00", REAL);

        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }

        for (int i = 0; i < orderList.size(); i++) {
            Order ps = orderList.get(i);
            Double total = (ps.getValue() * ps.getQuantity());
            aux = total;
            tableModel.addRow(new Object[]{ps.getId(), ps.getProduct(), ps.getQuantity(), DinheiroReal.format(ps.getValue()), DinheiroReal.format(total)});

        }

        /*TOTAL*/
        String temp = textTotal.getText();
        textTotal.setText("");
        aux = Double.parseDouble(temp) + aux;
        String s = String.valueOf(aux);
        textTotal.setText(s);

        /*CHANGING THE FOCUS AND CLEANING SOME FIELDS*/
        comboNameProduct.setSelectedItem("");
        textQuantity.setText("");
        TextDate.transferFocus();
    }
}
