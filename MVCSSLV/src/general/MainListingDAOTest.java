package general;

import dao.ads.AdsDAO;
import dao.ads.AdsDAOImpl;

public class MainListingDAOTest {

	public static void main(String[] args) {

		AdsDAO dao = new AdsDAOImpl();
		int i;
		i = dao.getCount();

		System.out.println("getCount()=" + i);

	}

}
