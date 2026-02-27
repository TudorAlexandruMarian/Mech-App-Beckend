package com.example.Mech_App.specifications;

import com.example.Mech_App.bo.Car;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CarSpecification {

    public static Specification<Car> withFilters(
            String mark,
            String model,
            String licenseNo,
            Long clientId
    ) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (mark != null && !mark.isBlank()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("mark")),
                                "%" + mark.toLowerCase() + "%"
                        )
                );
            }

            if (model != null && !model.isBlank()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("model")),
                                "%" + model.toLowerCase() + "%"
                        )
                );
            }

            if (licenseNo != null && !licenseNo.isBlank()) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("licenseNo")),
                                "%" + licenseNo.toLowerCase() + "%"
                        )
                );
            }

            if (clientId != null) {
                predicates.add(
                        criteriaBuilder.equal(root.get("clientId"), clientId)
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
