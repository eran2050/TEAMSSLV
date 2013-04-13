package mvc.adviewpage;

import java.util.ArrayList;
import java.util.Arrays;

import mvc.IModel;
import mvc.IModelCreator;
import org.springframework.stereotype.Component;

import domain.addescpage.AdDesc;
import domain.mainpage.Ads;

import javax.servlet.http.HttpServletRequest;

@Component
public class AdViewPageModelCreator implements IModelCreator {

	@Override
	public IModel createModel(HttpServletRequest request) {

		AdViewPageModel model = new AdViewPageModel();

		return model;
	}
}
