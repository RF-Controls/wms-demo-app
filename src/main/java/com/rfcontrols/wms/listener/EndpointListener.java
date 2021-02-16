package com.rfcontrols.wms.listener;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rfcontrols.wms.service.ZoneAssociationService;
import com.rfcontrols.wms.service.dto.TagBlinkLite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Simple java web socket client to subscribe to an endpoint supplied by the user.
 */
@Service
public class EndpointListener implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger logger = LoggerFactory.getLogger(EndpointListener.class);

    private final ScheduledExecutorService executorService;
    private final SimpMessagingTemplate template;

    @Value("${wms.rfcos.host}")
    private String host;

    @Value("${wms.rfcos.port}")
    private Integer port;

    @Value("${wms.rfcos.user}")
    private String username;

    @Value("${wms.rfcos.pass}")
    private String password;

    private long maxHeartbeatAge = 15000;
    private int heartbeatInterval = 15000;
    private long reconnectPause = 5000;

    private String websocketHost;
    private ObjectMapper mapper = new ObjectMapper();
    private WebSocketStompClient stompClient;

    private final ZoneAssociationService zoneAssociationService;

    public EndpointListener(ScheduledExecutorService executorService, SimpMessagingTemplate template, ZoneAssociationService zoneAssociationService) {
        this.executorService = executorService;
        this.template = template;
        this.zoneAssociationService = zoneAssociationService;
    }

    @Transactional(readOnly = true)
    public void startRegionListeners(){
        logger.info("Starting the region listeners...");
        try {
            if (stompClient != null) {
                stompClient.stop();
            }

            websocketHost = "ws://" + host + ":" + port + "/websockets/messaging/websocket";

            logger.info("Connecting to websocket server at the URI {}", websocketHost);
            WebSocketClient webSocketClient = new StandardWebSocketClient();
            stompClient = new WebSocketStompClient(webSocketClient);
            stompClient.setMessageConverter(new StringMessageConverter());
            stompClient.setTaskScheduler(new ConcurrentTaskScheduler(executorService));

            String authString = "Basic " + new String(Base64.getEncoder().encode((username + ":" + password).getBytes()));
            WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
            headers.add("Authorization", authString);

            StompSessionHandler sessionHandler = new StompSessionHandler();
            stompClient.connect(websocketHost, headers, sessionHandler);
        } catch (Exception e) {
            logger.error("There was an error starting the region listeners", e);
        }
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        startRegionListeners();
    }

    private class RegionTagBlinkHandler implements StompFrameHandler {

        @Override
        public Type getPayloadType(StompHeaders headers) {
            return String.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            try {
                String messageBody = (String) payload;
                List<TagBlinkLite> lites = mapper.readValue(messageBody, new TypeReference<List<TagBlinkLite>>() {});
                zoneAssociationService.updateLocations(lites);
            } catch (Exception e) {
                logger.error("Error handling message", e);
            }
        }
    }

    public class StompSessionHandler extends StompSessionHandlerAdapter {
        @Override
        public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
            super.handleException(session, command, headers, payload, exception);
            logger.error("Error connecting to websocket server at: " + websocketHost, exception);
        }

        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            logger.info("Connected to the websocket server!");
            session.subscribe("/topic/tagBlinkLite.*", new RegionTagBlinkHandler());
        }
    }
}
