package au.id.mcmaster.apoi.tableadapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

public class XLWorksheet {
	private Sheet worksheet;
	private FormulaEvaluator evaluator;
	
	public XLWorksheet(Sheet worksheet, FormulaEvaluator evaluator) {
		this.worksheet = worksheet;
		this.evaluator = evaluator;
		flattenMergedCells();
	}
	
	private void flattenMergedCells() {
		// remove the merged cells, and duplicate the data
		while (worksheet.getNumMergedRegions() > 0) {
			CellRangeAddress region = worksheet.getMergedRegion(0);
			int rowNum = region.getFirstRow();
		    int colStart = region.getFirstColumn();
		    int colEnd = region.getLastColumn();
			Row row = worksheet.getRow(rowNum);
			Cell cell = row.getCell(colStart);
		    String value = getCellStringValue(cell);
		    
		    System.out.println("Merged: " + colStart + " : " + colEnd + " : " + value);
		    
		    worksheet.removeMergedRegion(0);
		    for (int j=colStart+1; j<=colEnd; j++) {
		        Cell myCell = row.getCell(j);
		        myCell.setCellValue(value);
		    }				
		}
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

//	public String[][] getColumnData(Rectangle table) {
//		//Rectangle table = tableDefinition.getColumnDataRectangle();
//		//Sheet worksheet = workbook.getSheet(tableDefinition.getWorksheetName());
//		String[][] columnDataRows = getData(worksheet, table);
//		return columnDataRows;
//	}
//
//	public String[][] getRowData() {
//		Rectangle table = tableDefinition.getRowDataRectangle();
//		Sheet worksheet = workbook.getSheet(tableDefinition.getWorksheetName());
//		String[][] columnDataRows = getData(worksheet, table);
//		return columnDataRows;
//	}
//
//	public String[][] getValueData() {
//		Rectangle table = tableDefinition.getValueDataRectangle();
//		Sheet worksheet = workbook.getSheet(tableDefinition.getWorksheetName());
//		String[][] columnDataRows = getData(worksheet, table);
//		return columnDataRows;
//	}

//	public List<String> getColumnDataTitles(Rectangle table) {
//		List<String> columnDataTitles = new ArrayList<String>();
//		columnDataTitles.add("ANB");
//		Rectangle table = tableDefinition.getColumnDataRectangle();
//		Sheet worksheet = workbook.getSheet(tableDefinition.getWorksheetName());
//		for (int r=table.getStartY(); r<=table.getEndY(); r++) {
//			Row row = worksheet.getRow(r);
//			for (int c=table.getStartX()-1; c<table.getStartX(); c++) {
//				Cell cell = row.getCell(c);
//				String value = getCellStringValue(cell);
//				columnDataTitles.add(value);
//			}
//		}
//		return columnDataTitles;
//	}
	
	public XLDataGrid getData(XLRectangle table) {
		int width = table.getWidth();
		int height = table.getHeight();
		String[][] columnDataRows = new String[height][width];
		for (int r=table.getStartY(), y=0; r<=table.getEndY(); r++,y++) {
			Row row = worksheet.getRow(r);
			for (int c=table.getStartX(),x=0; c<=table.getEndX(); c++,x++) {
				Cell cell = row.getCell(c);
				String value = getCellStringValue(cell);
				columnDataRows[y][x] = value;
			}
		}
		return new XLDataGrid(columnDataRows);
	}
}
