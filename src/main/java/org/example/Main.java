package org.example;

public class Main {
    public static void main(String[] args) {
        PromptCleanConsumer consumer = new PromptCleanConsumer("prompt.clean");
        InferenceRequestProducer producer = new InferenceRequestProducer("inference.request");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down..");
            consumer.close();
            producer.close();
        }));

        System.out.printf("Starting Dispatch..");

        try {
            while (true) {
                String processedInferenceRequest = consumer.consumeAndProcess();
                if (processedInferenceRequest != null) {
                    producer.produce("new_inf_res", processedInferenceRequest);
                }

                Thread.sleep(100);
            }
        } catch (InterruptedException err) {
            System.err.println("Interrupted: " + err.getMessage());
        }
    }
}

