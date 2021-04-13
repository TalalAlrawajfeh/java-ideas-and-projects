package restful.client.async;

import callbacks.AsyncCallback;
import callbacks.Callback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.function.Supplier;

/**
 * Created by u624 on 4/20/17.
 */
public class AsyncClient {
    private URL url;

    public AsyncClient(URL url) {
        this.url = url;
    }

    public void processHttpResponse(Callback<Reader> readerCallback) throws ClientException {
        HttpURLConnection httpURLConnection = getHttpURLConnection();
        new AsyncCallback<Reader, HttpURLConnection>().process(readerCallback, new ReaderSupplier(httpURLConnection))
                .onComplete(HttpURLConnection::disconnect, new HttpURLConnectionSupplier(httpURLConnection));
    }

    private HttpURLConnection getHttpURLConnection() throws ClientException {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            setRequestAttributes(httpURLConnection);
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode != 200) {
                throw new ClientException("Connection error, status code: " + responseCode);
            }
            return httpURLConnection;
        } catch (IOException e) {
            throw new ClientException(e);
        }
    }

    private void setRequestAttributes(HttpURLConnection httpURLConnection) throws ProtocolException {
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("Accept", "application/text");
    }

    private class ReaderSupplier implements Supplier<Reader> {
        private HttpURLConnection httpURLConnection;

        public ReaderSupplier(HttpURLConnection httpURLConnection) {
            this.httpURLConnection = httpURLConnection;
        }

        @Override
        public Reader get() {
            try {
                return new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private class HttpURLConnectionSupplier implements Supplier<HttpURLConnection> {
        private HttpURLConnection httpURLConnection;

        public HttpURLConnectionSupplier(HttpURLConnection httpURLConnection) {
            this.httpURLConnection = httpURLConnection;
        }

        @Override
        public HttpURLConnection get() {
            return httpURLConnection;
        }
    }
}
