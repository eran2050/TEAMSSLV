package util.rest;

import java.util.ArrayList;

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

	@Autowired
	AdDescDAO	adsDao;

	@RequestMapping (value = "/ads/{id}", method = RequestMethod.GET)
	public @ResponseBody
	String getAdDescByAdsId(@PathVariable ("id") String id) {

		try {
			ArrayList<AdDesc> ads = adsDao.getFullAdDescByHQL(Integer.parseInt(id));
			Gson json = new Gson();
			String gson = json.toJson(ads);

			return gson;
		} catch (Exception e) {
			// handled by aspect
		}
		return null;
	}
}
