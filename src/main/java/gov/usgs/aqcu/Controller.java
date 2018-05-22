package gov.usgs.aqcu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import gov.usgs.aqcu.builder.ReportBuilderService;
import gov.usgs.aqcu.client.JavaToRClient;
import gov.usgs.aqcu.model.DvHydrographReport;
import gov.usgs.aqcu.parameter.DvHydrographRequestParameters;

@RestController
@RequestMapping("/dvhydro")
public class Controller {
	private Gson gson;
	private ReportBuilderService reportBuilderService;
	private JavaToRClient javaToRClient;

	@Autowired
	public Controller(
			ReportBuilderService reportBuilderService,
			JavaToRClient javaToRClient,
			Gson gson) {
		this.reportBuilderService = reportBuilderService;
		this.javaToRClient = javaToRClient;
		this.gson = gson;
	}

	@GetMapping(produces={MediaType.TEXT_HTML_VALUE})
	public ResponseEntity<?> getReport(@Validated DvHydrographRequestParameters requestParameters) {
		String requestingUser = getRequestingUser();
		DvHydrographReport report = reportBuilderService.buildReport(requestParameters, requestingUser);
		byte[] reportHtml = javaToRClient.render(requestingUser, "dvhydrograph", gson.toJson(report, DvHydrographReport.class));
		return new ResponseEntity<byte[]>(reportHtml, new HttpHeaders(), HttpStatus.OK);
	}

	@GetMapping(value="/rawData", produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<DvHydrographReport> getReportRawData(@Validated DvHydrographRequestParameters requestParameters) {
		DvHydrographReport report = reportBuilderService.buildReport(requestParameters, getRequestingUser());
		return new ResponseEntity<DvHydrographReport>(report, new HttpHeaders(), HttpStatus.OK);
	}

	String getRequestingUser() {
//		CidaAuthAuthenticationToken auth = (CidaAuthAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
//		return auth.getName();
		return "AQCU User";
	}

}
