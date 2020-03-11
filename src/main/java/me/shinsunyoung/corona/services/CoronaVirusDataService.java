package me.shinsunyoung.corona.services;

import me.shinsunyoung.corona.models.KoreaStats;
import me.shinsunyoung.corona.models.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.tomcat.jni.Local;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


@Service
public class CoronaVirusDataService {

    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_19-covid-Confirmed.csv";
    private static String KOREA_COVID_DATAS_URL = "http://ncov.mohw.go.kr/bdBoardList_Real.do?brdId=1&brdGubun=13";


    //@PostConstruct
    //@Scheduled(cron = "0 0 * * * *") // 초 분 시 일 월 년
    public List<LocationStats> fetchVirusData() throws IOException {

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

            int latestTotalCases = record.get(record.size() - 1).isEmpty() ? 0 : Integer.parseInt(record.get(record.size() - 1));
            int prevTotalCases = record.get(record.size() - 2).isEmpty() ? 0 : Integer.parseInt(record.get(record.size() - 2));

            LocationStats locationStats = LocationStats.builder()
                    .state(record.get("Province/State"))
                    .country(record.get("Country/Region"))
                    .latestTotalCases(latestTotalCases)
                    .diffFromPrevDay(latestTotalCases-prevTotalCases)
                    .build();

            //System.out.println(locationStats.toString());

            newStats.add(locationStats);

        }

        return newStats;

    }

    @PostConstruct
    public List<KoreaStats> getKoreaCovidDatas() throws IOException {

        List<KoreaStats> koreaStatsList = new ArrayList<>();
        Document doc = Jsoup.connect(KOREA_COVID_DATAS_URL).get();

        Elements contents = doc.select("table tbody tr");

        for(Element content : contents){
            Elements tdContents = content.select("td");

            KoreaStats koreaStats = KoreaStats.builder()
                    .country(content.select("th").text())
                    .diffFromPrevDay(Integer.parseInt(tdContents.get(0).text()))
                    .total(Integer.parseInt(tdContents.get(1).text()))
                    .death(Integer.parseInt(tdContents.get(2).text()))
                    .incidence(Double.parseDouble(tdContents.get(3).text()))
                    .inspection(Double.parseDouble(tdContents.get(4).text()))
                    .build();

            koreaStatsList.add(koreaStats);
        }

        return koreaStatsList;

    }

}
