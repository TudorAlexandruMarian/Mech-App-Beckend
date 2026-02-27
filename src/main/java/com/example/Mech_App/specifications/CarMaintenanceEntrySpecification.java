package com.example.Mech_App.specifications;

import com.example.Mech_App.bo.CarMaintenanceEntry;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
public class CarMaintenanceEntrySpecification {

    public static Specification<CarMaintenanceEntry> withFilters(
            Long carId,
            Long serviceEntryId,
            Long defaultMaintenanceItemId
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (carId != null) {
                predicates.add(cb.equal(root.get("carId"), carId));
            }

            if (serviceEntryId != null) {
                predicates.add(cb.equal(root.get("serviceEntryId"), serviceEntryId));
            }

            if (defaultMaintenanceItemId != null) {
                predicates.add(cb.equal(root.get("defaultMaintenanceItemId"), defaultMaintenanceItemId));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
