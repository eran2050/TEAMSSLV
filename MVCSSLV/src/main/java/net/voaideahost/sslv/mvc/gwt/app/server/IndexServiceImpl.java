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
	public String getMainListing(String viewMode, int page, String userName) {

		try {
			ArrayList<Ads> ads;
			if (viewMode.equals(Config.VAL_VIEW_MODE_ALL)) {
				ads = aDao.getMainListing(page);
			} else {
				ads = aDao.getAdsListByUserAndPage(userName, page);
			}
			Gson gson = new Gson();
			String toJson = gson.toJson(ads);

			return toJson;
		} catch (Exception e) {

			logger.error("getMainListing() " + e.getMessage());
		}

		return null;
	}

	@Override
	public Integer getTotalAds(String viewMode, String userName) {
		try {
			int num;
			if (viewMode.equals(Config.VAL_VIEW_MODE_ALL)) {
				num = aDao.getTotalAdsCount();
			} else {
				num = aDao.getAdsCountByUser(userName);
			}

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

		String serverSessionID = session().getId();

		logger.info("doLogin() start");
		logger.info("doLogin() clientUserName=" + clientUserName);
		logger.info("doLogin() clientSessionID=" + clientSessionID);
		logger.info("doLogin() serverSessionID=" + serverSessionID);

		try {
			ArrayList<Users> list = new ArrayList<Users>();
			Users user = uDao.getUserById(clientUserName);
			Gson gson = new Gson();
			String toJson;

			String serverUserName = Config.VAL_EMPTY;
			String serverUserDAO = Config.VAL_EMPTY;

			if (user != null && user.getId() != null) {
				serverUserDAO = user.getId();
			}

			// Register Session
			if (!serverUserDAO.equals(Config.VAL_EMPTY) && clientUserName.equals(serverUserDAO)) {

				session().setAttribute(Config.VAL_USERNAME, clientUserName);
				logger.info("doLogin() " + "saved clientUserName=" + clientUserName + " in session()");
				logger.info("doLogin() " + "passed serverSessionID=" + serverSessionID + " to Client");

				list.add(user);
				Users u2 = new Users();
				u2.setId(serverSessionID);
				list.add(u2);
				toJson = gson.toJson(list);
				logger.info(toJson);

				logger.info("doLogin() success");

				return toJson;
			}

			// Step 1 - Restoring Session
			if (session().getAttribute(Config.VAL_USERNAME) != null) {

				serverUserName = session().getAttribute(Config.VAL_USERNAME).toString();
				logger.info("doLogin() " + "using userNameSession=" + serverUserName + " as session() attribute");
				logger.info("doLogin() proceed");

				logger.info("doLogin() " + "got userNameSession=" + serverUserName + " in session()");
				logger.info("doLogin() " + "got serverSessionID=" + serverSessionID + " in session()");

				user = uDao.getUserById(serverUserName);
				list.add(user);
				Users u2 = new Users();
				u2.setId(serverSessionID);
				list.add(u2);
				toJson = gson.toJson(list);
				logger.info(toJson);
				logger.info("doLogin() session restorred");

				return toJson;
			}

			logger.info("doLogin() failed");
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

	@Override
	public String doLogout(String clientUserName, String clientSessionID) {

		String serverSessionID = session().getId();

		logger.info("doLogout() start");
		logger.info("doLogout() clientUserName=" + clientUserName);
		logger.info("doLogout() clientSessionID=" + clientSessionID);
		logger.info("doLogout() serverSessionID=" + serverSessionID);

		try {
			if (session().getAttribute(Config.VAL_USERNAME) != null) {

				String serverUserName = session().getAttribute(Config.VAL_USERNAME).toString();

				if (serverUserName.equals(clientUserName) && serverSessionID.equals(clientSessionID)) {

					session().removeAttribute(Config.VAL_USERNAME);
					session().invalidate();
					logger.info("doLogout() success");

					return serverSessionID;
				}
			}
		} catch (Exception e) {

			logger.error("doLogout() " + e.getMessage());
		}

		logger.info("doLogout() failed");
		return null;
	}
}
