package au.id.mcmaster.apoi.tableadapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class XLTableTest {
	private static final String FILE_NAME = "workbooks/TestSet1.xlsx";
	private static XLTableDefinition TABLE_ADAPTER = new XLTableDefinition(FILE_NAME,"TableAdapter",1,1,5,4,1,4);
	private static XLTableDefinition TABLE_ADAPTER2 = new XLTableDefinition(FILE_NAME,"TableAdapter2",3,8,5,3,1,4);

	@Test
	public void testCreate() throws Exception 
	{
		new XLTable(TABLE_ADAPTER);
		new XLTable(TABLE_ADAPTER2);
	}
	
	@Test
	public void testTopLeftCorner() throws Exception {
		System.out.println(TABLE_ADAPTER);
		System.out.println(new XLTable(TABLE_ADAPTER));
	}

	@Test
	public void testMiddleOfSheet() throws Exception {
		System.out.println(TABLE_ADAPTER2);
		System.out.println(new XLTable(TABLE_ADAPTER));
	}
	
	@Test
	public void testGetColumnTitles() throws Exception
	{
		XLTable table = new XLTable(TABLE_ADAPTER);
		List<String> columnTitles = table.getColumnDataTitles();
		String[] expected = new String[] {"ANB","a","b","c","d"};
		for (int i=0;i<columnTitles.size(); i++) {
			Assert.assertTrue(expected[i].equals(columnTitles.get(i)));
		}
	}
	
	@Test
	public void getValueMap() throws Exception
	{
		XLTable table = new XLTable(TABLE_ADAPTER);
		
		Map<String,String> expected = new HashMap<String,String>();
		expected.put("1 | BCD:1 | BC:2 | B:3 | BCDEF:4", "1");
		expected.put("2 | BCD:1 | BC:2 | B:3 | BCDEF:4", "6");
		expected.put("3 | BCD:1 | BC:2 | B:3 | BCDEF:4", "11");
		expected.put("4 | BCD:1 | BC:2 | B:3 | BCDEF:4", "16");
		
		expected.put("1 | BCD:1 | BC:2 | CDEF:3 | BCDEF:4", "2");
		expected.put("2 | BCD:1 | BC:2 | CDEF:3 | BCDEF:4", "7");
		expected.put("3 | BCD:1 | BC:2 | CDEF:3 | BCDEF:4", "12");
		expected.put("4 | BCD:1 | BC:2 | CDEF:3 | BCDEF:4", "17");
		
		expected.put("1 | BCD:1 | DE:2 | CDEF:3 | BCDEF:4", "3");
		expected.put("2 | BCD:1 | DE:2 | CDEF:3 | BCDEF:4", "8");
		expected.put("3 | BCD:1 | DE:2 | CDEF:3 | BCDEF:4", "13");
		expected.put("4 | BCD:1 | DE:2 | CDEF:3 | BCDEF:4", "18");
		
		expected.put("1 | E:1 | DE:2 | CDEF:3 | BCDEF:4", "4");
		expected.put("2 | E:1 | DE:2 | CDEF:3 | BCDEF:4", "9");
		expected.put("3 | E:1 | DE:2 | CDEF:3 | BCDEF:4", "14");
		expected.put("4 | E:1 | DE:2 | CDEF:3 | BCDEF:4", "19");

		expected.put("1 | F:1 | F:2 | CDEF:3 | BCDEF:4", "5");
		expected.put("2 | F:1 | F:2 | CDEF:3 | BCDEF:4", "10");
		expected.put("3 | F:1 | F:2 | CDEF:3 | BCDEF:4", "15");
		expected.put("4 | F:1 | F:2 | CDEF:3 | BCDEF:4", "20");

		Map<String,String> actual = table.getValueMap();
		
		for (String key : expected.keySet())
		{
			Assert.assertEquals(expected.get(key), (actual.get(key)));
		}
	}
	
	@Test
	public void testGetValueOptionsMap() throws Exception
	{
		XLTable table = new XLTable(TABLE_ADAPTER);
		
		Map<String,String[]> expectedMap = new HashMap<String,String[]>();
		expectedMap.put("a", new String[] {"BCD:1","E:1","F:1"});
		expectedMap.put("b", new String[] {"BC:2","DE:2","F:2"});
		expectedMap.put("c", new String[] {"B:3","CDEF:3"});
		expectedMap.put("d", new String[] {"BCDEF:4"});
		expectedMap.put("ANB", new String[] {"1","2","3","4"});
		
		Map<String,List<String>> actualMap = table.getValueOptionsMap();
		
		for (String key : expectedMap.keySet()) {
			String[] expected = expectedMap.get(key);
			List<String> actual = actualMap.get(key);
			for (int i=0; i<expected.length; i++) {
				Assert.assertEquals(expected[i], actual.get(i));
			}
		}
	}
}
