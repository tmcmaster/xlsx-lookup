package au.id.mcmaster.apoi.tableadapter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

public class XLWorkbook {
	private Workbook workbook;
	private FormulaEvaluator evaluator;
	
	public XLWorkbook(Workbook workbook) {
		this.workbook = workbook;
		this.evaluator = workbook.getCreationHelper().createFormulaEvaluator();
	}
	
	public XLWorksheet getWorksheetAdapter(String sheetName) {
		Sheet worksheet = workbook.getSheet(sheetName);
		XLWorksheet worksheetAdapter = new XLWorksheet(worksheet, evaluator);
		return worksheetAdapter;
	}
	
}
