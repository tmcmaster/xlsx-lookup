package au.id.mcmaster.apoi.tableadapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class XLTableTest {
	private static final String FILE_NAME = "workbooks/TestSet1.xlsx";
	private static XLTableDefinition GRID_V1_SIMPLE = new XLTableDefinition(FILE_NAME,"Grid V1 Simple","Grid - Version 1 - Simple","Grid_V1_Simple",1,1,5,4,1,4,"grid",1);
	private static XLTableDefinition GRID_V1_COMPLEX = new XLTableDefinition(FILE_NAME,"Grid V1 Complex","Grid - Version 1 - Complex","Grid_V1_Simple",3,8,5,3,1,4,"grid",1);
	private static XLTableDefinition MULTI_VALUE = new XLTableDefinition(FILE_NAME,"Multiple Input Values","Multiple Input Values","Multiple_Input_Values",2,5,5,4,3,4,"grid",2);

	@Test
	public void testCreate() throws Exception 
	{
		new XLTable(GRID_V1_SIMPLE);
		new XLTable(GRID_V1_COMPLEX);
	}
	
	@Test
	public void testTopLeftCorner() throws Exception {
		System.out.println(GRID_V1_SIMPLE);
		System.out.println(new XLTable(GRID_V1_SIMPLE));
	}

	@Test
	public void testMiddleOfSheet() throws Exception {
		System.out.println(GRID_V1_COMPLEX);
		System.out.println(new XLTable(GRID_V1_COMPLEX));
	}
	
	@Test
	public void testMergedRowCells() throws Exception {
		System.out.println(GRID_V1_COMPLEX);
		XLTable table = new XLTable(GRID_V1_COMPLEX);
		System.out.println(table);
		String[][] expected = new String[][] {
			new String[] { "BC:13", "BC:13", "D:13", "E:13", "F:13-15" },
			new String[] { "BCD:14-15", "BCD:14-15", "BCD:14-15", "E:14", "F:13-15" },
			new String[] { "BCD:14-15", "BCD:14-15", "BCD:14-15", "E:15", "F:13-15" }
		};
		XLDataGrid actual = table.getColumnDataGrid();
		for (int r=0; r<expected.length; r++) {
			for (int c=0; c<expected.length; c++) {
				Assert.assertEquals(expected[r][c], actual.getValue(r,c));
			}
		}
 	}
	
	@Test
	public void testGetColumnTitles() throws Exception
	{
		XLTable table = new XLTable(GRID_V1_SIMPLE);
		List<String> columnTitles = table.getColumnDataTitles();
		String[] expected = new String[] {"a","b","c","d"};
		for (int i=0;i<columnTitles.size(); i++) {
			Assert.assertTrue(expected[i].equals(columnTitles.get(i)));
		}
	}

	@Test
	public void testGetFieldList() throws Exception
	{
		XLTable table = new XLTable(GRID_V1_SIMPLE);
		List<String> columnTitles = table.getFieldList();
		String[] expected = new String[] {"a","b","c","d","ANB"};
		for (int i=0;i<columnTitles.size(); i++) {
			Assert.assertTrue(expected[i].equals(columnTitles.get(i)));
		}
	}

	
	@Test
	public void getValueMap() throws Exception
	{
		XLTable table = new XLTable(GRID_V1_SIMPLE);
		
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
		XLTable table = new XLTable(GRID_V1_SIMPLE);
		
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
	
	@Test
	public void testGetValueTree() throws Exception
	{
		XLTable table = new XLTable(MULTI_VALUE);
		System.out.println(table);
		XLValueTree valueTree = table.getValueTree();
		System.out.println(valueTree);
		String value = valueTree.getValue(new String[] {"C","H","L","P","e","h","3"});
		Assert.assertEquals("14", value);
		
	}
}
