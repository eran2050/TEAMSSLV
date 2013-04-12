package dao.newad;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import domain.newad.NewAd;
import domain.newaddesc.NewAdDesc;
import mvc.newad.NewAdPageParams;

@SuppressWarnings("unused")
 public class NewAdDAOImpl implements NewAdDAO {


public void setNewAd(String daoname,String daouser, ArrayList<NewAdPageParams> paramList2) {

		Session s = getSession();
			Long id1;
		 Transaction tx = s.beginTransaction();
		  NewAd na = new NewAd();
		  na.setName(daoname);
		  na.setOwner(daouser);
		  
		  
		  s.save(na);
		  
		  
		for(NewAdPageParams c : paramList2) {
		  NewAdDesc na1=new NewAdDesc();
		  System.out.println("Savex: "+c.getParamName());
		  na1.setCriteria1(c.getParamName());
		  na1.setValue1(c.getParamValue());
		  na1.setNewAd(na);
		  s.save(na1);
		  na1=null;
		   }

		  System.out.println("Saved1: "+na.getId()+na.getCreated());
		  ;
		  
		  tx.commit();
		 
		 
		  
		close(s);
		System.out.println("Saved2: "+na.getId());
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

}
