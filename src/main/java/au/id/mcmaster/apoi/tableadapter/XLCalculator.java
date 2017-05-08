package au.id.mcmaster.apoi.tableadapter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.tomcat.util.http.fileupload.util.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import static java.util.stream.Collectors.joining;


public class XLCalculator {
	private static final Logger log = LoggerFactory.getLogger(XLCalculator.class);

	private XLTableLoader tableLoader;
	
	public XLCalculator(XLTableLoader tableLoader) {
		this.tableLoader = tableLoader;
	}

	public String lookup(String lookupName, Map<String,String> valueMap) {
		log.debug(String.format("Getting value for Lookup(%s): ",lookupName, valueMap));
		XLLookup lookup = tableLoader.getLookup(lookupName);		
		String lookupValue = lookup.getValue();
		log.debug(String.format("Expanding Expression(%s): ",lookupValue, valueMap));		
		String result = resolveLookupValuePart(lookupValue, valueMap);
		log.debug(String.format("Evaluating expanded Expression(%s): ",result, valueMap));		
		SpelExpressionParser parser = new SpelExpressionParser();
		Expression expression = parser.parseExpression(result);
		return expression.getValue().toString();
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

	private String resolveTableValue(XLTable table, Map<String, String> valueMap) {
		Map<String,String> keyToValueMap = table.getValueMap();
		//List<String> fields = table.getColumnTitles();
		return "3";
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
}
