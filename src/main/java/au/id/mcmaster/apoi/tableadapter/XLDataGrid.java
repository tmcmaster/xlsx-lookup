package au.id.mcmaster.apoi.tableadapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class XLDataGrid {
	public static final XLDataGrid EMPTY = new XLDataGrid(new String[0][0]);
	
	private String[][] data;
	
	public XLDataGrid(String[][] data) {
		this.data = data;
	}
	
	public int getNumberOfRows() {
		return data.length;
	}
	
	public int getNumberOfColumns() {
		return data[0].length;
	}
	
	public String getValue(int row, int column) {
		return data[row][column];
	}
	
	public String[] getRow(int row) {
		return data[row];
	}
	
	public String[] joinRowAll(String seperator) {
		String[] rowDataAll = new String[data.length];
		for (int i=0; i<data.length; i++) {
			rowDataAll[i] = joinRow(i, seperator);
		}
		return rowDataAll;
	}
	
	public String joinRow(int row, String seperator) {
		String[] rowData = getRow(row);
		return join(rowData, seperator);
		
	}
	
	public String[] joinColumnAll(String seperator) {
		if (data.length == 0) {
			return new String[0];
		}
		String[] columnDataAll = new String[data[0].length];
		for (int i=0; i<data[0].length; i++) {
			columnDataAll[i] = joinColumn(i, seperator);
		}
		return columnDataAll;
	}
	
	public String joinColumn(int column, String seperator) {
		String[] columnData = getColumn(column);
		return join(columnData, seperator);
	}	
	
	private static String join(String data[], String seperator) {
		StringBuffer buffer = new StringBuffer();
		for (int i=0; i<data.length; i++) {
			if (i > 0) {
				buffer.append(seperator);
			}
			buffer.append(data[i]);
		}
		return buffer.toString();
	}
	
	public String[] getColumn(int column) {
		String[] columnData = new String[data.length];
		for (int i=0; i<data.length; i++) {
			columnData[i] = data[i][column];
		}
		return columnData;
	}
	
	public List<String> getRowUniqueList(int row) {
		String[] values = getRow(row);
		return uniqueList(values, true);
	}
	
	public List<String> getColumnUniqueList(int column) {
		String[] values = getColumn(column);
		return uniqueList(values, true);
	}
	
	public static List<String> uniqueList(String[] data, boolean split) {
		List<String> results = new ArrayList<String>();
		Set<String> set = new HashSet<String>();
		for (String item : data) {
			if (split) {
				String[] itemParts = item.split(",");
				for (String part : itemParts) {
					part = part.trim();
					if (!set.contains(part))
					{
						set.add(part);
						results.add(part);
					}
				}
			}
			else
			{
				if (!set.contains(item))
				{
					set.add(item);
					results.add(item);
				}
			}
		}
		return results;
	}
	
	@Override
	public String toString()
	{
		StringBuffer buffer = new StringBuffer();
		
		for (String[] row : data)
		{
			buffer.append("| ");
			for (int i=0; i<row.length; i++) {
				if (i > 0) {
					buffer.append("| ");
				}
				buffer.append(String.format("%10.10s ", row[i]));
			}
			buffer.append("|\n");
		}
		
		return buffer.toString();
	}
}
