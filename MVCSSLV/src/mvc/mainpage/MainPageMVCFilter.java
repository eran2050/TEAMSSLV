package mvc.mainpage;

import mvc.*;
import org.springframework.context.ApplicationContext;
import util.ApplicationContextSingleton;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebFilter("/MainPageMVCFilter")
public class MainPageMVCFilter implements Filter {

    private Map<String, URIMapping> mapping;

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

        assert uriMapping != null;
        ApplicationContext ctx = ApplicationContextSingleton.get();

        @SuppressWarnings("unchecked")
        IModelCreator modelCreator = (IModelCreator) ctx.getBean(uriMapping.getModelCreator()); //uriMapping.getModelCreator();
        IModel model = modelCreator.createModel(httpRequest);

        @SuppressWarnings("unchecked")
        IController controller = (IController) ctx.getBean(uriMapping.getController());
        controller.execute(model, httpRequest);

        httpRequest.setAttribute("model", model);

        String view = uriMapping.getView();
        RequestDispatcher dispatcher = httpRequest.getRequestDispatcher(view);
        dispatcher.forward(httpRequest, httpResponse);

    }

    public void init(FilterConfig fConfig) throws ServletException {

        mapping = new HashMap<String, URIMapping>();

        // Main Page
        URIMapping mainPageMapping = new URIMappingBuilder().build("/",
                MainPageModelCreator.class, MainPageController.class,
                "/jsp/mainpage.jsp");

        // Fill Mapping
        mapping.put(mainPageMapping.getUri(), mainPageMapping);
    }

    public void destroy() {
    }
}
