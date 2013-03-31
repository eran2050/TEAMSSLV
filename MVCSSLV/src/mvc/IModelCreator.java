package mvc;

import javax.servlet.http.HttpServletRequest;

public interface IModelCreator extends IBase {

    IModel createModel(HttpServletRequest request);

}
