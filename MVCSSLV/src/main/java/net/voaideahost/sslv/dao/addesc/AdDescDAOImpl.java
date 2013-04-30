package net.voaideahost.sslv.dao.addesc;

import java.util.ArrayList;

import net.voaideahost.sslv.dao.BaseDAO;
import net.voaideahost.sslv.domain.addesc.AdDesc;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
public class AdDescDAOImpl extends BaseDAO implements AdDescDAO {

	@Override
	@SuppressWarnings ("unchecked")
	public ArrayList<AdDesc> getFullAdDescByHQL(int adsId) {

		try {
			ArrayList<AdDesc> fullAdDesc;
			Session s = getSession();
			Query query = s.createQuery("from AdDesc where adsId = :adsId ");
			query.setParameter("adsId", adsId);
			fullAdDesc = (ArrayList<AdDesc>) query.list();

			return fullAdDesc;
		} catch (Exception e) {
			// handled by aspect
		}
		return null;
	}

	@Override
	@SuppressWarnings ("unchecked")
	public ArrayList<AdDesc> getFullAdDescByCriteria(int adsId) {

		try {
			ArrayList<AdDesc> fullAdDesc = new ArrayList<AdDesc>();
			Session session = getSession();
			Criteria criteria = session.createCriteria(AdDesc.class).add(
					Restrictions.eq("adsId", adsId));
			fullAdDesc = (ArrayList<AdDesc>) criteria.list();

			return fullAdDesc;
		} catch (Exception e) {
			// handled by aspect
		}
		return null;
	}

	@Override
	public boolean deleteByAdsId(int adsId) {

		try {
			Session s = getSession();
			StringBuilder hql = new StringBuilder();
			hql.append("delete from ").append(AdDesc.class.getName())
					.append(" where adsId = :adsId");
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
	public boolean updateAdDesc(ArrayList<AdDesc> fullDesc) {

		try {
			Session s = getSession();
			StringBuilder hql = new StringBuilder();

			for (AdDesc adDesc : fullDesc) {

				hql.setLength(0);
				hql.append("update ").append(AdDesc.class.getName())
						.append(" set criteria = :criteria, value=:value")
						.append(" where id = :id");
				s.createQuery(hql.toString()).setInteger("id", adDesc.getId())
						.setString("criteria", adDesc.getCriteria())
						.setString("value", adDesc.getValue()).executeUpdate();

				return true;
			}
		} catch (Exception e) {
			// handled by aspect
		}
		return false;
	}
}
