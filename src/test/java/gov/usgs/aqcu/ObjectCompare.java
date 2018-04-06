package gov.usgs.aqcu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.usgs.aqcu.builder.ReportBuilderService;

public final class ObjectCompare {
	private static final Logger LOG = LoggerFactory.getLogger(ReportBuilderService.class);
//TODO - perhaps this should just convert them both to json and do a json compare
	private ObjectCompare() {}

	public static void assertDaoTestResults(final Class<?> inClass, final Object inObject, final Object resultObject) {
		assertDaoTestResults(inClass, inObject, resultObject, null, true);
	}

	public static void assertDaoTestResults(final Class<?> inClass, final Object inObject, final Object resultObject,
			final List<String> ignoreProperties, final boolean allowNull) {
		for (PropertyDescriptor prop : getPropertyMap(inClass).values()) {
			if ((null != ignoreProperties && ignoreProperties.contains(prop.getName()))) {
				assertTrue(true);
			} else {
				try {
					if (null != prop.getReadMethod() && !"getClass".contentEquals(prop.getReadMethod().getName())) {
						Object inProp = prop.getReadMethod().invoke(inObject);
						Object resultProp = prop.getReadMethod().invoke(resultObject);
						if (!allowNull) {
							assertNotNull(prop.getName() + " original is null.", inProp);
							assertNotNull(prop.getName() + " result is null.", resultProp);
						};
						if (resultProp instanceof Collection) {
							//TODO - could try to match the lists...
							assertEquals(prop.getName(), ((Collection<?>) inProp).size(), ((Collection<?>) resultProp).size());
						} else {
							assertProperty(inProp, resultProp, prop);
						}
					}
				} catch (Exception e) {
					throw new RuntimeException("Error getting property: " + prop.getName(), e);
				}
			}
		}
	}

	private static void assertProperty(final Object inProp, final Object resultProp,
			final PropertyDescriptor prop) throws Exception {
		LOG.info(prop.getName() + " input: " + inProp + " result: " + resultProp);
		assertEquals(prop.getName(), inProp, resultProp);
	}

	private static HashMap<String, PropertyDescriptor> getPropertyMap(final Class<?> inClass) {
		HashMap<String, PropertyDescriptor> returnMethodList = new HashMap<String, PropertyDescriptor>();
		BeanInfo info = null;
		try {
			info = Introspector.getBeanInfo(inClass);
		} catch (IntrospectionException e1) {
			LOG.error("error introspecting bean: " + inClass.getCanonicalName(), e1);
		}
		returnMethodList = new HashMap<String, PropertyDescriptor>();
		if (info != null) {
			//for each of this objects setter method.
			for (PropertyDescriptor propDesc : info.getPropertyDescriptors()) {
				//assuming JavaBean convention
				returnMethodList.put(propDesc.getName(), propDesc);
			}
		}
		return returnMethodList;
	}
}
