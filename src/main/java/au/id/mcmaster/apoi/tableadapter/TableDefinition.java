package au.id.mcmaster.apoi.tableadapter;

public class TableDefinition
{
	private String workbookName;
	private String worksheetName;
	private Rectangle columnData = new Rectangle();
	private Rectangle rowData = new Rectangle();
	private Rectangle valueData = new Rectangle();
	
	public TableDefinition(String workbookName, String worksheetName, int tableStartX, int tableStartY, int columnDataWidth, int columnDataHeight, int rowDataWidth, int rowDataHeight) {
		this.workbookName = workbookName;
		this.worksheetName = worksheetName;
		
		// define the column data rectangle
		this.columnData.setStartX(tableStartX - 1 + rowDataWidth);
		this.columnData.setStartY(tableStartY - 1);
		this.columnData.setEndX(tableStartX - 1 + rowDataWidth + columnDataWidth - 1);
		this.columnData.setEndY(tableStartY - 1 + columnDataHeight - 1);
		
		//define the row data rectangle
		this.rowData.setStartX(tableStartX - 1);
		this.rowData.setStartY(tableStartY - 1 + columnDataHeight);
		this.rowData.setEndX(tableStartX - 1 + rowDataWidth - 1);
		this.rowData.setEndY(tableStartY - 1 + columnDataHeight + rowDataHeight - 1);
		
		//define the value data rectangle
		this.valueData.setStartX(tableStartX - 1 + rowDataWidth);
		this.valueData.setStartY(tableStartY - 1 + columnDataHeight);
		this.valueData.setEndX(tableStartX - 1 + rowDataWidth + columnDataWidth - 1);
		this.valueData.setEndY(tableStartY - 1 + columnDataHeight + rowDataHeight - 1);
		
	}

	public String getWorkbookName() {
		return this.workbookName;
	}
	
	public String getWorksheetName() {
		return this.worksheetName;
	}
	
	public Rectangle getColumnDataRectangle() {
		return columnData;
	}

	public Rectangle getRowDataRectangle() {
		return rowData;
	}

	public Rectangle getValueDataRectangle() {
		return valueData;
	}
}
