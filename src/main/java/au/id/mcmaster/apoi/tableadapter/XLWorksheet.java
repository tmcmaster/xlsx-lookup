package au.id.mcmaster.apoi.tableadapter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XLWorksheet {
	private static final Logger log = LoggerFactory.getLogger(XLWorksheet.class);
	
	private Sheet worksheet;
	private FormulaEvaluator evaluator;
	
	public XLWorksheet(Sheet worksheet, FormulaEvaluator evaluator) {
		this.worksheet = worksheet;
		this.evaluator = evaluator;
		flattenMergedCells();
	}
	
	private void flattenMergedCells() {
		log.debug("Flattening merged cells: " + worksheet.getSheetName());
		
		// remove the merged cells, and duplicate the data
		while (worksheet.getNumMergedRegions() > 0) {
			CellRangeAddress region = worksheet.getMergedRegion(0);
		    int colStart = region.getFirstColumn();
		    int colEnd = region.getLastColumn();
		    int rowStart = region.getFirstRow();
		    int rowEnd = region.getLastRow();
		    
			Row templateRow = worksheet.getRow(rowStart);
			Cell cell = templateRow.getCell(colStart);
		    String value = getCellStringValue(cell);
		    
		    log.debug("Merged: " + colStart + " : " + colEnd + " : " + value);
		    
		    worksheet.removeMergedRegion(0);
		    for (int r=rowStart; r<=rowEnd; r++) {
		    	Row row = worksheet.getRow(r);
			    for (int j=colStart; j<=colEnd; j++) {
			        Cell myCell = row.getCell(j);
			        myCell.setCellValue(value);
			    }				
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

	public XLDataGrid getData(XLRectangle table) {
		if (table.isNull()) {
			return XLDataGrid.EMPTY;
		}
		int numberOfRows = worksheet.getLastRowNum();
		if (table.getEndY() > numberOfRows) {
			throw new IndexOutOfBoundsException(String.format("Rectangle falls outside the sheet: %s(%d): %s", worksheet.getSheetName(), numberOfRows, table));
		}
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
