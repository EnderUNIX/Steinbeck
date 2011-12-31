/*
 * UAGUIPasswordCellRenderer.java
 *
 * Created on May 18, 2005, 2:32 PM
 */

package steinbeck;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.Component; // :(


/**
 *
 * @author Administrator
 */
public class UAGUIPasswordCellRenderer extends JLabel implements TableCellRenderer{
    
    /** Creates a new instance of UAGUIPasswordCellRenderer */
    public UAGUIPasswordCellRenderer()  {
    }
    
    public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex) {
        // 'value' is value contained in the cell located at
        // (rowIndex, vColIndex)
    
        if (isSelected) {
            // cell (and perhaps other cells) are selected
        }
    
        if (hasFocus) {
            // this cell is the anchor and the table has the focus
        }
        
        // Configure the component with the specified value
        setText("******");
    
        // Since the renderer is a component, return itself
        return this;
    }
    
    // The following methods override the defaults for performance reasons
    public void validate() {}
    public void revalidate() {}
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {}
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {}   
}
