package au.id.mcmaster.apoi.tableadapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class XLDataGrid {
	private String[][] data;
	
	public XLDataGrid(String[][] data) {
		this.data = data;
	}
	
	public int getNumberOfRows() {
		return data.length;
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
				buffer.append(" | ");
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
		return uniqueList(values);
	}
	
	public List<String> getColumnUniqueList(int column) {
		String[] values = getColumn(column);
		return uniqueList(values);
	}
	
	public static List<String> uniqueList(String[] data) {
		List<String> results = new ArrayList<String>();
		Set<String> set = new HashSet<String>();
		for (String item : data) {
			if (!set.contains(item))
			{
				set.add(item);
				results.add(item);
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
