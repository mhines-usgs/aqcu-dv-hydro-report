# aqcu-dv-hydro-report

Aquarious Customization DV Hydrograph Report

It is built as a war to be deployed into a Tomcat container.

Configured functionality includes:

- **Swagger Api Documentation** Located at https://localhost:8443/aqcu-dv-hydro-report/swagger-ui.html
- **Hystrix Dashboard** Located at https://localhost:8443/aqcu-dv-hydro-report/hystrix/monitor?stream=https%3A%2F%2Flocalhost%3A8443%2Faqcu-dv-hydro-report%2Fhystrix.stream%20

## Running the Application

The war built using maven can be deployed the same as any other to a Tomcat instance. Some additional configurations are needed before starting Tomcat:

- At this time, it only works when deployed with all the other aqcu modules. To wire it into them, modify the context.xml's "aqcu.reports.webservice" value to "https://localhost:8443/aqcu-gateway/aqcu-webservice"
- Add ```<Parameter name="spring.config.location" value="${catalina.base}/conf/aqcu-dv-hydro-report-application.yml" />``` to the context.xml file
- Copy aqcu-dv-hydro-report-application.yml to the conf folder and adjust any values as required.

https://nwissddvasaqcu.cr.usgs.gov:8443/aqcu-gateway/aqcu-webservice/service/reports/dvhydrograph/?primaryTimeseriesIdentifier=9461633ffc1d478fb8b566f64d325b62&firstStatDerivedIdentifier=dba053add97a4434b21616fbbd39d822&secondStatDerivedIdentifier=&thirdStatDerivedIdentifier=&fourthStatDerivedIdentifier=&firstReferenceIdentifier=&secondReferenceIdentifier=&thirdReferenceIdentifier=&station=01010000&lastMonths=3


https://reporting-qa.nwis.usgs.gov/timeseries-ws/service/reports/dvhydrograph/?primaryTimeseriesIdentifier=21697b76433043c4951e0ec49545d132&firstStatDerivedIdentifier=8140746f1a5346fa9460fddad3c28a16&secondStatDerivedIdentifier=&thirdStatDerivedIdentifier=&fourthStatDerivedIdentifier=&firstReferenceIdentifier=&secondReferenceIdentifier=&thirdReferenceIdentifier=&station=302948095422501&lastMonths=12




https://localhost:8880/aqcu-gateway/service/reports/dvhydrograph/?primaryTimeseriesIdentifier=21697b76433043c4951e0ec49545d132&firstStatDerivedIdentifier=8140746f1a5346fa9460fddad3c28a16&secondStatDerivedIdentifier=&thirdStatDerivedIdentifier=&fourthStatDerivedIdentifier=&firstReferenceIdentifier=&secondReferenceIdentifier=&thirdReferenceIdentifier=&station=302948095422501&waterYear=2016&token=2ad71416-161d-494d-b98d-3460d6b1a940


https://localhost:8880/aqcu-gateway/service/reports/dvhydrograph/?primaryTimeseriesIdentifier=4d62d997afea458a8ca722d55a479f5c&firstStatDerivedIdentifier=aec5c04ce5e540469d6c7685da66e73b&secondStatDerivedIdentifier=&thirdStatDerivedIdentifier=&fourthStatDerivedIdentifier=&firstReferenceIdentifier=&secondReferenceIdentifier=&thirdReferenceIdentifier=&station=251457080395802&waterYear=2015&token=2ad71416-161d-494d-b98d-3460d6b1a940


https://reporting-test.nwis.usgs.gov/timeseries-ws/service/reports/dvhydrograph/?primaryTimeseriesIdentifier=9461633ffc1d478fb8b566f64d325b62&firstStatDerivedIdentifier=dba053add97a4434b21616fbbd39d822&secondStatDerivedIdentifier=&thirdStatDerivedIdentifier=&fourthStatDerivedIdentifier=&firstReferenceIdentifier=&secondReferenceIdentifier=&thirdReferenceIdentifier=&station=01010000&lastMonths=1


		//calculate inverted flag
		result.setInverted(ParameterSpecService.isInvertedGwParam(result));
		
		//calculate ground water flag
		result.setGroundWater(ParameterSpecService.isGwParamater(result));
		
		//calculate discharge flag
		result.setDischarge(ParameterSpecService.isDischargeParameter(result));
		
		//groundwater filters
		GroundWaterParameters gwParam = GroundWaterParameters.getByDisplayName(result.getParameter());
		if(gwParam != null) {
			result.setWaterLevelType(gwParam.getGwLevEnt());
			result.setSeaLevelDatum(gwParam.getSeaLevDatum());
		}

			result.setSublocation(descs.get(0).getSubLocationIdentifier());

			result.setTimeSeriesType(descs.get(0).getTimeSeriesType());

			result.setPeriod(descs.get(0).getComputationPeriodIdentifier());

			result.setPublish(descs.get(0).isPublish());

			result.setPrimary(extractPrimaryFlag(descs.get(0)));
			
			result.setUniqueId(descs.get(0).getUniqueId());
			
			result.setComment(descs.get(0).getComment());
			
			result.setDescription(descs.get(0).getDescription());
			
			result.setExtendedAttributes(toAqcuExtendedAttributes(descs.get(0).getExtendedAttributes()));
		
		return result;