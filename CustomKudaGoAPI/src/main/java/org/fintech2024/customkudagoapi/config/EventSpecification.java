package org.fintech2024.customkudagoapi.config;

import org.fintech2024.customkudagoapi.entity.Event;
import org.fintech2024.customkudagoapi.entity.Place;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class EventSpecification {

    public static Specification<Event> hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("title"), "%" + name + "%");
        };
    }

    public static Specification<Event> hasPlace(Place place) {
        return (root, query, criteriaBuilder) -> {
            if (place == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("place"), place);
        };
    }

    public static Specification<Event> hasFromDate(Date fromDate) {
        return (root, query, criteriaBuilder) -> {
            if (fromDate == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), fromDate.toString());
        };
    }

    public static Specification<Event> hasToDate(Date toDate) {
        return (root, query, criteriaBuilder) -> {
            if (toDate == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), toDate.toString());
        };
    }

    public static Specification<Event> filterEvents(String name, Place place, Date fromDate, Date toDate) {
        return Specification.where(hasName(name))
                .and(hasPlace(place))
                .and(hasFromDate(fromDate))
                .and(hasToDate(toDate));
    }
}