package mvc;

import javax.servlet.http.HttpServletRequest;

public interface IController extends IBase {

    void execute(IModel model, HttpServletRequest request);
}