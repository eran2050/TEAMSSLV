package dao.ads;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import util.HibernateUtil;
import domain.mainpage.Ads;

public class AdsDAOImpl implements AdsDAO {

	@SuppressWarnings("unchecked")
	public ArrayList<Ads> getMainListing(int page) {

		ArrayList<Ads> l = null;
		Session s = getSession();
		int first = ((page - 1) * ADS_PER_MAIN_PAGE);

		try {
			l = (ArrayList<Ads>) s.createCriteria(Ads.class)
					.setFirstResult(first).setMaxResults(ADS_PER_MAIN_PAGE)
					.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(s);
		}

		return l;
	}

	public int getCount() {

		Session s = getSession();
		Long cnt = (long) 0;
		try {
			cnt = (Long) s.createCriteria(Ads.class)
					.setProjection(Projections.rowCount()).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(s);
		}

		return cnt.intValue();
	}

	public int getCountByUser(String usr) {

		Long cnt = (long) 0;
		Session s = getSession();
		try {
			cnt = (Long) s.createCriteria(Ads.class)
					.add(Restrictions.eq("owner", usr))
					.setProjection(Projections.rowCount()).uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(s);
		}

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
		ArrayList<Ads> ads = null;
		int first = ((page - 1) * ADS_PER_LOGIN_PAGE);

		try {
			ads = (ArrayList<Ads>) ses.createCriteria(Ads.class)
					.setFirstResult(first).setMaxResults(ADS_PER_LOGIN_PAGE)
					.add(Restrictions.eq("owner", usr)).list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(ses);
		}

		return ads;
	}

	@Override
	public Ads getById(int adsId) {
		final Session session = getSession();
		Ads ads = null;
		try {
			Criteria criteria = session.createCriteria(Ads.class).add(
					Restrictions.eq("id", new Integer(adsId)));
			ads = (Ads) criteria.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return ads;
	}

	@Override
	public void deleteById(int adsId) {
		Session s = getSession();
		try {
			s.beginTransaction();
			String hql = "delete from ".concat(Ads.class.getName()).concat(
					" where id = :adsId");
			s.createQuery(hql)
					.setString("adsId", new Integer(adsId).toString())
					.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(s);
		}
	}

}
