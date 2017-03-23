package com.hulb.aries.app;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hulb on 17/3/23.
 *
 * 如果创建一个新的controller 需要被前端访问,那么需要注册。
 * config.register(ElasticRouter.class);
 */
@Path("api/es")
public class ElasticRouter {

    private static Logger logger = LoggerFactory.getLogger(ElasticRouter.class);

    @GET
    @Path("search")
    @Produces("application/json")
    public String es(@QueryParam("q") String word,
                      @QueryParam("size") @DefaultValue("10") int size) {

        logger.debug("输入信息为{},输入参数为{}",word,size);

        Map<String,String> resultMap = new LinkedHashMap<>();
        resultMap.put("a","b");
        List<Map> resultMapList = new ArrayList<>();
        resultMapList.add(resultMap);
        return JSON.toJSONString(resultMapList);
    }
}
