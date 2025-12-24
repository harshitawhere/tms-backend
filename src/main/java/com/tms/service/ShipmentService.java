package com.tms.service;

import com.tms.model.Shipment;
import com.tms.repository.ShipmentRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;
import com.tms.specification.ShipmentSpecification;


@Service
public class ShipmentService {

    private final ShipmentRepository repository;

    public ShipmentService(ShipmentRepository repository) {
        this.repository = repository;
    }

    public Page<Shipment> getShipments(
        int page,
        int size,
        String status,
        String origin,
        String sortField,
        String sortDir
) {
    Sort sort = Sort.by(
            "desc".equalsIgnoreCase(sortDir)
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC,
            sortField == null ? "createdAt" : sortField
    );

    Pageable pageable = PageRequest.of(page, size, sort);

    Specification<Shipment> spec =
            Specification.where(ShipmentSpecification.hasStatus(status))
                         .and(ShipmentSpecification.hasOrigin(origin));

    return repository.findAll(spec, pageable);
}


    public Shipment getShipment(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Shipment save(Shipment shipment) {
        shipment.setCreatedAt(java.time.LocalDateTime.now());
        return repository.save(shipment);
    }

    public boolean deleteShipment(Long id) {
        if (!repository.existsById(id)) return false;
        repository.deleteById(id);
        return true;
    }
}
