package fr.epsi.goStyle;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;

public class HttpAsyTask extends AsyncTask<Void,Void,Object> {

    interface HttpAsyTaskListener{
        void webServiceDone(String result);
        void webServiceError(Exception e);
    }

    private HttpAsyTaskListener httpAsyTaskListener;

    private String url;
    private String method = "GET";
    private String token;
    private Map<String, String> body;


    public HttpAsyTask(String url, String method, Map<String, String> body, String token, HttpAsyTaskListener listener) {
        httpAsyTaskListener=listener;
        this.url = url;
        this.method = method;
        this.body = body;
        this.token = token;
    }

    @Override
    protected Object doInBackground(Void... voids) {
        try {
            return call(url, method, body, token);
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            return e.getMessage();
        } catch (JSONException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if(o instanceof Exception){
            httpAsyTaskListener.webServiceError((Exception)o);
        }
        else
            httpAsyTaskListener.webServiceDone(o.toString());
    }


    public Object call(String urlStr, String method, Map<String, String> body, String token) throws IOException, JSONException {

        URL url = new URL(urlStr);
        HttpURLConnection client = null;

        token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImN0eSI6IkpXVCJ9.eyJpYXQiOjE1ODA5MDM1OTcsImV4cCI6MTU4MDkwNzE5NywiZW1haWwiOiJhbGV4aXMuaGVyb2luQGVwc2kuZnIiLCJpZCI6MjR9.UTcK93Ac1RhmbDd4VL8d1pFUg6Q_I-Cy1WWJTyWMAyc--XhDx0B84-aAy7JtELTH4OLNEmuEBF-33mPERlK72cDqs_wx4C28l0NPP-U35Hrx-tbu1nxMJuk80Mm1QwOrVDZ58ECHcr0GZFy15ie1-fwEzqC1PUXex6g8mqo5wHIzqORP9S4w5ojtvumVR8rIJxZ5B03ww-gryttg0I9YToc-KblCIfPdfH_Btc5ytek-whRJsUpYP0wocKfExLEF8vt3bezTHoXtsYaZQ792zcg_7OiCR45EVueFHN_WcRAofyljCg1k_kCEzz3nvesS_K5RNXQt6BfRtFJBUW5r_WPXP13BViRpcTsRMBf42t3kQUMufJdEr1RMds0LFNABvvLjYvgDc8kx6t_WuSc4Vz-CJcPe5O7Jpz0WYG2D4gj-6IBfvIAbzPYa5rAuptR-FMUQbeB-zaoHAqHlVrM41JqTh1BRTQJ41YBRHKB4d6zFMvROVM0J1YlNP9mM6Ad_s6V7AEFrNAuG7VWmogRnw-0Sys5KrGCmvJk_aiD3epCkQTRFnXXtmO-K-ztkJaJl_y7Cps_Q4YAeJL27IJf3JvNjiLkLUoF1czm5W8riChwp2b4Z4cW-FxXbpwPqRIKc8c5mm5QatxYoRArK7RCuOEoNa4yYYF1P-nhVlZhqkUw";
        try {
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod(method);
            if(token != null && !token.isEmpty()) {
                client.setRequestProperty("Authorization", "Bearer " + token);
            }
            client.setRequestProperty("Content-Type", "application/json; utf-8");
            client.setRequestProperty("Accept", "application/json");

            if(body != null) {
                client.setDoOutput(true);
                String jsonInputString = "{";
                for (Map.Entry<String, String> entry : body.entrySet()) {
                    if (!jsonInputString.equals("{"))
                        jsonInputString = jsonInputString.concat(",");
                    jsonInputString = jsonInputString.concat("\"" + entry.getKey() + "\"");
                    jsonInputString = jsonInputString.concat(":");
                    jsonInputString = jsonInputString.concat("\"" + entry.getValue() + "\"");
                }
                jsonInputString = jsonInputString.concat("}");

                try(OutputStream os = client.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }
                System.out.println(jsonInputString);
            }

            int code = client.getResponseCode();

            if(code >= 300) {
                JSONObject errors = new JSONObject();
                errors.put("status", code);
                errors.put("message", client.getErrorStream());
                return new Exception(client.getErrorStream().toString());
            }
            else {
                InputStream in = new BufferedInputStream(client.getInputStream());
                String responseBody = convertStreamToString(in);
                JSONArray result = new JSONArray(responseBody);
                JSONObject status = new JSONObject();
                status.put("status", client.getResponseCode());
                result.put(status);
                return result;
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return e;
        } finally{
            if(client != null)
                client.disconnect();
        }

    }

    private String convertStreamToString(InputStream is){
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

            StringBuilder stringBuffer = new StringBuilder();
            String line;

            String NL = System.getProperty("line.separator");
            while ((line = bufferedReader.readLine()) != null)
            {
                stringBuffer.append(line).append(NL);
            }
            bufferedReader.close();
            return stringBuffer.toString();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
