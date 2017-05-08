package au.id.mcmaster.apoi.tableadapter;

import java.util.HashMap;
import java.util.Map;

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
		String value = calculator.lookup("Multiply", inputData);
		System.out.println(value);
	}

}
