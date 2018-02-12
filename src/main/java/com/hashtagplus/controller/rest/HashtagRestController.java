package com.hashtagplus.controller.rest;

import com.hashtagplus.model.Hashtag;
import com.hashtagplus.model.HtplUser;
import com.hashtagplus.model.repo.AggDao;
import com.hashtagplus.service.HashtagService;
import com.hashtagplus.service.MessageHashtagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class HashtagRestController {

  @Autowired
  private HashtagService hashtagService;

    @Autowired
    private MessageHashtagService messageHashtagService;

    @Autowired
    private HttpServletRequest context;


    @RequestMapping(method=GET, value={"/api/hashtags"})
    public List<Hashtag> getHashtags(
            @RequestParam(value="sortby", defaultValue="created_at") String sortby,
            @RequestParam(value="order", defaultValue="asc") String order,
            @RequestParam(value="page", defaultValue="1") int page,
            @RequestParam(value="limit", defaultValue="100") int limit) {
        HtplUser user = (HtplUser) context.getAttribute("user_from_token");

      Sort sort = new Sort(
              order.equalsIgnoreCase("asc")?Sort.Direction.ASC:Sort.Direction.DESC, sortby);

        List<Hashtag> results = hashtagService.getAllHashtags(user, sort, page, limit);
        return results;
    }


    @RequestMapping(method=GET, value={"/api/hashtags/aggregate"})
    public List<AggDao> getAggregatedHashtagCount() {
        HtplUser user = (HtplUser) context.getAttribute("user_from_token");
        List<AggDao> results = messageHashtagService.aggregate(user);
        return results;
    }

}
