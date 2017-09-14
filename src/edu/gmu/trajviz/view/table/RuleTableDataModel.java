package edu.gmu.trajviz.view.table;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.AbstractTableModel;

/**
 * 
 * Table Data Model for Grammar Rule Inducted
 * 
 * Build on the top of Manfred Lerner's code in GrammarViz
 * 
 * @author Qingzhe Li, Yifeng Gao
 * 
 */
public class RuleTableDataModel extends AbstractTableModel {

  /** Fancy serial. */
  private static final long serialVersionUID = -6894512220338171366L;

  protected final List<Object> rows;

  private String[] schema;

  /**
   * Constructor.
   */
  public RuleTableDataModel() {
    rows = new ArrayList<Object>();
  }

  /**
   * Add a new row.
   * 
   * @param item the new row;
   */
  public void addRow(Object[] item) {
    rows.add(item);
    fireTableDataChanged();
  }

  /**
   * Remove the row.
   * 
   * @param row the row index.
   */
  protected void removeRow(int row) {
    rows.remove(row);
    fireTableDataChanged();
  }

  /**
   * Get the row value.
   * 
   * @param row the row index.
   * @return the row value.
   */
  protected Object[] getRow(int row) {
    return (Object[]) rows.get(row);
  }

  /**
   * Update the row.
   * 
   * @param row the row index.
   * @param rowData the row data map.
   */
  protected void updateRow(int row, EnumMap<RuleTableColumns, Object> rowData) {
    Object[] changedItem = (Object[]) rows.get(row);
    for (Map.Entry<RuleTableColumns, Object> entry : rowData.entrySet()) {
      changedItem[entry.getKey().ordinal()] = entry.getValue();
    }
    fireTableRowsUpdated(row, row);
  }

  /**
   * Clear all the data.
   */
  protected void removeAllData() {
    rows.clear();
    fireTableDataChanged();
  }

  /**
   * Set the table schema.
   * 
   * @param schema the new schema.
   */
  public void setSchema(String[] schema) {
    this.schema = schema;
    fireTableStructureChanged();
  }

  @Override
  public void setValueAt(Object value, int row, int col) {
    Object[] changedItem = (Object[]) rows.get(row);
    changedItem[col] = value;
    fireTableRowsUpdated(row, row);
  }

  public Object getValueAt(int row, int column) {
    Object[] item = (Object[]) rows.get(row);
    return item[column];
  }

  @Override
  public String getColumnName(int index) {
    return schema[index];
  }

  public int getRowCount() {
    return rows.size();
  }

  public int getColumnCount() {
    return (schema == null) ? 0 : schema.length;
  }
}
