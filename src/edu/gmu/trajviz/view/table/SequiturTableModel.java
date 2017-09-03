package edu.gmu.trajviz.view.table;

import java.util.ArrayList;

import core.agi.AGrammarRules;
import core.gi.RuleInterval;

/**
 * Table Data Model for the sequitur JTable
 * 
 * @author Manfred Lerner, seninp
 * 
 */
public class SequiturTableModel extends SequiturTableDataModel {

	  /** Fancy serial. */
	  private static final long serialVersionUID = -2952232752352963293L;

	  /**
	   * Constructor.
	   */
	  public SequiturTableModel() {
		  SequiturTableColumns[] columns = SequiturTableColumns.values();
	    String[] schemaColumns = new String[columns.length];
	    for (int i = 0; i < columns.length; i++) {
	      schemaColumns[i] = columns[i].getColumnName();
	    }
	    setSchema(schemaColumns);
	  }

	  /**
	   * Updates the table model with provided data.
	   * 
	   * @param combinedGrammarRules the data for table.
	   */
	  public void update(AGrammarRules grammarRules) {
	    int rowIndex = 0;
	    rows.clear();
	    if (!(null == grammarRules)) {
	      for (rowIndex = 0; rowIndex < grammarRules.size(); rowIndex++) {
	        Object[] item = new Object[getColumnCount() + 1];
	        int nColumn = 0;
	        item[nColumn++] = rowIndex;
	     //   item[nColumn++] = ((Integer)grammarRules.get(rowIndex).get(0).getRule1()).toString()+","+((Integer)grammarRules.get(rowIndex).get(0).getRule2()).toString();
	        item[nColumn++]	= grammarRules.get(rowIndex).getRuleintervels().size();
	       // item[nColumn++] = grammarRules.get(rowIndex).toString();
	        /*
	        item[nColumn++] = combinedGrammarRules.get(rowIndex).getRuleLevel();
	        item[nColumn++] = combinedGrammarRules.get(rowIndex).getOccurrences().size();
	        item[nColumn++] = combinedGrammarRules.get(rowIndex).getRuleString();
	        item[nColumn++] = combinedGrammarRules.get(rowIndex).getExpandedRuleString();
	        item[nColumn++] = combinedGrammarRules.get(rowIndex).getRuleUseFrequency();
	        item[nColumn++] = combinedGrammarRules.get(rowIndex).getMeanLength();
	        item[nColumn++] = combinedGrammarRules.get(rowIndex).minMaxLengthAsString();
	        // item[nColumn++] = saxContainerList.get(rowIndex).getOccurenceIndexes();
	        */
	        rows.add(item);
	      }
	    }

	    fireTableDataChanged();
	  }
	 
	  public void update(AGrammarRules grammarRules, ArrayList<ArrayList<RuleInterval>> ruleIntervals, ArrayList<Integer> ruleMapLength) {
		  int rowIndex = 0;
		    rows.clear();
		    if (!(null == ruleIntervals)) {
		    //  for (rowIndex = 0; rowIndex < grammarRules.size(); rowIndex++) {
		//    	System.out.println("rowIndexSize::::::::::::::::::::::::::::::::::::::::::::::::::::"+filter.size());
		    	for (rowIndex = 0; rowIndex<ruleIntervals.size();rowIndex++){
		        Object[] item = new Object[getColumnCount() + 1];
		        int nColumn = 0;
		        item[nColumn++] = rowIndex;
		     //   item[nColumn++] = ((Integer)grammarRules.get(rowIndex).get(0).getRule1()).toString()+","+((Integer)grammarRules.get(rowIndex).get(0).getRule2()).toString();
		        item[nColumn++]	= ruleIntervals.get(rowIndex).size();//grammarRules.get(filter.get(rowIndex)).getRuleIntervals().size();     //Since some of first intervals are not correct, need to find reason.  
		       // item[nColumn++] = grammarRules.get(rowIndex).toString();
		        		        //item[nColumn++]	= this.getRuleMeanLength(ruleIntervals, rowIndex);
		        		        item[nColumn++] = ruleMapLength.get(rowIndex);
		        		        // item[nColumn++] = grammarRules.get(rowIndex).toString();
		        /*
		         
		        item[nColumn++] = combinedGrammarRules.get(rowIndex).getRuleLevel();
		        item[nColumn++] = combinedGrammarRules.get(rowIndex).getOccurrences().size();
		        item[nColumn++] = combinedGrammarRules.get(rowIndex).getRuleString();
		        item[nColumn++] = combinedGrammarRules.get(rowIndex).getExpandedRuleString();
		        item[nColumn++] = combinedGrammarRules.get(rowIndex).getRuleUseFrequency();
		        item[nColumn++] = combinedGrammarRules.get(rowIndex).getMeanLength();
		        item[nColumn++] = combinedGrammarRules.get(rowIndex).minMaxLengthAsString();
		        // item[nColumn++] = saxContainerList.get(rowIndex).getOccurenceIndexes();
		        */
		        rows.add(item);
		      }
		    }

		    fireTableDataChanged();
		  }
			
		
	  /*
	   * Important for table column sorting (non-Javadoc)
	   * 
	   * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	   */
	  public Class<?> getColumnClass(int columnIndex) {
	    /*
	     * for the RuleNumber and RuleFrequency column we use column class Integer.class so we can sort
	     * it correctly in numerical order
	     */
		if (columnIndex == SequiturTableColumns.RULE_NUMBER.ordinal())
		  return Integer.class;  
		//if (columnIndex == CombinedRulesTableColumns.COMBINED_RULE.ordinal())
		//	  return Integer.class;  
	    if (columnIndex == SequiturTableColumns.RULE_USE_FREQUENCY.ordinal())
	    	return Integer.class;
	    if (columnIndex == SequiturTableColumns.LENGTH.ordinal())
	    	return Integer.class;
	    /*
	    if (columnIndex == CombinedRulesTableColumns.RULE_LEVEL.ordinal())
	      return Integer.class;
	    if (columnIndex == CombinedRulesTableColumns.RULE_FREQUENCY.ordinal())
	      return Integer.class;
	    if (columnIndex == SequiturRulesTableColumns.SEQUITUR_RULE.ordinal())
	      return String.class;
	    if (columnIndex == SequiturRulesTableColumns.EXPANDED_SEQUITUR_RULE.ordinal())
	      return String.class;
	    if (columnIndex == SequiturRulesTableColumns.RULE_USE_FREQUENCY.ordinal())
	      return Integer.class;
	    if (columnIndex == SequiturRulesTableColumns.RULE_MEAN_LENGTH.ordinal())
	      return Integer.class;
	    if (columnIndex == SequiturRulesTableColumns.LENGTH.ordinal())
	      return String.class;
	    // if (columnIndex == SequiturTableColumns.RULE_INDEXES.ordinal())
	    // return String.class;
	*/
	    
	    return String.class;
	  }

	

}