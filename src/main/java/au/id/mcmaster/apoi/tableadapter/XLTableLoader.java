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
	private Map<String,XLTable> lookupTables = new HashMap<String,XLTable>();
	private Map<String,XLLookup> lookups = new HashMap<String,XLLookup>();
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
	
	public Collection<String> getLookupTableNames() {
		return lookupTables.keySet();
	}
	
	private void loadTables(String... fileNames) throws IOException
	{
		log.info("Loading tables");
		
		for (String fileName : fileNames) 
		{
			log.info("Loading tables from workbook: " + fileName);
			
			XLDataGrid valueData = getTableDefinitionData(fileName);
			log.debug("-->> Table Definition Data:\n" + valueData);
			for (int i=0; i<valueData.getNumberOfRows(); i++) {
				String[] row = valueData.getRow(i);
				String tableName = row[0];
				String tableAlias = row[1];
				this.aliases.put(tableAlias, tableName);
				if (tableName != null && tableName.trim().length() > 0 && !tableName.startsWith("#")) {
					XLTableDefinition tableDefinition = new XLTableDefinition(fileName, row);
					log.debug("  -->> Added a table definition: " + tableName);
					tableDefinitions.put(tableName, tableDefinition);
					createTable(tableName, tableDefinition.getType());
				}
			}
		}

		log.info("Tables have been loaded.");
	}

	public XLTable getTable(String tableNameOrAlias) {
		String tableName = tableNameOrAlias;
		XLTable table = tables.get(tableName);
		if (table == null)
		{
			tableName = aliases.get(tableName);
			table = tables.get(tableName);
		}
		if (table == null) {
			throw new ResourceNotFoundException("Could not find table: " + tableNameOrAlias);
		}
		return table;
	}
	
	public XLTable getLookupTable(String tableNameOrAlias) {
		String tableName = tableNameOrAlias;
		XLTable table = lookupTables.get(tableName);		

		if (table == null)
		{
			tableName = aliases.get(tableName);
			table = tables.get(tableName);
		}
		if (table == null) {
			throw new ResourceNotFoundException("Could not find table: " + tableNameOrAlias);
		}
		return table;
	}
	
	public XLLookup getLookup(String tableName) {
		return lookups.get(tableName);		
	}
	
	private XLTable createTable(String tableNameOrAlias, String type) {
		XLTableDefinition tableDefinition = this.tableDefinitions.get(tableNameOrAlias);
		String tableName = tableNameOrAlias;
		if (tableDefinition == null) {
			tableName = this.aliases.get(tableNameOrAlias);
			if (tableName != null) {
				tableDefinition = this.tableDefinitions.get(tableName);
			} else {
				throw new ResourceNotFoundException("Could not find the requested table: " + tableNameOrAlias);
			}
		}
		
		try
		{
			log.debug("About to load table: " + tableDefinition);
			XLTable table = new XLTable(tableDefinition);
	
			if ("grid".equals(table.getType())) {
				tables.put(tableName, table);
			} else if ("lookup".equals(table.getType())) {
				lookupTables.put(tableName, table);
				createLookups(table);
				
			}
			
			if (!type.equals(table.getType())) {
				throw new ResourceNotFoundException(String.format("Table(%s) was not of type(%s): %s", tableName, type, table.getType()));
			}
	
			return table;
		} 
		catch (IOException e)
		{
			throw new ResourceNotFoundException("Could not find the requested table: " + tableNameOrAlias);
		}
	}
	
	private void createLookups(XLTable table) {
		String[] lookupNames = table.getRowData().getColumn(0);
		String[] lookupValues = table.getValueData().getColumn(0);
		for (int i=0; i<lookupNames.length; i++) {
			XLLookup lookup = new XLLookup(lookupNames[i], lookupValues[i]);
			this.lookups.put(lookupNames[i], lookup);
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
