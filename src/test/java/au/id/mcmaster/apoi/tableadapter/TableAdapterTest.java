package au.id.mcmaster.apoi.tableadapter;

import static org.junit.Assert.fail;

import java.util.Map;

import org.junit.Test;

public class TableAdapterTest {
	private static final String FILE_NAME = "workbooks/MyFirstExcel.xlsx";

	@Test
	public void test() throws Exception {
		TableDefinition tableDefinition = new TableDefinition("workbooks/Testing.xlsx","Test1",1,2,24,7,1,5);
		TableAdapter tableAdapter = new TableAdapter(tableDefinition);
		tableAdapter.test();
	}
	
	@Test
	public void testTopLeftCorner() {
		TableDefinition tableDefinition = new TableDefinition(FILE_NAME,"Complex",1,1,5,4,1,4);
		printDataSets(tableDefinition);
	}

	@Test
	public void testMiddleOfSheet() {
		TableDefinition tableDefinition = new TableDefinition(FILE_NAME,"Complex",2,14,5,4,1,4);
		printDataSets(tableDefinition);
	}

	@Test
	public void testComplex() {
		TableDefinition tableDefinition = new TableDefinition("workbooks/Testing.xlsx", "Testing",1,2,36,8,1,5);
		printDataSets(tableDefinition);
	}

	private void printDataSets(TableDefinition tableDefinition) {
		try
		{
			TableAdapter tableAdapter = new TableAdapter(tableDefinition);
			tableAdapter.flattenMergedCells();
			
			String[][] columnData = tableAdapter.getColumnData();
			System.out.println("== Column Data ==");
			print2dStringArrayAsTable(columnData);
			
			String[][] rowData = tableAdapter.getRowData();
			System.out.println("== Row Data ==");
			print2dStringArrayAsTable(rowData);
			
			String[][] valueData = tableAdapter.getValueData();
			System.out.println("== Value Data ==");
			print2dStringArrayAsTable(valueData);
			
			Map<String,String> valueMap = tableAdapter.getValueMap();
			for (String key : valueMap.keySet()) {
				System.out.println(String.format("%s = %s", key, valueMap.get(key)));
			}
			System.out.println("Number of Values: " + valueMap.keySet().size());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail("Should not have thrown an exception: " + e.getMessage());
		}
	}

	private void print2dStringArrayAsTable(String[][] data) {
		for (String[] row : data)
		{
			System.out.print("| ");
			for (int i=0; i<row.length; i++) {
				if (i > 0) {
					System.out.print("| ");
				}
				System.out.print(String.format("%10.10s ", row[i]));
			}
			System.out.println("|");
		}
	}
}
