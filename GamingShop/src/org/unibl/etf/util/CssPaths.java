package org.unibl.etf.util;

import java.io.File;

public class CssPaths {
    private static final String c = File.separator;

    private static final String RESOURCES_FOLDER = ".." + c + "resources" + c;      // in terms of where Main is
    private static final String CSS_FOLDER = RESOURCES_FOLDER + "css" + c;

    public static final String CUSTOMER_MAIN_CSS = CSS_FOLDER + "CustomerMainCss.css";
    public static final String CUSTOMER_ACCOUNT_CSS = CSS_FOLDER + "CustomerAccountCss.css";
}
