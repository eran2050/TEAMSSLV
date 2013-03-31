package mvc;

public class URIMapping {

	private String			uri;
	private IModelCreator	modelCreator;
	private IController		controller;
	private String			view;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public IModelCreator getModelCreator() {
		return modelCreator;
	}

	public void setModelCreator(IModelCreator modelCreator) {
		this.modelCreator = modelCreator;
	}

	public IController getController() {
		return controller;
	}

	public void setController(IController controller) {
		this.controller = controller;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

}
