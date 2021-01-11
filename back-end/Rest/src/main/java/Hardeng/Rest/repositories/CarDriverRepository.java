package Hardeng.Rest.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Hardeng.Rest.models.CarDriver;
import Hardeng.Rest.models.Driver;
import Hardeng.Rest.models.Car;

@Repository
public interface CarDriverRepository extends JpaRepository<CarDriver, Integer> {
    /**
     * Fetches CarDriver by car and driver selected
     * @param car instance
     * @param driver instance
     * @return CarDriver
     */
    Optional<CarDriver> findByDriverAndCar(Driver driver, Car car);
}