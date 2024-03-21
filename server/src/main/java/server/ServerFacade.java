package server;

import com.google.gson.Gson;
import service.ServiceRecords.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;

public class ServerFacade {
    //Should I have a way to catch exceptions and display them to the user?
    private final String serverURL;
    public ServerFacade(String url) {
        serverURL = url;
    }
    public LoginResponse login(LoginRequest r) throws Exception {
        return this.makeRequest("POST", "/session", r,null, LoginResponse.class);
    }

    public RegisterResponse register(RegisterRequest r) throws Exception {
        return this.makeRequest("POST", "/user", r,null, RegisterResponse.class);
    }

    public void logout(LogoutRequest r) throws Exception {
        var headers = new HashMap<String, String>();
        headers.put("authorization", r.authToken());
        this.makeRequest("DELETE", "/session", null, headers, null);
    }

    public CreateGameResponse createGame(CreateGameRequest r, String authToken) throws Exception {
        var headers = new HashMap<String, String>();
        headers.put("authorization", authToken);
        return this.makeRequest("POST", "/game", r, headers, CreateGameResponse.class);
    }

    private <T> T makeRequest(String method, String path, Object request, HashMap<String, String> headers, Class<T> responseClass) throws Exception {
        try {
            URL url = (new URI(serverURL + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (headers != null) {
                for (HashMap.Entry<String, String> entry : headers.entrySet()) {
                    http.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass); //constructs a response object of the given type and returns it.
                }
            }
        }
        return response;
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, Exception {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new Exception("HTTP request unsuccessful. Error: "  + status);
        }
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

}
