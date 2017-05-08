package au.id.mcmaster.apoi.tableadapter;

import org.junit.Assert;
import org.junit.Test;

public class XLTableDefinitionTest {
	
	private static final String TEST_FILES = "workbooks/TestSet1.xlsx";

	@Test
	public void testCreation() {
		new XLTableDefinition(TEST_FILES,"worksheet","table","alias",1,1,5,4,1,4,"grid",1);
	}
	
	@Test
	public void testCreationTestRectangleSections() {

		XLTableDefinition tableDefinition = new XLTableDefinition("WorkbookFile","TabName","TableName","TableAlias",1,1,5,4,1,4,"grid",1);
		
		XLRectangle titleDataRectangle = tableDefinition.getTitleDataRectangle();
		Assert.assertSame(0, titleDataRectangle.getStartX());
		Assert.assertSame(0, titleDataRectangle.getStartY());
		Assert.assertSame(1, titleDataRectangle.getWidth());
		Assert.assertSame(4, titleDataRectangle.getHeight());		

		XLRectangle columnDataRectangle = tableDefinition.getColumnDataRectangle();
		Assert.assertSame(1, columnDataRectangle.getStartX());
		Assert.assertSame(0, columnDataRectangle.getStartY());
		Assert.assertSame(5, columnDataRectangle.getWidth());
		Assert.assertSame(4, columnDataRectangle.getHeight());
		
		XLRectangle rowDataRectangle = tableDefinition.getRowDataRectangle();
		Assert.assertSame(0, rowDataRectangle.getStartX());
		Assert.assertSame(4, rowDataRectangle.getStartY());
		Assert.assertSame(1, rowDataRectangle.getWidth());
		Assert.assertSame(4, rowDataRectangle.getHeight());
		
		XLRectangle valueDataRectangle = tableDefinition.getValueDataRectangle();
		Assert.assertSame(1, valueDataRectangle.getStartX());
		Assert.assertSame(4, valueDataRectangle.getStartY());
		Assert.assertSame(5, valueDataRectangle.getWidth());
		Assert.assertSame(4, valueDataRectangle.getHeight());		

		Assert.assertSame("WorkbookFile", tableDefinition.getWorkbookName());
		Assert.assertSame("TableName", tableDefinition.getTableName());
		Assert.assertSame("TabName", tableDefinition.getWorksheetName());
		Assert.assertSame("TableAlias", tableDefinition.getTableAlias());
	}

}
