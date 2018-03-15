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

https://localhost:8443/aqcu-dv-hydro-report/dvhydro/?primaryTimeseriesIdentifier=9461633ffc1d478fb8b566f64d325b62&firstStatDerivedIdentifier=dba053add97a4434b21616fbbd39d822&secondStatDerivedIdentifier=&thirdStatDerivedIdentifier=&fourthStatDerivedIdentifier=&firstReferenceIdentifier=&secondReferenceIdentifier=&thirdReferenceIdentifier=&station=01010000&startDate=1990-12-31T00:00:00.0000000Z&endDate=2018-01-01T23:59:59.0000000Z

https://localhost:8443/aqcu-dv-hydro-report/dvhydro/
?primaryTimeseriesIdentifier=9461633ffc1d478fb8b566f64d325b62
&firstStatDerivedIdentifier=dba053add97a4434b21616fbbd39d822
&secondStatDerivedIdentifier=
&thirdStatDerivedIdentifier=
&fourthStatDerivedIdentifier=
&firstReferenceIdentifier=
&secondReferenceIdentifier=
&thirdReferenceIdentifier=
&station=01010000
&startDate=1990-12-31T00:00:00.0000000Z
&endDate=2018-01-01T23:59:59.0000000Z


https://nwissddvasaqcu.cr.usgs.gov:8443/aqcu-gateway/aqcu-webservice/service/reports/dvhydrograph/
primaryTimeseriesIdentifier=9461633ffc1d478fb8b566f64d325b62
&firstStatDerivedIdentifier=dba053add97a4434b21616fbbd39d822
&secondStatDerivedIdentifier=dba053add97a4434b21616fbbd39d822
&thirdStatDerivedIdentifier=dba053add97a4434b21616fbbd39d822
&fourthStatDerivedIdentifier=453627b9811a48269c5945281c53a688
&firstReferenceIdentifier=a3e0571b8b754b5caad1c5302e0f7515
&secondReferenceIdentifier=a3e0571b8b754b5caad1c5302e0f7515
&thirdReferenceIdentifier=e7261f5b1c174b76a815b68d1a6e19b7
&station=01010000
&lastMonths=12
&comparisonStation=335719110101000
&comparisonTimeseriesIdentifier=f6b196dbdb2447b89f554408a5d25eaa
&excludeZeroNegative=true
&excludeDiscrete=true
&excludeMinMax=true
