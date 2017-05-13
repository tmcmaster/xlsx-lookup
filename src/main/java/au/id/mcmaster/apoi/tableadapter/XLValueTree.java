package au.id.mcmaster.apoi.tableadapter;

import java.util.Arrays;
import java.util.HashMap;


interface ValueTree
{
	public String getValue();
	public String getValue(String[] keys);
	public void setValue(String value);
	public void setValue(String[] keys, String value);
	public ValueTree get(String key);
	public ValueTree put(String key, ValueTree value);
}

class ValueTreeValue implements ValueTree {

	private String value = null;
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	
	@Override
	public String getValue(String[] keys) {
		throw new RuntimeException("This method should not be being called.");
	}


	@Override
	public void setValue(String[] keys, String value) {
		throw new RuntimeException("This method should not be being called.");
	}

	@Override
	public ValueTree get(String key) {
		throw new RuntimeException("This method should not be being called.");
	}

	@Override
	public ValueTree put(String key, ValueTree value) {
		throw new RuntimeException("This method should not be being called.");
	}	
}

@SuppressWarnings("serial")
public class XLValueTree extends HashMap<String,ValueTree> implements ValueTree {
	public String getValue() {
		throw new RuntimeException("This method should not be being called.");
	}

	public void setValue(String value) {
		throw new RuntimeException("This method should not be being called.");
	}

	public ValueTree get(String key) {
		return super.get(key);
	}
	
	public ValueTree put(String key, ValueTree value)
	{
		return super.put(key,value);
	}

	public String getValue(String[] strings) {
		if (strings.length == 0) {
			return null;
		}
		else
		{
			ValueTree child = get(strings[0]);
			if (child == null) {
				child = get("Any");
			}
			
			if (child == null) {
				return null;
			}
			else if (strings.length == 1)
			{
				return child.getValue();
			}
			else
			{
				return child.getValue(Arrays.copyOfRange(strings, 1, strings.length));		
			}
		}
	}

	public void setValue(String[] strings, String value) {
		if (strings.length == 0) {
			return;
		} else {
			String string = strings[0];
			String[] parts = (string.indexOf(",") > 0 ? string.split(",") : new String[] {string});
			for (String part : parts) {
				
				if (strings.length == 1)
				{
					ValueTree child = get(part);
					if (child == null) {
						child = new ValueTreeValue();
						put(part, child);
					}
					child.setValue(value);
				}
				else
				{
					ValueTree child = get(part);
					if (child == null) {
						child = new XLValueTree();
						put(part, child);
					}
					child.setValue(Arrays.copyOfRange(strings, 1, strings.length), value);
				}
			}
		}
	}
}
