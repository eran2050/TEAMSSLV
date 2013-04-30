package net.voaideahost.sslv.dao.ads;

import static net.voaideahost.sslv.common.Config.VAL_ADS_PER_LOGIN_PAGE;
import static net.voaideahost.sslv.common.Config.VAL_ADS_PER_MAIN_PAGE;

import java.util.ArrayList;

import net.voaideahost.sslv.dao.BaseDAO;
import net.voaideahost.sslv.domain.ads.Ads;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
public class AdsDAOImpl extends BaseDAO implements AdsDAO {

	@Override
	@SuppressWarnings ("unchecked")
	public ArrayList<Ads> getMainListing(int page) {

		try {
			ArrayList<Ads> l = null;
			Session s = getSession();
			int first = ((page - 1) * VAL_ADS_PER_MAIN_PAGE);
			l = (ArrayList<Ads>) s.createCriteria(Ads.class)
					.setFirstResult(first).setMaxResults(VAL_ADS_PER_MAIN_PAGE)
					.addOrder(Order.asc("created")).list();
			return l;
		} catch (Exception e) {
			// handled by aspect
		}

		return null;
	}

	@Override
	public int getTotalAdsCount() {

		try {
			Session s = getSession();
			Long cnt = (long) 0;
			cnt = (Long) s.createCriteria(Ads.class)
					.setProjection(Projections.rowCount()).uniqueResult();

			return cnt.intValue();
		} catch (Exception e) {
			// handled by aspect
		}
		return 0;
	}

	@Override
	public int getAdsCountByUser(String usr) {

		try {
			Long cnt = (long) 0;
			Session s = getSession();
			cnt = (Long) s.createCriteria(Ads.class)
					.add(Restrictions.eq("owner", usr))
					.setProjection(Projections.rowCount()).uniqueResult();

			return cnt.intValue();
		} catch (Exception e) {
			// handled by aspect
		}
		return 0;
	}

	@Override
	@SuppressWarnings ("unchecked")
	public ArrayList<Ads> getAdsListByUserAndPage(String usr, int page) {

		try {
			Session ses = getSession();
			ArrayList<Ads> ads = null;
			int first = ((page - 1) * VAL_ADS_PER_LOGIN_PAGE);
			ads = (ArrayList<Ads>) ses.createCriteria(Ads.class)
					.setFirstResult(first)
					.setMaxResults(VAL_ADS_PER_LOGIN_PAGE)
					.addOrder(Order.asc("created"))
					.add(Restrictions.eq("owner", usr)).list();

			return ads;
		} catch (Exception e) {
			// handled by aspect
		}

		return null;
	}

	@Override
	public Ads getSingleAdsById(int adsId) {

		try {
			Session session = getSession();
			Ads ads = null;
			Criteria criteria = session.createCriteria(Ads.class).add(
					Restrictions.eq("id", adsId));
			ads = (Ads) criteria.uniqueResult();

			return ads;
		} catch (Exception e) {
			// handled by aspect
		}
		return null;
	}

	@Override
	public boolean deleteSingleAdsById(int adsId) {

		try {
			Session s = getSession();
			StringBuilder hql = new StringBuilder();
			hql.append("delete from ").append(Ads.class.getName())
					.append(" where id = :adsId");
			s.createQuery(hql.toString())
					.setString("adsId", Integer.toString(adsId))
					.executeUpdate();

			return true;
		} catch (Exception e) {
			// handled by aspect
		}
		return false;
	}

	@Override
	public void updateAds(Ads ads) {

		try {
			Session s = getSession();
			StringBuilder hql = new StringBuilder();

			hql.setLength(0);
			hql.append("update ").append(Ads.class.getName())
					.append(" set name = :name").append(" where id = :id");
			s.createQuery(hql.toString()).setInteger("id", ads.getId())
					.setString("name", ads.getName()).executeUpdate();

		} catch (Exception e) {
			// handled by aspect
		}
	}

	@SuppressWarnings ("unchecked")
	@Override
	public ArrayList<Ads> getAdsListByUser(String usr) {

		try {
			Session ses = getSession();
			ArrayList<Ads> ads = null;
			ads = (ArrayList<Ads>) ses.createCriteria(Ads.class)
					.addOrder(Order.asc("created"))
					.add(Restrictions.eq("owner", usr)).list();

			return ads;
		} catch (Exception e) {
			// handled by aspect
		}

		return null;
	}
}
