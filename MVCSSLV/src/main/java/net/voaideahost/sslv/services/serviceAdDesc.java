package net.voaideahost.sslv.services;

import java.util.ArrayList;

import net.voaideahost.sslv.dao.addesc.AdDescDAO;
import net.voaideahost.sslv.domain.addesc.AdDesc;

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
@RequestMapping ("/service/addesc")
public class serviceAdDesc {

	private Logger	logger	= LoggerFactory.getLogger(this.getClass()
									.getSimpleName());

	@Autowired
	AdDescDAO		adsDao;

	@RequestMapping (value = "/ads/{id}", method = RequestMethod.GET)
	public @ResponseBody
	String getAdDescByAdsId(@PathVariable ("id") String id) {

		try {
			ArrayList<AdDesc> ads = adsDao.getFullAdDescByHQL(Integer
					.parseInt(id));
			Gson json = new Gson();
			String gson = json.toJson(ads);

			return gson;
		} catch (Exception e) {
			logger.error("getAdDescByAdsId() " + e.getMessage());
		}
		return null;
	}
}
