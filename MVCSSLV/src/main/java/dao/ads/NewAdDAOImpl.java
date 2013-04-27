package dao.ads;

import java.util.ArrayList;

import mvc.newadpage.NewAdPageParams;

import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dao.BaseDAO;
import domain.addesc.NewAdDesc;
import domain.ads.NewAd;

@Component
@Transactional
public class NewAdDAOImpl extends BaseDAO implements NewAdDAO {

	@Override
	public boolean setNewAd(String name, String userName,
			ArrayList<NewAdPageParams> paramList) {

		try {
			Session s = getSession();
			// Processing main text of advertisement to table
			NewAd na = new NewAd();
			na.setName(name);
			na.setOwner(userName);
			s.save(na);
			// Processing additional parameters to table
			for (NewAdPageParams c : paramList) {
				NewAdDesc na1 = new NewAdDesc();
				na1.setCriteria1(c.getParamName());
				na1.setValue1(c.getParamValue());
				na1.setNewAd(na);
				s.save(na1);
				na1 = null;
			}

			return true;
		} catch (Exception e) {
			// handled by aspect
		}
		return false;
	}
}
