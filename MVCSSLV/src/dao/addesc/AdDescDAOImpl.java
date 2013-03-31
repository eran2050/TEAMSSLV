package dao.addesc;

import java.util.ArrayList;

import org.hibernate.Query;
import org.hibernate.Session;

import util.HibernateUtil;

import domain.addescpage.AdDesc;

public class AdDescDAOImpl implements AdDescDAO {

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<AdDesc> getFullAdDesc(int adId) {
		// TODO Auto-generated method stub
		ArrayList<AdDesc> fullAdDesc = new ArrayList<AdDesc>();

		Session s = getSession();

		Query query = s.createQuery("from ad_desc where ADS_ID = :adsId ");
		query.setParameter("adsId", adId);
		fullAdDesc = (ArrayList<AdDesc>) query.list();
		
		close(s);

		return fullAdDesc;
	}

	private Session getSession() {

		Session s = HibernateUtil.getSessionFactory().openSession();
		if (!s.isConnected())
			s.reconnect(null);

		return s;
	}

	private void close(Session s) {

		if (s != null && s.isOpen()) {
			s.close();
		}
	}

}
