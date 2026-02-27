package com.example.Mech_App.specifications;

import com.example.Mech_App.bo.ServiceEntry;
import com.example.Mech_App.enums.ServiceStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceEntrySpecification {

    public static Specification<ServiceEntry> withFilters(
            Long carId,
            Long customerId,
            ServiceStatus status,
            LocalDateTime fromDate,
            LocalDateTime toDate
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (carId != null) {
                predicates.add(cb.equal(root.get("carId"), carId));
            }

            if (customerId != null) {
                predicates.add(cb.equal(root.get("customerId"), customerId));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if (fromDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("entryDate"), fromDate));
            }

            if (toDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("entryDate"), toDate));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
