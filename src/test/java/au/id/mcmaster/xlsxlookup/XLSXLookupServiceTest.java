package au.id.mcmaster.xlsxlookup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class XLSXLookupServiceTest {
	private static final String[] TEST_FILES = new String[] {"workbooks/Testing.xlsx", "workbooks/TestSet1.xlsx"};

	@Test
	public void testCreate() {
		new XLSXLookupService(TEST_FILES);
	}
	@Test
	public void testGetValue() {
		XLSXLookupService service = new XLSXLookupService(TEST_FILES);
		Map<String,String> queryMap = new HashMap<String,String>();
		queryMap.put("ANB", "4");
		queryMap.put("a", "BCD:1");
		queryMap.put("b", "BC:2");
		queryMap.put("c", "B:3");
		queryMap.put("d", "BCDEF:4");
		String value = service.getValue("Test 2", queryMap);
		Assert.assertEquals("16", value);
	}
	
	@Test
	public void testCalculations()
	{
		XLSXLookupService service = new XLSXLookupService(TEST_FILES);
		List<String> lookupNames = service.getLookupTableNames();
		System.out.println(lookupNames);
		String value = service.getLookupValue("Subtract", null);
		System.out.println(value);
	}
}
