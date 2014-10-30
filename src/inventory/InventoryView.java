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

    private JPanel painelPrincipal;
    private JLabel labelTitulo, rotuloNome, labelObs;
    private JTextField textoNome;
    private JTable tabelaCliente;
    private JButton botaoConsultar, botaoRemoverLinhas;
    private DefaultTableModel tableModel;

    public InventoryView() {
        painelPrincipal = new JPanel(null);

        /*TITULO*/
        labelTitulo = new JLabel("STOCK OF PRODUCTS");
        labelTitulo.setSize(300, 50);
        labelTitulo.setLocation(10, 5);
        labelTitulo.setFont(new Font("Verdana", Font.BOLD, 20));

        /* ADICIONANDO AO PAINEL */


        rotuloNome = new JLabel("NAME :");
        rotuloNome.setSize(50, 30);
        rotuloNome.setLocation(10, 65);


        textoNome = new JTextField();
        textoNome.setSize(730, 30);
        textoNome.setLocation(100, 65);


        botaoConsultar = new JButton("SEARCH");
        botaoConsultar.setSize(150, 30);
        botaoConsultar.setLocation(848, 65);

        /*EDIÇÃO DAS CELULAS NÃO PODE ACONTECER*/
        tableModel = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false;
            }
        };

        tabelaCliente = new JTable(tableModel);
        tableModel.addColumn("id");
        tableModel.addColumn("PRODUCT");
        tableModel.addColumn("QUANTITY");
        tableModel.addColumn("VALUE $ / UNIT");
        tableModel.addColumn("DATE OF STOCK");


        JScrollPane scrollTabelaCliente = new JScrollPane(tabelaCliente);
        scrollTabelaCliente.setSize(990, 400);
        scrollTabelaCliente.setLocation(10, 110);

        botaoRemoverLinhas = new JButton("REMOVE SELECT LINE(s)");
        botaoRemoverLinhas.setSize(350, 30);
        botaoRemoverLinhas.setLocation(300, 515);


        painelPrincipal.add(labelTitulo);
        painelPrincipal.add(rotuloNome);
        painelPrincipal.add(textoNome);

        painelPrincipal.add(botaoConsultar);
        painelPrincipal.add(botaoRemoverLinhas);

        painelPrincipal.add(scrollTabelaCliente);

        /*OBS*/
        labelObs = new JLabel("Double-click in the record to edit it!");
        labelObs.setSize(350, 15);
        labelObs.setLocation(10, 525);
        labelObs.setFont(new Font("arial", Font.BOLD, 12));
        labelObs.setForeground(Color.gray);

        painelPrincipal.add(labelObs);


        setTitle("Products in stock");
        setSize(800, 600);

        getContentPane().add(painelPrincipal);

        this.Eventos();
        this.consultar();


    }

    public void Eventos() {
    	InventoryView view = this;

        botaoConsultar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                consultar();
                botaoRemoverLinhas.transferFocus();

            }
        });

        textoNome.addKeyListener(
                new KeyAdapter() {

                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            consultar();
                        }
                    }
                });


        botaoRemoverLinhas.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {

                        /*VERIFICANDO SE ALGUMA COISA TA SELECIONADA*/
                        if (tabelaCliente.getRowCount() == 0) {
                            JOptionPane.showMessageDialog(null, "Select the rows that you want to remove", null, JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        int result = JOptionPane.showConfirmDialog(null, "You really want to remove the selected rows?", "ATTENTION", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                        if (result == 1) {
                            return;  // PARAR TUDO SE O OPERADOR COLOCAR NÃO !
                        }

                        for (int i = 0; i < tableModel.getRowCount(); i++) {
                            if (tabelaCliente.isCellSelected(i, 0)) {
                                Object id = tabelaCliente.getValueAt(i, 0);
                                System.out.println(id);
                                InventoryDao removerDoEstoque = new InventoryDao();
                                removerDoEstoque.remove(id);
                            }
                        }
                        consultar();
                    }
                });

        tabelaCliente.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                	
                    Integer id = null;
                    String nome = null;
                    Integer quantidade = null;
                    String data = null;
                    int i = tabelaCliente.getSelectedRow();

                    if (tabelaCliente.isCellSelected(i, 0)) {
                        id = Integer.parseInt(String.valueOf(tabelaCliente.getValueAt(i, 0)));
                        nome = tabelaCliente.getValueAt(i, 1).toString();
                        quantidade = Integer.parseInt(String.valueOf(tabelaCliente.getValueAt(i, 2)));
                        data = tabelaCliente.getValueAt(i, 4).toString();
                    }

					InventoryEdit estoqueEdicao = new InventoryEdit(view , id, nome, quantidade, data);
                    estoqueEdicao.setVisible(true);

                }
            }
        });

    }

    public void consultar() {

        ///// COPIA DO BOTAO DE CONSULTAR (VER DEPOIS)
        String nome = textoNome.getText();
        InventoryDao estoqueDao = new InventoryDao();
        estoqueDao.listInventory(nome);

        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }

        /* OBTENDO FORMATO CORRETO DO DINHEIRO */
        Locale US = new Locale("en", "UK");
        DecimalFormatSymbols REAL = new DecimalFormatSymbols(US);
        DecimalFormat DinheiroReal = new DecimalFormat("###,###,##0.00", REAL);

        /* OBETNDO FORAMTO CORRETO PARA DATA */
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        try {
            while (estoqueDao.list.next()) {
                int id = estoqueDao.list.getInt("id");
                String strNome = estoqueDao.list.getString("nome");
                int Intquantidade = estoqueDao.list.getInt("quantidade");
                String strValor = estoqueDao.list.getString("preco_venda");
                Date strData = estoqueDao.list.getDate("data_estoque");
                tableModel.addRow(new Object[]{id, strNome, Intquantidade, DinheiroReal.format(Double.parseDouble(strValor)), sdf.format(strData)});
            }
        } catch (SQLException ex) {
            Logger.getLogger(InventoryView.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
