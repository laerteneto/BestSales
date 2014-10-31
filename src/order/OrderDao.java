package order;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class OrderDao {

    public ResultSet list;
    public ResultSet produto_id;
    Statement stm = null;

    public ResultSet listAllCustomers() {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/projetoivo", "solar", "solar");
            stm = connection.createStatement();
            String sql = "SELECT id,nome FROM clientes ORDER BY nome;";
            list = stm.executeQuery(sql);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                System.out.println("Error closing the connection reading stock");
            }

        }
        return list;
    }

    public ResultSet listAllProducts() {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/projetoivo", "solar", "solar");
            stm = connection.createStatement();
            String sql = "SELECT t1.id, t1.produtos_id, t1.quantidade, t2.nome, t2.preco_compra, t2.preco_venda "
                    + "FROM estoque  AS t1 "
                    + "JOIN produtos AS t2 ON (t1.produtos_id = t2.id) "
                    + "ORDER BY t2.nome "
                    + ";";

            list = stm.executeQuery(sql);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                System.out.println("Error closing the connection reading stock");
            }

        }
        return list;
    }

    public ResultSet productsComboBox(String name) {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/projetoivo", "solar", "solar");
            stm = connection.createStatement();

            String sql = "SELECT t1.id, t1.produtos_id, t1.quantidade, t2.nome, t2.preco_compra, t2.preco_venda"
                    + " FROM estoque  AS t1"
                    + " JOIN produtos AS t2 ON (t1.produtos_id = t2.id)"
                    + " where UPPER(t2.nome) like '" + name.toUpperCase() + "%' order by nome";

            list = stm.executeQuery(sql);
        } catch (SQLException ex) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                System.out.println("Error closing the connection");
            }
        }
        return list;
    }

    public ResultSet readUniqueProduct(String name) {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/projetoivo", "solar", "solar");
            stm = connection.createStatement();

            String sql = "SELECT t1.id, t1.produtos_id, t1.quantidade, t2.nome, t2.preco_compra, t2.preco_venda "
                    + "FROM estoque  AS t1 "
                    + "JOIN produtos AS t2 ON (t1.produtos_id = t2.id) "
                    + "WHERE t2.nome = '" + name + "' ;";


            list = stm.executeQuery(sql);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                System.out.println("Error closing the connection reading stock");
            }

        }
        return list;
    }

    /*UPDATES E INSERTS*/
    public void UpdateInventory(int NewQuantity, int id) {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/projetoivo", "solar", "solar");
            stm = connection.createStatement();

            stm.executeUpdate("UPDATE estoque SET quantidade= " + NewQuantity + " WHERE id =" + id + ";");

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                System.out.println("Error closing the connection reading stock");
            }

        }
    }

    public void InsertCompras(int customer, String total, String date) {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/projetoivo", "solar", "solar");
            stm = connection.createStatement();

            stm.executeUpdate("INSERT INTO compras (clientes_id,valor,data_compra) VALUES ( " + customer + " , " + total + " , '" + date + "' );");

            JOptionPane.showMessageDialog(null, "ORDER Successfully Saved!");

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "ERROR SALVING  , CHECK DATA", null, JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR SALVING , CHECK DATA", null, JOptionPane.WARNING_MESSAGE);
            ex.printStackTrace();
        }catch (NumberFormatException ex){
            JOptionPane.showMessageDialog(null, "MAKE SURE IF THE DATA IS CORRECT", null, JOptionPane.WARNING_MESSAGE);
        }
        finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                System.out.println("Error closing the connection reading stock");
            }

        }
    }
}
