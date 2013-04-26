package util.rest;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import dao.ads.AdsDAO;
import domain.ads.Ads;

@Controller
@RequestMapping ("/service/ads")
public class serviceAds {

	@Autowired
	AdsDAO	adsDao;

	@RequestMapping (value = "/user/{id}", method = RequestMethod.GET)
	public @ResponseBody
<<<<<<< HEAD
	String getUserById(@PathVariable String id) {

		ArrayList<Ads> ads = adsDao.getByUser(id);
		Gson json = new Gson();
		String gson = json.toJson(ads);
		logger.info("getByUser(" + id + ") : " + gson);

		return gson;
=======
	String getAdsByUserId(@PathVariable ("id") String id) {

		try {
			ArrayList<Ads> ads = adsDao.getAdsListByUser(id);
			Gson json = new Gson();
			String gson = json.toJson(ads);

			return gson;
		} catch (Exception e) {
			// handled by aspect
		}
		return null;
>>>>>>> origin/Spring-MVC-Sashko-26apr2013
	}
}
