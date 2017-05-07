package au.id.mcmaster.apoi.tableadapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

public class XLTableLoader {
	private static final Logger log = LoggerFactory.getLogger(XLTableLoaderTest.class);
	
	private Map<String,XLTableDefinition> tableDefinitions = new HashMap<String,XLTableDefinition>();
	private Map<String,XLTable> tables = new HashMap<String,XLTable>();
	private Map<String,XLTable> lookups = new HashMap<String,XLTable>();
	private Map<String,String> aliases = new HashMap<String,String>();
	
	public XLTableLoader(String... spreadsheetFileNames)
	{
		try {
			loadTables(spreadsheetFileNames);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Collection<String> getTableNames() {
		return tables.keySet();
	}
	
	public Collection<String> getLookupNames() {
		return lookups.keySet();
	}
	
	private void loadTables(String... fileNames) throws IOException
	{
		log.info("Loading tables");
		
		//Map<String,XLTableDefinition> tableDefinitionMap = new HashMap<String,XLTableDefinition>();
		
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
					tableDefinitions.put(tableName, tableDefinition);
					createTable(tableName, tableDefinition.getType());
				}
			}
		}

		log.info("Tables have been loaded.");

		//return tableDefinitionMap;
	}

	public XLTable getTable(String tableName) {
		XLTable table = tables.get(tableName);
		
		if (table == null) {
			table = createTable(tableName, "grid");
			tables.put(tableName, table);
		}
		
		return table;
	}
	
	public XLTable getLookup(String tableName) {
		XLTable table = lookups.get(tableName);
		
		if (table == null) {
			table = createTable(tableName, "lookup");
			lookups.put(tableName, table);
		}
		
		return table;		
	}
	
	private XLTable createTable(String tableNameOrAlias, String type) {
		XLTableDefinition tableDefinition = this.tableDefinitions.get(tableNameOrAlias);
		if (tableDefinition == null) {
			String tableName = this.aliases.get(tableNameOrAlias);
			if (tableName != null) {
				tableDefinition = this.tableDefinitions.get(tableNameOrAlias);
			} else {
				throw new ResourceNotFoundException("Could not find the requested table: " + tableNameOrAlias);
			}
		}
		if (type.equals(tableDefinition.getType())) {
			try
			{
				log.debug("About to load table: " + tableDefinition);
				return new XLTable(tableDefinition);
			}
			catch (Exception e)
			{
				throw new RuntimeException("Could not load required value data: " + tableNameOrAlias, e);
			}
		}
		else
		{
			throw new ResourceNotFoundException(String.format("Table(%s) was not of type(%s): %s", tableNameOrAlias, type, tableDefinition.getType()));
		}		
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
