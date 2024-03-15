package server;

import com.google.gson.Gson;
import service.ServiceRecords.LoginRequest;
import service.ServiceRecords.LoginResponse;
import service.ServiceRecords.RegisterRequest;
import service.ServiceRecords.RegisterResponse;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {

    //Should I have a way to catch exceptions and display them to the user?
    private final String serverURL;
    public ServerFacade(String url) {
        serverURL = url;
    }
    //because how does it work with sending http requests to the server. what does that even mean.
    public LoginResponse login(LoginRequest r) {
        // so now we just need to understand the method, path, request, and then call the makeRequest method
        //Im gonna go get some lunch
        return null;
    }

    public RegisterResponse register(RegisterRequest r) {
        return null;
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws Exception {
        try {
            URL url = (new URI(serverURL + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        }
        catch (Exception e) {
            throw new Exception("This was bad lol");
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
            throw new Exception("This was bad");
        }
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

}
