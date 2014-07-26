package calibrationcurves.connection;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ljubo
 */
public class ConnectionBase {
    
    private static final String connectionString = "jdbc:sqlite:fibrinogen_curves.sqlite";
    static Connection conn = null;
    
    private static Connection cb() {
        try {
            if (conn == null) {
                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection(connectionString);
                conn.setAutoCommit(true);
            }
            return conn;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    }
    
    public ConnectionBase() {}

    public ResultSet izvrsiQuery(String kveri) {
        try {
            //ovde pravimo statement i izvrsavamo ga pomocu konekcije
            Statement stmt = cb().createStatement();
            ResultSet rs = stmt.executeQuery(kveri);
            return rs;
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionBase.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
            System.exit(1);
            return null;
        }
    }
    
    public int insertAndGetID(String query) {
        int result = -1;
        try {
            Statement stmt = cb().createStatement();
            stmt.execute(query);
            ResultSet rs = stmt.getGeneratedKeys();
            while (rs.next()) {
                result = Integer.parseInt(rs.getObject(1).toString());
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionBase.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
            System.exit(1);
        }
        
        return result;
    }
    
    public void izvrsiQueryBezRezultata(String kveri) {
        try {
            //ovde pravimo statement i izvrsavamo ga pomocu konekcije
            Statement stmt = cb().createStatement();
            stmt.execute(kveri);
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionBase.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex);
            System.exit(1);
        }
    }
    
    public DefaultTableModel napraviROTableModel(ResultSet rs)
    {
                
        //overriduje se isCellEditable da uvek vraca false, da bi model,
        //a s njim i tabela bili read-only
        DefaultTableModel aModel = new DefaultTableModel()
        {
            @Override
            public boolean isCellEditable(int row, int column) {
            return false;
        }
        };
                
        try
        {
            //iz metadata restultseta dobija se broj kolona
            int brojKolona = rs.getMetaData().getColumnCount();
            
            //pravi se niz stringova koji ima clanova koliko postoiji kolona u tabeli
            String[] naziviKolona = new String[brojKolona];
            
            //kroz petlju se dodaju imena kolona u niz
            for (int iCount = 0; iCount < brojKolona; iCount++)
            {
                naziviKolona[iCount] = rs.getMetaData().getColumnLabel(iCount+1);
            }
            
            //niz stringova naziviKolona se postavlja modelu, da ga koristi kao nazive kolona
            aModel.setColumnIdentifiers(naziviKolona);
        
            //model se popunjava redovima iz resultset-a
            //gde god se koristi resultset mora se staviti try catch za SQLException
            while (rs.next())
            {
                //pravi se niz objekata koji ima onoliko clanova koliko ima kolona u tabeli
                Object[] objekti = new Object[brojKolona];
                
                //kroz petlju se svakom clanu tog niza dodeljuje odgovarajuca vrednost iz resultseta
                for (int iCount = 0; iCount < brojKolona; iCount++)
                {
                    objekti[iCount] = rs.getObject(iCount+1);
                }
                
                //taj niz se dodaje modelu kao novi red
                aModel.addRow(objekti);
            }
        }
        catch (SQLException sqle) {
            System.out.println(sqle);
            //Logger.getLogger(this.class.getName()).log(Level.SEVERE, null, sqle);
        }
        
        //funkcija vraca napravljeni model
        return aModel;
    }
    
    public DefaultListModel makeListModel(ResultSet rs) {
        DefaultListModel listModel = new DefaultListModel();
        
        try
        {
            //dobija meta podatke o prosledjenom resultsetu
            ResultSetMetaData meta = rs.getMetaData();
            
            //u slucaju da je broj kolona dva
            if (meta.getColumnCount() == 2)
            {
                //nalazi imena kolona
                String firstColumnName = meta.getColumnName(1);
                String secondColumnName = meta.getColumnName(2);
                
                //i kroz petlju dodaje odgovarajuca polja u vektor
                while (rs.next())
                {
                    listModel.addElement(new Pair(rs.getInt(firstColumnName), rs.getString(secondColumnName)));
                }
            }
            //ako je broj kolona razlicit od dva
            else
            {
                //vraca gresku koja ce biti ispisana u list
                listModel.addElement(new Pair(0,"Wrong number of columns in resultset"));
            }
        }
        catch (SQLException sqle) {
            System.out.println(sqle);
//Logger.getLogger(fLog.class.getName()).log(Level.SEVERE, null, sqle);      
        }
        
        return listModel;
    }
    
    //da bi cbmodel bio napravljen metodom napraviComboBoxModel ResultSet koji mu se prosledjuje mora da vraca
    //samo dve kolone - prva je primary key - druga je vrednost koja ce biti ispisana u comboboxu
    public DefaultComboBoxModel napraviComboBoxModel(ResultSet rs)
    {
        //classa ComboHolder je nested classa definisana gore ^
        //cbModel je vector te klase
        Vector<Pair> cbModel = new Vector<Pair>();
        
        try
        {
            //dobija meta podatke o prosledjenom resultsetu
            ResultSetMetaData meta = rs.getMetaData();
            
            //u slucaju da je broj kolona dva
            if (meta.getColumnCount() == 2)
            {
                //nalazi imena kolona
                String firstColumnName = meta.getColumnName(1);
                String secondColumnName = meta.getColumnName(2);
                
                //i kroz petlju dodaje odgovarajuca polja u vektor
                while (rs.next())
                {
                    cbModel.add(new Pair(rs.getInt(firstColumnName), rs.getString(secondColumnName)));
                }
            }
            //ako je broj kolona razlicit od dva
            else
            {
                //vraca gresku koja ce biti ispisana u comboboxu
                cbModel.add(new Pair(0,"Wrong number of columns in resultset"));
            }
        }
        catch (SQLException sqle) {
            System.out.println(sqle);
//Logger.getLogger(fLog.class.getName()).log(Level.SEVERE, null, sqle);      
        }
    
        return new DefaultComboBoxModel(cbModel);
    }
    
    public static int vratiPozicijuNaOsnovuPrimaryKeyUCBModelu(javax.swing.ComboBoxModel cbModel, String vrednost)
    {
        //petljom se prolazi kroz vektor cbModel, dok se ne nadje na kojoj poziciji
        //se nalazi prosledjeno odeljenje, i onda se combobox cbOdeljenje postavi na tu poziciju
        for (int iCount = 0; iCount < cbModel.getSize(); iCount++)
        {
            if (String.valueOf(((Pair)cbModel.getElementAt(iCount)).getNum()).equals(vrednost))
            {
                return iCount;
            }
        }
        return 0;
    }
    
    public static String vratiPrimaryKeyNaOsnovuPozicijeUCBModelu(javax.swing.ComboBoxModel cbModel, int pozicija)
    {
       return String.valueOf(((Pair)cbModel.getElementAt(pozicija)).getNum());
    }   
    
    //pass se u tabeli cuva kao md5 hash pravog passworda; ova metoda
    //pretvara string unet u polju u md5 koji je uporediv s podacima iz tabele
    public static String string2md5(String s)
    {
        String hashTekst = "";
        try {
            byte[] bajtovaUSifri = s.getBytes("UTF-8");
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(bajtovaUSifri);
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1,digest);
            hashTekst = bigInt.toString(16);
            //u slucaju da md5 bude kraci od 32, treba ta mesta ispred popuniti nulama
            while (hashTekst.length() < 32)
            {
                hashTekst = "0" + hashTekst;
            }
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            System.out.println(ex);
                //Logger.getLogger(fLog.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        return hashTekst;
    }
}
