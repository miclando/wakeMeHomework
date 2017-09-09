package my.homework;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ValidationHandler implements Handler <RoutingContext> {
    private static final Logger logger = LoggerFactory.getLogger(ValidationHandler.class);
    private Map<String,CampaignModel> campaigns;
    private ConcurrentMap<String, AtomicInteger> userCap;
    private ConcurrentMap<String, AtomicInteger> campaignCounters;

    public ValidationHandler(Map<String, CampaignModel> campaigns, ConcurrentMap<String, AtomicInteger> userCap, ConcurrentMap<String, AtomicInteger> campaignCounters) {
        this.campaigns = campaigns;
        this.userCap = userCap;
        this.campaignCounters = campaignCounters;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        String userId = routingContext.request().getParam("user_id");
        List<JsonObject> res = new ArrayList<>();
        campaigns.values().forEach( model -> {
            String campaignId = model.getId();
            AtomicInteger campaignCount = campaignCounters.get(campaignId);
            if(campaignCount.get() == 0) {
                logger.debug("campaign {} reaced limit", campaignId);
            }
            else{
                String campIdUserId = model.getId() + "_" + userId;
                userCap.putIfAbsent(campIdUserId,new AtomicInteger(model.getMaxCountPerUser()));
                AtomicInteger cap=userCap.get(campIdUserId);
                if(cap.get() == 0){
                    logger.debug("user {} reached limit for campaign {} ", userId, model.getId());
                }
                else{
                    campaignCount.decrementAndGet();
                    cap.decrementAndGet();
                    logger.debug("Sending compign data 200");
                    JsonObject response = new JsonObject();
                    response.put("id",model.getId());
                    response.put("data",model.getData());
                    res.add(response);
                }
            }
        });
        if (res.size() > 0) {
            routingContext
                    .response()
                    .putHeader("content-type", "application/json")
                    .setStatusCode(200)
                    .end(res.toString());
        } else {
            routingContext.response().setStatusCode(204);
            routingContext.response().end();
        }
    }
}
