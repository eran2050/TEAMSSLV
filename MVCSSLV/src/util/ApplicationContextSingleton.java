package util;

import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationContextSingleton {

    private static volatile ApplicationContext applicationContext;

    public static ApplicationContext get() {

        if (applicationContext == null) {
            synchronized (SessionFactory.class) {
                if (applicationContext == null) {

                    applicationContext = new ClassPathXmlApplicationContext(
                            "BeanLocations.xml");
                }
            }
        }
        return applicationContext;
    }
}
