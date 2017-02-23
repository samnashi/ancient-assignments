
import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import no.geosoft.cc.geometry.Geometry;
import no.geosoft.cc.graphics.*;

public class MakeGraph extends JFrame implements ActionListener
{
    public JFrame F, parent; 
    JPanel jp_instructions, jp_enterData, jp_lower2;
    JButton jb_makeGraphType1, jb_makeGraphType2, jb_makeGraphType3, jb_graphMenuReturn;
    JTextArea numReportConfirm;
    JTextField jt_startYear, jt_endYear, jt_enterType;
    JLabel instruction1, instruction2, instruction3, instruction4, instruction5, instruction6;
    public Vector vect, monthlyTotals, typeYearlyTotals;
    String typeToCompare, typeToSearch, graphLabel;
    Expense expenseBeingCompared, expenseBeingComparedLower, expenseToStore, expenseToGraph;
    
    int yearToSearch, numberOfTypes, startYear, endYear, monthlyAmount, largestMonthlyAmount, 
    previousMonthlyAmount, typeYearlyAmount, previousTypeYearlyAmount, largestTypeYearlyAmount, interval;
    
    Integer yearTemp, monthTemp;
    double conversionFactor, barHeightTemp;
    
    public MakeGraph(JFrame parent, Vector expenses)
    {
        F = this;
        this.parent = parent;
        setTitle("Make Graph(s): Type 1: YEARLY COMPOSITION. Type 2: MONTHLY TOTALS. Type 3: YEAR-BY-YEAR COMPARISON");
        GridBagConstraints gbc = new GridBagConstraints();
        jp_instructions = new JPanel();
        jp_instructions.setLayout(new GridBagLayout());
        jp_enterData = new JPanel();
        jp_enterData.setLayout(new BoxLayout(jp_enterData, BoxLayout.X_AXIS));
        jp_lower2 = new JPanel();
        jp_lower2.setLayout(new BoxLayout(jp_lower2, BoxLayout.X_AXIS));
        
        jb_makeGraphType1 = new JButton("Make Graph Type 1");
        jb_makeGraphType1.addActionListener(this);
        jb_makeGraphType1.setActionCommand("graph1");
        
        jb_makeGraphType2 = new JButton("Make Graph Type 2");
        jb_makeGraphType2.addActionListener(this);
        jb_makeGraphType2.setActionCommand("graph2");
        
        jb_makeGraphType3 = new JButton("Make Graph Type 3");
        jb_makeGraphType3.addActionListener(this);
        jb_makeGraphType3.setActionCommand("graph3");
        
        jb_graphMenuReturn = new JButton("Main Menu");
        jb_graphMenuReturn.addActionListener(this);
        jb_graphMenuReturn.setActionCommand("return");
        
        jt_startYear = new JTextField(4);
        jt_endYear = new JTextField(4);
        jt_enterType = new JTextField(7);
        numReportConfirm = new JTextArea(1,1);
        numReportConfirm.setEditable(false);
        instruction1 = new JLabel("REQUIRED FIELD(S): ");
        instruction2 = new JLabel("Type 1: StartYear."); 
        instruction3 = new JLabel("Type 2: StartYear, EndYear, Type."); 
        instruction4 = new JLabel("Type 3: StartYear, EndYear. ");
        instruction5 = new JLabel("StartYear:                                                                       EndYear:                                                            Type:                                                                      ");
        instruction6 = new JLabel("                 ");
        gbc.weightx = 1; 
        gbc.anchor = GridBagConstraints.WEST;
        jp_instructions.add(instruction1, gbc);
        gbc.gridy = 1;
        jp_instructions.add(instruction2, gbc);
        gbc.gridy = 2;
        jp_instructions.add(instruction3, gbc);
        gbc.gridy = 3;
        jp_instructions.add(instruction4, gbc);
        gbc.gridy = 4;
        jp_instructions.add(instruction6, gbc);
        gbc.gridy = 5;
        jp_instructions.add(instruction5, gbc);

        jp_enterData.add(jt_startYear);
        jp_enterData.add(jt_endYear);
        jp_enterData.add(jt_enterType);
        jp_lower2.add(jb_makeGraphType1);
        jp_lower2.add(jb_makeGraphType2);
        jp_lower2.add(jb_makeGraphType3);
        jp_lower2.add(jb_graphMenuReturn);
        
        getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
        getContentPane().add(jp_instructions);
        getContentPane().add(jp_enterData);
        getContentPane().add(jp_lower2);
        
        setSize(800,200);
        setVisible(true);
        vect = expenses; 
    }
    public boolean makeGraphType1()
    {
        int t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, sum; // each t(i) variable stands for the yearly total of a type of expense. 
        t1 = t2 = t3 = t4 = t5 = t6 = t7 = t8 = t9 = t10 = t11 = t12 = sum = 0;
        expenseBeingCompared = new Expense();
        
        try{        
            yearToSearch = Integer.parseInt(jt_startYear.getText());
        }
        catch (NumberFormatException exp){ 
            JOptionPane.showMessageDialog(null, "Something is wrong with the year input(s)!", "Warning!", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if(yearToSearch > 2000 && yearToSearch < 2010) // RANGE CHECK
        {
            for(int i = 0; i < vect.size(); i++)
            {
                expenseBeingCompared = ((Expense) (vect.elementAt(i)));
                if (expenseBeingCompared.year == yearToSearch) //matching-year expenses
                {
                    if ((expenseBeingCompared.type).equalsIgnoreCase("rme")){ // assign each int to one type. Add the amounts of expenses of the same type. 
                        t1 = t1 + expenseBeingCompared.amount;
                    }
                    else if ((expenseBeingCompared.type).equalsIgnoreCase("food")){
                        t2 = t2 + expenseBeingCompared.amount;
                    }
                    else if ((expenseBeingCompared.type).equalsIgnoreCase("education")){
                        t3 = t3 + expenseBeingCompared.amount;
                    }
                    else if ((expenseBeingCompared.type).equalsIgnoreCase("clothes")){
                        t4 = t4 + expenseBeingCompared.amount;
                    }
                    else if ((expenseBeingCompared.type).equalsIgnoreCase("transportation")){
                        t5 = t5 + expenseBeingCompared.amount;
                    }
                    else if ((expenseBeingCompared.type).equalsIgnoreCase("amusement")){
                        t6 = t6 + expenseBeingCompared.amount;
                    }
                    else if ((expenseBeingCompared.type).equalsIgnoreCase("health")){
                        t7 = t7 + expenseBeingCompared.amount;
                    }
                    else if ((expenseBeingCompared.type).equalsIgnoreCase("social")){
                        t8 = t8 + expenseBeingCompared.amount;
                    }
                    else if ((expenseBeingCompared.type).equalsIgnoreCase("sports")){
                        t9 = t9 + expenseBeingCompared.amount;
                    }
                    else if ((expenseBeingCompared.type).equalsIgnoreCase("household")){
                        t10 = t10 + expenseBeingCompared.amount;
                    }
                    else if ((expenseBeingCompared.type).equalsIgnoreCase("installments")){
                        t11 = t11 + expenseBeingCompared.amount;
                    }
                    else if ((expenseBeingCompared.type).equalsIgnoreCase("others")){
                        t12 = t12 + expenseBeingCompared.amount;
                    }
                }
            }
            sum = t1 + t2 + t3 + t4 + t5 + t6 + t7 + t8 + t9 + t10 + t11 + t12; // add all the expenses' amounts in that particular year. 
            PieChartWindow graphType1 = new PieChartWindow(); 
            
            System.out.println("***Numerical Report****");
            System.out.println("For expenses in the year: " + yearToSearch + ", the breakdown per type is as follows:");
            if(t1 != 0){
              graphType1.addSector((double)t1/(double)sum, "RME", graphType1.getStyle()); // sends the graphing methods the fraction (of the circle, hence the double) each type represents. 
              System.out.println("RME: " + t1); 
            }
            if(t2 != 0){
              graphType1.addSector((double)t2/(double)sum, "Food", graphType1.getStyle());
              System.out.println("Food: " + t2); 
            }
            if(t3 != 0){
              graphType1.addSector((double)t3/(double)sum, "Education", graphType1.getStyle());
              System.out.println("Education: " + t3); 
            }
            if(t4 != 0){
              graphType1.addSector((double)t4/(double)sum, "Clothes", graphType1.getStyle());
              System.out.println("Clothes: " + t4); 
            }
            if(t5 != 0){
              graphType1.addSector((double)t5/(double)sum, "Transportation", graphType1.getStyle());
              System.out.println("Transportation: " + t5); 
            }
            if(t6 != 0){
              graphType1.addSector((double)t6/(double)sum, "Amusement", graphType1.getStyle());
              System.out.println("Amusement: " + t6); 
            }
            if(t7 != 0){
             graphType1.addSector((double)t7/(double)sum, "Health", graphType1.getStyle());
             System.out.println("Health: " + t7); 
            }
            if(t8 != 0){
              graphType1.addSector((double)t8/(double)sum, "Social", graphType1.getStyle());
              System.out.println("Social: " + t8); 
            }
            if(t9 != 0){
              graphType1.addSector((double)t9/(double)sum, "Sports", graphType1.getStyle());
              System.out.println("Sports: " + t9); 
            }
            if(t10 != 0){
              graphType1.addSector((double)t10/(double)sum, "Household", graphType1.getStyle());
              System.out.println("Household: " + t10); 
            }
            if(t11 != 0){
              graphType1.addSector((double)t11/(double)sum, "Installments", graphType1.getStyle());
              System.out.println("Installments: " + t11); 
            }
            if(t12 != 0){
             graphType1.addSector((double)t12/(double)sum, "Others", graphType1.getStyle());
             System.out.println("Others: " + t12); 
            }
            System.out.println("****End of report****");
            System.out.println(" "); // GIVE SPACE FOR NEXT REPORT
        }
        else if(yearToSearch < 2000 || yearToSearch > 2010){ //Range check
            JOptionPane.showMessageDialog(null, "Year is out of range! Input another year", "Warning!", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        else{
            JOptionPane.showMessageDialog(null, "Check your input!", "Warning!", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
    
        
    public boolean makeGraphType2() //Year-by-year comparison graph
    {
        Vector typeYearlyTotals = new Vector();
        expenseBeingCompared = new Expense();
        expenseBeingComparedLower = new Expense();
        expenseToStore = new Expense();
        
        try 
        {
            startYear = Integer.parseInt(jt_startYear.getText());
            endYear = Integer.parseInt(jt_endYear.getText());
        }
        
        catch (NumberFormatException exp){ 
            JOptionPane.showMessageDialog(null, "Something is wrong with the year input(s)!", "Warning!", JOptionPane.WARNING_MESSAGE);
            return false;
        }      
        typeYearlyAmount = yearToSearch = 0; // reset these to zeros. 
        typeToSearch = jt_enterType.getText();
        
        
        if( (startYear < endYear) && (startYear > 2000 && endYear < 2010) && //Spell check and range check
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
        
            interval = endYear - startYear;             
            for(int j = 0; j <= interval; j++) // Outer for loop. Changes the year to search accordingly. 
            {
                yearToSearch = startYear + j;
                for(int i = 0; i < vect.size(); i++) 
                //Inner for loop. Makes an entire pass through the vector, searching matching-year and matching-type expenses, adds the yearly total up accordingly. 
                {
                    expenseBeingCompared = ((Expense)(vect.elementAt(i)));
                    if ((expenseBeingCompared.year == yearToSearch) && ((expenseBeingCompared.type).equalsIgnoreCase(typeToSearch))){
                        typeYearlyAmount = expenseBeingCompared.amount;
                    }

                }
                expenseToStore = new Expense(yearToSearch, typeToSearch, typeYearlyAmount); // creates an expense object, containing only year, month, and that months' total.
                typeYearlyTotals.add(expenseToStore);
                
                if (typeYearlyTotals.size() == 1){
                    previousTypeYearlyAmount = largestTypeYearlyAmount = typeYearlyAmount; // only relevant if the monthlyTotals size is 1.
                }
                if (typeYearlyTotals.size() > 1)
                {
                    previousTypeYearlyAmount = ((Expense)typeYearlyTotals.elementAt(typeYearlyTotals.size() - 1)).amount;
                    if (largestTypeYearlyAmount < typeYearlyAmount){
                        largestTypeYearlyAmount = typeYearlyAmount; // determines the largest amount of monthly expenses in that interval (for the conversionFactor)
                    }
                }   
            
                if ((expenseBeingCompared.type).equalsIgnoreCase(typeToSearch)){
                    typeYearlyAmount = expenseBeingCompared.amount; // resets monthlyAmount, after the previous month's total was put into the monthlyTotals vector.  
                }
                
                else{
                    typeYearlyAmount = 0;
                }
                
            }
            BarChartWindow graphType2Window = new BarChartWindow(100 + 50 * typeYearlyTotals.size() + 1, 900); // settable window size
            System.out.println("****Numerical Report****");
            System.out.println("For the interval " + startYear + " to " + endYear + ", the total(s) for the expense under category " + typeToSearch + " is as follows: ");
            conversionFactor = (double)850 / (double)largestTypeYearlyAmount; // conversion factor. Maximum height of graph is 850 pixels. The largest month will get a 850-pixel high bar. 
            
            for (int i = 0; i < typeYearlyTotals.size(); i++) //Loop, graphing the contents of the typeYearlyTotals vector. 
            {
                expenseToGraph = new Expense();
                expenseToGraph = (Expense)(typeYearlyTotals.elementAt(i));
                System.out.println(expenseToGraph.year + ": " + expenseToGraph.amount);
                yearTemp =  new Integer(expenseToGraph.year);
                graphLabel = (yearTemp.toString());//label for the graph. Needs to be a string
                barHeightTemp = conversionFactor * expenseToGraph.amount;
                graphType2Window.addBar(graphLabel, (int)barHeightTemp, graphType2Window.getColor());
            }
            
            System.out.println("****End of Report****");
            System.out.println(" "); //GIVE SPACE FOR NEXT REPORT
            typeYearlyTotals.removeAllElements(); 
        }
        else if(startYear < 2000 || startYear > 2010 || endYear < 2000 || endYear > 2010){ // For out-of range inputs. Skips the processing block. 
            JOptionPane.showMessageDialog(null, "Year(s) is/are out of range! Check your input!", "Warning!", JOptionPane.WARNING_MESSAGE);
        }
        else if(startYear > endYear){ 
            JOptionPane.showMessageDialog(null, "Start year has to be smaller than end year!", "Warning!", JOptionPane.WARNING_MESSAGE);
        }
        else{
            JOptionPane.showMessageDialog(null, "Type is NOT recognized!", "Warning!", JOptionPane.WARNING_MESSAGE);
        }
       
        return true; 
    }
    
    public boolean makeGraphType3() // Monthly totals
    {
        Vector monthlyTotals = new Vector();
        expenseBeingCompared = new Expense();
        expenseBeingComparedLower = new Expense();
        expenseToStore = new Expense();
        
        try
        {
            startYear = Integer.parseInt(jt_startYear.getText());
            endYear = Integer.parseInt(jt_endYear.getText());
        }
        
        catch (NumberFormatException exp){ 
            JOptionPane.showMessageDialog(null, "Something is wrong with the year input(s)!", "Warning!", JOptionPane.WARNING_MESSAGE);
            return false;
        }      
        monthlyAmount = 0;
        
        if( (startYear <= endYear) && (startYear > 2000 && endYear < 2010))
        {
            for(int i = 0; i < vect.size(); i++)
            {
                expenseBeingCompared = ((Expense)(vect.elementAt(i)));
                if (i == 0){ // exception for the first element of vector. 
                    monthlyAmount = expenseBeingCompared.amount;
                }
                
                else if (((expenseBeingCompared.year <= endYear) &&(expenseBeingCompared.year >= startYear) && i != 0) || (expenseBeingCompared.year == endYear + 1 && expenseBeingCompared.month == 1)) // range check, and i check to avoid IndexOutOfBoundsException. 
                {
                    expenseBeingCompared = ((Expense)(vect.elementAt(i))); 
                    expenseBeingComparedLower = ((Expense)(vect.elementAt(i - 1))); //the element in the index of 1 less than the element that becomes expenseBeingCompared.
               
                    if (expenseBeingComparedLower.month == expenseBeingCompared.month){// detects whether there is a month change. If there isn't, then the totals are added up. 
                        monthlyAmount = monthlyAmount + expenseBeingCompared.amount; 
                    }
                    else if (expenseBeingCompared.month != expenseBeingComparedLower.month || expenseBeingCompared.year != expenseBeingComparedLower.year)
                    {
                        expenseToStore = new Expense(expenseBeingComparedLower.year, expenseBeingComparedLower.month, monthlyAmount); // creates an expense object, containing only year, month, and that months' total.
                        monthlyTotals.add(expenseToStore);
                        
                        if (monthlyTotals.size() == 1){
                            previousMonthlyAmount = largestMonthlyAmount = monthlyAmount; // only relevant if the monthlyTotals size is 1.
                        }
                        if (monthlyTotals.size() > 1){
                            previousMonthlyAmount = ((Expense)monthlyTotals.elementAt(monthlyTotals.size() - 1)).amount;
                            if (largestMonthlyAmount < monthlyAmount){
                                largestMonthlyAmount = monthlyAmount; // determines the largest amount of monthly expenses in that interval (to calculate conversionFactor). 
                            }
                        }    
                        monthlyAmount = expenseBeingCompared.amount; // resets monthlyAmount, after the previous month's total was put into the monthlyTotals vector.  
                    }
                }
            }
            
            BarChartWindow graphType3Window = new BarChartWindow(50 + 50 * monthlyTotals.size() + 1, 900); // settable window size
            conversionFactor = (double)850 / (double)largestMonthlyAmount; // conversion factor. Maximum height of graph is 850 pixels. The largest month will get a 850-pixel high bar. 
            
            System.out.println("****Numerical Report****");
            System.out.println("For the interval " + startYear + " to " + endYear + ", the monthly breakdown is as follows: ");
            for (int i = 0; i < monthlyTotals.size(); i++) //Loop, graphing the contents of the monthlyTotals vector. 
            {
                expenseToGraph = new Expense();
                expenseToGraph = (Expense)(monthlyTotals.elementAt(i));
                yearTemp =  new Integer(expenseToGraph.year);
                monthTemp = new Integer(expenseToGraph.month); //Integer representation of the year/month
                graphLabel = (yearTemp.toString() + "/" + monthTemp.toString());//label for the graph. Needs to be a string
                barHeightTemp = conversionFactor * expenseToGraph.amount;
                System.out.println(expenseToGraph.year + " - " + expenseToGraph.month + ": " + expenseToGraph.amount);
                graphType3Window.addBar(graphLabel, (int)barHeightTemp, graphType3Window.getColor());
            }
            monthlyTotals.removeAllElements(); 
            System.out.println("****End of Report****");   
            System.out.println(""); //GIVE SPACE FOR NEXT REPORT
        }
        else if(startYear < 2000 || startYear > 2010 || endYear < 2000 || endYear > 2010){ // Range check
            JOptionPane.showMessageDialog(null, "Year(s) is/are out of range! Check your input!", "Warning!", JOptionPane.WARNING_MESSAGE);
        }
        else if(startYear >= endYear){ 
            JOptionPane.showMessageDialog(null, "Start year has to be smaller than end year!", "Warning!", JOptionPane.WARNING_MESSAGE);
        }
        return true;
    }
    
    public void actionPerformed(ActionEvent evt)
    {
        if (evt.getActionCommand().equals("graph1")){ 
            makeGraphType1();
        }
        if (evt.getActionCommand().equals("graph2")){ 
            makeGraphType2();
        }
        if (evt.getActionCommand().equals("graph3")){ 
            makeGraphType3();
        }
        if (evt.getActionCommand().equals("return")){
            F.setVisible(false);
            parent.setVisible(true);
        }   
    }
   
}	