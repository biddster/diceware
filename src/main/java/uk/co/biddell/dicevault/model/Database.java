package uk.co.biddell.dicevault.model;

import pl.sind.keepass.exceptions.KeePassDataBaseException;
import pl.sind.keepass.kdb.KeePassDataBase;
import pl.sind.keepass.kdb.KeePassDataBaseManager;
import pl.sind.keepass.kdb.v1.Entry;
import pl.sind.keepass.kdb.v1.Group;
import pl.sind.keepass.kdb.v1.KeePassDataBaseV1;

import java.io.File;
import java.io.IOException;
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

    public final void open() throws KeePassDataBaseException, IOException {
        final KeePassDataBase keePassDb = KeePassDataBaseManager.openDataBase(
                new File("src/external/keepass4j/testing-pass-key.kdb"),
                new File("src/external/keepass4j/testing-key.key"),
                "testing");
        final KeePassDataBaseV1 kdb1 = (KeePassDataBaseV1) keePassDb;
//        System.out.println(kdb1.getGroups());
//        System.out.println(kdb1.getEntries());
        for(final Group g : kdb1.getGroups()) {
            System.out.println(g.getGroupName().getText());
            System.out.println(g.getUnknowns());
        }
        for(final Entry e: kdb1.getEntries()) {
            System.out.println(e.getPassword().getText());
        }
    }
}
