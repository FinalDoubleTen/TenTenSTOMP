package org.tenten.tentenstomp.domain.trip.dto.request;

import org.tenten.tentenstomp.global.common.enums.TripStatus;

public record TripUpdateMsg(
    String startDate,
    String endDate,
    Long numberOfPeople,
    String tripName,
    TripStatus tripStatus,
    String area,
    String subarea,
    Long budget
) {
}
