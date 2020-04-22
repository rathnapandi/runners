package com.axway.runners;

import com.axway.runners.service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.query.IndexQuery;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class RunnersApplicationTests {

    @Autowired
    private EventService eventService;

//	@Test
//	void contextLoads() {
//	}

//    @Test
//    public void storeFeed(){
//        Feed feed = new Feed();
//
//        feed.setSenderName("Rathna");
//        feed.setActivityId("123");
//        feed.setAthleteId("123111");
//        feed.setCountry("US");
//        feed.setDescription("Testrun");
//        Event event = eventService.findById("bPlJi3EBSSyDXHVORBwf");
//
//        event.addFeed(feed);
//        event.setVersion(System.currentTimeMillis());
//
//        eventService.saveEvent(event);
//    }

	@Test
    public void testExistingFeed(){
       Event event = eventService.findByFeedsUsingCustomQuery("1234");
       System.out.println(event);
    }
}
