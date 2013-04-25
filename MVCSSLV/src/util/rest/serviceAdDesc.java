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

import dao.addesc.AdDescDAO;
import domain.addesc.AdDesc;

@Controller
@RequestMapping ("/service/addesc")
public class serviceAdDesc {

	private Logger	logger	= LoggerFactory.getLogger(serviceUsers.class);

	@Autowired
	AdDescDAO		adsDao;

	@RequestMapping (value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody
	String getAdDescByAdsId(@PathVariable ("id") String id) {

		ArrayList<AdDesc> ads = adsDao.getFullAdDesc(Integer.parseInt(id));
		Gson json = new Gson();
		String gson = json.toJson(ads);
		logger.info("getAdDescByAdsId(" + id + ") : " + gson);

		return gson;
	}
}
