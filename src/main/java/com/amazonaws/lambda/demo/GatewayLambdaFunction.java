package com.amazonaws.lambda.demo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class GatewayLambdaFunction implements RequestHandler<Object, String> {
	
	private DynamoDB dynamoDB;
	
    @Override
    public String handleRequest(Object input, Context context) {
    	try {
        context.getLogger().log("Input: " + input);
        ExchangeRates rates = getLatestRates();
        initDynamoDbClient();
        updateDB(rates);
        return "Successful inserting the data! ";
    	} catch(Exception e) {
    		context.getLogger().log("Exception occurred"+e.getMessage());
    		return "failed to run Lambda";
    	}
    }
    
    private void updateDB(ExchangeRates rates) {
    	String date = getStringDate(new Date());
    	rates.getRates().entrySet().forEach(item ->
		this.dynamoDB.getTable("TRAVEL_FOREX_RATES").putItem(new PutItemSpec().withItem(new Item().withPrimaryKey("id", UUID.randomUUID().toString())
				.withString("base_currency_code", "USD")
				.withString("base_currency_name", "null")
				.withNumber("last_trade_rate", item.getValue())
				.withString("modified_by", "Data Jobs")
				.withString("refresh_date", date)
				.withString("trade_currency_name", "null")
				.withString("travel_currency_code", item.getKey())
				.withString("modified_date", date)
				)));
	}

	private String getStringDate(Date date) {
		return new SimpleDateFormat("MM-dd-yyyy").format(date);
	}

	private ExchangeRates getLatestRates() {
    	String url ="https://openexchangerates.org/api/latest.json?app_id=aac7f40ece9e46bba6b9e2&base=USD";
		ParameterizedTypeReference<ExchangeRates> typeRef = new ParameterizedTypeReference<ExchangeRates>() {
		};
		return new RestTemplate().exchange(url, HttpMethod.GET, null, typeRef).getBody();
	}
    
    private void initDynamoDbClient() {
    	AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_2).build();
    	this.dynamoDB = new DynamoDB(client);
    }

}
