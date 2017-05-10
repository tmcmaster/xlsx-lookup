package au.id.mcmaster.xlsxlookup;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class XLSXLookupControllerTest {
	private static final String TEST_FILES = "workbooks/Testing.xlsx workbooks/TestSet1.xlsx";
	
	@Test
	public void testCreate() {
		new XLSXLookupController(TEST_FILES);
	}

	@Test
	public void testTableValue() {
		XLSXLookupController controller = new XLSXLookupController(TEST_FILES);
		Map<String, String> queryMap = new HashMap<String, String>();
		queryMap.put("ANB", "4");
		queryMap.put("a", "BCD:1");
		queryMap.put("b", "BC:2");
		queryMap.put("c", "B:3");
		queryMap.put("d", "BCDEF:4");
		String value = controller.tableValue("Test 2", queryMap);
		Assert.assertEquals("16", value);
	}
	
	@Test
	public void testLookupValue() {
		XLSXLookupController controller = new XLSXLookupController(TEST_FILES);
		Map<String,String> inputData = new HashMap<String,String>();
		inputData.put("A", "BC:13");
		inputData.put("B", "BCD:14-15");
		inputData.put("C", "BCD:14-15");		
		inputData.put("ANB", "3");

		inputData.put("a", "BCD:1");
		inputData.put("b", "BC:2");
		inputData.put("c", "CDEF:3");
		inputData.put("d", "BCDEF:4");

		String value = controller.lookupValue("Multiply", inputData);
		Assert.assertEquals("144", value);
	}
}
