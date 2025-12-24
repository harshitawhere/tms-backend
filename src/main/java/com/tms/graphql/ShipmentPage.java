package com.tms.graphql;

import com.tms.model.Shipment;
import java.util.List;
import java.util.Collections;

public record ShipmentPage(List<Shipment> data, long totalCount) {
    public ShipmentPage {
        if (data == null) data = Collections.emptyList();
    }
}
