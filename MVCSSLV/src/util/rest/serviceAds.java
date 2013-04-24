package util.rest;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/service/ads")
public class serviceAds {

	private Logger logger = LoggerFactory.getLogger(serviceUsers.class);

	@Autowired
	AdsDAO adsDao;

	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public @ResponseBody
	String getAdsByUserId(@PathVariable String id) {

		ArrayList<Ads> ads = adsDao.getByUser(id);
		Gson json = new Gson();
		String gson = json.toJson(ads);
		logger.info("getAdsByUserId(" + id + ") : " + gson);

		return gson;
	}
}
