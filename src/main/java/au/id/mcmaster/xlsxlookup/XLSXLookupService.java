package au.id.mcmaster.xlsxlookup;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import au.id.mcmaster.apoi.tableadapter.XLDataGrid;
import au.id.mcmaster.apoi.tableadapter.XLOptionTree;
import au.id.mcmaster.apoi.tableadapter.XLTable;
import au.id.mcmaster.apoi.tableadapter.XLTableDefinition;
import au.id.mcmaster.apoi.tableadapter.XLWorkbook;
import au.id.mcmaster.apoi.tableadapter.XLWorksheet;


public class XLSXLookupService
{
	private Map<String,XLTableDefinition> tableDefinitions = new HashMap<String,XLTableDefinition>();
	private Map<String,XLTable> tables = new HashMap<String,XLTable>();
	private Map<String,Map<String,String>> valuesMap = new HashMap<String,Map<String,String>>();
	private Map<String,Map<String,List<String>>> keysMap = new HashMap<String,Map<String,List<String>>>();
	
	public XLSXLookupService()
	{
		try {
			this.tableDefinitions = loadTables("workbooks/Testing.xlsx","workbooks/private/Testing-Private.xlsx");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Collection<String> getTableNames() {
		return tableDefinitions.keySet();
	}
	
	public Collection<String> getFieldNames(String table) {
		XLTable tableAdapter = getTable(table);
		return tableAdapter.getColumnDataTitles();
	}
	
	public Map<String,List<String>> getValueOptionsMap(String table) {
		XLTable tableAdapter = getTable(table);
		return tableAdapter.getValueOptionsMap();
	}
	
	public String getValue(String tableName, Map<String,String> queryMap) {
		Collection<String> fields = getFieldNames(tableName);
		String[] args = new String[fields.size()];
		List<String> values = fields.stream().map(field -> queryMap.get(field)).collect(Collectors.toList());
		values.toArray(args);
		return getValue(tableName, args);
	}
	
	public XLOptionTree getColumnValuesOptionTree(String tableName) {
		XLTable tableAdapter = getTable(tableName);
		return tableAdapter.getColumnsValuesOptionsTree();
	}
	
	public String getValue(String tableName, String...keys) {
		List<String> list = new ArrayList<String>();
		Collections.addAll(list, keys);
		String key = list.stream().collect(Collectors.joining(" | "));
		System.out.println("  Generated Key = " + key);
		Map<String,String> valueMap = getValueMap(tableName);
		String value = valueMap.get(key);
		System.out.println("Looked up value = " + value);
		return value;
	}
	
	private Map<String,XLTableDefinition> loadTables(String... fileNames) throws IOException
	{
		Map<String,XLTableDefinition> tableDefinitionMap = new HashMap<String,XLTableDefinition>();
		
		for (String fileName : fileNames) 
		{
			XLDataGrid valueData = getTableDefinitionData(fileName);
			//System.out.println("--->>>> " + valueData);
			for (int i=0; i<valueData.getNumberOfRows(); i++) {
				String[] row = valueData.getRow(i);
				String tableName = row[0];
				if (tableName != null && tableName.trim().length() > 0) {
					XLTableDefinition tableDefinition = new XLTableDefinition(fileName, row);
					//System.out.println("-->> Added a table definition: " + tableName);
					tableDefinitionMap.put(tableName, tableDefinition);
				}
			}
		}
		
		return tableDefinitionMap;
	}

	private XLDataGrid getTableDefinitionData(String fileName) throws IOException {
		
		try (InputStream is = XLTable.class.getClassLoader().getResourceAsStream(fileName))
		{
			XLWorkbook workbookAdapter = new XLWorkbook(new XSSFWorkbook(is));
			XLWorksheet worksheetAdapter = workbookAdapter.getWorksheetAdapter(XLTableDefinition.DEFINITION_TABLE.getWorksheetName());
			XLDataGrid valueData = worksheetAdapter.getData(XLTableDefinition.DEFINITION_TABLE.getValueDataRectangle());
			return valueData;
		}
	}
	
	private XLTable getTable(String tableName) {
		XLTable table = tables.get(tableName);
		
		if (table == null) {
			try
			{
				XLTableDefinition tableDefinition = this.tableDefinitions.get(tableName);
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
	
	public Map<String,String> getValueMap(String tableName) {
		Map<String,String> valueMap = this.valuesMap.get(tableName);
		if (valueMap == null)
		{
			try
			{
				XLTable tableAdapter = getTable(tableName);
				valueMap = tableAdapter.getValueMap();
				this.valuesMap.put(tableName,valueMap);
			}
			catch (Exception e)
			{
				throw new RuntimeException("Could not load required value data: " + tableName);
			}
		}
		return valueMap;
	}
}