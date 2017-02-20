package model;

import java.util.List;

/**
 * Created by inafalcao on 2/19/17.
 */
public class Event {

    private String name; // required
    private String censorship; // required
    private String bengin; // required
    private String end; // required
    private String beginSales; // required
    private String endSales; // required
    private long eventTypeId; // required / hard code
    private long producerId; // required / hard code
    private String release; // required
    private long localId; // required
    private String photoBannerPrefix; // required
    private String photoBannerSuffix; // required
    private long cityId; // required
    private String cityName; // required
    private String cityUF; // required
    private boolean onlyExhibition; // required
    private List<String> categories; // required
    private int statusId; // required
    // private int blueprintId;

    public Event() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCensorship() {
        return censorship;
    }

    public void setCensorship(String censorship) {
        this.censorship = censorship;
    }

    public String getBengin() {
        return bengin;
    }

    public void setBengin(String bengin) {
        this.bengin = bengin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getBeginSales() {
        return beginSales;
    }

    public void setBeginSales(String beginSales) {
        this.beginSales = beginSales;
    }

    public String getEndSales() {
        return endSales;
    }

    public void setEndSales(String endSales) {
        this.endSales = endSales;
    }

    public long getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypeId(long eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    public long getProducerId() {
        return producerId;
    }

    public void setProducerId(long producerId) {
        this.producerId = producerId;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public long getLocalId() {
        return localId;
    }

    public void setLocalId(long localId) {
        this.localId = localId;
    }

    public String getPhotoBannerPrefix() {
        return photoBannerPrefix;
    }

    public void setPhotoBannerPrefix(String photoBannerPrefix) {
        this.photoBannerPrefix = photoBannerPrefix;
    }

    public String getPhotoBannerSuffix() {
        return photoBannerSuffix;
    }

    public void setPhotoBannerSuffix(String photoBannerSuffix) {
        this.photoBannerSuffix = photoBannerSuffix;
    }

    public long getCityId() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityUF() {
        return cityUF;
    }

    public void setCityUF(String cityUF) {
        this.cityUF = cityUF;
    }

    public boolean isOnlyExhibition() {
        return onlyExhibition;
    }

    public void setOnlyExhibition(boolean onlyExhibition) {
        this.onlyExhibition = onlyExhibition;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }
}
