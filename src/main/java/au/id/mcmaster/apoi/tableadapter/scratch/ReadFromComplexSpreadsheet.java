package au.id.mcmaster.apoi.tableadapter.scratch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import au.id.mcmaster.apoi.tableadapter.XLTableDefinition;

public class ReadFromComplexSpreadsheet {
	private static final String FILE_NAME = "generated/MyFirstExcel.xlsx";

	public static void main(String[] args) {

		
		try {

			XLTableDefinition tableDefinition = new XLTableDefinition(FILE_NAME,"SecondTab","SecondTab",1,1,5,4,1,4,"grid",1);
			
			System.out.println("Column Data: " + tableDefinition.getColumnDataRectangle());
			System.out.println("   Row Data: " + tableDefinition.getRowDataRectangle());
			System.out.println(" Value Data: " + tableDefinition.getValueDataRectangle());
			
			
			FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));
			Workbook workbook = new XSSFWorkbook(excelFile);
			Sheet datatypeSheet = workbook.getSheetAt(1);

			flattenMergedCells(datatypeSheet);
			
			workbook.close();
			
			System.out.println();
			
			ArrayList<Row> headingRows = new ArrayList<Row>();
			int dataRowStart = -1;
			int dataRowEnd = -1;
			Iterator<Row> iterator = datatypeSheet.iterator();
			while (iterator.hasNext()) {
				Row currentRow = iterator.next();
				Iterator<Cell> cellIterator = currentRow.iterator();
				int first = currentRow.getFirstCellNum();
				if (first == 1) {
					System.out.print(String.format("%10.10s |",""));
					headingRows.add(currentRow);
					while (cellIterator.hasNext()) {
						Cell currentCell = cellIterator.next();
						CellType cellType = currentCell.getCellTypeEnum();
						currentCell.getColumnIndex();
						System.out.print(String.format("%10.10s |", (cellType == CellType.NUMERIC ? currentCell.getNumericCellValue() : currentCell.getStringCellValue())));
					}
				} else {
					currentRow.getFirstCellNum();
					if (dataRowStart == -1) {
						dataRowStart = currentRow.getRowNum();
					}
					dataRowEnd = currentRow.getRowNum();
					while (cellIterator.hasNext()) {
						Cell currentCell = cellIterator.next();
						CellType cellType = currentCell.getCellTypeEnum();
						currentCell.getColumnIndex();
						System.out.print(String.format("%10.10s |", (cellType == CellType.NUMERIC ? currentCell.getNumericCellValue() : currentCell.getStringCellValue())));
					}
				}
				System.out.println();
			}
			
			System.out.println("dataRowStart = " + dataRowStart);
			System.out.println("dataRowEnd = " + dataRowEnd);
			
			List<String> columnKeys = generateColumnKeys(datatypeSheet, 0, dataRowStart-1);
			columnKeys.stream().forEach(s -> System.out.println("Key: " + s));			
			
			for (int r=dataRowStart; r<=dataRowEnd; r++) {
				Row row = datatypeSheet.getRow(r);
				int rowKey = new Double(row.getCell(0).getNumericCellValue()).intValue();
				for (Cell cell : row) {
					if (cell.getColumnIndex() > 0) {
						int value = new Double(cell.getNumericCellValue()).intValue();
						for (String columnKey : columnKeys) {
							System.out.println(columnKey + rowKey + " = " + value);
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void flattenMergedCells(Sheet datatypeSheet) {
		// remove the merged cells, and duplicate the data
		while (datatypeSheet.getNumMergedRegions() > 0) {
			CellRangeAddress region = datatypeSheet.getMergedRegion(0);
		    int rowNum = region.getFirstRow();
		    int colStart = region.getFirstColumn();
		    int colEnd = region.getLastColumn();
			Row row = datatypeSheet.getRow(rowNum);
		    String value = row.getCell(colStart).getStringCellValue();
		    
		    System.out.println("Merged: " + colStart + " : " + colEnd + " : " + value);
		    
		    datatypeSheet.removeMergedRegion(0);
		    for (int j=colStart+1; j<=colEnd; j++) {
		        Cell myCell = row.getCell(j);
		        myCell.setCellValue(value);
		    }				
		}
	}

	private static List<String> generateColumnKeys(Sheet datatypeSheet, int headingRowStart, int headingRowEnd) {
		int headingCellStart = datatypeSheet.getRow(headingRowStart).getFirstCellNum();
		int headingCellEnd = datatypeSheet.getRow(headingRowStart).getLastCellNum();

		ArrayList<StringBuffer> columnKeys = new ArrayList<StringBuffer>();
		for (int c=headingCellStart; c<headingCellEnd; c++) {
			columnKeys.add(new StringBuffer());
		}
//		StringBuffer[] columnKeyBuffers = new StringBuffer[columnKeys.size()];
//		columnKeys.toArray(columnKeyBuffers);
		
		for (int r = headingRowStart; r<=headingRowEnd; r++) {
			Row row = datatypeSheet.getRow(r);
			for (int c=headingCellStart, k=0; c<=headingCellEnd; c++,k++) {
				Cell cell = row.getCell(c);
				if (cell != null) {
					String value = row.getCell(c).getStringCellValue();
					columnKeys.get(k).append(value);
					columnKeys.get(k).append("-");
				}
			}
		}
		
		return columnKeys.stream().map(b -> b.toString()).collect(Collectors.toList());
	}

}
