package customer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class CustomerDao {

    public ResultSet list;
    Statement stm = null;
    boolean edited;
    boolean cadastrado = false;

    public ResultSet search(String nome) {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/projetoivo", "solar", "solar");
            stm = connection.createStatement();

            String sql = "select * from clientes where UPPER(nome) like '" + nome.toUpperCase() + "%' ORDER BY id DESC";

            list = stm.executeQuery(sql);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CustomerDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                System.out.println("Error when closing the connection");
            }
        }
        return list;
    }

    public void register(String sql) {
        Connection connection = null;
        Statement stm = null;
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/projetoivo", "solar", "solar");
            stm = connection.createStatement();
            int resultado = stm.executeUpdate(sql);
            if (resultado >= 1) {
                System.out.println("User created!!");
                JOptionPane.showMessageDialog(null, "Client successfully registered !");
                cadastrado = true;
            } else {
                System.out.println("Erro in customer's registration !");
                JOptionPane.showMessageDialog(null, "Error registering client, check the data !", null, JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error registering client, SSN must be unique !", null, JOptionPane.WARNING_MESSAGE);
            ex.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                System.out.println("Error when closing the connection");
            }

        }

    }

    public void readCustomer(String nome) {
        Connection connection = null;
        Statement stm = null;
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/projetoivo", "solar", "solar");
            stm = connection.createStatement();
            String SQL = "SELECT * FROM clientes WHERE id = " + nome;
            ResultSet resultado = stm.executeQuery(SQL);
            while (resultado.next()) {
                System.out.println(resultado.getArray("id") + " " + resultado.getArray("nome"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                System.out.println("Error when closing the connection from update");
                ex.printStackTrace();
            }
        }
    }

    public void edit(String sql) {
        Connection connection = null;
        Statement stm = null;
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/projetoivo", "solar", "solar");
            stm = connection.createStatement();
            int resultado = stm.executeUpdate(sql);
            if (resultado >= 1) {
                System.out.println("Updated client successfully!");
                JOptionPane.showMessageDialog(null, "Updated client successfully !");
            } else {
                System.out.println("Error in update the customer !");
                JOptionPane.showMessageDialog(null, "ERROR, check the fields !", null, JOptionPane.WARNING_MESSAGE);
            }
            edited = true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR, check the fields, SSN need be unique !", "ATTENTION", JOptionPane.WARNING_MESSAGE);
            ex.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                System.out.println("Error when closing the connection from update");
            }

        }
    }
}
