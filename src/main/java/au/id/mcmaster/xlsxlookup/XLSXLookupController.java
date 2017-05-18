package au.id.mcmaster.xlsxlookup;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import au.id.mcmaster.apoi.tableadapter.XLOptionTree;
import au.id.mcmaster.apoi.tableadapter.XLTable;
import au.id.mcmaster.apoi.tableadapter.XLValueTree;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/xlsx")
@Api(value = "XLSX Lookup", produces = "application/json")
public class XLSXLookupController {
	private static final Logger log = LoggerFactory.getLogger(XLSXLookupController.class);

	private XLSXLookupService lookupService;

	public XLSXLookupController(@Value("${xlsxlookup.spreadsheet.files}") String spreadsheetFileList) {
		log.info("Creating an instance of the XLSXLookupController. Spreadsheet list: " + spreadsheetFileList);
		this.lookupService = new XLSXLookupService(spreadsheetFileList.split(" "));
	}

	@ApiOperation(value = "Get a list of available table names", responseContainer="List", response = String.class, notes = "The tables that are listed are defined in a tab called 'Definitions' tab, within each of the spreadsheets that are loaded.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved list of table names"),
			@ApiResponse(code = 401, message = "You are not authorized to view the list of table names"),
			@ApiResponse(code = 403, message = "Accessing the list of table names was forbidden"),
			@ApiResponse(code = 404, message = "No tables are currently registered") })
	@RequestMapping(value = "/table", method = RequestMethod.GET)
	public Collection<String> table() {
		return lookupService.getTableNames();
	}

	@RequestMapping(value = "/table/{table}/options", method = RequestMethod.GET)
	public Map<String, List<String>> optionsMap(@PathVariable String table) {
		return lookupService.getValueOptionsMap(table);
	}

	@RequestMapping(value = "/table/{table}/optionstree", method = RequestMethod.GET)
	public XLOptionTree optionsTree(@PathVariable String table) {
		return lookupService.getColumnValuesOptionTree(table);
	}

	@RequestMapping(value = "/table/{table}/valuetree", method = RequestMethod.GET)
	public XLValueTree valueTree(@PathVariable String table) {
		return lookupService.getValueTree(table);
	}

	@RequestMapping(value = "/table/{table}/values", method = RequestMethod.GET)
	public Map<String, String> valueMap(@PathVariable String table) {
		return lookupService.getValueMap(table);
	}

	@RequestMapping(value = "/table/{table}/fields", method = RequestMethod.GET)
	public Collection<String> fields(@PathVariable String table) {
		return lookupService.getFieldNames(table);
	}

	@RequestMapping(value = "/table/{table}/value", method = RequestMethod.POST)
	public String tableValue(@PathVariable String table, @RequestBody Map<String, String> queryMap) {
		return lookupService.getValue(table, queryMap);
	}

	@RequestMapping(value = "/lookup", method = RequestMethod.GET)
	public List<String> lookup() {
		return lookupService.getLookupTableNames();
	}

	@RequestMapping(value = "/lookup/{lookup}/fields", method = RequestMethod.GET)
	public Collection<String> lookupFields(@PathVariable String lookup) {
		return lookupService.getLookupTableFields(lookup);
	}

	@RequestMapping(value = "/lookup/{lookup}/value", method = RequestMethod.POST)
	public String lookupValue(@PathVariable String lookup, @RequestBody Map<String, String> queryMap) {
		return lookupService.getLookupValue(lookup, queryMap);
	}

	@RequestMapping(value = "/lookup/{lookup}/options", method = RequestMethod.GET)
	public Map<String, List<String>> lookupOptionsMap(@PathVariable String lookup) {
		return lookupService.getLookupOptionsMap(lookup);
	}

	@RequestMapping(value = "/admin/{table}/definition", method = RequestMethod.GET)
	public XLTable tableDefinition(@PathVariable String table) {
		return lookupService.getTableDefinition(table);
	}

}
