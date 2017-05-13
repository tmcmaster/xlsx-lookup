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
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

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
	private XLDataGrid rowTitles;	
	private String type;
	int version;
	private String name;
	
	public XLTable(XLTableDefinition tableDefinition) throws IOException
	{
		this.type = tableDefinition.getType();
		this.version = tableDefinition.getVersion();
		this.name = tableDefinition.getTableName();
		
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
			this.rowTitles = getRowTitles(worksheetAdapter, tableDefinition);
		}
	}
	
	private XLDataGrid getRowTitles(XLWorksheet worksheetAdapter, XLTableDefinition tableDefinition) {
		if ("grid".equals(tableDefinition.getType()))
		{
			if (tableDefinition.getVersion() == 1) {
				return new XLDataGrid(new String[][] {new String[] {"ANB"}});
				
			} else {
				return worksheetAdapter.getData(tableDefinition.getRowTitleDataRectangle());				
			}
		}
		else if ("lookup".equals(tableDefinition.getType()))
		{
			return worksheetAdapter.getData(tableDefinition.getRowTitleDataRectangle());
		}
		
		throw new ResourceNotFoundException("Table type is not supported: " + tableDefinition.getType() + ":" + tableDefinition.getVersion());
	}

	protected XLDataGrid getColumnDataGrid() {
		return this.columnData;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("=> " + name + "(" + type + ") <=\n");
		buffer.append("== Column Data ==\n");
		buffer.append(columnData);
		buffer.append("== Row Data ==\n");
		buffer.append(rowData);
		buffer.append("== Value Data ==\n");
		buffer.append(valueData);
		buffer.append("== Column Titles ==\n");
		buffer.append(columnTitles);
		buffer.append("== Row Titles ==\n");
		buffer.append(rowTitles);
		
		return buffer.toString();
	}
	
	public List<String> getFieldList() {
		List<String> fields = new ArrayList<String>();
		
		fields.addAll(getColumnDataTitles());
		fields.addAll(getRowDataTitles());
		
		return fields;
	}
	
	public List<String> getColumnDataTitles() {
		String[] titleArray = columnTitles.getColumn(0);
		List<String> valueTitles = new ArrayList<String>();
		Collections.addAll(valueTitles, titleArray);
		return valueTitles;
	}
	
	public List<String> getRowDataTitles() {
		String[] titleArray = rowTitles.getRow(0);
		List<String> rowTitles = new ArrayList<String>();
		Collections.addAll(rowTitles, titleArray);
		return rowTitles;
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
	
	public XLValueTree getValueTree() {
		XLValueTree valueTree = new XLValueTree();
		int combinedSize = columnData.getNumberOfRows()+rowData.getNumberOfColumns();
		String[] combinedValues = new String[combinedSize];
		for (int c=0; c<columnData.getNumberOfColumns(); c++)
		{
			String[] columnValues = columnData.getColumn(c);
			System.arraycopy(columnValues, 0, combinedValues, 0, columnValues.length);
			for (int r=0; r<rowData.getNumberOfRows(); r++)
			{
				String value = valueData.getValue(r, c);
				String[] rowValues = rowData.getRow(r);
				System.arraycopy(rowValues, 0, combinedValues, columnValues.length, rowValues.length);
				valueTree.setValue(combinedValues, value);
			}
		}
		
		return valueTree;
	}
	
	/**
	 * Get a list of unique options for the row value, and options for each of the column header lines.
	 * 
	 * @return
	 */
	public Map<String,List<String>> getValueOptionsMap() {
		
		Map<String,List<String>> valueOptionsMap = new HashMap<String,List<String>>();
		
		// column data unique value options
		List<String> rowDataTitles = getRowDataTitles();
		for (int i=0; i<rowDataTitles.size(); i++) {
			String rowDataLabel = rowDataTitles.get(i);
			List<String> uniqueRowValues = rowData.getColumnUniqueList(i);
			valueOptionsMap.put(rowDataLabel, uniqueRowValues);
		}
		
		// column data unique value options
		List<String> columnDataTitles = getColumnDataTitles();
		for (int i=0; i<columnDataTitles.size(); i++) {
			String columnDataLabel = columnDataTitles.get(i);
			List<String> uniqueColumnValues = columnData.getRowUniqueList(i);
			//uniqueColumnValues.add("Undefined");
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public XLDataGrid getColumnData() {
		return columnData;
	}

	public XLDataGrid getRowData() {
		return rowData;
	}

	public XLDataGrid getValueData() {
		return valueData;
	}

	public XLDataGrid getColumnTitles() {
		return columnTitles;
	}
}
