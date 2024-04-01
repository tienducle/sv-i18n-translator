package com.tle.ollama.model.chat;

import java.util.ArrayList;
import java.util.List;

public class ChatCompletionRequest
{
    private final String model;

    private final List<Message> messages = new ArrayList<>();

    private boolean stream = false;

    public ChatCompletionRequest( String model )
    {
        this.model = model;
    }

    public void addMessage( Message message )
    {
        this.messages.add( message );
    }
}
