package au.id.mcmaster.xslxlookup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import au.id.mcmaster.apoi.tableadapter.XLTable;
import au.id.mcmaster.apoi.tableadapter.XLTableDefinition;


public class XLSXLookupService
{
	private Map<String,XLTableDefinition> tableDefinitions = new HashMap<String,XLTableDefinition>();
	private Map<String,XLTable> tableAdapters = new HashMap<String,XLTable>();
	private Map<String,Map<String,String>> valuesMap = new HashMap<String,Map<String,String>>();
	private Map<String,Map<String,List<String>>> keysMap = new HashMap<String,Map<String,List<String>>>();
	
	public XLSXLookupService()
	{
		tableDefinitions.put("Test1", new XLTableDefinition("workbooks/Testing.xlsx","Test1",1,2,36,8,1,50));
		tableDefinitions.put("Test2", new XLTableDefinition("workbooks/Testing.xlsx","Test2",1,1,5,4,1,4));
		tableDefinitions.put("Test3 - DEATH AND TPD", new XLTableDefinition("workbooks/Testing.xlsx","Test3",3,7,6,3,1,50));
		tableDefinitions.put("Test3 - INCOME PROTECTION", new XLTableDefinition("workbooks/Testing.xlsx","Test3",12,7,4,2,1,50));
		tableDefinitions.put("Energy Super GSC - DS", new XLTableDefinition("workbooks/private/Testing-Private.xlsx","Energy Super GSC - DS",1,3,37,8,1,50));
		tableDefinitions.put("Energy Super GSC - PR", new XLTableDefinition("workbooks/private/Testing-Private.xlsx","Energy Super GSC - PR",1,3,83,8,1,50));
	}
	
	public Collection<String> getTableNames() {
		return tableDefinitions.keySet();
	}
	
	private XLTable getTableAdapter(String tableName) {
		XLTable table = tableAdapters.get(tableName);
		
		if (table == null) {
			try
			{
				XLTableDefinition tableDefinition = this.tableDefinitions.get(tableName);
				table = new XLTable(tableDefinition);
				tableAdapters.put(tableName, table);
			}
			catch (Exception e)
			{
				throw new RuntimeException("Could not load required value data: " + tableName, e);
			}
			
		}
		
		return table;
	}
	
	private Map<String,String> getValueMap(String tableName) {
		Map<String,String> valueMap = this.valuesMap.get(tableName);
		if (valueMap == null)
		{
			try
			{
				XLTable tableAdapter = getTableAdapter(tableName);
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

	public Collection<String> getFieldNames(String table) {
		XLTable tableAdapter = getTableAdapter(table);
		return tableAdapter.getColumnDataTitles();
	}
	
	public Map<String,Collection<String>> getValueOptionsMap(String table) {
		XLTable tableAdapter = getTableAdapter(table);
		return tableAdapter.getValueOptionsMap();
	}
}