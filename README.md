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

