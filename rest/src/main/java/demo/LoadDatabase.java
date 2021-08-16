package demo;

import demo.helper.DistanceBetweenPoints;
import demo.helper.WithinUS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@Configuration
class LoadDatabase {

	private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
  private static Random random = new Random();
	/*
	A coordinate is considered valid if it has a valid latitude and longitude.
	The latitude must be between -90 and 90 inclusive and
	the longitude must be between -180 and 180 inclusive.
	 */

  private int LOWER_LATITUDE_RANGE = -90;
  private int UPPER_LATITUDE_RANGE = 90;
  private int LOWER_LONGITUDE_RANGE = -180;
  private int UPPER_LONGITUDE_RANGE = 180;

  @Value("${numberOfCoordinates}")
  private int count;

  @Value("${key}")
  private String key;

  private double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();
    long factor = (long) Math.pow(10, places);
    value = value * factor;
    long tmp = Math.round(value);
    return (double) tmp / factor;
  }

  private double getRandom(int low, int high) {
    double result  = low + random.nextDouble()*(high-low);
    return round (result,4);
  }

	@Bean
  CommandLineRunner initCoordinateDatabase(CoordinateRepository repository) {
    List coordinates = new ArrayList<Coordinate>();
    Coordinate coord = null;
    log.info("count = " + count);
	  for (int x=0; x<count;x++) {
      coord = new Coordinate(getRandom(LOWER_LATITUDE_RANGE,UPPER_LATITUDE_RANGE), getRandom(LOWER_LONGITUDE_RANGE,UPPER_LONGITUDE_RANGE));
      log.info("Preloading " + repository.save(coord));
      coordinates.add(coord);
    }


    Iterator i = coordinates.iterator();
	  boolean isUS;
	  try {
      while (i.hasNext()) {
        coord = (Coordinate) i.next();
        log.info("------------------------------");
        isUS = WithinUS.isWithinUS(coord.getLatitude(), coord.getLongitude(),key);
        log.info("COORDINATE: " + coord.getLatitude() + ", " + coord.getLongitude() + "  Within US: " +  isUS);
        if (!isUS) {
          log.info("Distance in Nautical miles to: ");
          log.info("----Tokyo: " + DistanceBetweenPoints.distance(coord.getLatitude(), coord.getLongitude(),Coordinate.TOKYO.getLatitude(),Coordinate.TOKYO.getLongitude()) );
          log.info("----Sydney: " + DistanceBetweenPoints.distance(coord.getLatitude(), coord.getLongitude(),Coordinate.SYDNEY.getLatitude(),Coordinate.SYDNEY.getLongitude()) );
          log.info("----Lima: " + DistanceBetweenPoints.distance(coord.getLatitude(), coord.getLongitude(),Coordinate.LIMA.getLatitude(),Coordinate.LIMA.getLongitude()) );
          log.info("----Mexico City: " + DistanceBetweenPoints.distance(coord.getLatitude(), coord.getLongitude(),Coordinate.MEXICOCITY.getLatitude(),Coordinate.MEXICOCITY.getLongitude()) );
          log.info("----Riyadh: " + DistanceBetweenPoints.distance(coord.getLatitude(), coord.getLongitude(),Coordinate.RIYADH.getLatitude(),Coordinate.RIYADH.getLongitude()) );
          log.info("----Reykjavik: " + DistanceBetweenPoints.distance(coord.getLatitude(), coord.getLongitude(),Coordinate.REYKJAVIK.getLatitude(),Coordinate.REYKJAVIK.getLongitude()) );
          log.info("----Zurich: " + DistanceBetweenPoints.distance(coord.getLatitude(), coord.getLongitude(),Coordinate.ZURICH.getLatitude(),Coordinate.ZURICH.getLongitude()) );
        }
      }
    }
	  catch(Exception ex){
      log.error("",ex);
	  }

    return args -> {
      log.info("Done loading DB. APIs up and running....");
    };
  }


}
