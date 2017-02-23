
import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class EditData extends JFrame implements ActionListener
{
    public JFrame F, parent; 
    public Vector vect;
    JPanel jp_up, jp_upper, jp_lower, jp_loadExpenseInstructions, jp_lower2, jp_editingInstructions, jp_lower3, jp_lowerContainer, jp_lowerContainer2;
    JButton jb_searchData, jb_editData, jb_loadData, jb_editMenuReturn;
    JTextArea displayResults;
    JScrollPane scrollPane;
    JTextField jt_enterSearchYear, jt_enterSearchMonth, jt_enterIndexToEdit;
    JTextField jt_editYear, jt_editMonth, jt_editType, jt_editAmt;
    JLabel loadExpenseInstructions, searchInstructions, editingInstructions, searchResults, enterSearchKey;
    int yearToSearch, monthToSearch, indexToLoad, resultsCounter, yearToStore, monthToStore, amountToStore;
    String typeToStore; 
    Integer yearTemp, monthTemp, amountTemp; 
    
    public EditData(JFrame parent, Vector expenses)
    {
        F = this;
        this.parent = parent;
        setTitle("Search and Edit Expenses");
        jp_up = new JPanel();
        jp_up.setLayout(new BoxLayout(jp_up, BoxLayout.X_AXIS));
        jp_upper = new JPanel();
        jp_upper.setLayout(new BoxLayout(jp_upper, BoxLayout.X_AXIS));
        jp_lower = new JPanel();
        jp_lower.setLayout(new BoxLayout(jp_lower, BoxLayout.Y_AXIS));
        jp_lower2 = new JPanel();
        jp_lower2.setLayout(new BoxLayout(jp_lower2, BoxLayout.X_AXIS));
        jp_editingInstructions = new JPanel();
        jp_editingInstructions.setLayout(new BoxLayout(jp_editingInstructions, BoxLayout.X_AXIS));
        jp_lower3 = new JPanel();
        jp_lower3.setLayout(new BoxLayout(jp_lower3, BoxLayout.X_AXIS)); 
        jp_loadExpenseInstructions = new JPanel();
        jp_loadExpenseInstructions.setLayout(new BoxLayout(jp_loadExpenseInstructions, BoxLayout.X_AXIS));
        jp_lowerContainer = new JPanel();
        jp_lowerContainer.setLayout(new BoxLayout(jp_lowerContainer, BoxLayout.X_AXIS));
        jp_lowerContainer2 = new JPanel();
        jp_lowerContainer2.setLayout(new BoxLayout(jp_lowerContainer2, BoxLayout.Y_AXIS)); 
        
        jb_searchData = new JButton("Search Expenses");
        jb_searchData.addActionListener(this);
        jb_searchData.setActionCommand("search");
        
        jb_editData = new JButton("Save Edited");
        jb_editData.addActionListener(this);
        jb_editData.setActionCommand("edit");
        
        jb_editMenuReturn = new JButton("Main Menu");
        jb_editMenuReturn.addActionListener(this);
        jb_editMenuReturn.setActionCommand("return");
        
        jb_loadData = new JButton("Load Expense");
        jb_loadData.addActionListener(this);
        jb_loadData.setActionCommand("load");
        
        loadExpenseInstructions = new JLabel("Input index of expense to be edited below (look at search results" + "\n" + "Then press LOAD. Make changes, then press SAVE EDIT");
        searchInstructions = new JLabel ("Enter year and month");
        searchResults = new JLabel("Search Results");
        enterSearchKey = new JLabel("Enter Search Key(s)");
        editingInstructions = new JLabel("Enter the changes here: year, month, type, amount. Then save."); 
        
        jt_enterSearchYear = new JTextField(4);
        jt_enterSearchMonth = new JTextField(2);
        jt_editYear = new JTextField(4);
        jt_editMonth = new JTextField(2);
        jt_editType = new JTextField(10);
        jt_editAmt = new JTextField(6);
        displayResults = new JTextArea(10,10);
        displayResults.setLineWrap(true);
        displayResults.setWrapStyleWord(true);
        displayResults.setEditable(false);
        scrollPane = new JScrollPane(displayResults);
        jt_enterIndexToEdit = new JTextField(5);
        
        jp_up.add(searchInstructions);
        
        jp_upper.add(jt_enterSearchYear);
        jp_upper.add(jt_enterSearchMonth); 
        jp_upper.add(jb_searchData);
        
        jp_lower.add(searchResults);
        jp_lower.add(scrollPane);
        
        jp_loadExpenseInstructions.add(loadExpenseInstructions);
        
        jp_lower2.add(jt_enterIndexToEdit);
        jp_lower2.add(jb_loadData);

        jp_editingInstructions.add(editingInstructions);
        
        jp_lower3.add(jt_editYear);
        jp_lower3.add(jt_editMonth);
        jp_lower3.add(jt_editType);
        jp_lower3.add(jt_editAmt); 
        jp_lower3.add(jb_editData); 
        jp_lower3.add(jb_editMenuReturn);
        
        jp_lowerContainer2.add(jp_loadExpenseInstructions);
        jp_lowerContainer2.add(jp_lower2);
        jp_lowerContainer2.add(jp_editingInstructions); 
        jp_lowerContainer2.add(jp_lower3);
        
        jp_lowerContainer.add(jp_lower);
        jp_lowerContainer.add(jp_lowerContainer2);
    
        getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
        getContentPane().add(jp_up); 
        getContentPane().add(jp_upper);
        getContentPane().add(jp_lowerContainer);
        
        setVisible(true);
        setSize(1000,350);
        vect = expenses; 
    }
    public boolean searchData() // Order of operations: searchData, loadData, editData
    {
        displayResults.setText(""); 
        resultsCounter = 0; //To determine whether to show "search completed-results not found" or "search completed." 
        Expense expenseBeingSearched = new Expense();
        try{    
            yearToSearch = Integer.parseInt(jt_enterSearchYear.getText());
            monthToSearch = Integer.parseInt(jt_enterSearchMonth.getText()); 
        }
        catch(NumberFormatException exp) //avoid NumberFormatExceptions
        {
            JOptionPane.showMessageDialog(null, "Input integers only!", "Warning!", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (yearToSearch > 2000 && yearToSearch < 2010 && monthToSearch >= 1 && monthToSearch <= 12) // Range check
        {
            for(int i = 0; i < vect.size(); i++) // Linear search algorithm
            {
                expenseBeingSearched = (Expense)vect.elementAt(i);
                if (expenseBeingSearched.year == yearToSearch)
                {
                    if(expenseBeingSearched.month == monthToSearch)
                    {
                        displayResults.append(i + ": " + expenseBeingSearched.year + "/" + expenseBeingSearched.month + ", " + expenseBeingSearched.type + ", " + expenseBeingSearched.amount + "\n");
                        resultsCounter++;
                    }
                }
            } 
            if (resultsCounter == 0){
                JOptionPane.showMessageDialog(null, "Search Completed. No Results were found", "Information", JOptionPane.WARNING_MESSAGE);
            }
            
            else{
                JOptionPane.showMessageDialog(null, "Search Completed.", "Information", JOptionPane.WARNING_MESSAGE);
                resultsCounter = 0; 
            }
        }
        
        else if (yearToSearch < 2000 || yearToSearch > 2010 || monthToSearch < 1 || monthToSearch > 12) // What to do in case of invalid input
        {
            JOptionPane.showMessageDialog(null, "Year and/or month input(s) are out of range!", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }            
        return true; 
    }
    public boolean loadData() //Displays the selected expense's information on editable JTextFields
    {
        Expense expenseToDisplay = new Expense(); 
        try{
            indexToLoad = Integer.parseInt(jt_enterIndexToEdit.getText());
        }
        catch (NumberFormatException exp){
            JOptionPane.showMessageDialog(null, "Integers only!", "Warning", JOptionPane.WARNING_MESSAGE);
            return false; 
        }
        if(indexToLoad < vect.size() - 1 && indexToLoad >= 0) // Avoid ArrayIndexOutOfBounds exceptions
        {
            expenseToDisplay = (Expense)vect.elementAt(indexToLoad);
            yearTemp =  new Integer(expenseToDisplay.year);
            monthTemp = new Integer(expenseToDisplay.month);
            amountTemp = new Integer (expenseToDisplay.amount); 
            //JTextFields can only display Strings. Convert ints to Integers, and then use the toString() method. 
            jt_editYear.setText(yearTemp.toString());
            jt_editMonth.setText(monthTemp.toString());
            jt_editType.setText(expenseToDisplay.type);
            jt_editAmt.setText(amountTemp.toString());
        }
        
        else{ // Alerts the user that the index is invalid. 
            JOptionPane.showMessageDialog(null, "That index is NOT valid!", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;     
    }
    
    public boolean editData() // Saves the changes the user made to that expense object, into the vector. 
    {
        try{
            yearToStore = Integer.parseInt(jt_editYear.getText());
            monthToStore = Integer.parseInt(jt_editMonth.getText());
            amountToStore = Integer.parseInt(jt_editAmt.getText());
            typeToStore = jt_editType.getText();
        }
        catch (NumberFormatException exp) //Avoid numberFormatExceptions
        {
            JOptionPane.showMessageDialog(null, "Input integers only! Check all inputs!", "Warning!", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if(((yearToStore > 2000 && yearToStore < 2010) && (monthToStore >= 1 && monthToStore <= 12)) //Spell check and range check. 
        && ((typeToStore).equalsIgnoreCase("rme")) 
        || ((typeToStore).equalsIgnoreCase("food")) 
        || ((typeToStore).equalsIgnoreCase("edu")) 
        || ((typeToStore).equalsIgnoreCase("clothes"))
        || ((typeToStore).equalsIgnoreCase("transportation")) 
        || ((typeToStore).equalsIgnoreCase("amusement")) 
        || ((typeToStore).equalsIgnoreCase("health")) 
        || ((typeToStore).equalsIgnoreCase("social")) 
        || ((typeToStore).equalsIgnoreCase("sports")) 
        || ((typeToStore).equalsIgnoreCase("household")) 
        || ((typeToStore).equalsIgnoreCase("installments")) 
        || ((typeToStore).equalsIgnoreCase("others")))         
        {
            
            Expense expenseToWrite = new Expense(yearToStore, monthToStore, typeToStore, amountToStore);
            vect.setElementAt(expenseToWrite, (Integer.parseInt(jt_enterIndexToEdit.getText())));
            //The expense object, with its new information attached, is inserted into its old index. 
            jt_editYear.setText("");
            jt_editMonth.setText("");
            jt_editType.setText("");
            jt_editAmt.setText("");
            jt_enterIndexToEdit.setText("");
            JOptionPane.showMessageDialog(null, "Data successfully edited!", "Warning", JOptionPane.WARNING_MESSAGE);
            
            Expense expenseToCompare = new Expense();
            Expense expenseTemp = new Expense();
            Expense expenseMin = new Expense();
            for(int i = 0; i < vect.size(); i++) //Selection-sorting algorithm
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
        }
        
        else if (yearToSearch < 2000 || yearToSearch > 2010 || monthToSearch < 1 || monthToSearch > 12)
        {
            JOptionPane.showMessageDialog(null, "Year and/or month input(s) are out of range!", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else if (!((typeToStore).equalsIgnoreCase("rme")) //If the user-inputted type is invalid, then the program skips and displays msg. 
        && (!(typeToStore).equalsIgnoreCase("food")) 
        && (!(typeToStore).equalsIgnoreCase("edu")) 
        && (!(typeToStore).equalsIgnoreCase("clothes"))
        && (!(typeToStore).equalsIgnoreCase("transportation")) 
        && (!(typeToStore).equalsIgnoreCase("amusement")) 
        && (!(typeToStore).equalsIgnoreCase("health")) 
        && (!(typeToStore).equalsIgnoreCase("social")) 
        && (!(typeToStore).equalsIgnoreCase("sports")) 
        && (!(typeToStore).equalsIgnoreCase("household")) 
        && (!(typeToStore).equalsIgnoreCase("installments")) 
        && (!(typeToStore).equalsIgnoreCase("others"))){
            JOptionPane.showMessageDialog(null, "Invalid type!", "Warning", JOptionPane.WARNING_MESSAGE);
        }        
        else if (typeToStore.equals("")){ // else if the type is blank, delete that expense object. 
            vect.removeElementAt(Integer.parseInt(jt_enterIndexToEdit.getText()));
        }
        return true; 
    }        
        
    public void actionPerformed(ActionEvent evt)
    {
        if (evt.getActionCommand().equals("search")){
            searchData();
        }
        if (evt.getActionCommand().equals("edit")){ 
            editData();
            displayResults.setText("");
        }
        if (evt.getActionCommand().equals("load")){
            loadData();
        }
        if (evt.getActionCommand().equals("return")){
            F.setVisible(false);
            parent.setVisible(true);
        }   
    }
   
}	