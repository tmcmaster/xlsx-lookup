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
			XLOptionTree child = get(strings[0]);
			if (strings.length == 1) {
				if (child == null) {
					put(strings[0], LEAF);
				}
			}
			else
			{
				if (child == null) {
					child = new XLOptionTree();
					put(strings[0], child);
				}
				String[] subString = Arrays.copyOfRange(strings, 1, strings.length);
				child.addOptions(subString);
			}
		}
	}
}