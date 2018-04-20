package gov.usgs.aqcu;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONObjectAs;

import org.json.JSONObject;

import com.google.gson.Gson;

import gov.usgs.aqcu.serializer.SwaggerGsonSerializer;
import gov.usgs.aqcu.util.AqcuGsonBuilderFactory;
import springfox.documentation.spring.web.json.Json;

public final class ObjectCompare {

	private ObjectCompare() {}

	public static void compare(final Object expectedObject, final Object actualObject) {
		Gson gson = AqcuGsonBuilderFactory.getConfiguredGsonBuilder()
				.registerTypeAdapter(Json.class, new SwaggerGsonSerializer())
				.serializeNulls()
				.create();
		try {
			assertThat(new JSONObject(gson.toJson(actualObject)),
					sameJSONObjectAs(new JSONObject(gson.toJson(expectedObject))).allowingAnyArrayOrdering());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

}
