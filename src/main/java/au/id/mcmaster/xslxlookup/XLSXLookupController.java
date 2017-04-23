package au.id.mcmaster.xslxlookup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class XLSXLookupController {
	XLSXLookupService lookupService = new XLSXLookupService();
	
	@RequestMapping("/greeting")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name) {
        return "greeting " + name;
    }
	
	@RequestMapping(value = "/lookup/table", method = RequestMethod.GET)
	public List<String> table() {
		List<String> list = new ArrayList<String>();
		Collections.addAll(list, new String[] { "Test1", "Test2" });
		return list;
	}
	
	@RequestMapping("/Test1")
	public String lookup(@RequestParam String policyNumber, 
						 @RequestParam String division,
						 @RequestParam String category,
						 @RequestParam String occupation, 
						 @RequestParam String waitingPeriod,
						 @RequestParam String benifitPeriod,
						 @RequestParam String coverType,
						 @RequestParam String policyPeriod,
						 @RequestParam String age) {
		
		System.out.println(String.format(" policyNumber = %s", policyNumber));
		System.out.println(String.format("     division = %s", division));
		System.out.println(String.format("     category = %s", category));
		System.out.println(String.format("   occupation = %s", occupation));
		System.out.println(String.format("waitingPeriod = %s", waitingPeriod));
		System.out.println(String.format("benifitPeriod = %s", benifitPeriod));
		System.out.println(String.format("    coverType = %s", coverType));
		System.out.println(String.format(" policyPeriod = %s", policyPeriod));
		System.out.println(String.format("          age = %s", age));
		
		return lookupService.getValue("Test1", age, policyNumber, division, category, occupation, waitingPeriod, benifitPeriod, coverType, policyPeriod);
	}
	
	@RequestMapping("/Test2")
	public String lookup(@RequestParam String a, 
						 @RequestParam String b,
						 @RequestParam String c,
						 @RequestParam String d,
						 @RequestParam String r) {
		
		System.out.println(String.format("a = %s", a));
		System.out.println(String.format("b = %s", b));
		System.out.println(String.format("c = %s", c));
		System.out.println(String.format("d = %s", d));
		System.out.println(String.format("r = %s", r));
		
		return lookupService.getValue("Test2", r, a, b, c, d);
	}
}
