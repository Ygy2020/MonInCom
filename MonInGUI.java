package swingGUI;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import fileIO.MonInFileIO;

/**
 * Swing GUI for the Monthly InCome <br>
 * it use the GroupLayout as Layout Manager
 * @author User
 * @version 1.0
 */
public class MonInGUI extends JPanel implements ActionListener, ItemListener, TableModelListener {
	
	String fileName = "MonInFile.txt";
	
	/**
	 * Need to avoid warning
	 */
	private static final long serialVersionUID = 1L;

	String salary, prlsmon, accrual, other, 
		   totActprf, totAccrual, totAccount;
	String constantloss[] = new String[10];
	
	JTextField jtfSalary; // hold the salary
	JTextField jtfPrfLsMon; // hold the profit from the last month
	JTextField jtfAccrual; // hold the accrual
	JTextField jtfOther; //hold other data 
	
	JTextField jtfTotActPrf; //total profit
	JTextField jtfTotAccrual; //total accrual
	JTextField jtfTotAccount; //total account
	
	JTable jtabConstLos; //hold the constant loss in a tab
		
	JButton jbtnCalc; // button to calc the total
	JButton jbtnSave; //button to save the modification on the file
	
	// displays prompt
	JLabel jlabSalary, jlabPrfLsMon, jlabAccrual, jlabOther; 
	JLabel jlabTotal, jlabTotActPrf, jlabTotAccrual, jlabTotAcc;
	JLabel jlabError; // display error messages
	
	JCheckBox jcbAdvSett;
	boolean boolSave = false;
	
	/**
	 * constructor 
	 */
	MonInGUI() {
		ReadFile();
		
		String[] columnNames = {"Constant Loss"};
		Object[][] data= new Object[10][1];
		
		for (int i=0; i<10;i++){
		      data[i][0]=constantloss[i];		
		}
			
		// create a new JFrame container
		JFrame jfrm = new JFrame("MonInCom");
		jfrm.setLayout(new FlowLayout());
		
		JPanel jpnl1 = new JPanel();
		JPanel jpnl2 = new JPanel();
		JPanel jpnl3 = new JPanel();
		
		// Specify GroupLayout for the layout manager
		GroupLayout layout = new GroupLayout(jfrm.getContentPane());
        jfrm.getContentPane().setLayout(layout);
		
		GroupLayout layout1 = new GroupLayout(jpnl1);
		jpnl1.setLayout(layout1);
		
		GroupLayout layout2 = new GroupLayout(jpnl3);
		jpnl3.setLayout(layout2);
		
		// give the frame an initial size 
		jfrm.setSize(600,400);
		jfrm.setExtendedState(jfrm.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		jfrm.setResizable(false);
		
		// terminate the program when the user closes the application
		jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		// create the text fields
		jtfSalary = new JTextField(salary,8);
		jtfPrfLsMon = new JTextField(prlsmon,8);
		jtfAccrual = new JTextField(accrual,8);
		jtfOther = new JTextField(other,8);
		jtfTotAccount = new JTextField(totAccount,8);
		jtfTotAccrual = new JTextField(totAccrual,8);
		jtfTotActPrf = new JTextField(totActprf,8);
		
		//force a max dimension to all JTextField
		jtfSalary.setMaximumSize(new Dimension(60,60));
		jtfPrfLsMon.setMaximumSize(new Dimension(60,60));
		jtfAccrual.setMaximumSize(new Dimension(60,60));
		jtfOther.setMaximumSize(new Dimension(60,60));
		jtfTotAccount.setMaximumSize(new Dimension(60,60)); 
		jtfTotAccrual.setMaximumSize(new Dimension(60,60)); 
		jtfTotActPrf.setMaximumSize(new Dimension(60,60));
		
		// set the action commands for the text fields
		jtfSalary.setActionCommand("Salary");
		jtfPrfLsMon.setActionCommand("PrfLsMon");
		jtfAccrual.setActionCommand("Accrual");
		jtfOther.setActionCommand("Other");
		jtfTotAccount.setActionCommand("TotAcc");
		jtfTotAccrual.setActionCommand("TotAccrual");
		jtfTotActPrf.setActionCommand("TotActPrf");
		
		jtfSalary.addActionListener(this);
		jtfPrfLsMon.addActionListener(this);
		jtfAccrual.addActionListener(this);
		jtfOther.addActionListener(this);
		jtfTotAccount.addActionListener(this);
		jtfTotAccrual.addActionListener(this);
		jtfTotActPrf.addActionListener(this);
				
		jtabConstLos = new JTable(data, columnNames);
		jtabConstLos.setPreferredScrollableViewportSize(new Dimension(140, 140));
		jtabConstLos.setFillsViewportHeight(true);
		
        JScrollPane scrollPane = new JScrollPane(jtabConstLos);
		
        jpnl2.setLayout(new FlowLayout());
		jpnl2.setSize(scrollPane.getWidth(),scrollPane.getHeight());
		jpnl2.add(scrollPane);
		
		
		jtabConstLos.getModel().addTableModelListener(this);
		
		//set not enabled jtfAccrual and jtabConstLoss
		jtfAccrual.setEnabled(false);
		jtabConstLos.setEnabled(false);		
		jtfTotAccount.setEnabled(false);
		jtfTotAccrual.setEnabled(false);
		jtfTotActPrf.setEnabled(false);
		
		//add action listener for the jtable		
		
		// create the buttons
		jbtnCalc = new JButton("Calc");
		jbtnSave = new JButton("Save");
		
		// add action listener for the compare button
		jbtnCalc.addActionListener(this);
		jbtnSave.addActionListener(this);
		
		// create the label
		jlabSalary = new JLabel("Salary: ");
		jlabPrfLsMon = new JLabel("Profit last month: ");
		jlabAccrual = new JLabel("Accrual: ");
		jlabOther = new JLabel("Other: ");
		jlabTotal = new JLabel("Total: ");
		jlabTotActPrf = new JLabel("Total Actual Profit: ");
		jlabTotAccrual = new JLabel("Total Accrual: ");
		jlabTotAcc = new JLabel("Total Account: ");
		jlabError = new JLabel("");
		
		// create the checkbox
		jcbAdvSett = new JCheckBox("Advanced Setting");
	
		// add the listener the Check Box
		jcbAdvSett.addItemListener(this);
			
		//built up the layout
		layout1.setVerticalGroup(
		   layout1.createSequentialGroup()
		      .addGap(10)		      
		      .addGroup(layout1.createParallelGroup(GroupLayout.Alignment.BASELINE)
		    	   .addComponent(jlabSalary)
		    	   .addComponent(jtfSalary))		         
		      .addGroup(layout1.createParallelGroup(GroupLayout.Alignment.BASELINE)
		           .addComponent(jlabPrfLsMon)
		    	   .addComponent(jtfPrfLsMon))
		      .addGroup(layout1.createParallelGroup(GroupLayout.Alignment.BASELINE)		    	   
		    	   .addComponent(jlabAccrual)
		           .addComponent(jtfAccrual))
		      .addGroup(layout1.createParallelGroup(GroupLayout.Alignment.BASELINE)		           
		           .addComponent(jlabOther)
		           .addComponent(jtfOther)));
		
		layout1.setHorizontalGroup(
		   layout1.createSequentialGroup()
			  .addGap(10) 
			  .addGroup(layout1.createParallelGroup(GroupLayout.Alignment.LEADING)
			       .addComponent(jlabSalary)
				   .addComponent(jlabPrfLsMon)
				   .addComponent(jlabAccrual)
				   .addComponent(jlabOther))
		      .addGroup(layout1.createParallelGroup(GroupLayout.Alignment.LEADING)
		    	   .addComponent(jtfSalary)
				   .addComponent(jtfPrfLsMon)
		    	   .addComponent(jtfAccrual)
		           .addComponent(jtfOther))
	    );
		
		
		layout2.setVerticalGroup(           
		   layout2.createSequentialGroup()
			  .addGroup(layout2.createParallelGroup(GroupLayout.Alignment.BASELINE)		           
		           .addComponent(jbtnCalc)
		           .addComponent(jcbAdvSett))
		      .addGroup(layout2.createParallelGroup(GroupLayout.Alignment.BASELINE)		           
		           .addComponent(jlabTotal))
		      .addGroup(layout2.createParallelGroup(GroupLayout.Alignment.BASELINE)		           
		           .addComponent(jlabTotActPrf)
		           .addComponent(jtfTotActPrf))
		      .addGroup(layout2.createParallelGroup(GroupLayout.Alignment.BASELINE)		           
		           .addComponent(jlabTotAccrual)
		           .addComponent(jtfTotAccrual))
		      .addGroup(layout2.createParallelGroup(GroupLayout.Alignment.BASELINE)		           
		           .addComponent(jlabTotAcc)
		           .addComponent(jtfTotAccount)
		           .addComponent(jbtnSave))
		      //.addGroup(layout2.createParallelGroup(GroupLayout.Alignment.BASELINE)
		      //   .addComponent(jlabError))
		      .addGap(10)	   
		);
		
		layout2.setHorizontalGroup(
		   layout2.createSequentialGroup()
		     .addGap(10) 
		     .addGroup(layout2.createParallelGroup(GroupLayout.Alignment.LEADING)
		    		.addComponent(jbtnCalc)
		            .addComponent(jlabTotal)
		            .addComponent(jlabTotActPrf)
		            .addComponent(jlabTotAccrual)
		            .addComponent(jlabTotAcc))
		            //.addComponent(jlabError))
		      .addGroup(layout2.createParallelGroup(GroupLayout.Alignment.LEADING)
		    		.addGap(jbtnCalc.getHeight())
		            .addGap(jlabTotal.getHeight())
		            .addComponent(jtfTotActPrf)
		            .addComponent(jtfTotAccrual)
		            .addComponent(jtfTotAccount)
		            .addGap(jlabError.getHeight()))
		      .addGap(10)      
		      .addGroup(layout2.createParallelGroup(GroupLayout.Alignment.TRAILING)
		    		.addComponent(jcbAdvSett)		    		
		      		.addComponent(jbtnSave))
		      //.addComponent(jlabError)
		      .addGap(10)		
		);		

		
		
		//add panel to the frame
		//jfrm.add(jpnl1);
		//jfrm.add(jpnl2);
		//jfrm.add(jpnl3);
		layout.setVerticalGroup(
		   layout.createSequentialGroup()
		      .addGap(10)		      
		      .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
				      .addComponent(jpnl1)
				      .addComponent(jpnl2))
			  .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
					  .addComponent(jpnl3))
			  .addComponent(jlabError)
	    );
		
		layout.setHorizontalGroup(
		   layout.createSequentialGroup()
		     .addGap(10) 
		     .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
		    		 .addComponent(jpnl1)
		    		 .addComponent(jpnl3)
		    		 .addComponent(jlabError))
		     .addComponent(jpnl2)
		 );
		
		
		// Display the frame
		jfrm.pack();
		jfrm.setVisible(true);		
	}
	
	/**
	 * Read the file associated with the program
	 */
	void ReadFile() {
		try{
			MonInFileIO srcFile = new MonInFileIO(fileName);
			String fileValue[];
			fileValue = srcFile.readSrcFile();
			
			for (int i=0; i<fileValue.length; i++){
				if (fileValue[i].contains("salary")) {
					salary = fileValue[i].substring(fileValue[i].indexOf("=")+1);
				}
				else if (fileValue[i].contains("prlsmon")){
					prlsmon = fileValue[i].substring(fileValue[i].indexOf("=")+1);
				}
				else if (fileValue[i].contains("accrual") && !fileValue[i].contains("totaccrual")){
					accrual = fileValue[i].substring(fileValue[i].indexOf("=")+1);
				}
				else if (fileValue[i].contains("other")){
					other = fileValue[i].substring(fileValue[i].indexOf("=")+1);
				}
				
				for (int j=0;j<10;j++){
					if (fileValue[i].contains("constantloss"+j)){
						constantloss[j] = fileValue[i].substring(fileValue[i].indexOf("=")+1);
					}				
				}
				
				if (fileValue[i].contains("totActprf")){
					totActprf = fileValue[i].substring(fileValue[i].indexOf("=")+1);
				}
				else if (fileValue[i].contains("totAccrual")){
					totAccrual = fileValue[i].substring(fileValue[i].indexOf("=")+1);
				}
				else if (fileValue[i].contains("totAccount")){
					totAccount = fileValue[i].substring(fileValue[i].indexOf("=")+1);
				}			
			}
		} catch (IOException ex){
			System.out.println("Error reading file: " + ex );
		}
	}
	
	@Override
	public void itemStateChanged(ItemEvent ie) {
		
		// obtain a reference to the check box that caused the event
		JCheckBox cb = (JCheckBox) ie.getItem();

		// change the control variable
		if (cb.isSelected())
			enableFields(true);		    
		else
			enableFields(false);		
	}
    
	void enableFields(boolean enable){
		//enable jtabConstLos and jtfAccrual
		jtabConstLos.setEnabled(enable);
		jtfAccrual.setEnabled(enable);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
						
		if (e.getActionCommand().equals("Calc")){
			if (!calcTotal()) jlabError.setText("Calc cannot be resolved");
		}
		else if  (e.getActionCommand().equals("Save")){
			if (!saveChange()) jlabError.setText("Error during save");
		}		
		else  {
			
			if (e.getActionCommand().equals("Salary")){ 
				 try{
					 Double.parseDouble(jtfSalary.getText());
					 jlabError.setText("");
				 } catch (NumberFormatException ex){
					 jlabError.setText("Only number allowed");
					 jtfSalary.setText("0");					 
				 }
			}
			else if(e.getActionCommand().equals("PrfLsMon")){ 
				 try{
					 Double.parseDouble(jtfPrfLsMon.getText());
					 jlabError.setText("");
				 } catch (NumberFormatException ex){
					 jlabError.setText("Only number allowed");
					 jtfPrfLsMon.setText("0");					 
				 }
			}
			else if(e.getActionCommand().equals("Accrual")){ 
				 try{
					 Double.parseDouble(jtfAccrual.getText());
					 jlabError.setText("");
				 } catch (NumberFormatException ex){
					 jlabError.setText("Only number allowed");
					 jtfAccrual.setText("0");					 
				 }
			}
			else if(e.getActionCommand().equals("Other")){ 
				 try{
					 Double.parseDouble(jtfOther.getText());
					 jlabError.setText("");
				 } catch (NumberFormatException ex){
					 jlabError.setText("Only number allowed");
					 jtfOther.setText("0");					 
				 }
			}
			else if(e.getActionCommand().equals("TotAcc")){ 
				 try{
					 Double.parseDouble(jtfTotAccount.getText());
					 jlabError.setText("");
				 } catch (NumberFormatException ex){
					 jlabError.setText("Only number allowed");
					 jtfTotAccount.setText("0");					 
				 }
			}
			else if(e.getActionCommand().equals("TotAccrual")){ 
				 try{
					 Double.parseDouble(jtfTotAccrual.getText());
					 jlabError.setText("");
				 } catch (NumberFormatException ex){
					 jlabError.setText("Only number allowed");
					 jtfTotAccrual.setText("0");					 
				 }
			}
			else if(e.getActionCommand().equals("TotActPrf")){ 
				 try{
					 Double.parseDouble(jtfTotActPrf.getText());
					 jlabError.setText("");
				 } catch (NumberFormatException ex){
					 jlabError.setText("Only number allowed");
					 jtfTotActPrf.setText("0");					 
				 }
			}			
		}
	}
	
	public void tableChanged(TableModelEvent e) {
		try{
			 Double.parseDouble(jtabConstLos.getValueAt(e.getLastRow(),e.getColumn()).toString());
			 jlabError.setText("");
		 } catch (NumberFormatException ex){
			 jlabError.setText("Only number allowed");
			 jtabConstLos.setValueAt("0",e.getLastRow(),e.getColumn());					 
		 }		
     }
	
	
	/**
	 * Calc the total, upon the value on the form
	 */
	boolean calcTotal(){
		try{
			double dSalary,dPrfLsMon,dAccrual,dOther,dTotAcc,dTotAccrual,dTotActPrf;
			double dConstantloss[] = new double[10];
			
			dSalary= Double.parseDouble(jtfSalary.getText());
			dPrfLsMon = Double.parseDouble(jtfPrfLsMon.getText());
			dAccrual = Double.parseDouble(jtfAccrual.getText());
			dOther = Double.parseDouble(jtfOther.getText());
			
			for (int i=0; i<jtabConstLos.getRowCount();i++) {
				dConstantloss[i] = Double.parseDouble(jtabConstLos.getValueAt(i, 0).toString());
				if (i==5) System.out.println(jtabConstLos.getValueAt(i, 0).toString());
			}
			
			dTotActPrf = dSalary+dPrfLsMon+dOther;
			for (int i=0; i<dConstantloss.length;i++) dTotActPrf -= dConstantloss[i]; 
			
			dTotAccrual = dAccrual + dConstantloss[2];
					
			dTotAcc = dTotAccrual+dTotActPrf;
			
			jtfTotAccount.setText(Double.toString(dTotAcc));
			jtfTotAccrual.setText(Double.toString(dTotAccrual));
			jtfTotActPrf.setText(Double.toString(dTotActPrf));
			jtfAccrual.setText(Double.toString(dAccrual));
			
			return true;
		}catch(Exception ex){
			jlabError.setText(ex.toString());
			return false;
		}
	}
	
	/**
	 * Save the modification on the file associated with the program
	 */
	boolean saveChange(){
		try{
			MonInFileIO srcFile = new MonInFileIO(fileName);
			double dConstantloss[] = new double[10];
						
			srcFile.WriteFile("salary", jtfSalary.getText());
			srcFile.WriteFile("prlsmon", jtfPrfLsMon.getText());
			srcFile.WriteFile("accrual", jtfAccrual.getText());			
			srcFile.WriteFile("other", jtfOther.getText());
			for (int i=0; i<jtabConstLos.getRowCount();i++) {
				dConstantloss[i] = Double.parseDouble(jtabConstLos.getValueAt(i, 0).toString());			
				srcFile.WriteFile("constantloss"+i, Double.toString(dConstantloss[i]));
			}
			
			srcFile.WriteFile("totActprf", jtfTotActPrf.getText());
			srcFile.WriteFile("totAccrual", jtfTotAccrual.getText());
			srcFile.WriteFile("totAccount", jtfTotAccount.getText());
			
			jtfAccrual.setText(jtfTotAccrual.getText());
			
			return true;
		} catch (IOException ex){
			jlabError.setText(ex.toString());
			return false;	
		}
	}
	
	/**
	 * The main methods, which launch our program
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new MonInGUI();
			}
		});
	}
}
