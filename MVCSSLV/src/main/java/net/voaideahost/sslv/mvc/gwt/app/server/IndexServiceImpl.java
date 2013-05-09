package net.voaideahost.sslv.mvc.gwt.app.server;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import net.voaideahost.sslv.common.Config;
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
import org.springframework.transaction.annotation.Transactional;
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
	public String doLogin(String clientUserName, String clientSessionID) {

		try {
			Users user = uDao.getUserById(clientUserName);
			Gson gson = new Gson();
			String toJson = gson.toJson(user);

			String serverUserName = Config.VAL_EMPTY;
			String serverSessionID = Config.VAL_EMPTY;
			String serverUserDAO = Config.VAL_EMPTY;

			if (user != null && user.getId() != null) {
				serverUserDAO = user.getId();
			}

			// Step 1 - Getting Session Params
			if (session().getAttribute(Config.VAL_USERNAME) != null && session().getAttribute(Config.VAL_SESSIONID) != null) {

				serverUserName = session().getAttribute(Config.VAL_USERNAME).toString();
				serverSessionID = session().getAttribute(Config.VAL_SESSIONID).toString();
				logger.info("doLogin() " + "using userNameSession=" + serverUserName + " as session() attribute");
				logger.info("doLogin() " + "using serverSessionID=" + serverSessionID + " as session() attribute");
				logger.info("doLogin() proceed");
			}

			// Step 2A - Restoring Session
			if (clientSessionID.equals(serverSessionID) && !clientUserName.equals(serverUserName)) {

				logger.info("doLogin() " + "got userNameSession=" + serverUserName + " in session()");
				logger.info("doLogin() " + "got serverSessionID=" + serverSessionID + " in session()");

				user = uDao.getUserById(serverUserName);
				toJson = gson.toJson(user);

				logger.info("doLogin() restorred session");

				return toJson;
			}

			// Step 2B - Register Session
			if (!serverUserDAO.equals(Config.VAL_EMPTY) && clientUserName.equals(serverUserDAO) && serverSessionID.equals(Config.VAL_EMPTY)) {

				session().setAttribute(Config.VAL_USERNAME, clientUserName);
				session().setAttribute(Config.VAL_SESSIONID, clientSessionID);
				logger.info("doLogin() " + "saved clientUserName=" + clientUserName + " in session()");
				logger.info("doLogin() " + "saved clientSessionID=" + clientSessionID + " in session()");
				logger.info("doLogin() success login");

				return toJson;
			}
			logger.info("doLogin() " + " failed login");

			return null;

		} catch (Exception e) {

			logger.error("doLogin() failed " + e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

	@Transactional
	public HttpSession session() {

		try {
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

			return attr.getRequest().getSession();
		} catch (Exception e) {

			logger.error("session() " + e.getMessage());
		}

		return null;
	}
}
