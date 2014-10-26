package customer;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

public class CustomerView extends JInternalFrame {

    private JPanel mainPainel;
    private JLabel labelTitle, labelName, labelObs;
    private JTextField textoName;
    private JTable tableCustomer;
    public  JButton buttomSearch;
    private DefaultTableModel tableModel;

    public CustomerView() {
        mainPainel = new JPanel(null);

        /*TITLE*/
        labelTitle = new JLabel("Customers List");
        labelTitle.setSize(300, 50);
        labelTitle.setLocation(10, 5);
        labelTitle.setFont(new Font("Verdana", Font.BOLD, 20));

        /* ADDING To THE PANEL */


        labelName = new JLabel("NAME :");
        labelName.setSize(50, 30);
        labelName.setLocation(10, 65);


        textoName = new JTextField();
        textoName.setSize(730, 30);
        textoName.setLocation(100, 65);

        buttomSearch = new JButton("SEARCH");
        buttomSearch.setSize(150, 30);
        buttomSearch.setLocation(848, 65);

        /*EDITION IS NOT ALLOWED HERE*/
        tableModel = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };

        tableCustomer = new JTable(tableModel);
        tableModel.addColumn("ID");
        tableModel.addColumn("NAME");
        tableModel.addColumn("SSN");
        tableModel.addColumn("PHONE");
        tableModel.addColumn("ADDRESS");
        tableModel.addColumn("REGISTER");


        JScrollPane scrollTabelaCliente = new JScrollPane(tableCustomer);
        scrollTabelaCliente.setSize(990, 400);
        scrollTabelaCliente.setLocation(10, 110);

        mainPainel.add(labelTitle);
        mainPainel.add(labelName);
        mainPainel.add(textoName);

        mainPainel.add(buttomSearch);

        mainPainel.add(scrollTabelaCliente);

        /*OBS*/
        labelObs = new JLabel("Double-click on the record to edit it !");
        labelObs.setSize(350, 15);
        labelObs.setLocation(10, 525);
        labelObs.setFont(new Font("arial", Font.BOLD, 12));
        labelObs.setForeground(Color.gray);

        mainPainel.add(labelObs);


        setTitle("Customers list");
        setSize(800, 600);

        getContentPane().add(mainPainel);

        this.Events();
        this.search();

    }
        
    public void Events() {
    	CustomerView view = this;
        buttomSearch.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                search();
            }
        });

        textoName.addKeyListener(
                new KeyAdapter() {

                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            search();
                        }
                    }
                });


        tableCustomer.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Integer id = null;
                    String nome = null;
                    String cpf = null;
                    String telefone = null;
                    String end = null;
                    String data = null;
                    int i = tableCustomer.getSelectedRow();

                    if (tableCustomer.isCellSelected(i, 0)) {
                        id = Integer.parseInt(String.valueOf(tableCustomer.getValueAt(i, 0)));
                        nome = tableCustomer.getValueAt(i, 1).toString();
                        cpf = tableCustomer.getValueAt(i, 2).toString();
                        telefone = tableCustomer.getValueAt(i, 3).toString();
                        if (tableCustomer.getValueAt(i, 4) == null) {
                            end = "";
                        } else {
                            end = tableCustomer.getValueAt(i, 4).toString();
                        }
                        data = tableCustomer.getValueAt(i, 5).toString();
                    }

                    CustomerEdit clienteEdicao = new CustomerEdit(view, id, nome, cpf, telefone, end, data);
                    clienteEdicao.setVisible(true);
                    clienteEdicao.Events();
                }
            }
        });

    }

    public void search() {

        String nome = textoName.getText();
        CustomerDao cliente = new CustomerDao();
        cliente.search(nome);
        System.out.println("Searched");
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }

        /* OBTAIN CORRECT DATE FORMAT */
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        try {
            while (cliente.list.next()) {
                int strCodigo = cliente.list.getInt("id");
                String strNome = cliente.list.getString("nome");
                String strCpf = cliente.list.getString("cpf");
                String strTelefone = cliente.list.getString("telefone");
                String strEnd = cliente.list.getString("endereco");
                Date strData = cliente.list.getDate("data_cadastro");
                tableModel.addRow(new Object[]{strCodigo, strNome, strCpf, strTelefone, strEnd, sdf.format(strData)});
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerView.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            cliente.stm.close();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerView.class.getName()).log(Level.SEVERE, null, ex);
        }

        tableCustomer.transferFocus();
    }
}
