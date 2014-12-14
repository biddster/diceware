package uk.co.biddell.dicevault.model;

import java.util.Date;

/**
 * Created by lukebiddell on 14/12/2014.
 */
public class Database {

    public final String getShortTitle() {
        return new Date().toString();
    }

    public final String getLongTitle() {
        return new Date().toString();
    }
}
