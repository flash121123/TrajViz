package edu.gmu.trajviz.view;

import java.beans.PropertyChangeEvent;


import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTableHeader;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

import com.roots.map.MapPanel;

import edu.gmu.trajviz.logic.MotifChartData;
import edu.gmu.trajviz.model.ItrSequiturModel;
import edu.gmu.trajviz.view.table.RuleTableColumns;
import edu.gmu.trajviz.view.table.RuleTableModel;
import gmu.edu.core.gi.RuleInterval;

/**
 * 
 * handling the chart panel and sequitur rules table
 * 
 * We visualize ItrSequitur Result on the top of Manfred Lerner, seninp's code
 * 
 * @author Qingzhe Li, Yifeng Gao
 * 
 */

public class ItrSequiturRulesPanel extends JPanel implements ListSelectionListener,
PropertyChangeListener {

/** Fancy serial. */
private static final long serialVersionUID = -2710973854572981568L;

public static final String FIRING_PROPERTY = "selectedRow";

private RuleTableModel sequiturTableModel = new RuleTableModel();

private JXTable sequiturTable;

public MapPanel mapPanel,mapPanel1;

private JScrollPane sequiturRulesPanel;

private String selectedSequiturRule;

private boolean acceptListEvents;

private MotifChartData chartData;

private ArrayList<ArrayList<RuleInterval>> ruleIntervals ;
private ArrayList<Integer> ruleMapLength;

//private ArrayList<Integer> frequency;
// the logger business
//
private static Logger consoleLogger;
private static Level LOGGING_LEVEL = Level.DEBUG;
static {
consoleLogger = (Logger) LoggerFactory.getLogger(ItrSequiturRulesPanel.class);
consoleLogger.setLevel(LOGGING_LEVEL);
}

/*
* 
* Comparator for the sorting of the Expanded Sequitur Rules Easy logic: sort by the length of the
* Expanded Sequitur Rules
*/
public Comparator<String> expandedRuleComparator = new Comparator<String>() {
public int compare(String s1, String s2) {
  return s1.length() - s2.length();
}
};



/**
* Constructor.
*/
public ItrSequiturRulesPanel() {
super();
this.sequiturTableModel = new RuleTableModel();
this.sequiturTable = new JXTable() {

  private static final long serialVersionUID = 2L;

  @Override
  protected JTableHeader createDefaultTableHeader() {
    return new JXTableHeader(columnModel) {
      private static final long serialVersionUID = 1L;

      @Override
      public void updateUI() {
        super.updateUI();
        // need to do in updateUI to survive toggling of LAF
        if (getDefaultRenderer() instanceof JLabel) {
          ((JLabel) getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        }
      }
    };
  }

};

this.sequiturTable.setModel(sequiturTableModel);
this.sequiturTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
this.sequiturTable.setShowGrid(false);

this.sequiturTable.getSelectionModel().addListSelectionListener(this);

@SuppressWarnings("unused")
org.jdesktop.swingx.renderer.DefaultTableRenderer renderer = (org.jdesktop.swingx.renderer.DefaultTableRenderer) sequiturTable
    .getDefaultRenderer(String.class);

// Make some columns wider than the rest, so that the info fits in.
TableColumnModel columnModel = sequiturTable.getColumnModel();
columnModel.getColumn(RuleTableColumns.RULE_NUMBER.ordinal()).setPreferredWidth(27);
columnModel.getColumn(RuleTableColumns.RULE_USE_FREQUENCY.ordinal()).setPreferredWidth(27);
columnModel.getColumn(RuleTableColumns.LENGTH.ordinal()).setPreferredWidth(36);
/*
columnModel.getColumn(SequiturRulesTableColumns.RULE_USE_FREQUENCY.ordinal()).setPreferredWidth(40);
columnModel.getColumn(SequiturRulesTableColumns.SEQUITUR_RULE.ordinal()).setPreferredWidth(100);
columnModel.getColumn(SequiturRulesTableColumns.EXPANDED_SEQUITUR_RULE.ordinal()).setPreferredWidth(
    150);
columnModel.getColumn(SequiturRulesTableColumns.RULE_MEAN_LENGTH.ordinal()).setPreferredWidth(120);
*/

/*
 * dont need sorter -qz
 *
TableRowSorter<SequiturRulesTableModel> sorter = new TableRowSorter<SequiturRulesTableModel>(
    sequiturRulesTableModel);
sequiturRulesTable.setRowSorter(sorter);
sorter.setComparator(SequiturRulesTableColumns.EXPANDED_SEQUITUR_RULE.ordinal(),
    expandedRuleComparator);
*/
DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
this.sequiturTable.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);

this.sequiturRulesPanel = new JScrollPane(sequiturTable);
}



/**
* create the panel with the sequitur rules table
* 
* @return sequitur panel
*/
public void resetPanel() {
// cleanup all the content
this.removeAll();
this.add(sequiturRulesPanel);
this.validate();
this.repaint();
}

/**
* @return sequitur table model
*/
public RuleTableModel getSequiturTableModel() {
return sequiturTableModel;
}

/**
* @return sequitur table
*/
public JTable getSequiturTable() {
return sequiturTable;
}

/**
* Resets the selection and resorts the table by the Rules.
*/
public void resetSelection() {
// TODO: there is the bug. commented out.
// sequiturTable.getSelectionModel().clearSelection();
// sequiturTable.setSortOrder(0, SortOrder.ASCENDING);
}

public void propertyChange(PropertyChangeEvent event) {
  event.getPropertyName();
}

public ArrayList<String> rname;

@Override
public void valueChanged(ListSelectionEvent arg) {

  if (!arg.getValueIsAdjusting() && this.acceptListEvents) {
    int col = sequiturTable.getSelectedColumn();
    int row = sequiturTable.getSelectedRow();
    consoleLogger.debug("Selected ROW: " + row + " - COL: " + col);
    String rule = String.valueOf(sequiturTable.getValueAt(row,
        RuleTableColumns.RULE_NUMBER.ordinal()));
    //String actualRules = clusters.get(Integer.valueOf(rule)).toString();
    System.out.println("rule:::::"+rule);
    this.firePropertyChange(FIRING_PROPERTY, this.selectedSequiturRule, rule);
 //   System.out.println("Original rules:::::"+map.get(Integer.valueOf(rule)));
    //System.out.println("Rule Intervals:::::"+ruleIntervals.get(Integer.valueOf(rule)));
    //System.out.println("Rule String:::::"+rname.get(Integer.valueOf(rule)));
    //System.out.println("Rule Content:"+Integer.valueOf(rule));
    
    for(RuleInterval xx : ruleIntervals.get(Integer.valueOf(rule)))
    {
    	
    	System.out.println(ItrSequiturModel.lat.subList(xx.startPos, xx.endPos));
    	System.out.println(ItrSequiturModel.lon.subList(xx.startPos, xx.endPos));
    	
    }
    
    System.out.println("End Rule Content");
    
 //   this.selectedSequiturRule = actualRule;
  }
}
/**
* Clears the panel.
*/
public void clear() {
this.acceptListEvents = false;
this.removeAll();
//this.chartData = null;
sequiturTableModel.update(null);

this.validate();
this.repaint();
this.acceptListEvents = true;
}

public void setRulesData(MotifChartData chartData) {
	this.acceptListEvents = false;
	
	this.chartData = chartData;
	sequiturTableModel.update(this.chartData.getGrammarRules());
	resetPanel();
	this.acceptListEvents = true;
}

public void setRulesData(MotifChartData chartData, ArrayList<ArrayList<RuleInterval>> ruleIntervals, ArrayList<HashSet<Integer>> mapToOriginRules,  ArrayList<String> s,ArrayList<Integer> ruleMapLength) {
	this.acceptListEvents = false;
	
	this.chartData = chartData;
	this.ruleIntervals = ruleIntervals;
	this.rname=s;
	this.ruleMapLength = new ArrayList<Integer>();
	for(String ss : s)
	{
		this.ruleMapLength.add(ss.split(" ").length);
	}
	for(int i=0;i<5;i++)
	{
		this.ruleMapLength.add(-1);
	}
	sequiturTableModel.update(this.chartData.getGrammarRules(),this.ruleIntervals,this.ruleMapLength);//,this.frequency);
	resetPanel();
	this.acceptListEvents = true;
}



public ArrayList<String> getRname() {
	return rname;
}



public void setRname(ArrayList<String> rname) {
	this.rname = rname;
}

}
