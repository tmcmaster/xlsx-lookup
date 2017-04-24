package au.id.mcmaster.apoi.tableadapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
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

public class TableAdapter {
	private TableDefinition tableDefinition;
	private Workbook workbook;
	private FormulaEvaluator evaluator;
	
	public TableAdapter(TableDefinition tableDefinition) throws IOException {
		this.tableDefinition = tableDefinition;
		String fileName = tableDefinition.getWorkbookName();
		InputStream is = TableAdapter.class.getClassLoader().getResourceAsStream(fileName);
		this.workbook = new XSSFWorkbook(is);
		this.evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		flattenMergedCells();
	}

	public void test() {
		Sheet worksheet = workbook.getSheetAt(2);
		flattenMergedCells();
		Row row = worksheet.getRow(10);
		Cell cell = row.getCell(3);
		//System.out.println(cell.getNumericCellValue());
		System.out.println(cell.getStringCellValue());
	}
	
	/**
	 * Get a list of unique options for the row value, and options for each of the column header lines.
	 * 
	 * @return
	 */
	public Map<String,Collection<String>> getValueOptionsMap() {
		
		Map<String,Collection<String>> valueOptionsMap = new HashMap<String,Collection<String>>();
		
		// get the labels for all of the input data
		List<String> valueTitles = getColumnDataTitles();

		// row data unique value options
		String rowDataLabel = valueTitles.get(0);
		String[][] rowData = getRowData();
		String[] rowDataValues = getColumnOfData(rowData,0);
		Collection<String> uniqueRowData = uniqueList(rowDataValues);
		valueOptionsMap.put(rowDataLabel, uniqueRowData);
		
		// column data unique value options
		String[][] columnData = getColumnData();
		for (int i=0; i<columnData.length; i++) {
			String columnDataLabel = valueTitles.get(i+1);
			Collection<String> uniqueColumnValues = uniqueList(columnData[i]);
			valueOptionsMap.put(columnDataLabel, uniqueColumnValues);
		}
		
		return valueOptionsMap;
	}
	
	public String[] getColumnOfData(String[][] data, int column) {
		String[] columnData = new String[data.length];
		for (int i=0; i<data.length; i++) {
			columnData[i] = data[i][column];
		}
		return columnData;
	}
	
	public Collection<String> uniqueList(String[] data) {
		Set<String> set = new HashSet<String>();
		set.add("Undefined");
		for (String item : data) {
			set.add(item);
		}
		return set;
	}
	
	public Map<String,String> getValueMap() {
		Map<String,String> valueMap = new HashMap<String,String>();
		
		String[][] columnData = getColumnData();
		String[] columnKeys = generateColumnKeys(columnData);
		System.out.println("Column Keys: " + columnKeys.length);
		
		String[][] rowData = getRowData();
		String[] rowKeys = generateRowKeys(rowData);
		System.out.println("Row Keys: " + rowKeys.length);
		
		String[][] valueData = getValueData();
		for (int r=0; r<rowKeys.length; r++) {
			for (int c=0; c<columnKeys.length; c++) {
				String key = String.format("%s | %s", rowKeys[r], columnKeys[c]);
				String value = valueData[r][c];
				System.out.println(String.format("%10.10s = %s", value, key));
				valueMap.put(key, value);
			}
		}
		return valueMap;
	}
	
	private String[] generateRowKeys(String[][] rowData) {
		String[] keys = new String[rowData.length];
		for (int r=0; r<rowData.length; r++) {
			StringBuffer stringBuffer = new StringBuffer();
			for (int c=0; c<rowData[r].length; c++) {
				if (c > 0) {
					stringBuffer.append(" | ");
				}
				String value = rowData[r][c];
				stringBuffer.append(value);
			}
			keys[r] = stringBuffer.toString();
		}
		return keys;
	}

	private String[] generateColumnKeys(String[][] columnData) {
		String[] keys = new String[columnData[0].length];

		ArrayList<StringBuffer> columnKeys = new ArrayList<StringBuffer>();
		for (int c=0; c<keys.length; c++) {
			columnKeys.add(new StringBuffer());
		}
		
		for (int r=0; r<columnData.length; r++) {
			String[] cells = columnData[r];

			for (int i=0; i<cells.length; i++) {
				if (r > 0) {
					columnKeys.get(i).append(" | ");
				}
				String value = cells[i];
				columnKeys.get(i).append(value);
			}
		}
		columnKeys.stream().map(b -> b.toString()).collect(Collectors.toList()).toArray(keys);
		return keys;
	}

	public String[][] getColumnData() {
		Rectangle table = tableDefinition.getColumnDataRectangle();
		Sheet worksheet = workbook.getSheet(tableDefinition.getWorksheetName());
		String[][] columnDataRows = getData(worksheet, table);
		return columnDataRows;
	}

	public String[][] getRowData() {
		Rectangle table = tableDefinition.getRowDataRectangle();
		Sheet worksheet = workbook.getSheet(tableDefinition.getWorksheetName());
		String[][] columnDataRows = getData(worksheet, table);
		return columnDataRows;
	}

	public String[][] getValueData() {
		Rectangle table = tableDefinition.getValueDataRectangle();
		Sheet worksheet = workbook.getSheet(tableDefinition.getWorksheetName());
		String[][] columnDataRows = getData(worksheet, table);
		return columnDataRows;
	}

	public List<String> getColumnDataTitles() {
		List<String> columnDataTitles = new ArrayList<String>();
		columnDataTitles.add("ANB");
		Rectangle table = tableDefinition.getColumnDataRectangle();
		Sheet worksheet = workbook.getSheet(tableDefinition.getWorksheetName());
		for (int r=table.getStartY(); r<=table.getEndY(); r++) {
			Row row = worksheet.getRow(r);
			for (int c=table.getStartX()-1; c<table.getStartX(); c++) {
				Cell cell = row.getCell(c);
				String value = getCellStringValue(cell);
				columnDataTitles.add(value);
			}
		}
		return columnDataTitles;
	}
	
	private String[][] getData(Sheet worksheet, Rectangle table) {
		String[][] columnDataRows = new String[table.getHeight()][table.getWidth()];
		for (int r=table.getStartY(), y=0; r<=table.getEndY(); r++,y++) {
			Row row = worksheet.getRow(r);
			for (int c=table.getStartX(),x=0; c<=table.getEndX(); c++,x++) {
				Cell cell = row.getCell(c);
				String value = getCellStringValue(cell);
				columnDataRows[y][x] = value;
			}
		}
		return columnDataRows;
	}
	
	private String getCellStringValue(Cell cell) {
		if (cell == null) {
			return null;
		}
		else
		{
			CellValue cellValue = evaluator.evaluate(cell);
			if (cellValue == null) {
				CellType cellType = cell.getCellTypeEnum();
				//System.out.println(cellType);
				String value = (cellType == CellType.NUMERIC ? new Integer(new Double(cell.getNumericCellValue()).intValue()).toString() : cell.getStringCellValue());		
				return value;
				
			}
			else
			{
			    switch (cellValue.getCellType()) {
			        case Cell.CELL_TYPE_BOOLEAN:
			            return new Boolean(cellValue.getBooleanValue()).toString();
			        case Cell.CELL_TYPE_NUMERIC:
			        	return new Integer(new Double(cellValue.getNumberValue()).intValue()).toString();
			        case Cell.CELL_TYPE_STRING:
			            return cellValue.getStringValue();
			    }
			    return cellValue.toString();
			}
		    
		}
	}
	
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
	
	public void flattenMergedCells() {
		String workbookName = tableDefinition.getWorkbookName();
		System.out.println("Workbook Name: " + workbookName);
		String sheetName = tableDefinition.getWorksheetName();
		System.out.println("Worksheet Name: " + sheetName);
		Sheet datatypeSheet = workbook.getSheet(sheetName);
		
		// remove the merged cells, and duplicate the data
		while (datatypeSheet.getNumMergedRegions() > 0) {
			CellRangeAddress region = datatypeSheet.getMergedRegion(0);
			int rowNum = region.getFirstRow();
		    int colStart = region.getFirstColumn();
		    int colEnd = region.getLastColumn();
			Row row = datatypeSheet.getRow(rowNum);
			Cell cell = row.getCell(colStart);
		    String value = getCellStringValue(cell);
		    
		    System.out.println("Merged: " + colStart + " : " + colEnd + " : " + value);
		    
		    datatypeSheet.removeMergedRegion(0);
		    for (int j=colStart+1; j<=colEnd; j++) {
		        Cell myCell = row.getCell(j);
		        myCell.setCellValue(value);
		    }				
		}
	}
}
