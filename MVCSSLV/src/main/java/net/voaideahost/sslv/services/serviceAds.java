package net.voaideahost.sslv.services;

import java.util.ArrayList;

import net.voaideahost.sslv.dao.ads.AdsDAO;
import net.voaideahost.sslv.domain.ads.Ads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@Controller
@RequestMapping ("/service/ads")
public class serviceAds {

	private Logger	logger	= LoggerFactory.getLogger(this.getClass()
									.getSimpleName());

	@Autowired
	AdsDAO			adsDao;

	@RequestMapping (value = "/user/{id}", method = RequestMethod.GET)
	public @ResponseBody
	String getAdsByUserId(@PathVariable ("id") String id) {

		try {
			ArrayList<Ads> ads = adsDao.getAdsListByUser(id);
			Gson json = new Gson();
			String gson = json.toJson(ads);

			return gson;
		} catch (Exception e) {
			logger.error("getAdsByUserId() " + e.getMessage());
		}
		return null;
	}
}