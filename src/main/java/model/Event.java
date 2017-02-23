package model;

import java.util.List;

/**
 * Created by inafalcao on 2/19/17.
 */
public class Event extends ObjectJson {

    public String name; // required
    public String censorship; // required
    public String begin; // required
    public String end; // required
    public String beginSales; // required
    public String endSales; // required
    public long eventTypeId; // required / hard code
    public long producerId = 59; // required / hard code
    public String release; // required
    public long localId; // required
    public PhotoBanner photoBanner = new PhotoBanner(); //todo
    //public String photoBannerPrefix; // required
    //public String photoBannerSuffix; // required
    public long cityId; // required
    public String cityName; // required
    public String cityUF; // required
    public boolean onlyExhibition; // required
    public List<String> categories; // required
    public int statusId; // required
    public EventType eventType = new EventType();


    // Estes são como campos transientes
    public Local local;
    public String banner;

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

    public String getBegin() {
        return begin;
    }

    public void setBegin(String bengin) {
        this.begin = bengin;
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

    /*public String getPhotoBannerPrefix() {
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
*/
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

    public void setLocal(Local local) {
        this.local = local;
    }

    public Local getLocal() {
        return this.local;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getBanner() {
        return this.banner;
    }
}
