package dao.ads;

import dao.BaseDAO;
import domain.mainpage.Ads;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Component
@Transactional
public class AdsDAOImpl extends BaseDAO implements AdsDAO {

	@SuppressWarnings("unchecked")
	public ArrayList<Ads> getMainListing(int page) {

		ArrayList<Ads> l = null;
		Session s = getSession();
		int first = ((page - 1) * ADS_PER_MAIN_PAGE);

		try {
			l = (ArrayList<Ads>) s.createCriteria(Ads.class)
					.setFirstResult(first).setMaxResults(ADS_PER_MAIN_PAGE)
					.addOrder(Order.asc("created")).list();
		} catch (Exception e) {
			e.printStackTrace();
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
		}

		return cnt.intValue();
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Ads> getByUser(String usr, int page) {

		Session ses = getSession();
		ArrayList<Ads> ads = null;
		int first = ((page - 1) * ADS_PER_LOGIN_PAGE);

		try {
			ads = (ArrayList<Ads>) ses.createCriteria(Ads.class)
					.setFirstResult(first).setMaxResults(ADS_PER_LOGIN_PAGE)
					.addOrder(Order.asc("created"))
					.add(Restrictions.eq("owner", usr)).list();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ads;
	}

	@Override
	public Ads getById(int adsId) {
		final Session session = getSession();
		Ads ads = null;
		try {
			Criteria criteria = session.createCriteria(Ads.class).add(
					Restrictions.eq("id", adsId));
			ads = (Ads) criteria.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ads;
	}

	@Override
	public void deleteById(int adsId) {
		Session s = getSession();
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("delete from ").append(Ads.class.getName())
					.append(" where id = :adsId");
			s.createQuery(hql.toString())
					.setString("adsId", Integer.toString(adsId))
					.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
