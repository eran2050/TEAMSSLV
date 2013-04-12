package test;

import dao.addesc.AdDescDAO;
import dao.addesc.AdDescDAOImpl;
import dao.ads.AdsDAO;
import dao.ads.AdsDAOImpl;
import domain.addescpage.AdDesc;
import domain.mainpage.Ads;

import java.util.ArrayList;

public class TestHbr {

    public static void main(String[] args) {
        AdDescDAO fullDesc = new AdDescDAOImpl();
        ArrayList<AdDesc> al = fullDesc.getFullAdDesc1(5);

        for (AdDesc anAl : al)
            System.out.println(anAl.getId() + " " + anAl.getAdsId()
                    + " " + anAl.getCriteria() + "  "
                    + anAl.getValue());

        AdsDAO dao = new AdsDAOImpl();

        Ads ads = dao.getById(5);

        System.out.println(ads.getName());
    }

}
