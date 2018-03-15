package gov.usgs.aqcu;


import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import gov.usgs.aqcu.builder.DvHydroReportBuilderService;
import gov.usgs.aqcu.client.JavaToRClient;
import gov.usgs.aqcu.model.DvHydroReport;
import gov.usgs.aqcu.parameter.TssRequestParameters;

@RunWith(SpringRunner.class)
@WebMvcTest(Controller.class)
@AutoConfigureMockMvc(secure=false)
public class ControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private DvHydroReportBuilderService service;

	@MockBean
	private JavaToRClient client;

	@Test
	public void getReportHappyPathTest() throws Exception {
		DvHydroReport reportPojo = new DvHydroReport();
		String reportHtml = "xxx";
		given(service.buildReport(any(TssRequestParameters.class), anyString())).willReturn(reportPojo);
		given(client.render(anyString(), anyString(), anyString())).willReturn(reportHtml.getBytes());

		mvc.perform(get("/dvhydro?primaryTimeseriesIdentifier=a&lastMonths=2&firstStatDerivedIdentifier=aa"))
			.andExpect(status().isOk())
			.andExpect(content().string(reportHtml))
		;

		verify(service).buildReport(any(TssRequestParameters.class), anyString());
		verify(client).render(anyString(), anyString(), anyString());
	}

	@Test
	public void getJsonHappyPathTest() throws Exception {
		DvHydroReport reportPojo = new DvHydroReport();
		given(service.buildReport(any(TssRequestParameters.class), anyString())).willReturn(reportPojo);

		mvc.perform(get("/dvhydro/rawData?primaryTimeseriesIdentifier=a&lastMonths=2&firstStatDerivedIdentifier=aa"))
			.andExpect(status().isOk())
			.andExpect(content().string("{\"firstStatDerived\":{\"type\":\"\",\"estimatedPeriods\":[],\"points\":[]},\"secondStatDerived\":{\"type\":\"\",\"estimatedPeriods\":[],\"points\":[]},\"thirdStatDerived\":{\"type\":\"\",\"estimatedPeriods\":[],\"points\":[]},\"fourthStatDerived\":{\"type\":\"\",\"estimatedPeriods\":[],\"points\":[]},\"firstReferenceTimeSeries\":{\"type\":\"\",\"estimatedPeriods\":[],\"points\":[]},\"secondReferenceTimeSeries\":{\"type\":\"\",\"estimatedPeriods\":[],\"points\":[]},\"thirdReferenceTimeSeries\":{\"type\":\"\",\"estimatedPeriods\":[],\"points\":[]},\"comparisonSeries\":{\"type\":\"\",\"estimatedPeriods\":[],\"points\":[]},\"primaryTsMetadata\":{},\"primarySeriesQualifiers\":[],\"primarySeriesApprovals\":[],\"reportMetadata\":{}}"))
		;

		verify(service).buildReport(any(TssRequestParameters.class), anyString());
	}

	@Test
	public void getReportSadPathTest() throws Exception {
		DvHydroReport reportPojo = new DvHydroReport();
		String reportHtml = "xxx";
		given(service.buildReport(any(TssRequestParameters.class), anyString())).willReturn(reportPojo);
		given(client.render(anyString(), anyString(), anyString())).willReturn(reportHtml.getBytes());

		mvc.perform(get("/dvhydro?lastMonths=2&firstStatDerivedIdentifier=aa"))
			.andExpect(status().isBadRequest())
			.andExpect(content().string(""))
		;

		verify(service, never()).buildReport(any(TssRequestParameters.class), anyString());
		verify(client, never()).render(anyString(), anyString(), anyString());
	}

	@Test
	public void getJsonSadPathTest() throws Exception {
		DvHydroReport reportPojo = new DvHydroReport();
		given(service.buildReport(any(TssRequestParameters.class), anyString())).willReturn(reportPojo);

		mvc.perform(get("/dvhydro/rawData?lastMonths=2&firstStatDerivedIdentifier=aa"))
			.andExpect(status().isBadRequest())
			.andExpect(content().string(""))
		;

		verify(service, never()).buildReport(any(TssRequestParameters.class), anyString());
	}

	@Test
	public void getRequestingUserTest() {
		Controller c = new Controller(null, null, null);
		assertEquals("testUser", c.getRequestingUser());
	}

}
