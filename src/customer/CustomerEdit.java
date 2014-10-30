package customer;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

public class CustomerEdit extends JDialog {

    private JPanel mainPainel;
    private JLabel labelTitle, labelName, labelSsn, labelPhone, labelDateRegister, labelId, labelEnd;
    private JTextField textName, textSsn, textPhone, textDateRegister, textId, textEnd;
    private JButton buttomCancel, buttomSave;
    private CustomerView view;

    CustomerEdit(CustomerView view, Integer id, String name, String ssn, String phone, String end, String date) {
        mainPainel = new JPanel(null);
        this.view = view;

        /*TITULO*/
        labelTitle = new JLabel("EDITION OF CUSTOMERS");
        labelTitle.setSize(300, 50);
        labelTitle.setLocation(10, 5);
        labelTitle.setFont(new Font("Verdana", Font.BOLD, 20));
        mainPainel.add(labelTitle);


        labelId = new JLabel("CUSTOMER'S CODE : ");
        labelId.setSize(300, 40);
        labelId.setLocation(10, 65);
        labelId.setFont(new Font("Verdana", Font.BOLD, 15));
        mainPainel.add(labelId);

        textId = new JTextField(id.toString());
        textId.setSize(40, 30);
        textId.setLocation(210, 70);
        textId.setEditable(false);
        textId.setFont(new Font("Verdana", Font.BOLD, 15));
        textId.setBorder(null);

        labelName = new JLabel("NAME :");
        labelName.setSize(100, 30);
        labelName.setLocation(10, 100);

        labelSsn = new JLabel("SSN :");
        labelSsn.setSize(100, 30);
        labelSsn.setLocation(10, 135);

        labelPhone = new JLabel("PHONE :");
        labelPhone.setSize(100, 30);
        labelPhone.setLocation(10, 170);

        labelEnd = new JLabel("ADDRESS :");
        labelEnd.setSize(100, 30);
        labelEnd.setLocation(10, 205);

        labelDateRegister = new JLabel("REGISTRATION:");
        labelDateRegister.setSize(120, 30);
        labelDateRegister.setLocation(10, 240);

        mainPainel.add(labelName);
        mainPainel.add(labelSsn);
        mainPainel.add(labelPhone);
        mainPainel.add(labelDateRegister);
        mainPainel.add(labelEnd);

        textName = new JTextField(name);
        textName.setSize(400, 30);
        textName.setLocation(130, 100);


       MaskFormatter ssnm; // Mask
        try {
            ssnm = new javax.swing.text.MaskFormatter("###-###-####");
            textSsn = new javax.swing.JFormattedTextField(ssnm);
            textSsn.setText(ssn);
            textSsn.setSize(160, 30);
            textSsn.setLocation(130, 135);
        } catch (ParseException ex) {
            Logger.getLogger(CustomerRegister.class.getName()).log(Level.SEVERE, null, ex);
        }

        textPhone = new javax.swing.JFormattedTextField(phone);
        textPhone.setSize(160, 30);
        textPhone.setLocation(130, 170);

        textEnd = new JTextField(end);
        textEnd.setSize(700, 30);
        textEnd.setLocation(130, 205);


        textDateRegister = new JTextField(date);
        textDateRegister.setSize(80, 30);
        textDateRegister.setLocation(130, 240);

        mainPainel.add(textName);
        mainPainel.add(textSsn);
        mainPainel.add(textPhone);
        mainPainel.add(textDateRegister);
        mainPainel.add(textId);
        mainPainel.add(textEnd);

        buttomCancel = new JButton("CANCEL");
        buttomSave = new JButton("UPDATE");

        buttomCancel.setSize(110, 30);
        buttomCancel.setLocation(590, 265);

        buttomSave.setSize(110, 30);
        buttomSave.setLocation(720, 265);

        mainPainel.add(buttomCancel);
        mainPainel.add(buttomSave);

        this.getContentPane().add(mainPainel);

        this.setTitle("Editing customers");
        this.setSize(850, 600);
        this.setLocationRelativeTo(null);

        this.Events();
    }
    

    public void Events() {

        buttomSave.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String id = textId.getText();
                        String name = textName.getText();
                        String ssn = textSsn.getText();
                        String phone = textPhone.getText();
                        String end = textEnd.getText();
                        String date_register = textDateRegister.getText();

                        if (name == null || name.trim().equals("")) {
                            JOptionPane.showMessageDialog(null, "Customer must be Filled!", null, JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        String sql = "";

                        sql += "UPDATE clientes SET nome = '" + name + "', cpf='" + ssn + "', telefone='" + phone + "', endereco = '" + end + "' , data_cadastro='" + date_register + "' WHERE id = " + id + " ;";

                        CustomerDao cd = new CustomerDao();
                        cd.edit(sql);

                        if (cd.edited) {
                            setVisible(false);
                        }
                        //Instantaneous UPDATE
                       view.search();
                    }
                    
                });

        buttomCancel.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        int result = JOptionPane.showConfirmDialog(null, "Want to cancel the changes?", "Confirmattion", JOptionPane.YES_NO_OPTION);
                        if (result == JOptionPane.YES_OPTION) {
                            setVisible(false);
                        }
                    }
                });
    }
}
