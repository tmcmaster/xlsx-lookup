package au.id.mcmaster.apoi.tableadapter;

public class XLLookup {
	private String name;
	private String value;
	public XLLookup(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "XLLookup [name=" + name + ", value=" + value + "]";
	}
}
