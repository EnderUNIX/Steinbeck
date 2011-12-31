/*
 * UAEntrance.java
 *
 * Created on May 9, 2005, 3:55 PM
 */

package steinbeck;

import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 *
 * @author  Administrator
 */
public class UAGUIEntrance extends javax.swing.JFrame {
    
    protected static UAGUIEntrance gui;
    protected UAGUISetupAudit setupAudit;
    protected UAAuditScriptManager scriptManager;
     
       
    /** Creates new form UAEntrance */
    public UAGUIEntrance() {
        initComponents();
        // our table is a single selection one
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);        
        // and it shows * chars in password column cells
        // 3 is the password column index
        TableColumn col = jTable1.getColumnModel().getColumn(3);
        
        col.setCellRenderer(new UAGUIPasswordCellRenderer());
        // setup window
        setupAudit = new UAGUISetupAudit(); 
        // script configuration windows
        scriptManager = new UAAuditScriptManager();
    }
    
    // removes an audit from the table
    protected void removeAudit(){
        DefaultTableModel dtm = (DefaultTableModel)gui.jTable1.getModel();
        int rowIndex = gui.jTable1.getSelectedRow();
        if(rowIndex >= 0)
            dtm.removeRow(rowIndex);
    }
    
    // remove all audits from the table
    protected void removeAllAudits(){
        DefaultTableModel dtm = (DefaultTableModel)gui.jTable1.getModel();
        int rowCount = dtm.getRowCount();
        if(rowCount > 0)
            for(int rowIndex = 0; rowIndex < rowCount; rowIndex++)
                dtm.removeRow(0);
    }

    // adds an audit to the table (audit data comes from single UAGUISetupAudit class)
    protected void addAudit(){
        Vector anAudit = new Vector();
        anAudit.add(setupAudit.getIP());
        anAudit.add(setupAudit.getPort());
        anAudit.add(setupAudit.getUsername());
        anAudit.add(setupAudit.getPassword());
        anAudit.add(setupAudit.getAuditor());
        anAudit.add(setupAudit.getRemotePath());
        anAudit.add(setupAudit.getLocalPath());
        
        DefaultTableModel dtm = (DefaultTableModel)gui.jTable1.getModel();
        dtm.addRow(anAudit);
    }
    
    private boolean shouldGetResult(){
        // we don't ask user to grab results or not. we'll try to get the results anyway. but if there is no results
        // then we'll output an error message to the standard output and inform the user.
        // then user has to get his own results.
        return true;
        /*
        if(gui.jCheckBoxMenuItem2.isSelected()){
            return true;
        }
        return false; 
        */
    }

    private boolean shouldCheckProgress(){
        // we don't ask user to check progress or not. we'll try to get check progress anyway. but if there is no progress
        // then we'll output that as an error message to the standard output and inform the user.
        // then user has to stop its own audits.
        return true;
        /*
        if(gui.jCheckBoxMenuItem1.isSelected()){
            return true;
        }
        return false; 
        */
    }
        
    protected boolean testConnection(){
        
        // create audit handler
        UAAuditHandler auditHandler = new UAAuditHandler();
        
        // add audits to audit handler
        // IP
        String IP = setupAudit.getIP();
        // Port
        String port = setupAudit.getPort();
        // Username 
        String username = setupAudit.getUsername();                    
        // password 
        String password = setupAudit.getPassword();                  
        // script
        
        String scriptName = setupAudit.getScriptName();

        // remote path
        String remotePath = setupAudit.getRemotePath();
        // local path
        String localPath = setupAudit.getLocalPath();
        // add the audit 
        UAAuditTestConnection auditTest = new UAAuditTestConnection(IP, username, port, password , remotePath, localPath, scriptName);

        if(auditTest.testConnection() == false ){
            return false;
        }
        
        return true;
        
    }
    
    protected int getRowCount(){
        DefaultTableModel dtm = (DefaultTableModel)gui.jTable1.getModel();
        return dtm.getRowCount();
    }
    
    protected Vector getRow(int rowIndex){
        DefaultTableModel dtm = (DefaultTableModel)gui.jTable1.getModel();
        if(rowIndex < 0 || rowIndex > dtm.getRowCount())
            return null;
        return (Vector)dtm.getDataVector().elementAt(rowIndex);
    }
    
    // edits an audit on the table (audit data comes from single UAGUISetupAudit class)
    protected void editAudit(){
        DefaultTableModel dtm = (DefaultTableModel)gui.jTable1.getModel();
        
        int rowIndex = gui.jTable1.getSelectedRow();
        dtm.removeRow(rowIndex);
        addAudit();
    }
    
    // shows a particular audit data double clicked on the table
    private void showAudit(){
        DefaultTableModel dtm = (DefaultTableModel)gui.jTable1.getModel();
        
        int columnIndex = 0;
        int rowIndex = gui.jTable1.getSelectedRow();
        
        if(rowIndex >= 0){
            setupAudit.setIP((String)dtm.getValueAt(rowIndex, columnIndex));
            setupAudit.setPort((String)dtm.getValueAt(rowIndex, columnIndex + 1));
            setupAudit.setUsername((String)dtm.getValueAt(rowIndex, columnIndex + 2));
            setupAudit.setPassword((String)dtm.getValueAt(rowIndex, columnIndex + 3));
            setupAudit.setAuditor((String)dtm.getValueAt(rowIndex, columnIndex + 4));
            setupAudit.setRemotePath((String)dtm.getValueAt(rowIndex, columnIndex + 5));
            setupAudit.setLocalPath((String)dtm.getValueAt(rowIndex, columnIndex + 6));
        
            this.setVisible(false);
            // this window will be edit window
            setupAudit.SetEditWindow();
            setupAudit.setVisible(true);        
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new JTable(){
            public boolean isCellEditable(int rowIndex, int vColIndex){
                return false;
            }
        };
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();

        getContentPane().setLayout(new java.awt.BorderLayout(10, 10));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Welcome To Steinbeck (The Remote Unix Script Runner)");
        setFont(new java.awt.Font("MS Reference Sans Serif", 0, 12));
        jScrollPane1.setFont(new java.awt.Font("Tahoma", 0, 12));
        jTable1.setFont(new java.awt.Font("MS Sans Serif", 0, 12));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "IP", "Port", "Username", "Password", "Script", "Remote Path", "Local Path"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableClicked(evt);
            }
        });

        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 10));

        jButton1.setBackground(new java.awt.Color(204, 204, 204));
        jButton1.setFont(new java.awt.Font("Tahoma", 0, 14));
        jButton1.setText("Start");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel1.add(jButton1);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        jPanel2.setPreferredSize(new java.awt.Dimension(30, 10));
        getContentPane().add(jPanel2, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel3, java.awt.BorderLayout.EAST);

        getContentPane().add(jPanel4, java.awt.BorderLayout.WEST);

        jMenuBar1.setFont(new java.awt.Font("Tahoma", 0, 12));
        jMenu1.setText("Scan");
        jMenu1.setFont(new java.awt.Font("Tahoma", 0, 12));
        jMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu1ActionPerformed(evt);
            }
        });

        jMenuItem1.setFont(new java.awt.Font("Tahoma", 0, 12));
        jMenuItem1.setText("Add");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });

        jMenu1.add(jMenuItem1);

        jMenuItem2.setFont(new java.awt.Font("Tahoma", 0, 12));
        jMenuItem2.setText("Edit");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });

        jMenu1.add(jMenuItem2);

        jMenuItem3.setFont(new java.awt.Font("Tahoma", 0, 12));
        jMenuItem3.setText("Remove");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });

        jMenu1.add(jMenuItem3);

        jMenuItem4.setFont(new java.awt.Font("Tahoma", 0, 12));
        jMenuItem4.setText("Remove All");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });

        jMenu1.add(jMenuItem4);

        jMenuBar1.add(jMenu1);

        jMenu4.setText("Manage");
        jMenu4.setFont(new java.awt.Font("Tahoma", 0, 12));
        jMenuItem6.setFont(new java.awt.Font("Tahoma", 0, 12));
        jMenuItem6.setText("Scripts");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });

        jMenu4.add(jMenuItem6);

        jMenuBar1.add(jMenu4);

        jMenu2.setText("Run");
        jMenu2.setFont(new java.awt.Font("Tahoma", 0, 12));
        jMenu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu2ActionPerformed(evt);
            }
        });

        jMenuItem5.setFont(new java.awt.Font("Tahoma", 0, 12));
        jMenuItem5.setText("Start");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });

        jMenu2.add(jMenuItem5);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Help");
        jMenu3.setFont(new java.awt.Font("Tahoma", 0, 12));
        jMenuItem9.setFont(new java.awt.Font("Tahoma", 0, 12));
        jMenuItem9.setText("Help Contents");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });

        jMenu3.add(jMenuItem9);

        jMenuItem10.setFont(new java.awt.Font("Tahoma", 0, 12));
        jMenuItem10.setText("About");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });

        jMenu3.add(jMenuItem10);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-800)/2, (screenSize.height-455)/2, 800, 455);
    }//GEN-END:initComponents

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
        // shows configuration window
        // it should open in mode 1
        scriptManager.setMode(1);
        scriptManager.setVisible(true);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        // TODO add your handling code here:
        // bad coding!!!
        jButton1ActionPerformed(null);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    // Help -> About
    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(this, "Steinbeck v1.2 [07/26/2006] \nurgunb@hotmail.com", "About Steinbeck" ,JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    // Configure -> Set Local Path
    // Run -> Start
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        // if there is no audits setup display a warning
        DefaultTableModel dtm = (DefaultTableModel)gui.jTable1.getModel();
        int rowCount = dtm.getRowCount();
        if(rowCount == 0){
            showErrorMessage("First add a script from Scan->Add");
            return;
        }
        
        // create (audit) script handler
        UAAuditHandler auditHandler = new UAAuditHandler();
        
        // add audits to audit handler
        try{
            if(rowCount > 0)
                for(int rowIndex = 0; rowIndex < rowCount; rowIndex++){
                    Vector anAudit = (Vector)dtm.getDataVector().elementAt(rowIndex);
                    int columnIndex = 0;
                    // IP
                    String IP = (String)anAudit.elementAt(columnIndex);
                    // Port
                    String port = (String)anAudit.elementAt(columnIndex+1);
                    // Username 
                    String username = (String)anAudit.elementAt(columnIndex+2);                    
                    // password 
                    String password = (String)anAudit.elementAt(columnIndex+3);                  
                    // script
                    String rawScript = (String)anAudit.elementAt(columnIndex+4);
                    
                    int indexOfLastSlash = rawScript.lastIndexOf(setupAudit.fileSeparator);
        
                    String scriptName = rawScript;
                    
                    // remote path
                    String remotePath = (String)anAudit.elementAt(columnIndex+5);
                    
                    // local path
                    String localPath = (String)anAudit.elementAt(columnIndex+6);
                    
                    // add the audit 
                    auditHandler.addAudit(IP, username, port, password , remotePath, localPath, scriptName, shouldCheckProgress(), shouldGetResult());
                }
        }
        catch(Exception e){
            showErrorMessage("Error ocurred when adding session(s): " + e.getMessage());
            return;        
        }
        
        // run audits
        try{
            auditHandler.startAllAudits();
        }
        catch(Exception e){
            showErrorMessage("Error ocurred when running session(s): " + e.getMessage());
            return;            
        }
              
        UAGUIProgress progressWindow = new UAGUIProgress(auditHandler);
        progressWindow.setVisible(true);
        
        // should we check progress bars
        if(shouldCheckProgress())
            progressWindow.start();
        else
            showErrorMessage("Progress Bars will not shown 'cause of the configuration: You are on your own to stop the session(s)!"); 
        
        this.hide();
    }//GEN-LAST:event_jButton1ActionPerformed

    // Manage -> Remove All Audits
    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:
        removeAllAudits();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    // Manage -> Remove Audits
    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        removeAudit();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    // Manage -> Edit Audit
    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        showAudit();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    // Audit table double click
    private void tableClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableClicked
        // One funny thing: if you have actions both for clickCount 1 and 2, 
        // then both actions will be run. So, here you can only include one
        // of them.
        /*
        if(evt.getClickCount() == 1){
            System.out.println("(single) Selected row: " + gui.jTable1.getSelectedRow());
        }
        */
        if(evt.getClickCount() == 2){
            showAudit();
        }
        
    }//GEN-LAST:event_tableClicked

    // Help -> Help Contents
    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        // TODO add your handling code here:
        new UAGUIHelpContents().setVisible(true);
        // hide();
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    // Manage -> Add Audit
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
        // we want to add a new script
        setupAudit.SetAddWindow();
        // shows configuration window
        setupAudit.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu2ActionPerformed

    private void jMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu1ActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                gui = new UAGUIEntrance();
                gui.setVisible(true);    
            }
        });
    }
    
    public void showErrorMessage(String message){
        JOptionPane.showMessageDialog(this, message, "An Error Ocurred" ,JOptionPane.ERROR_MESSAGE);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
    
}
