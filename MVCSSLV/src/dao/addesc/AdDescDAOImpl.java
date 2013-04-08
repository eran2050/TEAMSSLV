package dao.addesc;

import dao.BaseDAO;
import domain.addescpage.AdDesc;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Component
@Transactional
public class AdDescDAOImpl extends BaseDAO implements AdDescDAO {

    @SuppressWarnings("unchecked")
    @Override
    public ArrayList<AdDesc> getFullAdDesc(int adsId) {
        ArrayList<AdDesc> fullAdDesc;
        Session s = getSession();
        Query query = s.createQuery("from AdDesc where adsId = :adsId ");
        query.setParameter("adsId", adsId);
        fullAdDesc = (ArrayList<AdDesc>) query.list();

        return fullAdDesc;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ArrayList<AdDesc> getFullAdDesc1(int adsId) {
        ArrayList<AdDesc> fullAdDesc = new ArrayList<AdDesc>();
        final Session session = getSession();
        try {
            Criteria criteria = session.createCriteria(AdDesc.class).add(
                    Restrictions.eq("adsId", adsId));

            fullAdDesc = (ArrayList<AdDesc>) criteria.list();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fullAdDesc;
    }

    @Override
    public void deleteByAdsId(int adsId) {
        Session s = getSession();
        try {
            s.beginTransaction();
            StringBuilder hql = new StringBuilder();
            hql.append("delete from ").append(AdDesc.class.getName()).append(
                    " where adsId = :adsId");
            s.createQuery(hql.toString())
                    .setString("adsId", Integer.toString(adsId))
                    .executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
