package au.id.mcmaster.xlsxlookup;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import au.id.mcmaster.apoi.tableadapter.XLOptionTree;


@RestController
@RequestMapping("/xlsx")
public class XLSXLookupController {
	private XLSXLookupService lookupService;
	
	public XLSXLookupController(@Value("${xlsxlookup.spreadsheet.files}") String spreadsheetFileList) {
		this.lookupService = new XLSXLookupService(spreadsheetFileList.split(" "));
	}
	
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
	public String tableValue(@PathVariable String table, @RequestBody Map<String,String> queryMap) {
		return lookupService.getValue(table, queryMap);
	}
	
	@RequestMapping(value="/lookup", method=RequestMethod.GET)
	public List<String> lookup() {
		return lookupService.getLookupTableNames();
	}

	@RequestMapping(value = "/lookup/{lookup}/fields", method = RequestMethod.GET)
	public Collection<String> lookupFields(@PathVariable String lookup) {
		return lookupService.getLookupTableFields(lookup);
	}
	
	@RequestMapping(value="/lookup/{lookup}/value", method=RequestMethod.POST)
	public String lookupValue(@PathVariable String lookup, @RequestBody Map<String,String> queryMap) {
		return lookupService.getLookupValue(lookup, queryMap);
	}
}
