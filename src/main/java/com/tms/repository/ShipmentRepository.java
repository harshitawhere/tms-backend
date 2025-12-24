package com.tms.repository;

import com.tms.model.Shipment;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long>, JpaSpecificationExecutor<Shipment> {
}
