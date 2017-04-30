package au.id.mcmaster.xlsxlookup;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import au.id.mcmaster.apoi.tableadapter.XLOptionTree;


@RestController
public class XLSXLookupController {
	XLSXLookupService lookupService = new XLSXLookupService();
	
	@RequestMapping(value = "/table", method = RequestMethod.GET)
	public Collection<String> table() {
		return lookupService.getTableNames();
	}
	
	@RequestMapping(value = "/table/{table}/options", method = RequestMethod.GET)
	public Map<String,List<String>> optionsMap(@PathVariable String table) {
		return lookupService.getValueOptionsMap(table);
	}
	
	@RequestMapping(value = "/table/{table}/optionstree", method = RequestMethod.GET)
	public XLOptionTree optionsTree(@PathVariable String table) {
		return lookupService.getColumnValuesOptionTree(table);
	}
	
	@RequestMapping(value = "/table/{table}/values", method = RequestMethod.GET)
	public Map<String,String> valueMap(@PathVariable String table) {
		return lookupService.getValueMap(table);
	}
	
	@RequestMapping(value = "/table/{table}/fields", method = RequestMethod.GET)
	public Collection<String> fields(@PathVariable String table) {
		return lookupService.getFieldNames(table);
	}
	
	@RequestMapping(value="/table/{table}/value", method=RequestMethod.POST)
	public String lookupTableData(@PathVariable String table, @RequestBody Map<String,String> queryMap) {
		return lookupService.getValue(table, queryMap);
	}
}
