package com.tle.i18n.translator;

import com.tle.i18n.translator.step.ReformatStep;
import com.tle.i18n.translator.step.RevalidateStep;
import com.tle.i18n.translator.step.SyncStep;
import com.tle.i18n.translator.step.TranslateStep;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@SpringBootApplication
public class Application
{
    private final Config config;

    private final TranslateStep translateStep;
    private final SyncStep syncStep;
    private final ReformatStep reformatStep;
    private final RevalidateStep revalidateStep;

    public Application( Config config,
                        @Lazy TranslateStep translateStep,
                        @Lazy SyncStep syncStep,
                        @Lazy ReformatStep reformatStep,
                        @Lazy RevalidateStep revalidateStep )
    {
        this.config = config;
        this.translateStep = translateStep;
        this.syncStep = syncStep;
        this.reformatStep = reformatStep;
        this.revalidateStep = revalidateStep;
    }

    public static void main( String[] args )
    {
        SpringApplication.run( Application.class, args );
    }

    @EventListener( ApplicationReadyEvent.class )
    public void execute() throws IOException
    {
        final List<RunMode> runModes = config.getRunModes();
        runModes.sort(Comparator.comparingInt(RunMode::getId));

        for ( RunMode runMode : runModes )
        {
            switch ( runMode )
            {
                case SYNC:
                    this.syncStep.execute();
                    break;
                case REVALIDATE:
                    this.revalidateStep.execute();
                    break;
                case TRANSLATE:
                    this.translateStep.execute();
                    break;
                case REFORMAT:
                    this.reformatStep.execute();
                    break;
                default:
                    break;
            }
        }

    }

}
