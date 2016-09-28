package edu.gmu.trajviz.view.table;
/**
 * Enum for the columns in Sequitur rules JTable.
 * 
 * @author Manfred Lerner, seninp
 * 
 */
public enum SequiturTableColumns {
	
	 
	  RULE_NUMBER("R#"),
	 // RULE_LEVEL("Level"), 
	  //RULE_FREQUENCY("Frequency in R0"), 
	  //SEQUITUR_RULE("Rule"), 
	  //EXPANDED_SEQUITUR_RULE("Expanded Rule"),
	  RULE_USE_FREQUENCY("Used"),
	  LENGTH("Length");
	 // RULE_MEAN_LENGTH("Mean length"),
	 // LENGTH("Min-max length");
	  // RULE_INDEXES("String positions");

	  private final String columnName;

	  SequiturTableColumns(String columnName) {
	    this.columnName = columnName;
	  }

	  public String getColumnName() {
	    return columnName;
	  }
}
