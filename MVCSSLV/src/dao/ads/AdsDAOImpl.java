package dao.ads;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dao.BaseDAO;
import domain.ads.Ads;

@Component
@Transactional
public class AdsDAOImpl extends BaseDAO implements AdsDAO {

	private Logger logger = LoggerFactory.getLogger(AdsDAO.class);

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
			logger.error(e.getMessage());
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
			logger.error(e.getMessage());
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
			logger.error(e.getMessage());
		}

		return ads;
	}

	public Ads getById(int adsId) {
		final Session session = getSession();
		Ads ads = null;
		try {
			Criteria criteria = session.createCriteria(Ads.class).add(
					Restrictions.eq("id", adsId));
			ads = (Ads) criteria.uniqueResult();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return ads;
	}

	public boolean deleteById(int adsId) {
		Session s = getSession();
		try {
			StringBuilder hql = new StringBuilder();
			hql.append("delete from ").append(Ads.class.getName())
					.append(" where id = :adsId");
			s.createQuery(hql.toString())
					.setString("adsId", Integer.toString(adsId))
					.executeUpdate();
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}

	public void updateAds(Ads ads) {
		Session s = getSession();
		try {
			StringBuilder hql = new StringBuilder();

			hql.setLength(0);
			hql.append("update ").append(Ads.class.getName())
					.append(" set name = :name").append(" where id = :id");
			s.createQuery(hql.toString()).setInteger("id", ads.getId())
					.setString("name", ads.getName()).executeUpdate();
			logger.info("id" + ads.getId() + "name" + ads.getName());

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Ads> getByUser(String usr) {
		Session ses = getSession();
		ArrayList<Ads> ads = null;

		try {
			ads = (ArrayList<Ads>) ses.createCriteria(Ads.class)
					.addOrder(Order.asc("created"))
					.add(Restrictions.eq("owner", usr)).list();
			return ads;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}
}
