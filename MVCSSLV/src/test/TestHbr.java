package test;

import java.util.ArrayList;

import dao.addesc.AdDescDAO;
import dao.addesc.AdDescDAOImpl;
import dao.ads.AdsDAO;
import dao.ads.AdsDAOImpl;
import domain.addescpage.AdDesc;
import domain.mainpage.Ads;

public class TestHbr {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AdDescDAO fullDesc = new AdDescDAOImpl();
		ArrayList<AdDesc> al = fullDesc.getFullAdDesc1(5);

		int n = al.size();
		for (int i = 0; i < n; i++)
			System.out.println(al.get(i).getId() + " " + al.get(i).getAdsId()
					+ " " + al.get(i).getCriteria() + "  "
					+ al.get(i).getValue());

	

				AdsDAO dao = new AdsDAOImpl();
				
				Ads ads =  dao.getById(5);

				System.out.println(ads.getName());
	}

}
