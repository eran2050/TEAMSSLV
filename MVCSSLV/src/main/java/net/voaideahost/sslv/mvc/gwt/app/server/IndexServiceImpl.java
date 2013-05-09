package net.voaideahost.sslv.mvc.gwt.app.server;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import net.voaideahost.sslv.dao.addesc.AdDescDAO;
import net.voaideahost.sslv.dao.ads.AdsDAO;
import net.voaideahost.sslv.dao.users.UsersDAO;
import net.voaideahost.sslv.domain.addesc.AdDesc;
import net.voaideahost.sslv.domain.ads.Ads;
import net.voaideahost.sslv.domain.users.Users;
import net.voaideahost.sslv.mvc.gwt.app.client.IndexService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.gson.Gson;

@Service
public class IndexServiceImpl implements IndexService {

	Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Autowired
	private UsersDAO uDao;

	@Autowired
	private AdsDAO aDao;

	@Autowired
	private AdDescDAO dDao;

	@Override
	public String getMainListing(int page) {

		try {
			ArrayList<Ads> ads = aDao.getMainListing(page);
			Gson gson = new Gson();
			String toJson = gson.toJson(ads);

			return toJson;
		} catch (Exception e) {
			logger.error("getMainListing() " + e.getMessage());
		}

		return null;
	}

	@Override
	public Integer getTotalAds() {
		try {
			int num = aDao.getTotalAdsCount();

			return new Integer(num);
		} catch (Exception e) {
			logger.error("getTotalAds() " + e.getMessage());
		}

		return 0;
	}

	@Override
	public String getAdDesc(int adDescId) {

		try {
			ArrayList<AdDesc> ads = dDao.getFullAdDescByCriteria(adDescId);
			Gson gson = new Gson();
			String toJson = gson.toJson(ads);

			return toJson;
		} catch (Exception e) {
			logger.error("getAdDesc() " + e.getMessage());
		}

		return null;
	}

	@Override
	public String doLogin(String userName) {

		try {
			Users user = uDao.getUserById(userName);
			Gson gson = new Gson();
			String toJson = gson.toJson(user);

			String userNameSession = session().getAttribute("username").toString();
			if (null == userNameSession) {
				if (userName.equals(user.getId())) {
					session().setAttribute("username", userName);
					return toJson;
				} else {
					return null;
				}
			} else {
				if (userName.equals(userNameSession)) {
					return toJson;
				} else {
					return null;
				}
			}
		} catch (Exception e) {
			logger.error("doLogin() " + e.getMessage());
		}

		return null;
	}

	public HttpSession session() {

		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		return attr.getRequest().getSession(true);
	}
}
