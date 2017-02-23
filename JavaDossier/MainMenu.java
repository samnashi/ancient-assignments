
import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class Expense
{
    String type;
    int year, month, amount;
    
    public Expense()
    {
        type = "default";
        year = month = amount = 1;
    }
    public Expense (int yr, int mth, String typ, int amt)
    {
        year = yr;
        month = mth;
        type = typ;
        amount = amt;
    }
    public Expense (String typ, int amt)
    {
        type = typ;
        amount = amount;
    }
    public Expense (int yr, String typ, int amt)
    {
        year = yr;
        type = typ;
        amount = amt;
    }
    public Expense (int yr, int mth, int amt)
    {
        year = yr;
        month = mth;
        amount = amt;
    }
}
    
public class MainMenu extends JFrame implements ActionListener
{
    public JFrame F;
    JPanel jp_right,jp_left;
    JButton jb_addData,jb_editData, jb_makeGraph, jb_displayData, jb_writeData;
    
    Vector expenses = new Vector();
    StringTokenizer token;
    int year, month, amount;
    String type;
    public MainMenu() //CONSTRUCTOR
    {
        F = this;
        setTitle("GraphMaker v.1.0.0");
     
        jp_right=new JPanel();
        jp_right.setLayout(new BoxLayout(jp_right,BoxLayout.Y_AXIS));
        
        jp_left=new JPanel();
        jp_left.setLayout(new BoxLayout(jp_left,BoxLayout.Y_AXIS));
        
        //BuTTON ASSIGNMENTS
        jb_addData=new JButton("Add Expenses");
        jb_addData.addActionListener(this);
        jb_addData.setActionCommand("add");
  
        jb_editData = new JButton ("Edit Expenses");
        jb_editData.addActionListener(this);
        jb_editData.setActionCommand("edit");
        
        jb_makeGraph = new JButton("Make Graph");
        jb_makeGraph.addActionListener(this);
        jb_makeGraph.setActionCommand("make");
       
        jb_displayData = new JButton("Display Data");
        jb_displayData.addActionListener(this);
        jb_displayData.setActionCommand("display");
        
        jb_writeData = new JButton("Write Data");
        jb_writeData.addActionListener(this);
        jb_writeData.setActionCommand("write");
        
        //PANEL ASSIGNMENTS
        jp_left.add(jb_addData);
        jp_left.add(jb_editData);
        jp_right.add(jb_makeGraph);
        jp_right.add(jb_displayData);
         
        getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.X_AXIS));
        getContentPane().add(jp_left);
        getContentPane().add(jp_right);  
        getContentPane().add(jb_writeData);
    } 

    public boolean readData()
    {
        try
        {
            BufferedReader inFile = new BufferedReader (new FileReader("expenses.txt"));
            String line;
            Expense blank = new Expense(); 
            line = inFile.readLine();

            while(line != null)            
            {                
                token = new StringTokenizer(line, ";");//string tokenizer
                
                year = Integer.parseInt(token.nextToken());
                month = Integer.parseInt(token.nextToken());  
                type = token.nextToken();            
                amount = Integer.parseInt(token.nextToken());               
                Expense newExpense = new Expense(year, month, type, amount);
                expenses.add(newExpense);
                line = inFile.readLine();
            }     
            inFile.close();
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "Error while trying to load the expenses file!", "Warning!", JOptionPane.WARNING_MESSAGE);
            Expense blank = new Expense();
            expenses.add(blank); 
            return false;
        } 
        
        
        return true;
    }
    
    public boolean writeData()
    {
         try
         {
            PrintWriter outFile = new PrintWriter(new FileWriter("expenses.txt"));
                for(int i = 0 ;i < expenses.size();i++)
                {
                    Expense expenseToWrite = new Expense();
                    expenseToWrite = (Expense)expenses.get(i);
                    outFile.print(expenseToWrite.year + ";");
                    outFile.print(expenseToWrite.month + ";");
                    outFile.print(expenseToWrite.type + ";");
                    outFile.print(expenseToWrite.amount + ";");
                    outFile.println();
                }
                outFile.close();  
                JOptionPane.showMessageDialog(null, "Data successfully written.", "Confirmation", JOptionPane.WARNING_MESSAGE);
         }
         catch(IOException exp)
         {
            JOptionPane.showMessageDialog(null, "Error while trying to write the expenses file!", "Warning!", JOptionPane.WARNING_MESSAGE);
            return false;
        } 
        return true;
    }

    public void actionPerformed(ActionEvent evt)
    {
        if (evt.getActionCommand().equals("add")) 
        {
            F.setVisible(false);
            new AddData(F,expenses);
        }
        if (evt.getActionCommand().equals("edit")){
            F.setVisible(false);
            new EditData(F, expenses);
        }
        if (evt.getActionCommand().equals("make")){
            F.setVisible(false);
            new MakeGraph(F, expenses);
        }
        if(evt.getActionCommand().equals("display")){
            F.setVisible(false);
            new DisplayData(F, expenses);
        }
        if (evt.getActionCommand().equals("write")){
            writeData();
        }
    }
  
    public static void main (String[] args)
    {
        MainMenu mainMenu= new MainMenu();
        WindowQuitter wquit = new WindowQuitter();
        
        mainMenu.setSize(350,100);
        mainMenu.setResizable(false);
        mainMenu.setVisible(true); 
        mainMenu.readData();
            
        mainMenu.addWindowListener(wquit);
    }
}
    
class WindowQuitter extends WindowAdapter
{
    public void windowClosing( WindowEvent e )
    {           
        System.exit( 0 );  
    }
}