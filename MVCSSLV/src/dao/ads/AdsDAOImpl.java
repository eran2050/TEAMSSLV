package dao.ads;

import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import util.HibernateUtil;
import domain.mainpage.Ads;

public class AdsDAOImpl implements AdsDAO {

	@SuppressWarnings("unchecked")
	public ArrayList<Ads> getMainListing(int page) {

		Session s = getSession();
		int first = ((page - 1) * ADS_PER_PAGE);

		ArrayList<Ads> l = (ArrayList<Ads>) s.createCriteria(Ads.class)
				.setFirstResult(first).setMaxResults(ADS_PER_PAGE).list();
		close(s);

		return l;
	}

	public int getCount() {

		Session s = getSession();
		Long cnt = (Long) s.createCriteria(Ads.class)
				.setProjection(Projections.rowCount()).uniqueResult();
		close(s);

		return cnt.intValue();
	}

	public int getCountByUser(String usr) {

		Session s = getSession();
		Long cnt = (Long) s.createCriteria(Ads.class)
				.add(Restrictions.eq("owner", usr))
				.setProjection(Projections.rowCount()).uniqueResult();
		close(s);

		return cnt.intValue();
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

	@SuppressWarnings("unchecked")
	public ArrayList<Ads> getByUser(String usr, int page) {

		Session ses = getSession();
		ArrayList<Ads> ads;
		int first = ((page - 1) * ADS_PER_PAGE);

		ads = (ArrayList<Ads>) ses.createCriteria(Ads.class)
				.setFirstResult(first).setMaxResults(ADS_PER_PAGE)
				.add(Restrictions.eq("owner", usr)).list();
		close(ses);

		return ads;
	}

}
