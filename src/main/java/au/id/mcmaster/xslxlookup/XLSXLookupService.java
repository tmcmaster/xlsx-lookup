package au.id.mcmaster.xslxlookup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import au.id.mcmaster.apoi.tableadapter.TableAdapter;
import au.id.mcmaster.apoi.tableadapter.TableDefinition;


public class XLSXLookupService
{
	private Map<String,TableDefinition> tableDefinitions = new HashMap<String,TableDefinition>();
	private Map<String,Map<String,String>> valueMaps = new HashMap<String,Map<String,String>>();
	
	public XLSXLookupService()
	{
		tableDefinitions.put("Test1", new TableDefinition("workbooks/Testing.xlsx","Test1",1,2,36,8,1,5));
		tableDefinitions.put("Test2", new TableDefinition("workbooks/Testing.xlsx","Test2",1,1,5,4,1,4));
	}
	
	private Map<String,String> getValueMap(String tableName) {
		Map<String,String> valueMap = this.valueMaps.get(tableName);
		if (valueMap == null)
		{
			try
			{
				TableDefinition tableDefinition = this.tableDefinitions.get(tableName);
				TableAdapter tableAdapter = new TableAdapter(tableDefinition);
				valueMap = tableAdapter.getValueMap();
				this.valueMaps.put(tableName,valueMap);
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
}