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

	public void setNewAd(String name, String userName,
			ArrayList<NewAdPageParams> paramList) {

		Session s = getSession();
		NewAd na = new NewAd();
		na.setName(name);
		na.setOwner(userName);

		s.save(na);

		for (NewAdPageParams c : paramList) {
			NewAdDesc na1 = new NewAdDesc();
			System.out.println("Savex: " + c.getParamName());
			na1.setCriteria1(c.getParamName());
			na1.setValue1(c.getParamValue());
			na1.setNewAd(na);
			s.save(na1);
			na1 = null;
		}

		System.out.println("Saved1: " + na.getId() + na.getCreated());
		System.out.println("Saved2: " + na.getId());
	}

	@Override
	public boolean adExists(String name, String userName,
			ArrayList<NewAdPageParams> paramList) {
		return false;
	}
}
