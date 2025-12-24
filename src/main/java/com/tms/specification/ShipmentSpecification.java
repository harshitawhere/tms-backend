package com.tms.specification;

import com.tms.model.Shipment;
import org.springframework.data.jpa.domain.Specification;

public class ShipmentSpecification {

    public static Specification<Shipment> hasStatus(String status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Shipment> hasOrigin(String origin) {
        return (root, query, cb) ->
                origin == null ? null : cb.equal(root.get("origin"), origin);
    }
}
