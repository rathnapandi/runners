package com.axway.runners;

import com.axway.runners.model.Event;
import com.axway.runners.model.Feed;
import com.axway.runners.service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.Date;

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

    @Test
    public void testLoadFeed(){
        Event event = eventService.findById("bPlJi3EBSSyDXHVORBwf");
        event.setVersion(System.currentTimeMillis());

        Feed feed = new Feed();
        feed.setSenderName("Sridhar Chandrasekaran");
        feed.setTimeStamp("1587503352");
        feed.setMessage("Sridhar Chandrasekaran completed the activity at: Tue Apr 21 14:09:12 MST 2020");
        feed.setAthleteId("3333072386");
        feed.setDistance(0.6437f);
        feed.setDuration(5.6f);
        feed.setDescription("Test FiTogether App1");
        feed.setCountry("US");
        feed.setEventTime(Calendar.getInstance().getTime());
        feed.setEventDateTime(new Date());
        feed.setType("Run");

        event.addFeed(feed);

        feed.setSenderName("Rathna Natarajan");
        feed.setTimeStamp("1587503352");
        feed.setMessage("Rathna Natarajan completed the activity at: Tue Apr 21 14:09:12 MST 2020");
        feed.setAthleteId("3333072384");
        feed.setDistance(1.6437f);
        feed.setDuration(10.6f);
        feed.setDescription("Test FiTogether App1");
        feed.setCountry("US");
        feed.setEventTime(Calendar.getInstance().getTime());
        feed.setEventDateTime(new Date());
        feed.setType("Run");
        event.addFeed(feed);



            eventService.saveEvent(event);

    }
}
