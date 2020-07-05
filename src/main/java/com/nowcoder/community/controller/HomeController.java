package com.nowcoder.community.controller;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController implements CommunityConstant {
    @Autowired
    private DiscussPostService discussPostService;
      @Autowired
      private LikeService likeService;
    @Autowired
    private UserService userService;

    @RequestMapping(path="/index",method = RequestMethod.GET)
    public  String getIndexPage(Model model, Page page){
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");
        //从数据库中查询到帖子出来，装到容器list之中。
        List<DiscussPost> list=discussPostService.findDiscussPosts(0,page.getOffset(),page.getLimit());
        //
        List<Map<String,Object>> discussPosts=new ArrayList<>();
        if(list!=null){
            //每一个帖子对应一个userId。
            for(DiscussPost post:list){
                Map<String,Object> map=new HashMap<>();
                map.put("post",post);
                User user=  userService.findUserById(post.getUserId());
                map.put("user",user);
                long likeCount=likeService.fingEntityLikeCount(ENTITY_TYPE_POST,post.getId());
                map.put("likeCount",likeCount);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts",discussPosts);
        return "/index";
    }
    @RequestMapping(path = "/error",method = RequestMethod.GET)
    public  String getErrorPage(){
        return "/error/500";
    }
}
