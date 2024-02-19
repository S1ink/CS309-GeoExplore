package hb403.geoexplore.Request_Filter;

public class Address {
    private String street;
    private String city;
    private String closestAddress;


    public Address(String closestUnit, String streetLine, String cityLine){
        this.street = streetLine;
        this.city = cityLine;
        this.closestAddress = closestUnit;
    }

    public String getStreet(){
        return street;
    }
    public String getCity(){
        return city;
    }
    public String getAddress(){
        return closestAddress + ", " + street+ ", " + city;
    }
}