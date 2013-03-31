package mvc;

public class URIMappingBuilder {

	public URIMapping build(String u, IModelCreator mc, IController m, String v) {
		URIMapping mapping = new URIMapping();

		mapping.setUri(u);
		mapping.setModelCreator(mc);
		mapping.setController(m);
		mapping.setView(v);

		return mapping;
	}
}
