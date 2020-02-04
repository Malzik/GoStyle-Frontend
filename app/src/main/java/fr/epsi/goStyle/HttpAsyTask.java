package fr.epsi.goStyle;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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


    public Object call(String urlStr, String method, Map<String, String> body, String token) throws IOException {

        URL url = new URL(urlStr);
        HttpURLConnection client = null;

        try {
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod(method);
            if(token != null && !token.isEmpty()) {
                client.setRequestProperty("Authorization", "Bearer " + token);
            }
            client.setRequestProperty("Content-Type", "application/json; utf-8");
            client.setRequestProperty("Accept", "application/json");
            client.setDoOutput(true);

            String jsonInputString = "{";

            for (Map.Entry<String, String> entry : body.entrySet()) {
                if(!jsonInputString.equals("{"))
                    jsonInputString = jsonInputString.concat(",");
                jsonInputString = jsonInputString.concat("\"" + entry.getKey() + "\"");
                jsonInputString = jsonInputString.concat(":");
                jsonInputString = jsonInputString.concat("\"" + entry.getValue() + "\"");
            }
            jsonInputString = jsonInputString.concat("}");

            try(OutputStream os = client.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            System.out.println(jsonInputString);
            System.out.println(client.getInputStream());
            InputStream in = new BufferedInputStream(client.getInputStream());
            return convertStreamToString(in);
        } finally {
            if(client != null)
                client.disconnect();
        }

    }

    private String convertStreamToString(InputStream is){
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

            StringBuffer stringBuffer = new StringBuffer("");
            String line;

            String NL = System.getProperty("line.separator");
            while ((line = bufferedReader.readLine()) != null)
            {
                stringBuffer.append(line + NL);
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
