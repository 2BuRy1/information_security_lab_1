package com.security.lab.serializer;

import org.springframework.web.util.HtmlUtils;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

public class XssStringSerializer extends ValueSerializer<String> {

    @Override
    public void serialize(String value, JsonGenerator gen, SerializationContext ctxt) {
        if (value == null) {
            gen.writeNull();
        } else {
            gen.writeString(HtmlUtils.htmlEscape(value));
        }
    }
}
