package dao.addesc;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dao.BaseDAO;
import domain.addesc.AdDesc;

@Component
@Transactional
public class AdDescDAOImpl extends BaseDAO implements AdDescDAO {

	@Override
	@SuppressWarnings("unchecked")
	public ArrayList<AdDesc> getFullAdDescByHQL(int adsId) {

		ArrayList<AdDesc> fullAdDesc;
		Session s = getSession();
		Query query = s.createQuery("from AdDesc where adsId = :adsId ");
		query.setParameter("adsId", adsId);
		fullAdDesc = (ArrayList<AdDesc>) query.list();

		return fullAdDesc;
	}

	@Override
	@SuppressWarnings("unchecked")
	public ArrayList<AdDesc> getFullAdDescByCriteria(int adsId) {

		ArrayList<AdDesc> fullAdDesc = new ArrayList<AdDesc>();
		Session session = getSession();
		Criteria criteria = session.createCriteria(AdDesc.class).add(
				Restrictions.eq("adsId", adsId));
		fullAdDesc = (ArrayList<AdDesc>) criteria.list();

		return fullAdDesc;
	}

	@Override
	public boolean deleteByAdsId(int adsId) {

		Session s = getSession();
		StringBuilder hql = new StringBuilder();
		hql.append("delete from ").append(AdDesc.class.getName())
				.append(" where adsId = :adsId");
		s.createQuery(hql.toString())
				.setString("adsId", Integer.toString(adsId)).executeUpdate();

		return true;
	}

	@Override
	public boolean updateAdDesc(ArrayList<AdDesc> fullDesc) {

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

		}
		return true;
	}
}
