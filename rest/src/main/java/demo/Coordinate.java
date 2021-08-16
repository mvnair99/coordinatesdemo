package demo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Coordinate {

  private @Id @GeneratedValue Long id;
  private Double longitude;
  private Double latitude;


  public static final Coordinate TOKYO = new Coordinate(139.6503,35.6762);
  public static final Coordinate SYDNEY = new Coordinate(151.2099,-33.8688);
  public static final Coordinate RIYADH = new Coordinate(46.7385,24.7743);
  public static final Coordinate ZURICH = new Coordinate(8.5417,47.3769);

  public static final Coordinate MEXICOCITY = new Coordinate(-99.1332,19.4326);
  public static final Coordinate REYKJAVIK = new Coordinate(-21.8277,64.1282);
  public static final Coordinate LIMA = new Coordinate(-77.0428,-12.0463);


  public Coordinate(){}

  public Coordinate (Double lo, Double lat){
    longitude = lo;
    latitude = lat;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return this.id;
  }


  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  @Override
  public String toString() {
    return "Coordinate{" + "latitude=" + this.latitude + ", longitude='" + this.longitude + '}';
  }


}
