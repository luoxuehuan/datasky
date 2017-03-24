package com.hulb.aries.app;

import com.alibaba.fastjson.JSON;
import com.dtwave.common.scripts.ScriptEnginePool;
import com.dtwave.common.scripts.ScriptFactory;
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
public class JavaScriptRouter {

    private static Logger logger = LoggerFactory.getLogger(JavaScriptRouter.class);

    /**
     * 现在默认使用了JavaScript解析引擎 池size 为30
     */
    private static ScriptEnginePool scriptEnginePool = ScriptFactory.getInstance().getOrCreateJavaScriptEngine();

    @GET
    @Path("search")
    @Produces("application/json")
    public void es(@QueryParam("q") String script,
                      @QueryParam("size") @DefaultValue("10") int size) {

        logger.debug("输入信息为{},输入参数为{}",script,size);
        try {
            /**
             * 解析出来是个啥呢。
             */
            scriptEnginePool.getEngine().eval(script);
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
