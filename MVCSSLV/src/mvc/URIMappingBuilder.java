package mvc;

public class URIMappingBuilder {

    @SuppressWarnings("rawtypes")
    public URIMapping build(String u, Class mc, Class m, String v) {
        URIMapping mapping = new URIMapping();

        mapping.setUri(u);
        mapping.setModelCreator(mc);
        mapping.setController(m);
        mapping.setView(v);

        return mapping;
    }
}
