package me.shinsunyoung.corona.services;

import me.shinsunyoung.corona.models.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.tomcat.jni.Local;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


@Service
public class CoronaVirusDataService {

    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_19-covid-Confirmed.csv";


    @PostConstruct
    @Scheduled(cron = "* 0 * * * *") // 초 분 시 일 월 년
    public void fetchVirusData() throws IOException {

        List<LocationStats> newStats = new ArrayList<>();

        CloseableHttpClient httpClient = HttpClients.createDefault();

        CloseableHttpResponse httpResponse = httpClient.execute(new HttpGet(VIRUS_DATA_URL));

        StringWriter writer = new StringWriter();
        IOUtils.copy(httpResponse.getEntity().getContent(), writer, StandardCharsets.UTF_8);

//        System.out.println(writer.toString()); // print

        httpClient.close();

        StringReader csvBodyReader = new StringReader(writer.toString());

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);

        for (CSVRecord record : records) {

            LocationStats locationStats = LocationStats.builder()
                    .state(record.get("Province/State"))
                    .country(record.get("Country/Region"))
                    .latestTotalCases(Integer.parseInt(record.get(record.size() - 1)))
                    .build();

            System.out.println(locationStats.toString());

            newStats.add(locationStats);

        }


    }
}
