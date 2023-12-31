package org.tenten.tentenstomp.domain.trip.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tenten.tentenstomp.domain.trip.dto.request.*;
import org.tenten.tentenstomp.domain.trip.dto.response.TripInfoMsg;
import org.tenten.tentenstomp.domain.trip.dto.response.TripItemMsg;
import org.tenten.tentenstomp.domain.trip.dto.response.TripMemberMsg;
import org.tenten.tentenstomp.domain.trip.dto.response.TripPathMsg;
import org.tenten.tentenstomp.domain.trip.entity.Trip;
import org.tenten.tentenstomp.domain.trip.repository.TripItemRepository;
import org.tenten.tentenstomp.global.messaging.kafka.producer.KafkaProducer;
import org.tenten.tentenstomp.global.messaging.redis.publisher.RedisPublisher;
import org.tenten.tentenstomp.domain.trip.repository.TripRepository;
import org.tenten.tentenstomp.global.response.GlobalStompResponse;
import org.tenten.tentenstomp.global.util.RedisChannelUtil;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.tenten.tentenstomp.global.common.constant.TopicConstant.*;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final RedisChannelUtil redisChannelUtil;
    private final TripItemRepository tripItemRepository;
    private final RedisPublisher redisPublisher;
    private final KafkaProducer kafkaProducer;

    private final Map<String, HashSet<Long>> connectedMemberMap = new HashMap<>();
//    private final PathComponent pathComponent;

    @Transactional
    public void updateTrip(String tripId, TripUpdateMsg tripUpdateMsg) {
        Trip trip = tripRepository.getReferenceById(Long.parseLong(tripId));

        TripInfoMsg tripResponseMsg = trip.changeTripInfo(tripUpdateMsg);
        tripRepository.save(trip);

        kafkaProducer.send(TRIP_INFO, tripResponseMsg);
    }
    @Transactional
    public void addTripItem(String tripId, TripItemAddMsg tripItemAddMsg) {
        Trip trip = tripRepository.getReferenceById(Long.parseLong(tripId));
        /*
        비즈니스 로직
         */

        TripItemMsg tripItemMsg = new TripItemMsg(
            Long.parseLong(tripId), LocalDate.parse(tripItemAddMsg.visitDate()).toString(), null
        );
        TripPathMsg tripPathMsg = new TripPathMsg(
            Long.parseLong(tripId), LocalDate.parse(tripItemAddMsg.visitDate()).toString(), null
        );

        kafkaProducer.send(TRIP_ITEM, tripItemMsg);
        kafkaProducer.send(PATH, tripPathMsg);
    }
    @Transactional
    public void updateTripItemOrder(String tripId, TripItemOrderUpdateMsg orderUpdateMsg) {
        /*
        비즈니스 로직
         */

        TripItemMsg tripItemMsg = new TripItemMsg(
            Long.parseLong(tripId), LocalDate.parse(orderUpdateMsg.visitDate()).toString(), null
        );
        TripPathMsg tripPathMsg = new TripPathMsg(
            Long.parseLong(tripId), LocalDate.parse(orderUpdateMsg.visitDate()).toString(), null
        );

        kafkaProducer.send(TRIP_ITEM, tripItemMsg);
        kafkaProducer.send(PATH, tripPathMsg);
    }

    @Transactional(readOnly = true)
    public void connectMember(String tripId, MemberConnectMsg memberConnectMsg) {
        /*
        비즈니스 로직
         */
        TripMemberMsg tripMemberMsg = new TripMemberMsg(
            Long.parseLong(tripId), null
        );

        kafkaProducer.send(MEMBER, tripMemberMsg);
    }
    @Transactional(readOnly = true)
    public void disconnectMember(String tripId, MemberDisconnectMsg memberDisconnectMsg) {
        /*
        비즈니스 로직
         */
        TripMemberMsg tripMemberMsg = new TripMemberMsg(
            Long.parseLong(tripId), null
        );

        kafkaProducer.send(MEMBER, tripMemberMsg);
    }



}
