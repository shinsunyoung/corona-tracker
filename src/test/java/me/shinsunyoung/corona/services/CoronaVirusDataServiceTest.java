package me.shinsunyoung.corona.services;


import java.util.ArrayList;
import java.util.List;

import me.shinsunyoung.corona.models.KoreaStats;
import me.shinsunyoung.corona.models.LocationStats;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CoronaVirusDataServiceTest {

    @Autowired
    CoronaVirusDataService coronaVirusDataService;

    @Test
    public void fetchVirusData_동작테스트() throws IOException {

        //given
        List<LocationStats> coronaList = new ArrayList<>();

        //when
        coronaList = coronaVirusDataService.fetchVirusData();

        //then
        assertThat(coronaList.get(0).getState()).isEqualTo("Anhui");
        assertThat(coronaList.get(0).getLatestTotalCases()).isGreaterThan(0);
        assertThat(coronaList.get(0).getCountry()).isEqualTo("Mainland China");
    }

    @Test
    public void getKoreaCovidDatas_동작테스트() throws IOException {

        // given
        List<KoreaStats> coronaList = new ArrayList<>();

        // when
        coronaList = coronaVirusDataService.getKoreaCovidDatas();

        // then
        assertThat(coronaList.get(0).getCountry()).isEqualTo("합계");
        assertThat(coronaList.get(0).getTotal()).isGreaterThan(0);

    }

}