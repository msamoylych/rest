package org.java.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.math.BigDecimal;

public class ServerTest {

    private static final String URL = "http://localhost:9000/account/transfer";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static final int OK = 200;
    private static final int BAD_REQUEST = 400;
    private static final int UNPROCESSABLE_ENTITY = 422;

    private OkHttpClient client;
    private ObjectMapper objectMapper;

    @BeforeClass
    public void before() {
        Server.main(new String[]{});
        client = new OkHttpClient();
        objectMapper = new ObjectMapper();
    }

    @AfterClass
    public void after() {
        client.dispatcher().executorService().shutdown();
        client.connectionPool().evictAll();
    }

    @Test
    public void testBadRequest() throws IOException {
        Assert.assertEquals(request(null, null, null), BAD_REQUEST); // Empty request
        Assert.assertEquals(request("", "", null), BAD_REQUEST); // Empty request
        Assert.assertEquals(request("1", "2", "-10"), BAD_REQUEST); // Negative amount
    }

    @Test
    public void testUnprocessableEntity() throws IOException {
        Assert.assertEquals(request("11", "22", "1"), UNPROCESSABLE_ENTITY); // Account not found
        Assert.assertEquals(request("1", "2", "1000"), UNPROCESSABLE_ENTITY); // Not enough money
    }

    @Test
    public void testOK() throws IOException {
        Assert.assertEquals(request("1", "2", "10"), OK);
        Assert.assertEquals(request("1", "2", "10.43"), OK);
        Assert.assertEquals(request("2", "1", "70"), OK);
    }

    @Test(threadPoolSize = 4, invocationCount = 100)
    public void testMultithreading() throws IOException {
        Assert.assertEquals(request("3", "4", "0.1"), OK);
    }

    private int request(String from, String to, String amount) throws IOException {
        Request request = new Request.Builder()
                .url(URL)
                .post(RequestBody.create(JSON, makeContent(from, to, amount)))
                .build();
        Response response = client.newCall(request).execute();
        return response.code();
    }

    private String makeContent(String from, String to, String amount) {
        ObjectNode node = objectMapper.createObjectNode();
        if (from != null) {
            node.put("from", from);
        }
        if (to != null) {
            node.put("to", to);
        }
        if (amount != null && !amount.isEmpty()) {
            node.put("amount", new BigDecimal(amount));
        }
        return node.toString();
    }
}
