package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by inafalcao on 2/19/17.
 */
public class Session {
    public List<Sector> sectors;
    public String beginSales;
    public String beginTime;
    public String endSales;
    public String endTime;

    public Session(String beginSales, String begintime, String endSales, String endTime) {
        this.beginSales = beginSales;
        this.beginTime = begintime;
        this.endSales = endSales;
        this.endTime = endTime;
    }

    public void addSector(Sector sector) {
        if(sectors == null) {
            sectors = new ArrayList<Sector>();
        }
        sectors.add(sector);
    }

}
