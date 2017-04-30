package au.id.mcmaster.apoi.tableadapter;

import java.util.Set;

import org.apache.commons.collections4.MapUtils;
import org.junit.Test;

public class XLOptionTreeTest {
	@Test
	public void testCreate() {
		new XLOptionTree();
	}
	
	/**
	 * |      BCD:1 |      BCD:1 |      BCD:1 |        E:1 |        F:1 |
	 * |       BC:2 |       BC:2 |       DE:2 |       DE:2 |        F:2 |
	 * |        B:3 |     CDEF:3 |     CDEF:3 |     CDEF:3 |     CDEF:3 |
	 * |    BCDEF:4 |    BCDEF:4 |    BCDEF:4 |    BCDEF:4 |    BCDEF:4 |
	 */
	@SuppressWarnings("serial")
	@Test
	public void testPopulate() {
		String[][] testData = new String[][] {
			new String[] { "BCD:1", "BC:2", "B:3",    "BCDEF:4"},
			new String[] { "BCD:1", "BC:2", "CDEF:3", "BCDEF:4"},
			new String[] { "BCD:1", "DE:2", "CDEF:3", "BCDEF:4"},
			new String[] { "E:1", 	"DE:2", "CDEF:3", "BCDEF:4"},
			new String[] { "F:1", 	"F:2",  "CDEF:3", "BCDEF:4"}
		};
		
		XLOptionTree optionsTree = new XLOptionTree();
		for (String[] column : testData) {
			optionsTree.addOptions(column);
		}
		
		XLOptionTree map = new XLOptionTree();
		map.put("BCD:1", new XLOptionTree() {
			{
				put("BC:2", new XLOptionTree() {
					{
						put("B:3", new XLOptionTree() {
							{
								put("BCDEF:4", null);
							}
						});
						put("CDEF:3", new XLOptionTree() {
							{
								put("BCDEF:4", null);
							}
						});
					}
				});
				put("DE:2", new XLOptionTree() {
					{
						put("CDEF:3", new XLOptionTree() {
							{
								put("BCDEF:4", null);
							}
						});
					}
				});
			}
		});
		map.put("E:1", new XLOptionTree() {
			{
				put("DE:2", new XLOptionTree() {
					{
						put("CDEF:3", new XLOptionTree() {
							{
								put("BCDEF:4", null);
							}
						});
					}
				});
			}
		});
		map.put("F:1", new XLOptionTree() {
			{
				put("F:2", new XLOptionTree() {
					{
						put("CDEF:3", new XLOptionTree() {
							{
								put("BCDEF:4", null);
							}
						});
					}
				});
			}
		});
		
		MapUtils.debugPrint(System.out, "myMap",map);
		
		System.out.println(map.getOptions("BCD:1","BC:2"));
		System.out.println(optionsTree.getOptions("BCD:1","BC:2"));
		
		System.out.println(optionsTree.getOptions());
		
//		XLOptionTree optionTree = XLOptionTree.create(testData);
//		
//		System.out.println(optionTree);		
	}
}
