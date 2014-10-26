///*    LAERTE CONTROLE DE ESTOQUE DE UMA EMPRESA DE MATERIAL DE CONSTRUÇÃO       *///
package system;

/**
/*
/* Laerte
/*
 **/
/*INTERFACE*/
import order.OrderView;
import customer.CustomerRegister;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JDesktopPane;

/* VIEWS*/
import inventory.InventoryRegister;
import customer.CustomerView;
import sale.SaleView;
import inventory.InventoryView;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JLabel;
import product.ProductRegister;
import product.ProductView;
import report.ReportViewOverall;
import report.ReportViewToday;

public class SystemWindow extends JFrame {

    private JPanel mainPainel;
    private JMenuBar menuBar;
    private JMenu menuSystem, menuCustomer, menuInventory, menuProduct, menuReport;
    private JMenuItem menuItemSystemOrder, menuItemSystemExit;
    private JMenuItem menuItemListCustomer, menuItemRegisterCustomer, menuItemPurchaseCustomer;
    private JMenuItem menuItemListInventory, menuItemRegisterInventory;
    private JMenuItem menuItemListProducts, menuItemRegisterProducts;
    private JMenuItem menuItemReportOverall, menuItemReportToday;
    private JDesktopPane desktopPane;
    JLabel labelTime, labelDate;
    public String dateString;
    public JLabel labelFooter, labelBrand;

    public SystemWindow() {
        mainPainel = new JPanel(new BorderLayout());

        /* Inicio - Montagem do Menu */

        menuBar = new JMenuBar();

        /* Menus PAI */
        menuSystem = new JMenu("System");
        menuCustomer = new JMenu("Customers");
        menuInventory = new JMenu("Inventory");
        menuProduct = new JMenu("Product");
        menuReport = new JMenu("Reports");


        /* Son menus (submenus)  */
        /* SYSTEM */
        menuItemSystemOrder = new JMenuItem("New Order");
        menuItemSystemExit = new JMenuItem("Exit");

        /* CUSTOMERS */
        menuItemListCustomer = new JMenuItem("List of customers");
        menuItemRegisterCustomer = new JMenuItem("Register Customer");
        menuItemPurchaseCustomer = new JMenuItem("Shopping Customer");

        /* INVENTORY */
        menuItemListInventory = new JMenuItem("List of products in inventory");
        menuItemRegisterInventory = new JMenuItem("Register product into inventory");

        /* PRODUCTS */
        menuItemListProducts = new JMenuItem("List all products");
        menuItemRegisterProducts = new JMenuItem("Register new products");

        /* REPORTS */
        menuItemReportToday = new JMenuItem("Report today");
        menuItemReportOverall = new JMenuItem("Global report");

        /*ADD SUBMENUS*/
        menuSystem.add(menuItemSystemOrder);
        menuSystem.add(menuItemSystemExit);

        menuCustomer.add(menuItemListCustomer);
        menuCustomer.add(menuItemRegisterCustomer);
        menuCustomer.add(menuItemPurchaseCustomer);

        menuInventory.add(menuItemListInventory);
        menuInventory.add(menuItemRegisterInventory);

        menuProduct.add(menuItemListProducts);
        menuProduct.add(menuItemRegisterProducts);

        menuReport.add(menuItemReportToday);
        menuReport.add(menuItemReportOverall);

        /*ADD MENUS TO BAR OF MENU*/
        menuBar.add(menuSystem);
        menuBar.add(menuCustomer);
        menuBar.add(menuInventory);
        menuBar.add(menuProduct);
        menuBar.add(menuReport);

        /* END MENU */


        /*******************SYSTEM TIME********************/
        ClockThread r = new ClockThread();
        r.labelTime = new JLabel();//instancia o label
        r.labelTime.setSize(70, 10);//seta a posição do label
        r.labelTime.setLocation(1010, 50);
        r.iniciaRelogio();
        mainPainel.add(r.labelTime);

        /********************SYSTEM DATE********************/
        Date data = new Date();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        dateString = df.format(data);



        labelDate = new JLabel(dateString);
        labelDate.setSize(100, 10);
        labelDate.setLocation(1000, 33);
        mainPainel.add(labelDate);



        this.setJMenuBar(menuBar); //Add bat to menu in JFrame

        labelFooter = new JLabel("Software Engineering - CSCE 361 - FALL 2014");
        labelFooter.setSize(300, 20);
        labelFooter.setLocation(780, 690);
        labelFooter.setForeground(Color.gray);
        labelFooter.setFont(new Font("verdana", Font.BOLD, 11));
        mainPainel.add(labelFooter);
           

        //painelPrincipal.setBackground(Color.WHITE); // INITIAL PAGE
        desktopPane = new JDesktopPane();

        //Best Sales Name
        labelBrand = new JLabel("Best Sales");
        labelBrand.setSize(500,500);
        labelBrand.setLocation(320,100);
        labelBrand.setForeground(Color.blue);
        labelBrand.setFont(new Font("Verdana", Font.BOLD, 80));
        desktopPane.add(labelBrand);
        
        mainPainel.add(desktopPane);


        this.getContentPane().add(mainPainel);

        this.setTitle("BEST SALES");
        this.setSize(1100, 780);
        this.setLocationRelativeTo(null);

        this.EventsOfMenu();
    }

    public void EventsOfMenu() {
        // Events of JFrame        
        this.addWindowListener(
                new WindowAdapter() {

                    @Override
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                });

        /* Events of items of menu from the bar of menu*/
        /* CUSTOMERS */
        menuItemListCustomer.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            desktopPane.removeAll();
                            CustomerView customer = new CustomerView();
                            desktopPane.add(customer);
                            customer.setVisible(true);
                            customer.setMaximum(true);
                        } catch (PropertyVetoException ex) {
                            Logger.getLogger(SystemWindow.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });

        menuItemRegisterCustomer.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            desktopPane.removeAll();
                            CustomerRegister customer_register = new CustomerRegister();
                            desktopPane.add(customer_register);
                            customer_register.setVisible(true);
                            customer_register.setMaximum(true);
                        } catch (PropertyVetoException ex) {
                            Logger.getLogger(SystemWindow.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });

        menuItemPurchaseCustomer.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        try {
                            desktopPane.removeAll();
                            SaleView purchase_customer = new SaleView();
                            desktopPane.add(purchase_customer);
                            purchase_customer.setVisible(true);
                            purchase_customer.setMaximum(true);
                        } catch (PropertyVetoException ex) {
                            Logger.getLogger(SystemWindow.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });

        menuItemSystemOrder.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        try {
                            desktopPane.removeAll();
                            OrderView order = new OrderView();
                            desktopPane.add(order);
                            order.setVisible(true);
                            order.setMaximum(true);
                        } catch (PropertyVetoException ex) {
                            Logger.getLogger(SystemWindow.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });

        menuItemRegisterInventory.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        try {
                            desktopPane.removeAll();
                            InventoryRegister inventory_register = new InventoryRegister();
                            desktopPane.add(inventory_register);
                            inventory_register.setVisible(true);
                            inventory_register.setMaximum(true);
                        } catch (PropertyVetoException ex) {
                            Logger.getLogger(SystemWindow.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
        menuItemListInventory.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        try {
                            desktopPane.removeAll();
                            InventoryView inventory_view = new InventoryView();
                            desktopPane.add(inventory_view);
                            inventory_view.setVisible(true);
                            inventory_view.setMaximum(true);
                        } catch (PropertyVetoException ex) {
                            Logger.getLogger(SystemWindow.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });

        menuItemListProducts.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        try {
                            desktopPane.removeAll();
                            ProductView product_view = new ProductView();
                            desktopPane.add(product_view);
                            product_view.setVisible(true);
                            product_view.setMaximum(true);
                        } catch (PropertyVetoException ex) {
                            Logger.getLogger(SystemWindow.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });

        menuItemRegisterProducts.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        try {
                            desktopPane.removeAll();
                            ProductRegister productC = new ProductRegister();
                            desktopPane.add(productC);
                            productC.setVisible(true);
                            productC.setMaximum(true);
                        } catch (PropertyVetoException ex) {
                            Logger.getLogger(SystemWindow.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });


        menuItemReportToday.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        try {
                            desktopPane.removeAll();
                            ReportViewToday report_today = new ReportViewToday();
                            desktopPane.add(report_today);
                            report_today.setVisible(true);
                            report_today.setMaximum(true);
                        } catch (PropertyVetoException ex) {
                            Logger.getLogger(SystemWindow.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });


        menuItemReportOverall.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        try {
                            desktopPane.removeAll();
                            ReportViewOverall report_overall = new ReportViewOverall();
                            desktopPane.add(report_overall);
                            report_overall.setVisible(true);
                            report_overall.setMaximum(true);
                        } catch (PropertyVetoException ex) {
                            Logger.getLogger(SystemWindow.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });


        /* SYSTEM EXIT */
        menuItemSystemExit.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit the system?", "Confirmation", JOptionPane.YES_NO_OPTION);
                        if (result == JOptionPane.YES_OPTION) {
                            System.exit(0);
                        }
                    }
                });
    }
    // MAIN!!!!!!!
    public static void main(String[] args) {
        SystemWindow js = new SystemWindow();
        js.setVisible(true); // FALSE to activate login screen, TRUE to deactivate

        /*LOGIN- comment here to deactivate the function of login ---- warning: the system will not have protection*/
//        Login l = new Login();
//        l.setVisible(true);

    }
}
