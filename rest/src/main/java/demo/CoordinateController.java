package demo;

import demo.helper.DistanceBetweenPoints;
import demo.helper.WithinUS;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class CoordinateController {

  private static final Logger log = LoggerFactory.getLogger(CoordinateController.class);
  private final CoordinateRepository repository;

  @Value("${key}")
  private String key;

  CoordinateController(CoordinateRepository repository) {
    this.repository = repository;
  }

  @GetMapping("/coordinates")
  CollectionModel<EntityModel<Coordinate>> getAllDataSets() {

    List<EntityModel<Coordinate>> coordinates = repository.findAll().stream()
      .map(coord -> EntityModel.of(coord,
        linkTo(methodOn(CoordinateController.class).getAllDataSets()).withRel("coordinates")))
      .collect(Collectors.toList());

    log.info("No. of coordinates: " + coordinates.size() );
    return CollectionModel.of(coordinates, linkTo(methodOn(CoordinateController.class).getAllDataSets()).withSelfRel());
  }

  @GetMapping("/coordinates/tokyo/{lat}/{lon}")
  double getDistFromTokyo(@PathVariable Double lat, @PathVariable Double lon) {
    return DistanceBetweenPoints.distance(lat, lon,Coordinate.TOKYO.getLatitude(),Coordinate.TOKYO.getLongitude());
  }

  @GetMapping("/coordinates/riyadh/{lat}/{lon}")
  double getDistFromRiyadh(@PathVariable Double lat, @PathVariable Double lon) {
    return DistanceBetweenPoints.distance(lat, lon,Coordinate.RIYADH.getLatitude(),Coordinate.RIYADH.getLongitude());
  }

  @GetMapping("/coordinates/sydney/{lat}/{lon}")
  double getDistFromSydney(@PathVariable Double lat, @PathVariable Double lon) {
    return DistanceBetweenPoints.distance(lat, lon,Coordinate.SYDNEY.getLatitude(),Coordinate.SYDNEY.getLongitude());
  }

  @GetMapping("/coordinates/zurich/{lat}/{lon}")
  double getDistFromZurich(@PathVariable Double lat, @PathVariable Double lon) {
    return DistanceBetweenPoints.distance(lat, lon,Coordinate.ZURICH.getLatitude(),Coordinate.ZURICH.getLongitude());
  }

  @GetMapping("/coordinates/mc/{lat}/{lon}")
  double getDistFromMC(@PathVariable Double lat, @PathVariable Double lon) {
    return DistanceBetweenPoints.distance(lat, lon,Coordinate.MEXICOCITY.getLatitude(),Coordinate.MEXICOCITY.getLongitude());
  }

  @GetMapping("/coordinates/reykjavik/{lat}/{lon}")
  double getDistFromReykjavik(@PathVariable Double lat, @PathVariable Double lon) {
    return DistanceBetweenPoints.distance(lat, lon,Coordinate.REYKJAVIK.getLatitude(),Coordinate.REYKJAVIK.getLongitude());
  }

  @GetMapping("/coordinates/lima/{lat}/{lon}")
  double getDistFromLima(@PathVariable Double lat, @PathVariable Double lon) {
    return DistanceBetweenPoints.distance(lat, lon,Coordinate.LIMA.getLatitude(),Coordinate.LIMA.getLongitude());
  }

  @PostMapping("/coordinates/add/{lat}/{lon}")
  public void addData(@PathVariable Double lat, @PathVariable Double lon) {
    repository.save(new Coordinate(lat,lon));
  }

  @GetMapping("/coordinates/withinUnitedStates/{lat}/{lon}")
  boolean isWithinUs(@PathVariable double lat, @PathVariable double lon) {
    try{
      return WithinUS.isWithinUS(lat, lon, key);
    }
    catch(Exception e){
      log.error("Issue with finding within the US", e);
      throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Service down", e);
    }

  }


  @GetMapping("/coordinates/get/{lat}/{lon}")
  public boolean getData(@PathVariable Double lat, @PathVariable Double lon) {
    if ((repository.findCoordinate(lat,lon))!=null)
      return true;
    return false;
  }

}
