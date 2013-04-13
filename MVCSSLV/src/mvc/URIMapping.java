package mvc;

public class URIMapping {

	private String uri;
	@SuppressWarnings("rawtypes")
	private Class modelCreator;
	@SuppressWarnings("rawtypes")
	private Class controller;
	private String view;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	@SuppressWarnings("rawtypes")
	public Class getModelCreator() {
		return modelCreator;
	}

	@SuppressWarnings("rawtypes")
	public void setModelCreator(Class modelCreator) {
		this.modelCreator = modelCreator;
	}

	@SuppressWarnings("rawtypes")
	public Class getController() {
		return controller;
	}

	@SuppressWarnings("rawtypes")
	public void setController(Class controller) {
		this.controller = controller;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

}
