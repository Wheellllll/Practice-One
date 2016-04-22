package server;

import octoteam.tahiti.performance.recorder.MeasurementRecorder;

import java.util.ArrayList;

public class MessageRecorder extends MeasurementRecorder<ArrayList<String>> {

    private ArrayList<String> msgs;

    public MessageRecorder() {
        super("msg", new MessageRecordFormatter());
        reset();
    }

    @Override
    public void reset() {
        msgs = new ArrayList<>();
    }

    @Override
    protected ArrayList<String> getReportData() {
        return msgs;
    }

    public void record(String msg) {
        msgs.add(msg);
    }


}
