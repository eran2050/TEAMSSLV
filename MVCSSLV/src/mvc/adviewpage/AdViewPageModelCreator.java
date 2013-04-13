package mvc.adviewpage;

import javax.servlet.http.HttpServletRequest;

import mvc.IModel;
import mvc.IModelCreator;

import org.springframework.stereotype.Component;

@Component
public class AdViewPageModelCreator implements IModelCreator {

	public IModel createModel(HttpServletRequest request) {

		AdViewPageModel model = new AdViewPageModel();

		return model;
	}
}
