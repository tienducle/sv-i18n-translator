package com.tle.i18n.translator.adapter.translation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tle.i18n.translator.common.Message;
import com.tle.i18n.translator.translation.TranslationRequest;
import com.tle.i18n.translator.translation.TranslationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
public abstract class TranslationAdapter
{
    private static final Logger LOGGER = LoggerFactory.getLogger( TranslationAdapter.class );

    @Value( "${translation.adapter.maxAttempts:4}" )
    private int maxAttempts;

    @Value( "${translation.adapter.maxHistorySize:0}" )
    private int maxHistorySize;

    @Value( "${translation.adapter.contextMessagesFilePath:}" )
    private String contextMessagesFile;

    private final List<Message> contextMessages = new ArrayList<>();

    private final ArrayDeque<Message> history = new ArrayDeque<>();

    public TranslationAdapter()
    {
        LOGGER.info( "Initialized TranslationAdapter" );
    }

    /**
     * Translates the text in the given {@link TranslationRequest} object and returns a {@link TranslationResult}
     * containing one or more translated texts.
     * <p></p>
     * Depending on {@link TranslationResult#hasError()} and {@link TranslationResult#isRetryable()},
     * the translation will be retried if no satisfiable result was found.
     */
    public abstract TranslationResult translate( TranslationRequest translation );

    public void addRequestToHistory( String userMessageString )
    {
        addMessageToHistory( new Message( "user", userMessageString ) );
    }

    public void addResponseToHistory( String assistantResponseMessageString )
    {
        addMessageToHistory( new Message( "assistant", assistantResponseMessageString ) );
    }

    private void addMessageToHistory( Message message )
    {
        final String role = message.getRole();

        if ( history.peekLast() != null && history.peekLast().getRole().equals( role ) )
        {
            throw new IllegalStateException( String.format( "Cannot add two consecutive '%s' messages to the history.", role ) );
        }

        if ( history.size() >= maxHistorySize )
        {
            history.pollFirst();
        }

        history.add( message );
    }

    protected Iterator<Message> getHistoryMessages()
    {
        return history.iterator();
    }

    /**
     * Returns the maximum number of attempts for translating a text.
     * Configured in the application.properties file under the key "translation.adapter.maxAttempts".
     */
    public int getMaxAttempts()
    {
        return maxAttempts;
    }

    protected List<Message> getContextMessages()
    {
        return contextMessages;
    }

    @EventListener( ApplicationStartedEvent.class )
    protected void initializeContextMessages()
    {
        if ( contextMessagesFile == null || contextMessagesFile.isEmpty() )
        {
            return;
        }

        LOGGER.info( "Reading context messages file: {}", contextMessagesFile );
        final ObjectMapper objectMapper = new ObjectMapper();
        final TypeReference<List<Map<String, String>>> typeRef = new TypeReference<>() {};
        final File file = new File( contextMessagesFile );
        List<Map<String, String>> contextMessagesFileContent = null;

        try
        {
            contextMessagesFileContent = objectMapper.readValue( file, typeRef );
        }
        catch ( Exception e )
        {
            LOGGER.error( "Error reading context messages file: {}", contextMessagesFile, e );
        }

        if ( contextMessagesFileContent == null )
        {
            return;
        }

        for ( Map<String, String> contextMessage : contextMessagesFileContent )
        {
            final String role = contextMessage.get( "role" );
            final String content = contextMessage.get( "content" );

            contextMessages.add( new Message( role, content ) );
            LOGGER.info( "Added '{}' context message: {}", role, content );
        }

    }
}
