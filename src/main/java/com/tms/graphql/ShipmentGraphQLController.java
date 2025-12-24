package com.tms.graphql;

import com.tms.model.Shipment;
import com.tms.service.ShipmentService;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import java.util.Collections;

@Controller
public class ShipmentGraphQLController {

    private final ShipmentService service;

    public ShipmentGraphQLController(ShipmentService service) {
        this.service = service;
    }

    @QueryMapping
public ShipmentPage shipments(
        @Argument int page,
        @Argument int size,
        @Argument String status,
        @Argument String origin,
        @Argument String sortField,
        @Argument String sortDir
) {
    Page<Shipment> result = service.getShipments(
            page,
            size,
            status,
            origin,
            sortField,
            sortDir
    );

    return new ShipmentPage(
            result.getContent() != null ? result.getContent() : Collections.emptyList(),
            result.getTotalElements()
    );
}


    @QueryMapping
    public Shipment shipment(@Argument Long id) {
        return service.getShipment(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @MutationMapping
    public Shipment addShipment(
            @Argument String shipmentNumber,
            @Argument String origin,
            @Argument String destination,
            @Argument String status,
            @Argument String carrier,
            @Argument Double weight
    ) {
        Shipment s = new Shipment();
        s.setShipmentNumber(shipmentNumber);
        s.setOrigin(origin);
        s.setDestination(destination);
        s.setStatus(status);
        s.setCarrier(carrier);
        s.setWeight(weight);
        return service.save(s);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @MutationMapping
    public Shipment updateShipment(
                @Argument Long id,
                @Argument String status,
                @Argument String carrier
    ) {
        Shipment s = service.getShipment(id);
        if (s == null) return null;

        if (status != null) s.setStatus(status);
        if (carrier != null) s.setCarrier(carrier);

        return service.save(s);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @MutationMapping
    public Boolean deleteShipment(@Argument Long id) {
        return service.deleteShipment(id);
    }

}
