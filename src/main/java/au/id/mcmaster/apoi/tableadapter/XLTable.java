package au.id.mcmaster.apoi.tableadapter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * TODO: Need to support row data columns
 * 
 * @author Tim McMaster
 */
public class XLTable {

	private XLDataGrid columnData;
	private XLDataGrid rowData;
	private XLDataGrid valueData;
	private XLDataGrid columnTitles;
	
	public XLTable(XLTableDefinition tableDefinition) throws IOException
	{
		String fileName = tableDefinition.getWorkbookName();
		try (InputStream is = XLTable.class.getClassLoader().getResourceAsStream(fileName))
		{
			if (is == null) {
				throw new FileNotFoundException("Could not find the workbook: " + fileName);
			}
			XLWorkbook workbookAdapter = new XLWorkbook(new XSSFWorkbook(is));
			XLWorksheet worksheetAdapter = workbookAdapter.getWorksheetAdapter(tableDefinition.getWorksheetName());
			this.columnData = worksheetAdapter.getData(tableDefinition.getColumnDataRectangle());
			this.rowData = worksheetAdapter.getData(tableDefinition.getRowDataRectangle());
			this.valueData = worksheetAdapter.getData(tableDefinition.getValueDataRectangle());
			this.columnTitles = worksheetAdapter.getData(tableDefinition.getTitleDataRectangle());
		}
	}
	
	protected XLDataGrid getColumnDataGrid() {
		return this.columnData;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("== Column Data ==\n");
		buffer.append(columnData);
		buffer.append("== Row Data ==\n");
		buffer.append(rowData);
		buffer.append("== Value Data ==\n");
		buffer.append(valueData);
		buffer.append("== Title Data ==\n");
		buffer.append(columnTitles);
		
		return buffer.toString();
	}
	
	public List<String> getColumnDataTitles() {
		return getColumnDataTitles(false);
	}
	public List<String> getColumnDataTitles(boolean addANB) {
		String[] titleArray = columnTitles.getColumn(0);
		List<String> valueTitles = new ArrayList<String>();
		valueTitles.add("ANB");
		Collections.addAll(valueTitles, titleArray);
		return valueTitles;
	}
	
	public XLOptionTree getColumnsValuesOptionsTree() {
		XLOptionTree optionsTree = new XLOptionTree();
		for (int i=0; i<columnData.getNumberOfColumns(); i++)
		{
			String[] columnValues = columnData.getColumn(i);
			optionsTree.addOptions(columnValues);
		}
		return optionsTree;
	}
	
	/**
	 * Get a list of unique options for the row value, and options for each of the column header lines.
	 * 
	 * @return
	 */
	public Map<String,List<String>> getValueOptionsMap() {
		
		Map<String,List<String>> valueOptionsMap = new HashMap<String,List<String>>();
		
		List<String> valueTitles = getColumnDataTitles(true);
		
		// row data unique value options
		String rowDataLabel = valueTitles.get(0);
		
		List<String> uniqueRowData = rowData.getColumnUniqueList(0);
		valueOptionsMap.put(rowDataLabel, uniqueRowData);
		
		// column data unique value options
		for (int i=0; i<columnData.getNumberOfRows(); i++) {
			String columnDataLabel = valueTitles.get(i+1);
			List<String> uniqueColumnValues = columnData.getRowUniqueList(i);
			uniqueColumnValues.add("Undefined");
			valueOptionsMap.put(columnDataLabel, uniqueColumnValues);
		}
		
		return valueOptionsMap;
	}
	
	public Map<String,String> getValueMap() {
		Map<String,String> valueMap = new HashMap<String,String>();
		
		String[] columnKeys = columnData.joinColumnAll(" | ");
		System.out.println("Column Keys: " + columnKeys.length + " : " + Arrays.toString(columnKeys));
		
		String[] rowKeys = rowData.joinRowAll(" | ");
		System.out.println("Row Keys: " + rowKeys.length + " : " + Arrays.toString(rowKeys));
		
		for (int r=0; r<rowKeys.length; r++) {
			for (int c=0; c<columnKeys.length; c++) {
				String seperator = (columnKeys.length > 0 && rowKeys.length > 0 ? " | " : "");
				String key = String.format("%s%s%s", rowKeys[r], seperator, columnKeys[c]);
				String value = valueData.getValue(r,c);
				System.out.println(String.format("%10.10s = %s", value, key));
				valueMap.put(key, value);
			}
		}
		
		return valueMap;
	}
}
