package com.system.facede.repository;

import com.system.facede.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByCustomUserId(Long customUserId);

    boolean existsByTypeAndValueAndCustomUserId(String type, String value, Long customUserId);

}

