package com.nordclan.samsara_grabber.domain.gateway;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
public class GateWay {
    private Model model;
    private String serial;

    @Getter
    @AllArgsConstructor
    public enum Model {
        AG15("AG15"),
        AG24("AG24"),
        AG24EU("AG24EU"),
        AG26("AG26"),
        AG26EU("AG26EU"),
        AG41("AG41"),
        AG41EU("AG41EU"),
        AG45("AG45"),
        AG45EU("AG45EU"),
        AG46("AG46"),
        AG46EU("AG46EU"),
        AG46P("AG46P"),
        AG51("AG51"),
        AG51EU("AG51EU"),
        AG52("AG52"),
        AG52EU("AG52EU"),
        IG15("IG15"),
        IG21("IG21"),
        IG41("IG41"),
        IG61("IG61"),
        SG1("SG1"),
        SG1B("SG1B"),
        SG1G("SG1G"),
        SG1G32("SG1G32"),
        SG1x("SG1x"),
        VG32("VG32"),
        VG33("VG33"),
        VG34("VG34"),
        VG34EU("VG34EU"),
        VG34FN("VG34FN"),
        VG34M("VG34M"),
        VG54ATT("VG54ATT"),
        VG54EU("VG54EU"),
        VG54FN("VG54FN"),
        VG54NA("VG54NA"),
        VG54NAE("VG54NAE"),
        VG54NAH("VG54NAH");

        private String value;
    }
}
