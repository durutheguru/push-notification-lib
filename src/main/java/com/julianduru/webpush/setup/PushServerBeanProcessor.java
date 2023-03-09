package com.julianduru.webpush.setup;

import com.julianduru.queueintegrationlib.model.OperationStatus;
import com.julianduru.queueintegrationlib.module.subscribe.DynamicConsumerFactory;
import com.julianduru.queueintegrationlib.module.subscribe.context.ConsumerHandler;
import com.julianduru.queueintegrationlib.module.subscribe.context.ConsumerPredicate;
import com.julianduru.queueintegrationlib.module.subscribe.context.QueueConsumerContainer;
import com.julianduru.webpush.annotation.PushServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * created by Julian Duru on 24/02/2023
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PushServerBeanProcessor implements BeanPostProcessor {


    private final PushRegistry pushRegistry;


    private final QueueConsumerContainer consumerContainer;


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        try {
            if (hasPushServer(bean)) {
                log.debug("Encountered Bean with actions: {}", beanName);
                registerPushServer(bean);
            }

            return bean;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private boolean hasPushServer(Object bean) {
        var methods = bean.getClass().getMethods();
        boolean pushServerPresent = false;

        for (Method method: methods) {
            if (method.isAnnotationPresent(PushServer.class)) {
                pushServerPresent = true;
                break;
            }
        }

        return pushServerPresent;
    }


    /**
     *
     * @param bean
     */
    private void registerPushServer(Object bean) throws Exception {
        var methods = bean.getClass().getMethods();

        for (Method method: methods) {
            if (!method.isAnnotationPresent(PushServer.class)) {
                continue;
            }

            var supportedConsumer = ConsumerPredicate.findByMethod(method);
            if (supportedConsumer.isEmpty()) {
                throw new IllegalStateException(
                    """
                    Cannot proceed with Consumer registration. Unsupported Method Signature. 
                    
                    Supported signatures:
                    - OperationStatus methodName(? payload)
                    - OperationStatus methodName(String reference, ? payload)
                    - OperationStatus methodName(String reference, Map<String, String> header, ? payload) 
                    """
                );
            }

            var methodReturnType = method.getReturnType();
            if (methodReturnType != OperationStatus.class) {
                throw new IllegalStateException("Consumer method must return OperationStatus: " + method.getName());
            }

            var nodeId = String.format(
                "push-server-%d-%d", RandomUtils.nextLong(0, 1_000_000_000), System.currentTimeMillis()
            );

            log.info("Setting Push Server Node Id: {}", nodeId);
            pushRegistry.initializeNode(nodeId);

            consumerContainer.registerHandler(
                nodeId,
                ConsumerHandler.builder()
                    .bean(bean)
                    .method(method)
                    .authorities(new String[]{})
                    .build()
            );
        }
    }


}
