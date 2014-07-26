package calibrationcurves.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Ljubo Raicevic <rljubo90@gmail.com>
 */
public class ViewUtils {
    
    public static DefaultTableModel napraviROTableModel(ResultSet rs) {
                
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
    
    public static DefaultListModel makeListModel(ResultSet rs) {
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
    public static DefaultComboBoxModel napraviComboBoxModel(ResultSet rs) {
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
    
    public static int vratiPozicijuNaOsnovuPrimaryKeyUCBModelu(
            ComboBoxModel cbModel, 
            String vrednost) {
        
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
    
    public static String vratiPrimaryKeyNaOsnovuPozicijeUCBModelu(
            ComboBoxModel cbModel, 
            int pozicija) {
        
       return String.valueOf(((Pair)cbModel.getElementAt(pozicija)).getNum());
    }  
}
