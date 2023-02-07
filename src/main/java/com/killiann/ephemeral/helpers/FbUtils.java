package com.killiann.ephemeral.helpers;

import com.google.gson.Gson;
import com.killiann.ephemeral.models.FbUserInfoResponse;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FbUtils {

    private static final String baseUrl = "https://graph.facebook.com/";
        // v14.0/me?fields=id%2Cname&access_token=EAASNWL6vRmsBAFzAmZCvTDLKU4vNZCZC0aL17HIfMumxjbPLNc46LdKzmG78pubWIhBRgJp8TUXWwmZCiSBRFU4WeFZCRJRzeJOhzq1riUPq0C5ctfNMmBb7qVZAkTmeZBiqw2lDVvO5bE8RbtXBfDzPO66k56x6fxZBVaplUQTmdNEcF3bsO4yADRLYqEtqCIgZD"

    public static Optional<FbUserInfoResponse> getUserInfo(String accessToken, String apiVersion) throws IOException {

        Optional<FbUserInfoResponse> optFbUserResponse = Optional.empty();

        URL url = new URL(baseUrl + apiVersion + "/me");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");

        Map<String, String> parameters = new HashMap<>();
        parameters.put("fields", "id,name,email,picture{url}");
        parameters.put("access_token", accessToken);

        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
        out.flush();
        out.close();

        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        int status = con.getResponseCode();

        BufferedReader in = null;

        if (status > 299) {
            in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        } else {
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        }

        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        con.disconnect();

        Gson g = new Gson();
        return Optional.ofNullable(g.fromJson(String.valueOf(content), FbUserInfoResponse.class));
    }
}
