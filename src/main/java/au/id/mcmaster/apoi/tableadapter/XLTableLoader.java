package au.id.mcmaster.apoi.tableadapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XLTableLoader {
	private static final Logger log = LoggerFactory.getLogger(XLTableLoaderTest.class);
	
	private Map<String,XLTableDefinition> tableDefinitions = new HashMap<String,XLTableDefinition>();
	private Map<String,XLTable> tables = new HashMap<String,XLTable>();

	public XLTableLoader(String... spreadsheetFileNames)
	{
		try {
			this.tableDefinitions = loadTables(spreadsheetFileNames);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Collection<String> getTableNames() {
		return tableDefinitions.keySet();
	}
	
	public Map<String,XLTableDefinition> loadTables(String... fileNames) throws IOException
	{
		log.info("Loading tables");
		
		Map<String,XLTableDefinition> tableDefinitionMap = new HashMap<String,XLTableDefinition>();
		
		for (String fileName : fileNames) 
		{
			log.info("Loading tables from workbook: " + fileName);
			
			XLDataGrid valueData = getTableDefinitionData(fileName);
			log.debug("-->> Table Definition Data:\n" + valueData);
			for (int i=0; i<valueData.getNumberOfRows(); i++) {
				String[] row = valueData.getRow(i);
				String tableName = row[0];
				if (tableName != null && tableName.trim().length() > 0 && !tableName.startsWith("#")) {
					XLTableDefinition tableDefinition = new XLTableDefinition(fileName, row);
					log.debug("  -->> Added a table definition: " + tableName);
					tableDefinitionMap.put(tableName, tableDefinition);
				}
			}
		}

		log.info("Tables have been loaded.");

		return tableDefinitionMap;
	}

	public XLTable getTable(String tableName) {
		XLTable table = tables.get(tableName);
		
		if (table == null) {
			try
			{
				XLTableDefinition tableDefinition = this.tableDefinitions.get(tableName);
				log.debug("About to load table: " + tableDefinition);
				table = new XLTable(tableDefinition);
				tables.put(tableName, table);
			}
			catch (Exception e)
			{
				throw new RuntimeException("Could not load required value data: " + tableName, e);
			}
			
		}
		
		return table;
	}
	
	private XLDataGrid getTableDefinitionData(String fileName) throws IOException {
		
		try (InputStream is = XLTable.class.getClassLoader().getResourceAsStream(fileName))
		{
			if (is == null) {
				throw new IOException("Could not load TableDefinition: " + fileName);
			}
			XLWorkbook workbookAdapter = new XLWorkbook(new XSSFWorkbook(is));
			XLWorksheet worksheetAdapter = workbookAdapter.getWorksheetAdapter(XLTableDefinition.DEFINITION_TABLE.getWorksheetName());
			XLDataGrid valueData = worksheetAdapter.getData(XLTableDefinition.DEFINITION_TABLE.getValueDataRectangle());
			workbookAdapter.close();
			return valueData;
		}
	}
}
