package cloverOrder.src.main.java.inMaven;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

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
    private static String orderId = "";
    private static String cashTender = "";
    private static String creditTender = "";
    private static Map<String, String> tenderId = new HashMap<>();
    private final static String clientId = "NEXV1ABWSPX0E";
    private final static String appSecret = "d6eded17-d021-108d-8ff0-30e96f379670vis";

    public static void main(String[] args) throws IOException, URISyntaxException {
        String lineItemId = "T1QE73HYF7TZ2";
        System.out.println("=========Clover Order===========");
        // getMenu();
        // createOrder();
        // addLineItem(lineItemId);
        // getTender();
        // cashTender = tenderId.get("Cash");
        // creditTender = tenderId.get("Credit Card");
         if(paymentCheck() == true){
        // transaction();
         }

    }

    private static void getMenu() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://sandbox.dev.clover.com/v3/merchants/" + merchantId + "/items?access_token=" + apiToken)
                .get().build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        try (Response response = client.newCall(request).execute()) {
            orderId = response.body().string().split(",")[1].split(":")[1].substring(2, 15);
            // System.out.println(orderId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addLineItem(String lineItemId) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        String requestBody = "{\"item\":{\"id\":\"" + lineItemId + "\"}}";
        RequestBody body = RequestBody.create(mediaType, requestBody);
        Request request = new Request.Builder()
                .url("https://sandbox.dev.clover.com/v3/merchants/" + merchantId + "/orders/" + orderId
                        + "/line_items?access_token=" + apiToken)
                .post(body).addHeader("content-type", "application/json").build();

        Response response = client.newCall(request).execute();
    }

    private static void getTender() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://sandbox.dev.clover.com/v3/merchants/" + merchantId + "/tenders?access_token=" + apiToken)
                .get().addHeader("accept", "application/json").build();

        try (Response response = client.newCall(request).execute()) {
            String[] s = response.body().string().split(",");
            for (int i = 4; i < s.length; i += 8) {
                String tenderLabel = s[i].split(":")[1];
                tenderId.put(tenderLabel.substring(2, tenderLabel.length() - 1),
                        s[i - 3].split(" ")[2].substring(1, 14));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean paymentCheck() throws IOException, URISyntaxException {
        //OAuthStepOne();
        //OAuthStepTwo();
        //OAuthStepThree();
        getInformationForPayEndpoint();
        return true;
    }

    private static void transaction() throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        String requestBody = "{\"tender\":{\"id\":\"" + cashTender + "\"},\"amount\":" + totalPrice
                + ",\"tipAmount\":121,\"taxAmount\":" + totalPrice * 0.1 + "}";
        RequestBody body = RequestBody.create(mediaType, requestBody);
        Request request = new Request.Builder()
                .url("https://sandbox.dev.clover.com/v3/merchants/" + merchantId + "/orders/" + orderId
                        + "/payments?access_token=" + apiToken)
                .post(body).addHeader("content-type", "application/json").build();

        Response response = client.newCall(request).execute();
        System.out.println(response);
    }

    private static void OAuthStepOne() throws IOException, URISyntaxException {
        //Request merchant authorization
        URI uri= new URI("https://sandbox.dev.clover.com/oauth/authorize?client_id="+clientId+"&merchant_id="+merchantId);
   
        java.awt.Desktop.getDesktop().browse(uri);
    }
    private static void OAuthStepTwo() throws IOException, URISyntaxException {
        //Receive an authorization code
        URI uri= new URI("https://www.example.com/oauth_callback?merchant_id="+merchantId+"&client_id="+clientId+"&code=ae20c593-9725-0e63-27a6-5c362d39e090");
   
        java.awt.Desktop.getDesktop().browse(uri);
    }
    private static void OAuthStepThree() throws IOException, URISyntaxException {
        //Receive apiToken
        URI uri= new URI("https://sandbox.dev.clover.com/oauth/token?client_id="+clientId+"&client_secret="+appSecret+"&code=ae20c593-9725-0e63-27a6-5c362d39e090");
   
        java.awt.Desktop.getDesktop().browse(uri);
        //7f85179c-2902-3c34-6515-b3339024922a
    }

    private static void getInformationForPayEndpoint(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://apisandbox.dev.clover.com/v2/merchant/" + merchantId + "/pay/key?access_token=7f85179c-2902-3c34-6515-b3339024922a")
                .get().addHeader("accept", "application/json").build();
        try (Response response = client.newCall(request).execute()) {
            String[] s = response.body().string().split(",");
            for(int i=0;i<s.length;i++){
                System.out.println(s[i]);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
                
    }



    
}
