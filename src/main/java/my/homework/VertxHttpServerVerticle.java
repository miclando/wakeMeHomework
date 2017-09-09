package my.homework;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;



public class VertxHttpServerVerticle extends AbstractVerticle{
    private static final Logger logger = LoggerFactory.getLogger(VertxHttpServerVerticle.class);

    private Map<String,CampaignModel> campaigns;
    private ConcurrentMap<String, AtomicInteger> userCap;
    private ConcurrentMap<String, AtomicInteger> campaignCounters;


    @Override
    public void start(Future<Void> fut) throws Exception {
        campaigns=initCampaigns();
        campaignCounters=initCounters(campaigns);
        userCap=new ConcurrentHashMap<>();
        Router router = Router.router(vertx);
        router.get("/campaigns").handler(new ValidationHandler(campaigns, userCap,campaignCounters));

         vertx
            .createHttpServer(new HttpServerOptions().setLogActivity(false))
            .requestHandler(router::accept)
            .listen(8080, result -> {
                if (result.succeeded()) {
                    fut.complete();
                } else {
                    fut.fail(result.cause());
                }
        });
    }

    private ConcurrentMap<String, AtomicInteger> initCounters(Map<String, CampaignModel> campaigns) {
        ConcurrentMap<String, AtomicInteger> countersMap=new ConcurrentHashMap<>();
        campaigns.values().forEach(model ->{
            countersMap.put(model.getId(),new AtomicInteger(model.getMaxCount()));
        });
        return countersMap;
    }


    private Map<String,CampaignModel> initCampaigns(){
        Map<String,CampaignModel> campaigns=new ConcurrentHashMap<>();
        Map<CampaignModel.CapCount,Integer> cap= new HashMap<>();
        cap.put(CampaignModel.CapCount.MAX_COUNT_PER_USER,3);
        cap.put(CampaignModel.CapCount.MAX_COUNT,500);
        campaigns.put("123", new CampaignModel("123","my campaign","{data!!!!!}",cap));
        cap= new HashMap<>();
        cap.put(CampaignModel.CapCount.MAX_COUNT_PER_USER,2);
        cap.put(CampaignModel.CapCount.MAX_COUNT,100);
        campaigns.put("12", new CampaignModel("12","my campaign","{data!!!!!}",cap));
        cap= new HashMap<>();
        cap.put(CampaignModel.CapCount.MAX_COUNT_PER_USER,4);
        cap.put(CampaignModel.CapCount.MAX_COUNT,300);
        campaigns.put("2", new CampaignModel("1","my campaign","{data!!!!!}",cap));
        return campaigns;
    }
}
