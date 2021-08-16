package demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CoordinateRepository extends JpaRepository<Coordinate, Long>  {

  @Query("SELECT u FROM Coordinate u WHERE u.latitude = ?1 and u.longitude = ?2")
  Coordinate findCoordinate(double latitude, double longitude);

}
