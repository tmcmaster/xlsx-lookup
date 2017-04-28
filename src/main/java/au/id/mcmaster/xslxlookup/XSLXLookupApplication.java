package au.id.mcmaster.xslxlookup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import au.id.mcmaster.apoi.tableadapter.XLTable;
import au.id.mcmaster.apoi.tableadapter.XLTableDefinition;

@SpringBootApplication
public class XSLXLookupApplication {

	public static void main(String[] args) {
		SpringApplication.run(XSLXLookupApplication.class, args);
	}
}



