/*
 * Copyright 2017 The Hyve and King's College London
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.radarcns.mock;

import java.io.IOException;
import org.apache.avro.specific.SpecificRecord;
import org.radarcns.data.Record;
import org.radarcns.key.MeasurementKey;
import org.radarcns.mock.data.MockCsvParser;
import org.radarcns.producer.KafkaSender;
import org.radarcns.producer.KafkaTopicSender;

/**
 * Send mock data from a CSV file.
 *
 * <p>The value type is dynamic, so we will not check any of the generics.
 */
public class MockFileSender {
    private final KafkaSender sender;
    private final MockCsvParser parser;

    public MockFileSender(KafkaSender<MeasurementKey, SpecificRecord> sender,
            MockCsvParser parser) {
        this.parser = parser;
        this.sender = sender;
    }

    /**
     * Send data from the configured CSV file synchronously.
     * @throws IOException if data could not be read or sent.
     */
    @SuppressWarnings("unchecked")
    public void send() throws IOException {
        try (KafkaTopicSender topicSender = sender.sender(parser.getTopic())) {
            while (parser.hasNext()) {
                Record<SpecificRecord, SpecificRecord> record = parser.next();
                topicSender.send(record.offset, record.key, record.value);
            }
        }
    }
}
