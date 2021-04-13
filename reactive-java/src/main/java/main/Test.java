package main;

import restful.client.async.AsyncClient;

import java.io.BufferedWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by u624 on 4/20/17.
 */
public class Test {
    public static void main(String[] args) throws Exception {
        for (int i = 1; i <= 20; i++) {
            String number = Integer.toString(i);
            String url = "http://libgen.io/search.php?&req=math&page=" + number;
            String file = "/home/u624/Desktop/Test/response" + number + ".txt";
            new AsyncClient(new URL(url)).processHttpResponse(reader -> {
                try (BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(file))) {
                    int character;
                    while ((character = reader.read()) != -1) {
                        bufferedWriter.write(character);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
