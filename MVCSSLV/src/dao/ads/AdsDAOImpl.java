package dao.ads;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dao.BaseDAO;
import domain.ads.Ads;

@Component
@Transactional
public class AdsDAOImpl extends BaseDAO implements AdsDAO {

	@Override
	@SuppressWarnings("unchecked")
	public ArrayList<Ads> getMainListing(int page) {

		ArrayList<Ads> l = null;
		Session s = getSession();
		int first = ((page - 1) * VAL_ADS_PER_MAIN_PAGE);
		l = (ArrayList<Ads>) s.createCriteria(Ads.class).setFirstResult(first)
				.setMaxResults(VAL_ADS_PER_MAIN_PAGE)
				.addOrder(Order.asc("created")).list();
		return l;
	}

	@Override
	public int getTotalAdsCount() {

		Session s = getSession();
		Long cnt = (long) 0;
		cnt = (Long) s.createCriteria(Ads.class)
				.setProjection(Projections.rowCount()).uniqueResult();

		return cnt.intValue();
	}

	@Override
	public int getAdsCountByUser(String usr) {

		Long cnt = (long) 0;
		Session s = getSession();
		cnt = (Long) s.createCriteria(Ads.class)
				.add(Restrictions.eq("owner", usr))
				.setProjection(Projections.rowCount()).uniqueResult();

		return cnt.intValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	public ArrayList<Ads> getAdsListByUserAndPage(String usr, int page) {

		Session ses = getSession();
		ArrayList<Ads> ads = null;
		int first = ((page - 1) * VAL_ADS_PER_LOGIN_PAGE);
		ads = (ArrayList<Ads>) ses.createCriteria(Ads.class)
				.setFirstResult(first).setMaxResults(VAL_ADS_PER_LOGIN_PAGE)
				.addOrder(Order.asc("created"))
				.add(Restrictions.eq("owner", usr)).list();

		return ads;
	}

	@Override
	public Ads getSingleAdsById(int adsId) {

		Session session = getSession();
		Ads ads = null;
		Criteria criteria = session.createCriteria(Ads.class).add(
				Restrictions.eq("id", adsId));
		ads = (Ads) criteria.uniqueResult();

		return ads;
	}

	@Override
	public boolean deleteSingleAdsById(int adsId) {

		Session s = getSession();
		StringBuilder hql = new StringBuilder();
		hql.append("delete from ").append(Ads.class.getName())
				.append(" where id = :adsId");
		s.createQuery(hql.toString())
				.setString("adsId", Integer.toString(adsId)).executeUpdate();

		return true;
	}

	@Override
	public void updateAds(Ads ads) {

		Session s = getSession();
		StringBuilder hql = new StringBuilder();

		hql.setLength(0);
		hql.append("update ").append(Ads.class.getName())
				.append(" set name = :name").append(" where id = :id");
		s.createQuery(hql.toString()).setInteger("id", ads.getId())
				.setString("name", ads.getName()).executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Ads> getAdsListByUser(String usr) {

		Session ses = getSession();
		ArrayList<Ads> ads = null;
		ads = (ArrayList<Ads>) ses.createCriteria(Ads.class)
				.addOrder(Order.asc("created"))
				.add(Restrictions.eq("owner", usr)).list();

		return ads;
	}
}
