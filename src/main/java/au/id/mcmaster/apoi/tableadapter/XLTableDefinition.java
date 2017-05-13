package au.id.mcmaster.apoi.tableadapter;

public class XLTableDefinition
{
	public static final XLTableDefinition DEFINITION_TABLE = new XLTableDefinition(null,"Definitions","Definitions","Definitions",1,2,11,2,1,25,"grid",1);

	private String workbookName;
	private String worksheetName;
	private String tableName;
	private String tableAlias;
	private String type;
	private int version;
	
	private XLRectangle columnData = new XLRectangle();
	private XLRectangle rowData = new XLRectangle();
	private XLRectangle valueData = new XLRectangle();
	private XLRectangle titleData = new XLRectangle(); // column titles
	private XLRectangle rowTitles = new XLRectangle();

	public XLTableDefinition(String workbookName, String worksheetName, String tableName, String tableAlias,
			int tableStartX, int tableStartY, int columnDataWidth, int columnDataHeight, 
			int rowDataWidth, int rowDataHeight, String type, int version) {
		this.type = type;
		this.version = version;
		init(workbookName, worksheetName, tableName, tableAlias,
				tableStartX, tableStartY, columnDataWidth, columnDataHeight, 
				rowDataWidth, rowDataHeight, type, version);
	}
	
	public XLTableDefinition(String fileName, String... defintionData) {
		String workbookName = fileName;
		String tableName = defintionData[0];
		String tableAlias = defintionData[1];
		String worksheetName = defintionData[2];
		int tableStartX = new Integer(defintionData[3]);
		int tableStartY = Double.valueOf(defintionData[4]).intValue();
		int columnDataWidth = Double.valueOf(defintionData[5]).intValue();
		int columnDataHeight = Double.valueOf(defintionData[6]).intValue();
		int rowDataWidth = Double.valueOf(defintionData[7]).intValue();
		int rowDataHeight = Double.valueOf(defintionData[8]).intValue();
		String type = defintionData[9];
		int version = Double.valueOf(defintionData[10]).intValue();
		
		init(workbookName, worksheetName, tableName, tableAlias,
				tableStartX, tableStartY, columnDataWidth, columnDataHeight, 
				rowDataWidth, rowDataHeight, type, version);
	}
	
	private void init(String workbookName, String worksheetName, String tableName, String tableAlias, 
			int tableStartX, int tableStartY, int columnDataWidth, int columnDataHeight, 
			int rowDataWidth, int rowDataHeight, String type, int version) {
		this.workbookName = workbookName;
		this.worksheetName = worksheetName;
		this.tableName = tableName;
		this.tableAlias = tableAlias;
		this.type = type;
		this.version = version;
		String gridType = type + "/" + version;
		if ("grid/1".equals(gridType)) {
			initGrid1(tableStartX, tableStartY,columnDataWidth, columnDataHeight, rowDataWidth, rowDataHeight);
		} else if ("grid/2".equals(gridType)) {
			initGrid2(tableStartX, tableStartY,columnDataWidth, columnDataHeight, rowDataWidth, rowDataHeight);
		} else if ("lookup/1".equals(gridType)) {
			initLookup1(tableStartX, tableStartY,columnDataWidth, columnDataHeight, rowDataWidth, rowDataHeight);
		} else {
			throw new RuntimeException(String.format("Not a supported table type/version: %s/%d", type, version));
		}
	}
		
	private void initGrid1(int tableStartX, int tableStartY, int columnDataWidth, int columnDataHeight, int rowDataWidth, int rowDataHeight) {
		// define the titles for the column data
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

	private void initGrid2(int tableStartX, int tableStartY, int columnDataWidth, int columnDataHeight, int rowDataWidth, int rowDataHeight) {
		// define the titles for the column data
		this.titleData.setStartX(tableStartX - 1 + rowDataWidth - 1);
		this.titleData.setStartY(tableStartY - 1);
		this.titleData.setEndX(tableStartX - 1 + rowDataWidth - 1);
		this.titleData.setEndY(tableStartY - 1 + columnDataHeight - 1);
		
		//define the row titles
		this.rowTitles.setStartX(tableStartX - 1);
		this.rowTitles.setStartY(tableStartY - 1 + columnDataHeight);
		this.rowTitles.setEndX(tableStartX - 1 + rowDataWidth - 1);
		this.rowTitles.setEndY(tableStartY - 1 + columnDataHeight);
		
		// define the column data rectangle
		this.columnData.setStartX(tableStartX - 1 + rowDataWidth + 1);
		this.columnData.setStartY(tableStartY - 1);
		this.columnData.setEndX(tableStartX - 1 + rowDataWidth + columnDataWidth);
		this.columnData.setEndY(tableStartY - 1 + columnDataHeight - 1);
		
		//define the row data rectangle
		this.rowData.setStartX(tableStartX - 1);
		this.rowData.setStartY(tableStartY - 1 + columnDataHeight + 1);
		this.rowData.setEndX(tableStartX - 1 + rowDataWidth - 1);
		this.rowData.setEndY(tableStartY - 1 + columnDataHeight + rowDataHeight);
		
		//define the value data rectangle
		this.valueData.setStartX(tableStartX - 1 + rowDataWidth + 1);
		this.valueData.setStartY(tableStartY - 1 + columnDataHeight + 1);
		this.valueData.setEndX(tableStartX - 1 + rowDataWidth + columnDataWidth);
		this.valueData.setEndY(tableStartY - 1 + columnDataHeight + rowDataHeight);	
	}

	private void initLookup1(int tableStartX, int tableStartY, int columnDataWidth, int columnDataHeight, int rowDataWidth, int rowDataHeight) {
		// define the titles for the column data
//		this.titleData.setStartX(tableStartX - 1);
//		this.titleData.setStartY(tableStartY - 1);
//		this.titleData.setEndX(tableStartX - 1 + rowDataWidth - 1);
//		this.titleData.setEndY(tableStartY - 1 + columnDataHeight - 1);
		
		this.rowTitles.setStartX(tableStartX - 1);
		this.rowTitles.setStartY(tableStartY - 1);
		this.rowTitles.setEndX(tableStartX - 1 + rowDataWidth - 1);
		this.rowTitles.setEndY(tableStartY - 1 + columnDataHeight - 1);

		// define the column data rectangle
//		this.columnData.setStartX(tableStartX - 1 + rowDataWidth);
//		this.columnData.setStartY(tableStartY - 1);
//		this.columnData.setEndX(tableStartX - 1 + rowDataWidth + columnDataWidth - 1);
//		this.columnData.setEndY(tableStartY - 1 + columnDataHeight - 1);
		
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

	public XLRectangle getRowTitleDataRectangle() {
		return rowTitles;
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

	public String getTableAlias() {
		return tableAlias;
	}

	public void setTableAlias(String tableAlias) {
		this.tableAlias = tableAlias;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}
