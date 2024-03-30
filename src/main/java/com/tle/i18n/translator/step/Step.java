package com.tle.i18n.translator.step;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.LinkedHashMap;

public abstract class Step
{
    protected final ObjectMapper objectMapper = new ObjectMapper();
    protected final TypeReference<LinkedHashMap<String, String>> typeRef = new TypeReference<>() {};

    public Step()
    {
        objectMapper.configure( JsonReadFeature.ALLOW_TRAILING_COMMA.mappedFeature(), true );
        objectMapper.configure( JsonReadFeature.ALLOW_JAVA_COMMENTS.mappedFeature(), true );
        objectMapper.configure( JsonReadFeature.ALLOW_YAML_COMMENTS.mappedFeature(), true );
    }

    abstract void execute() throws IOException;
}
