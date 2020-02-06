package de.ff.jf.bftag.alarmmonitor.pdf;

import de.ff.jf.bftag.alarmmonitor.models.Alarm;
import de.ff.jf.bftag.alarmmonitor.workflow.WorkflowStep;

import java.io.IOException;
import java.nio.file.Path;

public abstract class PdfExporter implements Printable, WorkflowStep {

    public static final String DATE_FORMAT_NOW = "yyyy-MM-dd_HH:mm:ss";

    public PdfExporter() {

    }

    public abstract Path generatePdf(Alarm alarm) throws IOException;

}
