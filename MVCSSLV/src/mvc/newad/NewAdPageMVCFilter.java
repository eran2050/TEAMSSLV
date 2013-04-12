package mvc.newad;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mvc.IController;
import mvc.IModel;
import mvc.IModelCreator;
import mvc.URIMapping;
import mvc.URIMappingBuilder;

@WebFilter ("/NewAdPageMVCFilter")
public class NewAdPageMVCFilter  implements Filter {

	private Map<String, URIMapping>	mapping;

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String requestURI = httpRequest.getRequestURI();
		String contextPath = httpRequest.getContextPath();
		String uri = requestURI.replace(contextPath, "");

		URIMapping uriMapping = mapping.get(uri);
		if (uriMapping == null) {
			httpResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		else
		{
		assert uriMapping != null;
		IModelCreator modelCreator = uriMapping.getModelCreator();
		IModel model = modelCreator.createModel(httpRequest);

		IController controller = uriMapping.getController();
		model = controller.execute(model);

		httpRequest.setAttribute("model", model);

		String view = uriMapping.getView();
		RequestDispatcher dispatcher = httpRequest.getRequestDispatcher(view);
		dispatcher.forward(httpRequest, httpResponse);
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {

		mapping = new HashMap<String, URIMapping>();

		// Main Page
		URIMapping NewAdPageMapping = new URIMappingBuilder().build("/add/",
				new NewAdModelCreator(), new NewAdPageController(),
				"/jsp/newpage.jsp");

		// Fill Mapping
		mapping.put(NewAdPageMapping.getUri(), NewAdPageMapping);
	}

	public void destroy() {
	}
	
}
