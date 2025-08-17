package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        PromptCleanConsumer consumer = new PromptCleanConsumer("prompt.clean");
        InferenceRequestProducer producer = new InferenceRequestProducer(
            "inference.request"
        );

        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    logger.info("Shutting down..");
                    consumer.close();
                    producer.close();
                })
            );

        logger.info("Starting Dispatch..");

        try {
            while (true) {
                String processedInferenceRequest = consumer.consumeAndProcess();
                if (processedInferenceRequest != null) {
                    producer.produce("new_inf_res", processedInferenceRequest);
                }

                Thread.sleep(100);
            }
        } catch (InterruptedException err) {
            logger.error("Interrupted: " + err.getMessage());
        }
    }
}
