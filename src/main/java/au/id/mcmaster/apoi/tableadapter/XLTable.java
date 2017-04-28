package au.id.mcmaster.apoi.tableadapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
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
	private XLDataGrid titleData;
	
	
	public XLTable(XLTableDefinition tableDefinition) throws IOException
	{
		String fileName = tableDefinition.getWorkbookName();
		try (InputStream is = XLTable.class.getClassLoader().getResourceAsStream(fileName))
		{
			XLWorkbook workbookAdapter = new XLWorkbook(new XSSFWorkbook(is));
			XLWorksheet worksheetAdapter = workbookAdapter.getWorksheetAdapter(tableDefinition.getWorksheetName());
			this.columnData = worksheetAdapter.getData(tableDefinition.getColumnDataRectangle());
			this.rowData = worksheetAdapter.getData(tableDefinition.getRowDataRectangle());
			this.valueData = worksheetAdapter.getData(tableDefinition.getValueDataRectangle());
			this.titleData = worksheetAdapter.getData(tableDefinition.getTitleDataRectangle());
		}
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
		buffer.append(titleData);
		
		return buffer.toString();
	}
	
	public List<String> getColumnDataTitles() {
		String[] titleArray = titleData.getColumn(0);
		List<String> valueTitles = new ArrayList<String>();
		valueTitles.add("ANB");
		Collections.addAll(valueTitles, titleArray);
		return valueTitles;
	}
	
	/**
	 * Get a list of unique options for the row value, and options for each of the column header lines.
	 * 
	 * @return
	 */
	public Map<String,Collection<String>> getValueOptionsMap() {
		
		Map<String,Collection<String>> valueOptionsMap = new HashMap<String,Collection<String>>();
		
		List<String> valueTitles = getColumnDataTitles();
		
		//String[] valueTitles = this.titleData.getColumn(0);

		// row data unique value options
		String rowDataLabel = valueTitles.get(0);
		
		Collection<String> uniqueRowData = rowData.getColumnUniqueList(0);
		valueOptionsMap.put(rowDataLabel, uniqueRowData);
		
		// column data unique value options
		for (int i=0; i<columnData.getNumberOfRows(); i++) {
			String columnDataLabel = valueTitles.get(i+1);
			Collection<String> uniqueColumnValues = columnData.getRowUniqueList(i);
			valueOptionsMap.put(columnDataLabel, uniqueColumnValues);
		}
		
		return valueOptionsMap;
	}
	
//	private static List<String> valueTitles(String[][] titleData) {
//		List<String> valueTitles = new ArrayList<String>();
//		for (String[] row : titleData) {
//			valueTitles.add(row[0]);
//		}
//		return valueTitles;
//	}
	
	public String[] getColumnOfData(String[][] data, int column) {
		String[] columnData = new String[data.length];
		for (int i=0; i<data.length; i++) {
			columnData[i] = data[i][column];
		}
		return columnData;
	}
	
	public Collection<String> uniqueList(String[] data) {
		List<String> results = new ArrayList<String>();
		Set<String> set = new HashSet<String>();
		for (String item : data) {
			if (!set.contains(item))
			{
				set.add(item);
				results.add(item);
			}
		}
		//results.sort((p1, p2) -> p1.compareTo(p2));
		//Collections.reverse(results);
		results.add("Undefined");
		//Collections.reverse(results);
		return results;
	}
	
	public Map<String,String> getValueMap() {
		Map<String,String> valueMap = new HashMap<String,String>();
		
		String[] columnKeys = columnData.joinColumnAll(" | ");
		System.out.println("Column Keys: " + columnKeys.length);
		
		String[] rowKeys = rowData.joinColumnAll(" | ");
		System.out.println("Row Keys: " + rowKeys.length);
		
		for (int r=0; r<rowKeys.length; r++) {
			for (int c=0; c<columnKeys.length; c++) {
				String key = String.format("%s | %s", rowKeys[r], columnKeys[c]);
				String value = valueData.getValue(r,c);
				System.out.println(String.format("%10.10s = %s", value, key));
				valueMap.put(key, value);
			}
		}
		
		return valueMap;
	}
	
//	private static String[] generateRowKeys(String[][] rowData) {
//		String[] keys = new String[rowData.length];
//		for (int r=0; r<rowData.length; r++) {
//			StringBuffer stringBuffer = new StringBuffer();
//			for (int c=0; c<rowData[r].length; c++) {
//				if (c > 0) {
//					stringBuffer.append(" | ");
//				}
//				String value = rowData[r][c];
//				stringBuffer.append(value);
//			}
//			keys[r] = stringBuffer.toString();
//		}
//		return keys;
//	}
//
//	private static String[] generateColumnKeys(String[][] columnData) {
//		String[] keys = new String[columnData[0].length];
//
//		ArrayList<StringBuffer> columnKeys = new ArrayList<StringBuffer>();
//		for (int c=0; c<keys.length; c++) {
//			columnKeys.add(new StringBuffer());
//		}
//		
//		for (int r=0; r<columnData.length; r++) {
//			String[] cells = columnData[r];
//
//			for (int i=0; i<cells.length; i++) {
//				if (r > 0) {
//					columnKeys.get(i).append(" | ");
//				}
//				String value = cells[i];
//				columnKeys.get(i).append(value);
//			}
//		}
//		columnKeys.stream().map(b -> b.toString()).collect(Collectors.toList()).toArray(keys);
//		return keys;
//	}


	

	
	public void deleteHiddenColumns() {
//		Sheet sheet = workbook.getSheetAt(tableDefinition.getWorksheet());
//			
//		Row row = sheet.getRow(0);
//		
//        java.util.Iterator<Row> rowIter = sheet.iterator();
//        while(rowIter.hasNext())
//        {
//            Row row= rowIter.next();
//            XSSFCell cell=row.getCell(5);
//            row.removeCell(cell);
//        }
	}

//	public List<String> getColumnDataTitles() {
//		// get the labels for all of the input data
//		Rectangle titleDataRectangle = this.tableDefinition.getTitleDataRectangle();
//		String[][] titleData = worksheetAdapter.getData(titleDataRectangle);
//		String[] titleArray = getColumnOfData(titleData, 0);
//		List<String> valueTitles = new ArrayList<String>();
//		Collections.addAll(valueTitles, titleArray);
//		return valueTitles;
//	}
}
