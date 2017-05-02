package au.id.mcmaster.xlsxlookup;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class XLSXLookupControllerTest {
	private static final String TEST_FILES = "workbooks/Testing.xlsx";
	@Test
	public void testCreate() {
		new XLSXLookupController(TEST_FILES);
	}
	@Test
	public void testGetValue() {
		XLSXLookupController controller = new XLSXLookupController(TEST_FILES);
		Map<String,String> queryMap = new HashMap<String,String>();
		queryMap.put("ANB", "4");
		queryMap.put("a", "BCD:1");
		queryMap.put("b", "BC:2");
		queryMap.put("c", "B:3");
		queryMap.put("d", "BCDEF:4");
		String value = controller.lookupTableData("Test 2", queryMap);
		Assert.assertEquals("16", value);
	}
}
