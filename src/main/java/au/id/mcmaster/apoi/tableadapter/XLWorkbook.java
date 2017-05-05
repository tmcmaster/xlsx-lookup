package au.id.mcmaster.apoi.tableadapter;

import java.io.IOException;

import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

public class XLWorkbook {
	private Workbook workbook;
	private FormulaEvaluator evaluator;
	
	public XLWorkbook(Workbook workbook) {
		this.workbook = workbook;
		this.evaluator = workbook.getCreationHelper().createFormulaEvaluator();
	}
	
	public XLWorksheet getWorksheetAdapter(String sheetName) {
		Sheet worksheet = workbook.getSheet(sheetName);
		if (worksheet == null) {
			throw new ResourceNotFoundException("Could not find the requested WorkSheet: " + sheetName);
		}
		XLWorksheet worksheetAdapter = new XLWorksheet(worksheet, evaluator);
		return worksheetAdapter;
	}
	
	public void close() {
		try {
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
