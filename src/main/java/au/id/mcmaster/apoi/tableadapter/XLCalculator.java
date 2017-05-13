package au.id.mcmaster.apoi.tableadapter;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;


public class XLCalculator {
	private static final Logger log = LoggerFactory.getLogger(XLCalculator.class);

	private XLTableLoader tableLoader;
	
	public XLCalculator(XLTableLoader tableLoader) {
		this.tableLoader = tableLoader;
	}

	public List<String> getRequiredTables(String lookupName) {
		List<String> tableNames = new ArrayList<String>();
		XLLookup lookup = tableLoader.getLookup(lookupName);
		String lookupValue = lookup.getValue();
		resolveRequiredTables(lookupValue, tableNames);
		return unique(tableNames);
	}
	
	private void resolveRequiredTables(String lookupValue, List<String> requiredTables) {
		String[] lookupValueParts = lookupValue.split("\\s+");
		for (String lookupValuePart : lookupValueParts) {
			if (!Pattern.matches("[\\+-/\\*]", lookupValuePart) && !isNumber(lookupValuePart)) {
				XLLookup lookup = tableLoader.getLookup(lookupValuePart);
				if (lookup != null) {
					resolveRequiredFields(lookup.getValue(), requiredTables);
				}
				else
				{
					XLTable table = tableLoader.getTable(lookupValuePart);
					log.debug(String.format("Resolving '%s' to '%s'",lookupValuePart, (table == null ? null : table.getName())));
					requiredTables.add(table.getName());
				}
			}
		}
	}
	
	public List<String> getRequiredFields(String lookupName) {
		List<String> requiredFields = new ArrayList<String>();
		XLLookup lookup = tableLoader.getLookup(lookupName);
		String lookupValue = lookup.getValue();
		resolveRequiredFields(lookupValue, requiredFields);
		return unique(requiredFields);
	}
	
	private void resolveRequiredFields(String lookupValue, List<String> requiredFields) {
		String[] lookupValueParts = lookupValue.split("\\s+");
		for (String lookupValuePart : lookupValueParts) {
			if (!Pattern.matches("[\\+-/\\*]", lookupValuePart) && !isNumber(lookupValuePart)) {
				XLLookup lookup = tableLoader.getLookup(lookupValuePart);
				if (lookup != null) {
					resolveRequiredFields(lookup.getValue(), requiredFields);
				}
				else
				{
					XLTable table = tableLoader.getTable(lookupValuePart);
					requiredFields.addAll(table.getFieldList());
				}
			}
		}
	}

	public Map<String,List<String>> getFieldOptionsMap(String lookupName) {
		Map<String,List<String>> optionsMap = new HashMap<String,List<String>>();
		XLLookup lookup = tableLoader.getLookup(lookupName);
		String lookupValue = lookup.getValue();
		resolveFieldOptionsMap(lookupValue, optionsMap);
		return optionsMap;
	}
	
	private void resolveFieldOptionsMap(String lookupValue, Map<String,List<String>> optionsMap) {
		String[] lookupValueParts = lookupValue.split("\\s+");
		for (String lookupValuePart : lookupValueParts) {
			if (!Pattern.matches("[\\+-/\\*]", lookupValuePart) && !isNumber(lookupValuePart)) {
				XLLookup lookup = tableLoader.getLookup(lookupValuePart);
				if (lookup != null) {
					resolveFieldOptionsMap(lookup.getValue(), optionsMap);
				}
				else
				{
					XLTable table = tableLoader.getTable(lookupValuePart);
					if (table != null) {
						mergeOptionMaps(optionsMap, table.getValueOptionsMap());
					}
				}
			}
		}
	}
	
	private void mergeOptionMaps(Map<String,List<String>> optionMap1, Map<String,List<String>> optionMap2) {
		for (String key : optionMap2.keySet()) {
			List<String> options1 = optionMap1.get(key);
			List<String> options2 = optionMap2.get(key);
			if (options1 == null) {
				optionMap1.put(key, options2);
			}
			else
			{
				Set<String> set = new HashSet<String>(options1);
				for (String option : options2) {
					if (!set.contains(option)) {
						set.add(option);
						options1.add(option);
					}
				}
			}
		}
	}

	public String lookup(String lookupName, Map<String,String> valueMap) {
		log.debug(String.format("Getting value for Lookup(%s): ",lookupName, valueMap));
		XLLookup lookup = tableLoader.getLookup(lookupName);		
		String lookupValue = lookup.getValue();
		log.debug(String.format("Expanding Expression(%s): ",lookupValue, valueMap));		
		String resultString = resolveLookupValuePart(lookupValue, valueMap);
		log.debug(String.format("Evaluating expanded Expression(%s): ",resultString, valueMap));		
		SpelExpressionParser parser = new SpelExpressionParser();
		Expression expression = parser.parseExpression(resultString);
		String result = expression.getValue().toString();
		log.debug(String.format("Evaluated Result(%s): ",result));		
		return result;
	}

	private String resolveLookupValuePart(String lookupValue, Map<String,String> valueMap) {
		String[] lookupValueParts = lookupValue.split("\\s+");
		if (lookupValueParts.length == 1)
		{
			if (Pattern.matches("[\\+-/\\*]", lookupValue)) {
				return lookupValue;
			}
			else if (isNumber(lookupValue))
			{
				return lookupValue;
			}
			else
			{
				XLLookup lookup = tableLoader.getLookup(lookupValue);
				if (lookup == null)
				{
					XLTable table = tableLoader.getTable(lookupValue);
					if (table == null) {
						throw new ResourceNotFoundException("Could not resolve calculation: " + lookupValue);						
					}
					else
					{
						String value = resolveTableValue(table, valueMap);
						if (value == null) {
							throw new ResourceNotFoundException(String.format("Could not resolve table(%s) value: ", table.getName(), valueMap));
						}
						else
						{
							return value;
						}
					}
				}
				else
				{
					log.debug(String.format("Resolving '%s' to '%s'",lookupValue, (lookup == null ? null : lookup.getName())));
					String value = lookup.getValue();
					return resolveLookupValuePart(value, valueMap);
				}
			}
		}
		else
		{
			return Arrays.stream(lookupValueParts).map(v -> resolveLookupValuePart(v, valueMap)).collect(joining(" "));
		}
	}
	
	@Deprecated
	private String resolveTableValueOld(XLTable table, Map<String, String> valueMap) {
		Map<String,String> keyToValueMap = table.getValueMap();
		List<String> fields = table.getFieldList();
		String keyString = fields.stream().map(k -> valueMap.get(k)).collect(joining(" | "));
		String result = keyToValueMap.get(keyString);
		System.out.println("-------->>>>> " + table.getName() + " : " + fields.toString() + " : " + result);
		return result;
	}

	private String resolveTableValue(XLTable table, Map<String, String> valueMap) {
		XLValueTree valueTree = table.getValueTree();
		List<String> fields = table.getFieldList();
		List<String> valueList = fields.stream().map(f -> valueMap.get(f)).collect(toList());
		String[] values = new String[valueList.size()];
		valueList.toArray(values);
		return valueTree.getValue(values);
	}
	
	private List<String> unique(List<String> requiredFields) {
		Set<String> set = new HashSet<String>();
		List<String> list = new ArrayList<String>();
		for (String field : requiredFields) {
			if (!set.contains(field)) {
				set.add(field);
				list.add(field);
			}
		}
		return list;
	}

	private boolean isNumber(String lookupValue)
	{
		try
		{
	        Double.parseDouble(lookupValue);
	        return false;
	    }
		catch (NumberFormatException e)
		{
	        return false;
	    }
	}

	public List<String> getLookupNames() {
		return this.tableLoader.getLookupNames();
	}

	public List<String> getOptionsMap(String lookup) {
		return null;
	}
}
