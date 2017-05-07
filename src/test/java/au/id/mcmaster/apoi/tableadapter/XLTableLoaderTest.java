package au.id.mcmaster.apoi.tableadapter;

import java.util.Collection;
import java.util.Map;

import org.junit.Test;

public class XLTableLoaderTest {
	@Test
	public void testCreate() {
		new XLTableLoader();
	}
	
	@Test
	public void testLoadingTestSet1() {
		new XLTableLoader("workbooks/TestSet1.xlsx");
	}
	
	@Test
	public void testLoadingTesting() {
		new XLTableLoader("workbooks/Testing.xlsx");
	}
	
	@Test
	public void testLoadingTestingPrivate() {
		new XLTableLoader("workbooks/private/Testing-Private.xlsx");
	}
	
	@Test
	public void testLoadingAllOfTheTables() {
		XLTableLoader tableLoader = new XLTableLoader("workbooks/TestSet1.xlsx", "workbooks/Testing.xlsx", "workbooks/private/Testing-Private.xlsx");
		for (String tableName : tableLoader.getTableNames()) {
			System.out.println("Loading Table: " + tableName);
			XLTable table = tableLoader.getTable(tableName);
			System.out.println(table);
		}
	}
	
	@Test
	public void testGrid1() {
		XLTableLoader tableLoader = new XLTableLoader("workbooks/TestSet1.xlsx");
		XLTable table = tableLoader.getTable("Grid - Version 1 - Simple");
		System.out.println(table);
		System.out.println(table.getValueMap());
	}
	
	@Test
	public void testGrid2() {
		XLTableLoader tableLoader = new XLTableLoader("workbooks/TestSet1.xlsx");
		XLTable table = tableLoader.getTable("Grid - Version 2");
		System.out.println(table);
		System.out.println(table.getValueMap());
	}
	
	@Test
	public void testLookup1() {
		XLTableLoader tableLoader = new XLTableLoader("workbooks/TestSet1.xlsx");
		XLTable table = tableLoader.getTable("Lookup - Version 1");
		Map<String,String> valueMap = table.getValueMap();
		System.out.println(valueMap);
	}
	
	@Test
	public void testTableNames() {
		XLTableLoader tableLoader = new XLTableLoader("workbooks/TestSet1.xlsx");
		Collection<String> tableNames = tableLoader.getTableNames();
		System.out.println("Table Names: " + tableNames);
	}
	
	@Test
	public void testLookupNames() {
		XLTableLoader tableLoader = new XLTableLoader("workbooks/TestSet1.xlsx");
		Collection<String> lookupNames = tableLoader.getLookupNames();
		System.out.println("Lookup Names: " + lookupNames);
	}
}
