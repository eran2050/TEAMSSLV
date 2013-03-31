package util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class HibernateUtil {

	private static volatile SessionFactory	sessionFactory;
	private static ServiceRegistry			serviceRegistry;

	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			synchronized (SessionFactory.class) {
				if (sessionFactory == null) {

					Configuration configuration = new Configuration()
							.configure();

					serviceRegistry = new ServiceRegistryBuilder()
							.applySettings(configuration.getProperties())
							.buildServiceRegistry();
					sessionFactory = configuration
							.buildSessionFactory(serviceRegistry);
				}
			}

		}
		return sessionFactory;
	}
}
