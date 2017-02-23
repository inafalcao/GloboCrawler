package model;

/**
 * Created by inafalcao on 2/19/17.
 */
public class Local extends ObjectJson {

    public String name;
    public String address;
    public String state;
    public String city;
    public String cityName;
    public String cityUf;
    public long postalCode;

    public int totalCapacity; // required
    public float lat; // required
    public float lng; // required

    /*
    case class Local(name: Option[String] = None,
                 site: Option[String] = None,
                 totalCapacity: Option[Int] = None,
                 address: Option[String] = None,
                 state: Option[String] = None,
                 city: Option[String] = None,
                 postalCode: Option[Long] = None,
                 lat: Option[Float] = None,
                 lng: Option[Float] = None,
                 imagePrefix: Option[String] = None,
                 imageSuffix: Option[String] = None,
                 id: Option[Long] = None,
                 timeZoneId: Option[String])
     */

    /*"{"lat":-23.5417974,
            "lng":-46.629501300000015,
            "address":"Rua Cantareira, 306 - Centro, São Paulo",
            "site":"http://www.saopaulo.sp.gov.br/",
            "name":"Mercado Municipal de São Paulo",
            "city":"São Paulo",
            "state":"São Paulo",
            "postalCode":1103200,
            "totalCapacity":2000,
            "cityUF":"SP",
            "cityName":"São Paulo"
}"*/

    /*public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public long getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(long postalCode) {
        this.postalCode = postalCode;
    }*/
}
