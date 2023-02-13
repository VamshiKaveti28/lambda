package com.external;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Year;
import org.json.JSONObject;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

public class LTCG {

	private static DynamoDB dynamoDb;
	private static String DYNAMO_DB_TABLE_NAME = "ltcg_data";
	private static Regions REGION = Regions.US_EAST_1;
	static String projectionExpression = "healthaide,private,semiprivate,region";

	public static JSONObject getLTCGData(final JSONObject objectEle) {
		initDynamoDbClient();
		return calculateHVSData(objectEle);
	}

	private static JSONObject calculateHVSData(final JSONObject objectEle) {
		// {"healthaide":24.5,"semiPrivate":249.5,"privateRoom":278.4}
		if (objectEle != null && objectEle.has("region")) {
			return pullData(objectEle.getInt("region"), objectEle);
		}

		return null;

	}

	private static void initDynamoDbClient() {
		try {
			AmazonDynamoDBClient client = new AmazonDynamoDBClient();
			client.setRegion(Region.getRegion(REGION));
			dynamoDb = new DynamoDB(client);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static JSONObject pullData(final int region, final JSONObject objectEle) {
		try {
			final Table table = dynamoDb.getTable(DYNAMO_DB_TABLE_NAME);

			Item item = table.getItem("sno", region);

			return convertAsJSON(item, objectEle);

		} catch (Exception e) {
			System.err.println("Unable to scan the table:");
			System.err.println(e.getMessage());
		}
		return null;

	}

	private static JSONObject convertAsJSON(final Item item, final JSONObject objectEle) {

		JSONObject jsonObj = new JSONObject();

		final int rate = objectEle.getInt("inflationRate");
		final int futureCost = objectEle.getInt("futureCost");

		int year = Year.now().getValue();

		final int years = futureCost - year;


		final double healthaide = item.getDouble("healthaide");

		final double privateRoom = item.getDouble("private");

		final double semiprivate = item.getDouble("semiprivate");

		jsonObj.put("healthaide", getCalculatedValue(healthaide, rate, years));

		jsonObj.put("semiPrivate", getCalculatedValue(semiprivate, rate, years));

		jsonObj.put("privateRoom", getCalculatedValue(privateRoom, rate, years));

		return jsonObj;

	}

	// computing the value of commonRatio ^ n
	private static float power(float commonRatio, int n) {
		
		// if the exponent is zero
		// value is 1
		if (n == 0) {
			return 1;
		}

		int j = 0;

		float power = 1;

		// loop for computing the value of
		// commonRatio ^ n
		while (j < n) {
			power = (power * commonRatio);
			j = j + 1;
		}

		return power;

	}

	private static BigDecimal getCalculatedValue(final double actualVal, final int rate, final int years) {


		return new BigDecimal(actualVal * power((1 + (rate / 100)), years)).setScale(2, RoundingMode.HALF_UP);  
	}
}
