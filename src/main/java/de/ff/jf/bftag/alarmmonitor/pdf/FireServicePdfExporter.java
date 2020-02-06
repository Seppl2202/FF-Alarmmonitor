package de.ff.jf.bftag.alarmmonitor.pdf;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import de.ff.jf.bftag.alarmmonitor.RessourceFolderURL;
import de.ff.jf.bftag.alarmmonitor.models.Alarm;
import de.ff.jf.bftag.alarmmonitor.workflow.FireServiceDispatchWorkflow;

import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class FireServicePdfExporter extends PdfExporter {

    public FireServicePdfExporter() {

    }

    Random r = new Random();

    @Override
    public void executeStep() {
        Alarm alarm = FireServiceDispatchWorkflow.currentAlarm;
        try {
            generatePdf(alarm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Path generatePdf(Alarm alarm) throws IOException {

        String src = "/Users/sesc4/Desktop/Java/Alarmmonitor/src/main/resources/files/vorlage.pdf";
        String destinationAppendix = new SimpleDateFormat(DATE_FORMAT_NOW).format(new Date()).toString();
        String dest = RessourceFolderURL.ressourceFolderBaseURL + "/export/FAX_" + destinationAppendix;
        PdfDocument pdf = new PdfDocument(
                new PdfReader(src), new PdfWriter(dest));
        fillAlarmDetails(alarm, pdf);
        pdf.close();
        return null;

    }

    private void fillAlarmDetails(Alarm alarm, PdfDocument document) {
        Random r = new Random();
        PdfAcroForm form = PdfAcroForm.getAcroForm(document, true);
        form.getField("faxnummer").setValue(getRandomCallNumber());
        form.getField("empfaenger").setValue("072553970582");
        form.getField("send_date").setValue(getCurrentDate());
        form.getField("sender_time").setValue(getAlarmDispatchTime(alarm));
        form.getField("sender_name").setValue("ILS Hambrücken");
        form.getField("sender_phone").setValue("072553970582");
        form.getField("dispatch_number").setValue(getDispatchNumber(alarm));
        form.getField("caller_name").setValue(getCallerInformation(alarm));
        form.getField("location_street").setValue(alarm.getAddress().getStreet() + " " + alarm.getAddress().getNumber());
        form.getField("location").setValue(alarm.getAddress().getZipCode() +  " " + alarm.getAddress().getLocation());
        form.getField("location_part").setValue(alarm.getAddress().getLocation());
        form.getField("catchphrase").setValue(alarm.getKeyword().getKeyword());
        form.getField("keyword").setValue(alarm.getKeyword().getStage());
        StringBuilder builder = new StringBuilder();
        alarm.getAlarmedCars().forEach(car -> {
            builder.append(car);
            builder.append("   (S3:  Uhr)");
            builder.append("\r\n");
        });
        form.getField("cars").setValue(builder.toString());

    }

    private String getCallerInformation(Alarm alarm) {
        return alarm.getCaller().getName() + " : " + alarm.getCaller().getPhoneNumber();
    }

    private String getAlarmDispatchTime(Alarm alarm) {
        return  new SimpleDateFormat("hh:mm").format(alarm.getAlarmDispatchDetails().getAlarmDispatchedDate()).toString();
    }

    private String getRandomCallNumber() {

        int number = r.nextInt(999999);
        return String.format("%06d", number);
    }

    private String getCurrentDate() {
         String pattern = "dd-MM-yyyy";
       return new SimpleDateFormat(pattern).format(new Date());
    }
private String getDispatchNumber(Alarm alarm) {
        int sixDigit = r.nextInt(999999);
        int threeDigit = r.nextInt(999);

        return alarm.getKeyword().getStage().substring(0,1) + " " + String.format("%06d", sixDigit) + " " + String.format("%03d", threeDigit);

}

    @Override
    public boolean print() {
        return false;
    }


}
