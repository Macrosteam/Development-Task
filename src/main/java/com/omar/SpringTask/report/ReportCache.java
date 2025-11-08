package com.omar.SpringTask.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ReportCache {


    //since the task does not ask us to store a date column, we need
    //to store the ID of the first purchase/refund received both today
    //and yesterday so we can conclude what are the purchases/refunds
    //we received yesterday, so we can create the report. They will simply
    //be all the rows between these two values

    //this class is responsible for storing the ID and Date of the
    //first purchase/refund received in each of the last two distinct dates.

    public List<IdDate> purchases = new ArrayList<>();
    public List<IdDate> refunds = new ArrayList<>();

    private static final String PATH = "ReportCache.json";


    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public void read() {
        File file = new File(PATH);

        if (file.exists()) {
            try {
                ReportState state = mapper.readValue(file, ReportState.class);

                purchases = state.purchases != null ? state.purchases : new ArrayList<>();
                refunds = state.refunds != null ? state.refunds : new ArrayList<>();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void write() {
        ReportState state = new ReportState();
        state.purchases = this.purchases;
        state.refunds = this.refunds;

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(PATH), state);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Long> getPurchaseRange() {
        read(); // refresh from file before reading
        return getRange(purchases);
    }

    public List<Long> getRefundRange() {
        read();
        return getRange(refunds);
    }

    public void newPurchase(Long ID) {
        read();
        updateList(purchases, ID);
        write();
    }

    public void newRefund(Long ID) {
        read();
        updateList(refunds, ID);
        write();
    }


    private List<Long> getRange(List<IdDate> list) {
        List<Long> ret = new ArrayList<>();
        LocalDate yesterday = LocalDate.now().minusDays(1);

        if (!list.isEmpty() && yesterday.equals(list.get(0).date)) {
            ret.add(list.get(0).id);
            ret.add(Long.MAX_VALUE);
        } else if (list.size() > 1 && yesterday.equals(list.get(1).date)) {
            ret.add(list.get(1).id);
            ret.add(list.get(0).id);
        } else {
            ret.add(1L);
            ret.add(1L);
        }
        return ret;
    }

    private void updateList(List<IdDate> list, Long ID) {
        LocalDate today = LocalDate.now();

        if (list.isEmpty()) {
            list.add(new IdDate(ID, today));
        } else if (list.get(0).date.isBefore(today)) {

            if (list.size() < 2) list.add(new IdDate());
            list.get(1).id = list.get(0).id;
            list.get(1).date = list.get(0).date;
            list.get(0).id = ID;
            list.get(0).date = today;
        }
    }

    public static class IdDate {

        public Long id;
        public LocalDate date;

        public IdDate() {}

        public IdDate(Long id, LocalDate date) {
            this.id = id;
            this.date = date;
        }
    }

    private static class ReportState {

        public List<IdDate> purchases;
        public List<IdDate> refunds;
    }
}
