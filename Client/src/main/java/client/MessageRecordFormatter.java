package client;

import octoteam.tahiti.performance.formatter.IReportFormatable;

import java.util.ArrayList;

public class MessageRecordFormatter implements IReportFormatable<ArrayList<String>> {

    @Override
    public String formatReport(ArrayList<String> strings) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (String s : strings) {
            if (first) {
                first = false;
            } else {
                builder.append("\n");
            }
            builder.append(s);
        }
        return builder.toString();
    }
}
