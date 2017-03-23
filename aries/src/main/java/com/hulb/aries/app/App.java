package com.hulb.aries.app;

import com.alibaba.fastjson.JSON;
import com.hankcs.hanlp.corpus.io.IOUtil;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.*;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by hulb on 17/3/23.
 *
 * http://0.0.0.0:16669/api/search/disease?q=00
 */
@Path("api/search")
public class App {

    private static Logger logger = LoggerFactory.getLogger(App.class);

    @GET
    @Path("disease")
    @Produces("application/json")
    public String get(@QueryParam("q") String word,
                      @QueryParam("size") @DefaultValue("10") int size) {

        logger.debug("输入信息为{},输入参数为{}",word,size);
        Map<String,String> resultMap = new LinkedHashMap<>();
        resultMap.put("a","disease");
        List<Map> resultMapList = new ArrayList<>();
        resultMapList.add(resultMap);
        return JSON.toJSONString(resultMapList);
    }

    public static void main(String[] args) {
        URI baseUri = UriBuilder.fromUri("http://" + "0.0.0.0" + "/").port(16669).build();
        try {
            ResourceConfig config = new ResourceConfig();
            config.register(App.class);
            /**
             * 新的controller,需要在这里注册。
             */
            config.register(ElasticRouter.class);
            config.register(MultiPartFeature.class);
            HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseUri, config);
            server.start();
            logger.info("启动Rest服务成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}