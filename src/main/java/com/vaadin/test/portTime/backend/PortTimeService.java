package com.vaadin.test.portTime.backend;

import org.apache.commons.beanutils.BeanUtils;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Separate Java service class. Backend implementation for the address book
 * application, with "detached entities" simulating real world DAO. Typically
 * these something that the Java EE or Spring backend services provide.
 */
// Backend service class. This is just a typical Java backend implementation
// class and nothing Vaadin specific.
public class PortTimeService {

    // Create dummy data by randomly combining first and last names
    static String[] fnames = {"DEHAM", "NLRTM", "GBFXT", "BEZEE", "FRLEH",
        "ESVLC", "PTLEI", "PLGDY", "FIHEL", "SEGOT", "DKCOP", "RULED", "LVRIX"};

    private static PortTimeService instance;

    public static PortTimeService createDemoService() {
        if (instance == null) {

            final PortTimeService portTimeService = new PortTimeService();

            Random r = new Random(0);
            Calendar cal = Calendar.getInstance();
            for (int i = 0; i < fnames.length; i++) {
                PortTime portTime = new PortTime();
                portTime.setPort(fnames[i]);
                cal.set(1930 + r.nextInt(70),
                        r.nextInt(11), r.nextInt(28));
                portTime.setEta(cal.getTime());
                cal.set(1930 + r.nextInt(70),
                        r.nextInt(11), r.nextInt(28));
                portTime.setEtd(cal.getTime());
                cal.set(1930 + r.nextInt(70),
                        r.nextInt(11), r.nextInt(28));
                portTime.setAta(cal.getTime());
                cal.set(1930 + r.nextInt(70),
                        r.nextInt(11), r.nextInt(28));
                portTime.setAtd(cal.getTime());
                cal.set(1930 + r.nextInt(70),
                        r.nextInt(11), r.nextInt(28));
                portTime.setCut_off(cal.getTime());
                portTimeService.save(portTime);
            }
            instance = portTimeService;
        }

        return instance;
    }

    private HashMap<Long, PortTime> portTimes = new HashMap<>();
    private long nextId = 0;

    public synchronized List<PortTime> findAll(String stringFilter) {
        ArrayList arrayList = new ArrayList();
        for (PortTime portTime : portTimes.values()) {
            try {
                boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
                        || portTime.toString().toLowerCase()
                        .contains(stringFilter.toLowerCase());
                if (passesFilter) {
                    arrayList.add(portTime.clone());
                }
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(PortTimeService.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
//        Collections.sort(arrayList, new Comparator<PortTime>() {
//
//            @Override
//            public int compare(PortTime o1, PortTime o2) {
//                return (int) (o2.getId() - o1.getId());
//            }
//        });
        Collections.sort(arrayList, (PortTime o1, PortTime o2) -> (int) (o2.getId() - o1.getId()));
        return arrayList;
    }

    public synchronized long count() {
        return portTimes.size();
    }

    public synchronized void delete(PortTime value) {
        portTimes.remove(value.getId());
    }

    public synchronized void save(PortTime entry) {
        if (entry.getId() == null) {
            entry.setId(nextId++);
        }
        try {
            entry = (PortTime) BeanUtils.cloneBean(entry);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        portTimes.put(entry.getId(), entry);
    }

}
