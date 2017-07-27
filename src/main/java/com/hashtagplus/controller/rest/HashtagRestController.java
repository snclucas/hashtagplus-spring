package com.hashtagplus.controller.rest;

import com.hashtagplus.model.HtplUser;
import com.hashtagplus.model.repo.AggDao;
import com.hashtagplus.service.MessageHashtagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class HashtagRestController {

    @Autowired
    private MessageHashtagService messageHashtagService;

    @Autowired
    private HttpServletRequest context;


    @RequestMapping(method=GET, value={"/api/hashtags/aggregate"})
    public List<AggDao> getAggregatedHashtagCount() {
        HtplUser user = (HtplUser) context.getAttribute("user_from_token");
        List<AggDao> results = messageHashtagService.aggregate(user);
        return results;
    }

}
