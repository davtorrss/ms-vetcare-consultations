package com.duoc.ms_vetcare_consultations.messaging;

public final class RabbitNames {

    private RabbitNames() {
    }

    // Exchanges
    public static final String DIRECT_EXCHANGE = "vetcare.direct.exchange";
    public static final String TOPIC_EXCHANGE = "vetcare.topic.exchange";
    public static final String DLX_EXCHANGE = "vetcare.dlx.exchange";

    // Consultations Queue
    public static final String CONSULTATIONS_QUEUE =
            "vetcare.consultations.queue";

    public static final String CONSULTATIONS_DLQ =
            "vetcare.consultations.dlq";

    // Routing keys
    public static final String CONSULTATION_CREATED =
            "consultation.created";

    public static final String CONSULTATION_STATUS_CHANGED =
            "consultation.status.changed";

    public static final String CONSULTATIONS_DLQ_ROUTING_KEY =
            "consultation.dlq";
}