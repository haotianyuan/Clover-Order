package cloverOrder.src.main.java.inMaven;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Clover Order
 */
public final class App {
    private App() {
    }

    private final static String merchantId = "263E8B8MBKAE1";
    private final static String apiToken = "7cfea3cb-1f13-d262-8282-eb0445537c8e";
    private static int totalPrice = 10088;

    public static void main(String[] args) throws IOException {
        System.out.println("=========Clover Order===========");
        //createOrder();
        getOrder();
    }

    private static void createOrder() throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        String requestBody = "{\"taxRemoved\":\"false\",\"merchant\":{\"id\":" + "\"" + merchantId + "\""
                + "},\"total\":" + totalPrice + ",\"currency\":\"USD\"}";
        RequestBody body = RequestBody.create(mediaType, requestBody);
        Request request = new Request.Builder()
                .url("https://sandbox.dev.clover.com/v3/merchants/" + merchantId + "/orders?access_token=" + apiToken)
                .post(body).addHeader("content-type", "application/json").build();

        Response response = client.newCall(request).execute();
    }

    private static void getOrder() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
        .url("https://sandbox.dev.clover.com/v3/merchants/263E8B8MBKAE1/orders?access_token=7cfea3cb-1f13-d262-8282-eb0445537c8e")
        .get()
        .addHeader("accept", "application/json")
        .build();

        Response response = client.newCall(request).execute();
        System.out.println(response.body().toString());
    }
}
