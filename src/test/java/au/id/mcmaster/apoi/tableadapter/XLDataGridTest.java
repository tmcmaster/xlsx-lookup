package au.id.mcmaster.apoi.tableadapter;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class XLDataGridTest {
	private static final String[][] TEST_DATA = {
		new String[] {"a","b","c","d"},
		new String[] {"1","2","3","4"},
		new String[] {"A","B","C","D"}
	};
	
	private static final String[][] TEST_DATA_WITH_DUPLICATES = {
		new String[] {"a","2","c","d"},
		new String[] {"1","2","3","4"},
		new String[] {"A","A","B","B"}
	};
	
	@Test
	public void testCreate() {
		new XLDataGrid(new String[0][]);
	}
	
	@Test
	public void testNumberOfRows() {
		int numberOfRows = new XLDataGrid(TEST_DATA).getNumberOfRows();
		Assert.assertEquals(3, numberOfRows);
	}
	
	@Test
	public void testNumberOfColumns() {
		int numberOfColumns = new XLDataGrid(TEST_DATA).getNumberOfColumns();
		Assert.assertEquals(4, numberOfColumns);
	}
	
	@Test
	public void testGettingValues() {
		XLDataGrid dataGrid = new XLDataGrid(TEST_DATA);
		
		for (int r=0; r<TEST_DATA.length; r++) {
			for (int c=0; c<TEST_DATA.length; c++) {
				String expected = TEST_DATA[r][c];
				String actual = dataGrid.getValue(r, c);
				Assert.assertEquals(expected, actual);
			}
		}
	}
	
	@Test
	public void testGettingRow() {
		XLDataGrid dataGrid = new XLDataGrid(TEST_DATA);
		String[] row = dataGrid.getRow(1);
		for (int c=0; c<TEST_DATA[0].length; c++) {
			Assert.assertEquals(TEST_DATA[1][c], row[c]);
		}
	}
	
	@Test
	public void testGettingColumn() {
		XLDataGrid dataGrid = new XLDataGrid(TEST_DATA);
		String[] column = dataGrid.getColumn(1);
		for (int r=0; r<TEST_DATA.length; r++) {
			Assert.assertEquals(TEST_DATA[r][1], column[r]);
		}
	}
	
	@Test
	public void testJoinRow() {
		XLDataGrid dataGrid = new XLDataGrid(TEST_DATA);
		String actual = dataGrid.joinRow(2, " | ");
		String expected = "A | B | C | D";
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testJoinRowAll() {
		XLDataGrid dataGrid = new XLDataGrid(TEST_DATA);
		String[] actualArray = dataGrid.joinRowAll(" | ");
		String[] expectedArray = new String[] {
			"a | b | c | d",
			"1 | 2 | 3 | 4",
			"A | B | C | D"
		};
		for (int r=0; r<expectedArray.length; r++) {
			String expected = expectedArray[r];
			String actual = actualArray[r];
			Assert.assertEquals(expected, actual);
		}
	}
	
	@Test
	public void testJoinColumn() {
		XLDataGrid dataGrid = new XLDataGrid(TEST_DATA);
		String actual = dataGrid.joinColumn(2, " | ");
		String expected = "c | 3 | C";
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testJoinColumnAll() {
		XLDataGrid dataGrid = new XLDataGrid(TEST_DATA);
		String[] actualArray = dataGrid.joinColumnAll(" | ");
		String[] expectedArray = new String[] {
			"a | 1 | A",
			"b | 2 | B",
			"c | 3 | C",
			"d | 4 | D"
		};
		for (int r=0; r<expectedArray.length; r++) {
			String expected = expectedArray[r];
			String actual = actualArray[r];
			Assert.assertEquals(expected, actual);
		}
	}

	@Test
	public void testGetRowUniqueList()  {
		XLDataGrid dataGrid = new XLDataGrid(TEST_DATA_WITH_DUPLICATES);
		List<String> actual = dataGrid.getRowUniqueList(2);
		String[] expected = {"A","B"};
		Assert.assertEquals(expected.length, actual.size());
		for (int i=0; i<expected.length; i++) {
			Assert.assertEquals(expected[i], actual.get(i));
		}
	}
	
	@Test
	public void testGetColumnUniqueList()  {
		XLDataGrid dataGrid = new XLDataGrid(TEST_DATA_WITH_DUPLICATES);
		List<String> actual = dataGrid.getColumnUniqueList(1);
		String[] expected = {"2","A"};
		Assert.assertEquals(expected.length, actual.size());
		for (int i=0; i<expected.length; i++) {
			Assert.assertEquals(expected[i], actual.get(i));
		}
	}
	
	@Test
	public void testUniqueList() {
		String[] testData = new String[] {
			"a","b","a","f","f","a","a","a"
		};
		
		String[] expected = new String[] { "a", "b", "f" };
		List<String> actual = XLDataGrid.uniqueList(testData);
		Assert.assertEquals(expected.length, actual.size());
		for (int i=0; i<expected.length; i++) {
			Assert.assertEquals(expected[i], actual.get(i));
		}
	}
	
	@Test
	public void testToString() {
		StringBuffer buf = new StringBuffer();
		buf.append("|          a |          b |          c |          d |\n");
		buf.append("|          1 |          2 |          3 |          4 |\n");
		buf.append("|          A |          B |          C |          D |\n");
		String expected = buf.toString();
		String actual = new XLDataGrid(TEST_DATA).toString();
		System.out.println(actual);
		Assert.assertEquals(expected, actual);
	}
}
