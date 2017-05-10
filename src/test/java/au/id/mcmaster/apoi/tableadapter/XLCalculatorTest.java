package au.id.mcmaster.apoi.tableadapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class XLCalculatorTest {
	@Test
	public void testCreate()
	{
		new XLCalculator(new XLTableLoader("workbooks/TestSet1.xlsx"));
	}
	
	@Test
	public void testSimpleLookup()
	{
		XLCalculator calculator = new XLCalculator(new XLTableLoader("workbooks/TestSet1.xlsx"));
		Map<String,String> inputData = new HashMap<String,String>();
		inputData.put("A", "BC:13");
		inputData.put("B", "BCD:14-15");
		inputData.put("C", "BCD:14-15");		
		inputData.put("ANB", "3");

		inputData.put("a", "BCD:1");
		inputData.put("b", "BC:2");
		inputData.put("c", "CDEF:3");
		inputData.put("d", "BCDEF:4");
		String value = calculator.lookup("Multiply", inputData);
		Assert.assertEquals("144", value);
	}

	@Test
	public void testGetRequiredTables()
	{
		XLCalculator calculator = new XLCalculator(new XLTableLoader("workbooks/TestSet1.xlsx"));
		List<String> tableNames = calculator.getRequiredTables("Multiply");
		System.out.println(tableNames);
	}
	
	@Test
	public void testGetLookupFields()
	{
		XLCalculator calculator = new XLCalculator(new XLTableLoader("workbooks/TestSet1.xlsx"));
		List<String> requiredFields = calculator.getRequiredFields("Multiply");
		System.out.println(requiredFields);
	}

//	@Test
//	public void testGetOptionsMap()
//	{
//		XLCalculator calculator = new XLCalculator(new XLTableLoader("workbooks/TestSet1.xlsx"));
//		Map<String,List<String>> optionsMap = calculator.getOptionsMap("Multiply");
//		System.out.println(optionsMap);
//	}

	@Test
	public void testComplexLookup()
	{
		XLCalculator calculator = new XLCalculator(new XLTableLoader("workbooks/TestSet1.xlsx"));
		List<String> requiredFields = calculator.getRequiredFields("Multiply");
		System.out.println(requiredFields);
		Map<String,String> inputData = new HashMap<String,String>();
		inputData.put("A", "BC:13");
		inputData.put("B", "BCD:14-15");
		inputData.put("C", "BCD:14-15");		
		inputData.put("ANB", "3");

		inputData.put("a", "BCD:1");
		inputData.put("b", "BC:2");
		inputData.put("c", "CDEF:3");
		inputData.put("d", "BCDEF:4");
		String value = calculator.lookup("Multiply", inputData);
		Assert.assertEquals("144", value);
	}

}
