package au.id.mcmaster.apoi.tableadapter;

public class XLTableDefinition
{
	private String workbookName;
	private String worksheetName;
	private XLRectangle columnData = new XLRectangle();
	private XLRectangle rowData = new XLRectangle();
	private XLRectangle valueData = new XLRectangle();
	private XLRectangle titleData = new XLRectangle();
	
	public XLTableDefinition(String workbookName, String worksheetName, int tableStartX, int tableStartY, int columnDataWidth, int columnDataHeight, int rowDataWidth, int rowDataHeight) {
		this.workbookName = workbookName;
		this.worksheetName = worksheetName;
		
		this.titleData.setStartX(tableStartX - 1);
		this.titleData.setStartY(tableStartY - 1);
		this.titleData.setEndX(tableStartX - 1 + rowDataWidth - 1);
		this.titleData.setEndY(tableStartY - 1 + columnDataHeight - 1);
		
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
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("== " + workbookName + " : " + worksheetName + " ==\n");
		buffer.append("Title Data: " + titleData + "\n");
		buffer.append("Column Data: " + columnData + "\n");
		buffer.append("Row Data: " + rowData + "\n");
		buffer.append("Value Data: " + valueData + "\n");
		
		return buffer.toString();
	}

	public String getWorkbookName() {
		return this.workbookName;
	}
	
	public String getWorksheetName() {
		return this.worksheetName;
	}
	
	public XLRectangle getColumnDataRectangle() {
		return columnData;
	}

	public XLRectangle getRowDataRectangle() {
		return rowData;
	}

	public XLRectangle getValueDataRectangle() {
		return valueData;
	}
	
	public XLRectangle getTitleDataRectangle() {
		return titleData;
	}
}
