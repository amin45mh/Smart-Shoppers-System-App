package EECS3311.model;

public class Location {
    private String location;
    private double lat, lng;

    public Location(String location, double lat, double lng) {
        this.location = location;
        this.lat = lat;
        this.lng = lng;
    }

    /**
     * @param otherLoc : another Location object to calculate distance from
     * @return Hamilton distance of this location from otherLoc
     */
    public double distance(Location otherLoc){
        return Math.abs(lat - otherLoc.lat)  + Math.abs(lng - otherLoc.lng);
    }

    public String getLocation() {
        return location;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
