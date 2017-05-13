package au.id.mcmaster.apoi.tableadapter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class XLOptionTree extends HashMap<String,XLOptionTree>{

	private static final XLOptionTree LEAF = new XLOptionTree();
	
	public Set<String> getOptions(String... strings) {
		if (strings.length == 0) {
			return keySet();
		}
		else
		{
			XLOptionTree child = get(strings[0]);
			if (child == LEAF || child == null) {
				return new HashSet<String>();
			}
			
			if (strings.length == 1)
			{
				return child.keySet();
			}
			else
			{
				String[] subString = Arrays.copyOfRange(strings, 1, strings.length);
				return child.getOptions(subString);		
			}
		}
	}

	public void addOptions(String... strings) {
		if (strings.length == 0) {
			return;
		} else {
			String string = strings[0];
			String[] parts = (string.indexOf(",") > 0 ? string.split(",") : new String[] {string});
			for (String part : parts) {
				XLOptionTree child = get(part);
				if (strings.length == 1) {
					if (child == null) {
						put(part, LEAF);
					}
				}
				else
				{
					if (child == null) {
						child = new XLOptionTree();
						put(part, child);
					}
					String[] subString = Arrays.copyOfRange(strings, 1, strings.length);
					child.addOptions(subString);
				}
			}
		}
	}
}