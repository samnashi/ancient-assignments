import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class AddData extends JFrame implements ActionListener
{
    public JFrame F, parent; 
    JPanel jp_center, jp_upper;
    JButton jb_addMenuAddData, jb_addMenuReturn;
    JTextField jt_enterYear, jt_enterMonth, jt_enterType, jt_enterAmount;
    JLabel jl_instructions; 
    public Vector vect;
    int yearToSearch, monthToSearch;
    
    public AddData(JFrame parent, Vector expenses)
    {
        F = this;
        this.parent = parent;
        setTitle("Add Data");
        jp_center = new JPanel();
        jp_upper = new JPanel();
        jp_center.setLayout(new BoxLayout(jp_center, BoxLayout.X_AXIS));
        jp_upper.setLayout(new BoxLayout(jp_upper, BoxLayout.X_AXIS));
        
        jl_instructions = new JLabel("Year, Month, Type, Amount. Integers Only!");
        
        jb_addMenuAddData = new JButton("Add Expense");
        jb_addMenuAddData.addActionListener(this);
        jb_addMenuAddData.setActionCommand("add");
        
        jb_addMenuReturn = new JButton("Main Menu");
        jb_addMenuReturn.addActionListener(this);
        jb_addMenuReturn.setActionCommand("return");
        
        jt_enterYear = new JTextField();
        jt_enterMonth = new JTextField();
        jt_enterType = new JTextField();
        jt_enterAmount = new JTextField();
        
        jp_center.add(jt_enterYear);
        jp_center.add(jt_enterMonth);
        jp_center.add(jt_enterType);
        jp_center.add(jt_enterAmount);
        jp_center.add(jb_addMenuAddData);
        jp_center.add(jb_addMenuReturn);
        
        jp_upper.add(jl_instructions); 
        
        getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
        getContentPane().add(jp_upper);
        getContentPane().add(jp_center);
        
        setSize(500,100);
        vect = expenses; //MAKES SURE THAT THE VECTOR IN HERE IS EQUAL TO THE VECTOR IN MAINMENU
        setVisible(true);
        
    }
    public boolean addData()//add new bill to vector
    {
        Expense expenseToAdd = new Expense();
        
        Expense expenseToCompare = new Expense(); 
        Expense expenseTemp = new Expense();
        Expense expenseMin = new Expense(); // THE LAST THREE ARE FOR THE SORTING ALGORITHM
        
        try{
            expenseToAdd.year = Integer.parseInt(jt_enterYear.getText());
            expenseToAdd.month = Integer.parseInt(jt_enterMonth.getText());
            expenseToAdd.type = jt_enterType.getText();
            expenseToAdd.amount = Integer.parseInt(jt_enterAmount.getText());
        }
        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(null, "Incorrect input type(s). Check all the fields again!", "Warning!", JOptionPane.WARNING_MESSAGE);
            return false;
        }
            
            
        if((Integer.parseInt(jt_enterYear.getText()) > 2000 && Integer.parseInt(jt_enterYear.getText()) < 2010) && //SPELL CHECK AND RANGE CHECK
        ((jt_enterType.getText()).equalsIgnoreCase("rme")) 
        || ((jt_enterType.getText()).equalsIgnoreCase("food")) 
        || ((jt_enterType.getText()).equalsIgnoreCase("edu")) 
        || ((jt_enterType.getText()).equalsIgnoreCase("clothes"))
        || ((jt_enterType.getText()).equalsIgnoreCase("transportation")) 
        || ((jt_enterType.getText()).equalsIgnoreCase("amusement")) 
        || ((jt_enterType.getText()).equalsIgnoreCase("health")) 
        || ((jt_enterType.getText()).equalsIgnoreCase("social")) 
        || ((jt_enterType.getText()).equalsIgnoreCase("sports")) 
        || ((jt_enterType.getText()).equalsIgnoreCase("household")) 
        || ((jt_enterType.getText()).equalsIgnoreCase("installments")) 
        || ((jt_enterType.getText()).equalsIgnoreCase("others")))         
        { 
            vect.add(expenseToAdd); //ONLY ADD THE EXPENSE IF THE USER'S INPUT PASSES THE CHECKS
            
            for(int i = 0; i < vect.size(); i++) // SELECTION SORTING ALGORITHM
            {
                expenseMin = (Expense)vect.elementAt(i); 
                
                for (int j = i; j < vect.size(); j++)
                {
                    expenseToCompare = ((Expense)vect.elementAt(j));
                    
                    if (expenseMin.year > expenseToCompare.year)
                    {
                        expenseTemp = new Expense(expenseMin.year, expenseMin.month, expenseMin.type, expenseMin.amount); 
    
                        expenseMin = new Expense(expenseToCompare.year, expenseToCompare.month, expenseToCompare.type, expenseToCompare.amount); 
                        
                        vect.setElementAt(expenseMin, i);
                        vect.setElementAt(expenseTemp, j);
                    }
                    
                    else if (expenseMin.year == expenseToCompare.year && expenseMin.month > expenseToCompare.month)
                    {
                        expenseTemp = new Expense(expenseMin.year, expenseMin.month, expenseMin.type, expenseMin.amount); 
    
                        expenseMin = new Expense(expenseToCompare.year, expenseToCompare.month, expenseToCompare.type, expenseToCompare.amount); 
                        
                        vect.setElementAt(expenseMin, i);
                        vect.setElementAt(expenseTemp, j);
                    }
                }
            }
            JOptionPane.showMessageDialog(null, "Data successfully added.", "Confirmation", JOptionPane.WARNING_MESSAGE);
            jt_enterYear.setText("");
            jt_enterMonth.setText("");
            jt_enterType.setText("");
            jt_enterAmount.setText("");
        }
        //IF THE USER INPUT IS INVALID, THE PROGRAM WOULD JUST SKIP THE BLOCK ABOVE AND GO DISPLAY THE APPROPRIATE WARNING MESSAGE    
        else if (expenseToAdd.year < 2000 || expenseToAdd.month < 1 || expenseToAdd.month > 12){
            JOptionPane.showMessageDialog(null, "Year and/or month are out of range! Input another set of values", "Warning!", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else{
            JOptionPane.showMessageDialog(null, "Invalid Type!", "Warning!", JOptionPane.WARNING_MESSAGE);
            return false;      
        }
       
        return true;
    }
    public void actionPerformed(ActionEvent evt)
    {
        if (evt.getActionCommand().equals("add")){ 
            addData();
        }
        if (evt.getActionCommand().equals("return")){
            F.dispose();
            parent.setVisible(true);
        }   
    }
   
}	