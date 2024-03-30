package com.tle.openai.model.chat;

public class ChatCompletionConfiguration
{
    private String model = "gpt-3.5-turbo";

    private int maxTokens = 4000;

    private double initTemperature = 0.2;

    private int n = 1;

    public ChatCompletionConfiguration( String model, int maxTokens, double initTemperature, int n )
    {
        this.model = model;
        this.maxTokens = maxTokens;
        this.initTemperature = initTemperature;
        this.n = n;
    }

    public static Builder builder()
    {
        return new Builder();
    }

    public String getModel()
    {
        return model;
    }

    public int getMaxTokens()
    {
        return maxTokens;
    }

    public double getInitTemperature()
    {
        return initTemperature;
    }

    public int getN()
    {
        return n;
    }

    @Override
    public String toString()
    {
        return "ChatCompletionConfiguration{" +
               "model='" + model + '\'' +
               ", maxTokens=" + maxTokens +
               ", temperature=" + initTemperature +
               ", n=" + n +
               '}';
    }

    public static class Builder
    {
        private String model = "gpt-3.5-turbo";

        private int maxTokens = 4000;

        private double temperature = 0.2;

        private int n = 1;

        public Builder withModel( String model )
        {
            this.model = model;
            return this;
        }

        public Builder withMaxTokens( int maxTokens )
        {
            this.maxTokens = maxTokens;
            return this;
        }

        public Builder withTemperature( double temperature )
        {
            this.temperature = temperature;
            return this;
        }

        public Builder withN( int n )
        {
            this.n = n;
            return this;
        }

        public ChatCompletionConfiguration build()
        {
            return new ChatCompletionConfiguration( model, maxTokens, temperature, n );
        }
    }
}
