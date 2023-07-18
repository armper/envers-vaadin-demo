package gov.noaa.ims.common.service;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {

    Address findByStreet(String street);

}
