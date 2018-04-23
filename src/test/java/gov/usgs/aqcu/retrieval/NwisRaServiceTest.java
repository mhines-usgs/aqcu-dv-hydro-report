package gov.usgs.aqcu.retrieval;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import gov.usgs.aqcu.ObjectCompare;
import gov.usgs.aqcu.client.NwisRaClient;
import gov.usgs.aqcu.model.WaterLevelRecord;
import gov.usgs.aqcu.model.WaterQualitySampleRecord;
import gov.usgs.aqcu.model.WqValue;
import gov.usgs.aqcu.model.nwis.GroundWaterParameter;
import gov.usgs.aqcu.model.nwis.ParameterRecord;
import gov.usgs.aqcu.parameter.DvHydrographRequestParameters;

@RunWith(SpringRunner.class)
public class NwisRaServiceTest {

	@MockBean
	private NwisRaClient nwisRaClient;

	private NwisRaService service;
	private DvHydrographRequestParameters parameters;

	@Before
	public void setup() throws Exception {
		service = new NwisRaService(nwisRaClient);
		parameters = new DvHydrographRequestParameters();
		parameters.setStartDate(LocalDate.of(2018, 4, 1));
		parameters.setEndDate(LocalDate.of(2018, 4, 23));
	}

	@Test
	public void getPartialDateStringTest() throws Exception {
		parameters.setStartDate(LocalDate.of(2018, 4, 1));
		parameters.setEndDate(LocalDate.of(2018, 4, 23));
		assertEquals("20180401,20180423", service.getPartialDateString(parameters, ZoneOffset.UTC));
	}

	@Test
	public void getAqParameterUnitsTest() {
		given(nwisRaClient.getParameters(NwisRaService.AQ_PARAMS_FILTER_VALUE))
				.willReturn(getAqParameterUnitsResponseEntity());
		List<ParameterRecord> expected = Stream.of(getParameterRecord("A"), getParameterRecord("B"))
				.collect(Collectors.toList());
		List<ParameterRecord> actual = service.getAqParameterUnits();
		ObjectCompare.compare(expected, actual);
	}

	@Test
	public void getAqParameterNamesTest() {
		given(nwisRaClient.getParameters(NwisRaService.AQ_NAME_PARAMS_FILTER_VALUE))
				.willReturn(getAqParameterNamesResponseEntity());
		List<ParameterRecord> expected = Stream.of(getParameterRecord("C"), getParameterRecord("D"))
				.collect(Collectors.toList());
		List<ParameterRecord> actual = service.getAqParameterNames();
		ObjectCompare.compare(expected, actual);
	}

	@Test
	public void getGwLevelsTest() {
		given(nwisRaClient.getWaterLevelRecords(anyString(), anyString(), anyString(), anyString(), anyString()))
				.willReturn(getGwLevelsResponseEntity());
		List<WaterLevelRecord> expected = Stream.of(
				getWaterLevelRecord(BigDecimal.ONE, OffsetDateTime.of(2018, 04, 02, 13, 12, 0, 0, ZoneOffset.of("-6"))),
				getWaterLevelRecord(BigDecimal.TEN, OffsetDateTime.of(2018, 04, 12, 13, 15, 0, 0, ZoneOffset.of("-6"))))
				.collect(Collectors.toList());
		List<WaterLevelRecord> actual = service.getGwLevels(parameters, "123", GroundWaterParameter.FAQ209,
				ZoneOffset.UTC);
		ObjectCompare.compare(expected, actual);
		verify(nwisRaClient).getWaterLevelRecords("123", NwisRaService.GW_LEV_COLUMN_GROUPS_TO_RETRIEVE,
				"20180401,20180423", "S", "LMSL");
	}

	@Test
	public void getQwDataTest() {
		given(nwisRaClient.getWaterQualitySampleRecords(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
				.willReturn(getQwDataResponseEntity());
		List<WaterQualitySampleRecord> expected = Stream.of(
				getWaterQualitySampleRecord("a", OffsetDateTime.of(2018, 04, 02, 13, 12, 0, 0, ZoneOffset.of("-6")), "00600", BigDecimal.ONE),
				getWaterQualitySampleRecord("b", OffsetDateTime.of(2018, 04, 12, 13, 15, 0, 0, ZoneOffset.of("-6")), "00600", BigDecimal.TEN))
				.collect(Collectors.toList());
		List<WaterQualitySampleRecord> actual = service.getQwData(parameters, "123", "00600", ZoneOffset.UTC);
		ObjectCompare.compare(expected, actual);
		verify(nwisRaClient).getWaterQualitySampleRecords("123", NwisRaService.QW_COLUMN_GROUPS_TO_RETRIEVE,
				"true", "true", "00600", "00600", "20180401,20180423");
	}

	private ResponseEntity<String> getAqParameterUnitsResponseEntity() {
		return new ResponseEntity<String>("{\"records\": ["
				+ "{\"PARM_CD\":\"A\", \"PARM_NM\":\"nameA\", \"PARM_ALIAS_NM\":\"aliasA\", \"PARM_UNT_TX\":\"unitA\", \"PARM_DS\":\"descA\", \"PARM_MEDIUM_TX\":\"mediumA\"}"
				+ ",{\"PARM_CD\":\"B\", \"PARM_NM\":\"nameB\", \"PARM_ALIAS_NM\":\"aliasB\", \"PARM_UNT_TX\":\"unitB\", \"PARM_DS\":\"descB\", \"PARM_MEDIUM_TX\":\"mediumB\"}"
				+ " ]}", HttpStatus.OK);
	}

	private ResponseEntity<String> getAqParameterNamesResponseEntity() {
		return new ResponseEntity<String>("{\"records\": ["
				+ "{\"PARM_CD\":\"C\", \"PARM_NM\":\"nameC\", \"PARM_ALIAS_NM\":\"aliasC\", \"PARM_UNT_TX\":\"unitC\", \"PARM_DS\":\"descC\", \"PARM_MEDIUM_TX\":\"mediumC\"}"
				+ ",{\"PARM_CD\":\"D\", \"PARM_NM\":\"nameD\", \"PARM_ALIAS_NM\":\"aliasD\", \"PARM_UNT_TX\":\"unitD\", \"PARM_DS\":\"descD\", \"PARM_MEDIUM_TX\":\"mediumD\"}"
				+ " ]}", HttpStatus.OK);
	}

	private ResponseEntity<String> getGwLevelsResponseEntity() {
		return new ResponseEntity<String>("{\"records\": ["
				+ "{\"SITE_NO\":\"123\", \"LEV_TZ_CD\":\"CST\", \"LEV_VA\": \"\", \"SL_LEV_VA\":\"1\", \"GW_LOCAL_LEV_DT\":\"20180402\", \"GW_LOCAL_LEV_TM\":\"1312\"}"
				+ ",{\"SITE_NO\":\"123\", \"LEV_TZ_CD\":\"CST\", \"LEV_VA\": \"\", \"SL_LEV_VA\":\"10\", \"GW_LOCAL_LEV_DT\":\"20180412\", \"GW_LOCAL_LEV_TM\":\"1315\"}"
				+ " ]}", HttpStatus.OK);
	}

	private ResponseEntity<String> getQwDataResponseEntity() {
		return new ResponseEntity<String>("{\"records\": ["
				+ "{\"QW_RECORD_NO\":\"a\", \"QW_MEDIUM_NM\":\"medium\", \"QW_SAMPLE_START_TZ_CD\": \"CST\", \"QW_SAMPLE_START_LOCAL_DISP_FM\":\"201804021312\", \"P00600\":\"1.0\", \"R00600\":\"R00600\"}"
				+ ",{\"QW_RECORD_NO\":\"b\", \"QW_MEDIUM_NM\":\"medium\", \"QW_SAMPLE_START_TZ_CD\": \"CST\", \"QW_SAMPLE_START_LOCAL_DISP_FM\":\"201804121315\", \"P00600\":\"10.0\", \"R00600\":\"R00600\"}"
				+ " ]}", HttpStatus.OK);
	}

	private ParameterRecord getParameterRecord(String code) {
		ParameterRecord parameterRecord = new ParameterRecord();
		parameterRecord.setCode(code);
		parameterRecord.setName("name" + code);
		parameterRecord.setAlias("alias" + code);
		parameterRecord.setUnit("unit" + code);
		parameterRecord.setDescription("desc" + code);
		parameterRecord.setMedium("medium" + code);
		return parameterRecord;
	}

	private WaterLevelRecord getWaterLevelRecord(BigDecimal groundWaterLevel, OffsetDateTime recordDateTime) {
		WaterLevelRecord waterLevelRecord = new WaterLevelRecord();
		waterLevelRecord.setSiteNumber("123");
		waterLevelRecord.setGroundWaterLevel(groundWaterLevel);
		waterLevelRecord.setDate(recordDateTime);
		waterLevelRecord.setTimeZone("CST");
		return waterLevelRecord;
	}

	private WaterQualitySampleRecord getWaterQualitySampleRecord(String recordNumber, OffsetDateTime sampleStartTemporal, String pcode, BigDecimal value) {
		WaterQualitySampleRecord waterQualitySampleRecord = new WaterQualitySampleRecord();
		waterQualitySampleRecord.setRecordNumber(recordNumber);
		waterQualitySampleRecord.setMedium("medium");
		waterQualitySampleRecord.setSampleStartDateTime(sampleStartTemporal);
		waterQualitySampleRecord.setTimeZone("CST");
		WqValue wqValue = new WqValue();
		wqValue.setParameter(pcode);
		wqValue.setValue(value);
		wqValue.setRemark("R" + pcode);
		waterQualitySampleRecord.setValue(wqValue);
		return waterQualitySampleRecord;
	}
}
