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

package org.radarbase.data;

import java.io.IOException;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;

/** An AvroEncoder to encode known SpecificRecord classes. */
public class AvroDatumEncoder implements AvroEncoder {
    private final EncoderFactory encoderFactory;
    private final boolean binary;
    private final GenericData genericData;

    /**
     * Create a SpecificRecordEncoder.
     * @param binary whether to use binary encoding or JSON.
     */
    public AvroDatumEncoder(GenericData genericData, boolean binary) {
        this.genericData = genericData;
        this.encoderFactory = EncoderFactory.get();
        this.binary = binary;
    }

    @Override
    public <T> AvroWriter<T> writer(Schema schema, Class<? extends T> clazz) throws IOException {
        @SuppressWarnings("unchecked")
        DatumWriter<T> writer = (DatumWriter<T>)genericData.createDatumWriter(schema);
        return new AvroRecordWriter<>(encoderFactory, schema, writer, binary);
    }
}
