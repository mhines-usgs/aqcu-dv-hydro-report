package gov.usgs.aqcu;

import java.util.List;
import java.time.Instant;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.format.annotation.DateTimeFormat;

import com.aquaticinformatics.aquarius.sdk.timeseries.serializers.InstantDeserializer;

import gov.usgs.aqcu.builder.DvHydroReportBuilderService;
import gov.usgs.aqcu.client.JavaToRClient;
import gov.usgs.aqcu.model.DvHydroReport;

@RestController
@Validated
@RequestMapping("/dvhydro")
public class Controller {
	private static final Logger LOG = LoggerFactory.getLogger(Controller.class);
	private Gson gson;
	private DvHydroReportBuilderService reportBuilderService;
	private JavaToRClient javaToRClient;

	@Autowired
	public Controller(
		DvHydroReportBuilderService reportBuilderService,
		JavaToRClient javaToRClient,
		Gson gson) {
		this.reportBuilderService = reportBuilderService;
		this.javaToRClient = javaToRClient;
		this.gson = gson;
	}
	
	@GetMapping(produces={MediaType.TEXT_HTML_VALUE})
	public ResponseEntity<?> getReport(
			@RequestParam String primaryTimeseriesIdentifier,
			@RequestParam(required=false) String firstStatDerivedIdentifier,
			@RequestParam(required=false) String secondStatDerivedIdentifier,
			@RequestParam(required=false) String thirdStatDerivedIdentifier,
			@RequestParam(required=false) String fourthStatDerivedIdentifier,
			@RequestParam(required=false) String firstReferenceIdentifier,
			@RequestParam(required=false) String secondReferenceIdentifier,
			@RequestParam(required=false) String thirdReferenceIdentifier,
			@RequestParam(required=false) String comparisonTimeseriesIdentifier,
			@RequestParam(required=true) @DateTimeFormat(pattern=InstantDeserializer.Pattern) Instant startDate,
			@RequestParam(required=true) @DateTimeFormat(pattern=InstantDeserializer.Pattern) Instant endDate,
			@RequestParam(required=false) List<String> excludedCorrections) throws Exception {
		//Pull Requesting User From Headers
		String requestingUser = "testUser";
		
		//Build the DV Hydro Report JSON
		DvHydroReport report = reportBuilderService.buildReport(
			primaryTimeseriesIdentifier, 
			firstStatDerivedIdentifier,
			secondStatDerivedIdentifier,
			thirdStatDerivedIdentifier,
			fourthStatDerivedIdentifier,
			firstReferenceIdentifier,
			secondReferenceIdentifier,
			thirdReferenceIdentifier,
			comparisonTimeseriesIdentifier,
			startDate, 
			endDate, 
			requestingUser);
		
		byte[] reportHtml = javaToRClient.render(requestingUser, "dvhydro", gson.toJson(report, DvHydroReport.class));
		return new ResponseEntity<byte[]>(reportHtml, new HttpHeaders(), HttpStatus.OK);
	}
	
	@GetMapping(value="/rawData", produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<DvHydroReport> getReportRawData(
			@RequestParam String primaryTimeseriesIdentifier,
			@RequestParam(required=false) String firstStatDerivedIdentifier,
			@RequestParam(required=false) String secondStatDerivedIdentifier,
			@RequestParam(required=false) String thirdStatDerivedIdentifier,
			@RequestParam(required=false) String fourthStatDerivedIdentifier,
			@RequestParam(required=false) String firstReferenceIdentifier,
			@RequestParam(required=false) String secondReferenceIdentifier,
			@RequestParam(required=false) String thirdReferenceIdentifier,
			@RequestParam(required=false) String comparisonTimeseriesIdentifier,
			@RequestParam(required=true) @DateTimeFormat(pattern=InstantDeserializer.Pattern) Instant startDate,
			@RequestParam(required=true) @DateTimeFormat(pattern=InstantDeserializer.Pattern) Instant endDate,
			@RequestParam(required=false) List<String> excludedCorrections) throws Exception {
		//Pull Requesting User From Headers
		String requestingUser = "testUser";
		
		//Build the DV Hydro Report JSON
		DvHydroReport report = reportBuilderService.buildReport(
			primaryTimeseriesIdentifier, 
			firstStatDerivedIdentifier,
			secondStatDerivedIdentifier,
			thirdStatDerivedIdentifier,
			fourthStatDerivedIdentifier,
			firstReferenceIdentifier,
			secondReferenceIdentifier,
			thirdReferenceIdentifier,
			comparisonTimeseriesIdentifier,
			startDate, 
			endDate, 
			requestingUser);
		
		return new ResponseEntity<DvHydroReport>(report, new HttpHeaders(), HttpStatus.OK);
	}
}
