package dao.addesc;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import util.HibernateUtil;
import domain.addescpage.AdDesc;

public class AdDescDAOImpl implements AdDescDAO {

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<AdDesc> getFullAdDesc(int adsId) {
		// TODO Auto-generated method stub
		ArrayList<AdDesc> fullAdDesc = new ArrayList<AdDesc>();
		Session s = getSession();
		Query query = s.createQuery("from AdDesc where adsId = :adsId ");
		query.setParameter("adsId", adsId);
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

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<AdDesc> getFullAdDesc1(int adsId) {
		ArrayList<AdDesc> fullAdDesc = new ArrayList<AdDesc>();
		final Session session = getSession();
		try {
			Criteria criteria = session.createCriteria(AdDesc.class).add(
					Restrictions.eq("adsId", new Integer(adsId)));

			fullAdDesc = (ArrayList<AdDesc>)  criteria.list();

			return fullAdDesc;
		} finally {
			session.close();
			
		}
	}

}
