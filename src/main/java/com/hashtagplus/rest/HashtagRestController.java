package com.hashtagplus.rest;

import com.hashtagplus.model.HtplUserDetails;
import com.hashtagplus.model.Message;
import com.hashtagplus.model.MessageHashtag;
import com.hashtagplus.model.form.MessageFormData;
import com.hashtagplus.model.repo.AggDao;
import com.hashtagplus.service.HashtagService;
import com.hashtagplus.service.MessageHashtagService;
import com.hashtagplus.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class HashtagRestController {

    @Autowired
    private MessageHashtagService messageHashtagService;

    @Autowired
    private HttpServletRequest context;


    @RequestMapping(method=GET, value={"/api/hashtags/aggregate"})
    public List<AggDao> getMessagesWithHashtags() {
        HtplUserDetails user = (HtplUserDetails) context.getAttribute("user_from_token");
        List<AggDao> results = messageHashtagService.aggregate(user);
        return results;
    }

}
