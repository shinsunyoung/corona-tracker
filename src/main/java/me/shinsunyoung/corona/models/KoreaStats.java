package me.shinsunyoung.corona.models;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@Getter
public class KoreaStats {

    private String country;

    private int diffFromPrevDay;

    private int total;

    private int death;

    private double incidence; // 발병률

    private double inspection; // 일일 검사환자 수

}
