package au.id.mcmaster.apoi.tableadapter;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

public class XLTableTest {
	private static final String FILE_NAME = "workbooks/TestSet1.xlsx";
	private static XLTableDefinition TABLE_ADAPTER = new XLTableDefinition(FILE_NAME,"TableAdapter",1,1,5,4,1,4);
	private static XLTableDefinition TABLE_ADAPTER2 = new XLTableDefinition(FILE_NAME,"TableAdapter2",3,8,5,3,1,4);

	@Test
	public void testCreate() {
		try {
			new XLTable(TABLE_ADAPTER);
			new XLTable(TABLE_ADAPTER2);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Should not have thrown an exception: " + e.getMessage());
		}
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
}
