package com.aluracursos.literalura.model;
import java.util.List;

public enum Idioma {
    AF("af", "Afrikaans", "Afrikáans"),
    ALE("ale", "Aleut", "Aleutiano"),
    AR("ar", "Arabic", "Árabe"),
    APW("apw", "Arapaho", "Arapajó"),
    BRX("brx", "Bodo", "Bodo"),
    BRE("bre", "Breton", "Bretón"),
    BG("bg", "Bulgarian", "Búlgaro"),
    RML("rml", "Caló", "Caló"),
    CA("ca", "Catalan", "Catalán"),
    CEB("ceb", "Cebuano", "Cebuano"),
    CS("cs", "Czech", "Checo"),
    ET("et", "Estonian", "Estonio"),
    FA("fa", "Farsi", "Farsi"),
    FY("fy", "Frisian", "Frisón"),
    FUR("fur", "Friulian", "Friulano"),
    GD("gd", "Gaelic, Scottish", "Gaélico escocés"),
    GL("gl", "Galician", "Gallego"),
    KYK("kyk", "Gamilaraay", "Gamilaraay"),
    GRC("grc", "Greek, Ancient", "Griego antiguo"),
    HE("he", "Hebrew", "Hebreo"),
    IS("is", "Icelandic", "Islandés"),
    ILO("ilo", "Iloko", "Iloko"),
    ILA("ila", "Interlingua", "Interlingua"),
    IU("iu", "Inuktitut", "Inuktitut"),
    GA("ga", "Irish", "Irlandés"),
    JA("ja", "Japanese", "Japonés"),
    CSB("csb", "Kashubian", "Cacheubio"),
    KHA("kha", "Khasi", "Khasi"),
    KO("ko", "Korean", "Coreano"),
    LT("lt", "Lithuanian", "Lituano"),
    MI("mi", "Maori", "Maorí"),
    MYN("myn", "Mayan Languages", "Lenguas mayas"),
    ENM("enm", "Middle English", "Inglés medio"),
    NAH("nah", "Nahuatl", "Náhuatl"),
    NAP("nap", "Napoletano-Calabrese", "Napolitano-Calabrés"),
    NV("nv", "Navajo", "Navajo"),
    NAI("nai", "North American Indian", "Indio norteamericano"),
    NO("no", "Norwegian", "Noruego"),
    OC("oc", "Occitan", "Occitano"),
    OJ("oj", "Ojibwa", "Ojibwa"),
    ANG("ang", "Old English", "Inglés antiguo"),
    PL("pl", "Polish", "Polaco"),
    RO("ro", "Romanian", "Rumano"),
    RU("ru", "Russian", "Ruso"),
    SA("sa", "Sanskrit", "Sánscrito"),
    SR("sr", "Serbian", "Serbio"),
    SL("sl", "Slovenian", "Esloveno"),
    TBW("tbw", "Tagabawa", "Tagabawa"),
    TE("te", "Telugu", "Telugu"),
    BO("bo", "Tibetan", "Tibetano"),
    CY("cy", "Welsh", "Galés"),
    YI("yi", "Yiddish", "Yidis"),
    ZH("zh", "Chinese", "Chino"),
    DA("da", "Danish", "Danés"),
    NL("nl", "Dutch", "Holandés"),
    EN("en", "English", "Inglés"),
    EO("eo", "Esperanto", "Esperanto"),
    FI("fi", "Finnish", "Finlandés"),
    FR("fr", "French", "Francés"),
    DE("de", "German", "Alemán"),
    EL("el", "Greek", "Griego"),
    HU("hu", "Hungarian", "Húngaro"),
    IT("it", "Italian", "Italiano"),
    LA("la", "Latin", "Latín"),
    PT("pt", "Portuguese", "Portugués"),
    ES("es", "Spanish", "Español"),
    SV("sv", "Swedish", "Sueco"),
    TL("tl", "Tagalog", "Tagalo");

    private final String sigla;
    private final String idiomaEnIngles;
    private final String idiomaEnEspanol;

    Idioma(String sigla, String idiomaEnIngles, String idiomaEnEspañol) {
        this.sigla = sigla;
        this.idiomaEnIngles = idiomaEnIngles;
        this.idiomaEnEspanol = idiomaEnEspañol;
    }

    public static Idioma desdeListaIdioma(List<String> idiomas) {
        if (idiomas == null || idiomas.isEmpty()) {
            throw new IllegalArgumentException("La lista de idiomas está vacía o es nula.");
        }
        String primerIdioma = idiomas.get(0);
        for (Idioma idioma : Idioma.values()) {
            if (idioma.sigla.equalsIgnoreCase(primerIdioma)) {
                return idioma;
            }
        }
        throw new IllegalArgumentException("Ningún idioma encontrado para: " + idiomas.toString());
    }

    public static Idioma desdeUsuarioEnEspanol(String idiomaUsuario){
        for(Idioma idioma : Idioma.values()){
            if(idioma.idiomaEnEspanol.equalsIgnoreCase(idiomaUsuario)){
                return idioma;
            }
        }
        throw new IllegalArgumentException("No existen textos en " + idiomaUsuario + " en la base de datos");
    }

    public String getSigla() {
        return sigla;
    }

    public String getIdiomaEnIngles() {
        return idiomaEnIngles;
    }

    public String getIdiomaEnEspanol() {
        return idiomaEnEspanol;
    }
}