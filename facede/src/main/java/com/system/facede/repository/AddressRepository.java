package com.system.facede.repository;

import com.system.facede.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByCustomUserId(Long customUserId);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
            "FROM Address a WHERE a.type = :type " +
            "AND LOWER(TRIM(a.value)) = LOWER(TRIM(:value))")
    boolean existsByTypeAndNormalizedValue(@Param("type") String type,
                                           @Param("value") String value);

    @Query("SELECT a FROM Address a WHERE a.type = :type AND LOWER(TRIM(a.value)) = LOWER(TRIM(:value))")
    List<Address> findAllByTypeAndNormalizedValue(@Param("type") String type, @Param("value") String value);

}
