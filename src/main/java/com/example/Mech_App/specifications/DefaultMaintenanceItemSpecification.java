package com.example.Mech_App.specifications;


import com.example.Mech_App.bo.DefaultMaintenanceItem;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class DefaultMaintenanceItemSpecification {

    public static Specification<DefaultMaintenanceItem> withFilters(
            String name,
            Long minLifeKm,
            Long maxLifeKm
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("name")),
                                "%" + name.toLowerCase() + "%"
                        )
                );
            }

            if (minLifeKm != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(root.get("minLifeKm"), minLifeKm)
                );
            }

            if (maxLifeKm != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(root.get("maxLifeKm"), maxLifeKm)
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
