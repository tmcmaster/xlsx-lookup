package au.id.mcmaster.xlsxlookup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import au.id.mcmaster.apoi.tableadapter.XLCalculator;
import au.id.mcmaster.apoi.tableadapter.XLOptionTree;
import au.id.mcmaster.apoi.tableadapter.XLTable;
import au.id.mcmaster.apoi.tableadapter.XLTableLoader;

public class XLSXLookupService
{
	private static final Logger log = LoggerFactory.getLogger(XLSXLookupService.class);
	
	private Map<String,Map<String,String>> valuesMap = new HashMap<String,Map<String,String>>();
	private Map<String,Map<String,List<String>>> keysMap = new HashMap<String,Map<String,List<String>>>();
	
	private XLTableLoader tableLoader;
	private XLCalculator calculator;

	
	public XLSXLookupService(String... spreadsheetFiles)
	{
		this.tableLoader = new XLTableLoader(spreadsheetFiles);
		this.calculator = new XLCalculator(this.tableLoader);
	}
	
	public Collection<String> getTableNames() {
		return tableLoader.getTableNames();
	}
	
	public List<String> getFieldNames(String table) {
		XLTable tableAdapter = tableLoader.getTable(table);
		return new ArrayList<String>(tableAdapter.getColumnDataTitles(true));
	}
	
	public Map<String,List<String>> getValueOptionsMap(String table) {
		XLTable tableAdapter = tableLoader.getTable(table);
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
		XLTable tableAdapter = tableLoader.getTable(tableName);
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
	
	public String getLookupValue(String lookupName, Map<String,String> inputMap) {
		XLCalculator calculator = new XLCalculator(tableLoader);
		return calculator.lookup(lookupName, inputMap);
	}
	
	public Map<String,String> getValueMap(String tableName) {
		Map<String,String> valueMap = this.valuesMap.get(tableName);
		if (valueMap == null)
		{
			try
			{
				XLTable tableAdapter = tableLoader.getTable(tableName);
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
	
	public List<String> getLookupTableNames() {
		
		return this.calculator.getLookupNames();
	}
	
	public List<String> getLookupTableFields(String lookup) {
		
		return this.calculator.getRequiredFields(lookup);
	}
	
	public List<String> getLookupOptionMap(String lookup) {
		
		return this.calculator.getOptionsMap(lookup);
	}

	public Map<String, List<String>> getLookupOptionsMap(String lookup) {
		return this.calculator.getFieldOptionsMap(lookup);
	}	
}