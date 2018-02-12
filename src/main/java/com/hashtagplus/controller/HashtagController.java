package com.hashtagplus.controller;

import com.hashtagplus.model.Hashtag;
import com.hashtagplus.model.HtplUser;
import com.hashtagplus.model.Message;
import com.hashtagplus.model.form.HashtagFormData;
import com.hashtagplus.model.form.MessageFormData;
import com.hashtagplus.service.HashtagService;
import com.hashtagplus.service.MessageHashtagService;
import com.hashtagplus.service.MessageService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class HashtagController {

    @Autowired
    private HashtagService hashtagService;

    @Autowired
    private MessageHashtagService messageHashtagService;


    @RequestMapping(method=GET, value={"/hashtags"})
    public ModelAndView getHashtags(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam(value="sortby", defaultValue="created_at") String sortby,
            @RequestParam(value="order", defaultValue="asc") String order,
            @RequestParam(value="page", defaultValue="1") int page,
            @RequestParam(value="limit", defaultValue="100") int limit) {

      HtplUser htplUser = (HtplUser) user;
        Sort sort = new Sort(
                order.equalsIgnoreCase("asc")?Sort.Direction.ASC:Sort.Direction.DESC, sortby);

        List<Hashtag> hashtags =  this.hashtagService.getAllHashtags(htplUser, sort, page, limit);
        ModelAndView mav = new ModelAndView("hashtags");
        mav.addObject("hashtagFormData", new HashtagFormData());
        mav.addObject("hashtags", hashtags);
        mav.addObject("user", null);
        return mav;
    }

}