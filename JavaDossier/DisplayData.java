import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class DisplayData extends JFrame implements ActionListener
{
	public JFrame F, parent; 
	JPanel jp_upper, jp_lower, jp_lower2, jp_lowerContainer;
	JButton jb_displayMenuReturn;
	JTextArea displayResults;
	JScrollPane scrollPane;
	JTextField jt_enterSearchKey, jt_enterIndexToEdit, jt_edit;
	JLabel results;
	String yearToDisplay, monthToDisplay, typeToDisplay, amountToDisplay;
	public Vector vect;
	
	public DisplayData(JFrame parent, Vector expenses)
	{
		F = this;
		this.parent = parent;
		setTitle("Display Data");
		jp_upper = new JPanel();
		jp_upper.setLayout(new BoxLayout(jp_upper, BoxLayout.X_AXIS));
		jp_lower = new JPanel();
		jp_lower.setLayout(new BoxLayout(jp_lower, BoxLayout.Y_AXIS));
		
		jb_displayMenuReturn = new JButton("Main Menu");
		jb_displayMenuReturn.addActionListener(this);
		jb_displayMenuReturn.setActionCommand("return");
		
		results = new JLabel("Data found:");

		displayResults = new JTextArea(100,100);
		displayResults.setLineWrap(true);
		displayResults.setWrapStyleWord(true);
		scrollPane = new JScrollPane(displayResults);

		jp_upper.add(jb_displayMenuReturn);
		
		jp_lower.add(results);
		jp_lower.add(scrollPane);
		
		getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
		getContentPane().add(jp_upper);
		getContentPane().add(jp_lower);
		
		setVisible(true);
		setSize(400,400);
		vect = expenses; // Makes sure the vector here is equal to the vector created in MainMenu
		
		Expense expenseToDisplay = new Expense();
   	 	for(int i = 0; i < vect.size(); i++)
   	 	//Sorting algorithm, in case the user edits the actual text file him/herself. 
        {                
        	expenseToDisplay = ((Expense)vect.elementAt(i));
        	yearToDisplay = (expenseToDisplay.year + "");
        	monthToDisplay = (expenseToDisplay.month + "");
        	typeToDisplay = (expenseToDisplay.type);
        	amountToDisplay = (expenseToDisplay.amount + "");
        	displayResults.append(yearToDisplay + "/" + monthToDisplay + ", " + typeToDisplay + ", " 
        	+ amountToDisplay + "\n");
        } 
	}
	
	public void actionPerformed(ActionEvent evt)
    {
        if (evt.getActionCommand().equals("return"))
        {
	        F.dispose();
	        parent.setVisible(true);
	    }	
    }
}	