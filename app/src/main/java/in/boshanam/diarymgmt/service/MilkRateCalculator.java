package in.boshanam.diarymgmt.service;

import android.app.Activity;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.boshanam.diarymgmt.command.ListenerAdapter;
import in.boshanam.diarymgmt.domain.CollectedMilk;
import in.boshanam.diarymgmt.domain.MilkType;
import in.boshanam.diarymgmt.domain.Rate;
import in.boshanam.diarymgmt.repository.FireBaseDao;

/**
 * Created by Siva on 2/16/2018.
 */

public class MilkRateCalculator {
    private List<Rate> cowMilkRates;
    private List<Rate> buffaloMilkRates;

    private MilkRateCalculator() {
        this.buffaloMilkRates = new ArrayList<>();
        this.cowMilkRates = new ArrayList<>();
    }

    public static void build(final Activity activity, final String dairyId, final Date from, final Date to,
                      final ListenerAdapter<MilkRateCalculator> listenerAdapter) {

        final MilkRateCalculator milkRateCalculator = new MilkRateCalculator();
        FireBaseDao.fetchRates(activity, dairyId, MilkType.BUFFALO, from, to, new ListenerAdapter<List<Rate>>() {
            @Override
            public void onSuccess(List<Rate> rates) {
                milkRateCalculator.buffaloMilkRates.addAll(rates);
                FireBaseDao.fetchRates(activity, dairyId, MilkType.COW, from, to, new ListenerAdapter<List<Rate>>() {
                    @Override
                    public void onSuccess(List<Rate> rates) {
                        milkRateCalculator.cowMilkRates.addAll(rates);
                        listenerAdapter.onSuccess(milkRateCalculator);
                    }

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listenerAdapter.onFailure(e);
                    }
                });
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                listenerAdapter.onFailure(e);
            }
        });
    }

    public Rate findRateForDate(Date date, MilkType milkType) {
        if (milkType == MilkType.BUFFALO) {
            for (Rate buffaloMilkRate : buffaloMilkRates) {
                if (date.after(buffaloMilkRate.getEffectiveDate())) {
                    return buffaloMilkRate;
                }
            }
        } else {
            for (Rate cowMilkRate : cowMilkRates) {
                if (date.after(cowMilkRate.getEffectiveDate())) {
                    return cowMilkRate;
                }
            }
        }
        return null;
    }

    public void computeAndSetMilkRate(CollectedMilk collectedMilk) {
        Rate rate = findRateForDate(collectedMilk.getDate(), collectedMilk.getMilkType());
        if (rate != null) {
            if (rate.getFat() <= 0) {
                throw new RuntimeException("Invalid Rate, Fat% is zero." + rate);
            }
            float fat = collectedMilk.getFat();
            float oneUnitFatPrice = rate.getPrice() / rate.getFat();
            float collectedMilkFatPrice = collectedMilk.getFat() * oneUnitFatPrice;
            collectedMilk.setPerLtrPriceUsed(collectedMilkFatPrice);
            collectedMilk.setMilkPriceComputed(collectedMilkFatPrice * collectedMilk.getMilkQuantity());
        } else {
            throw new RuntimeException("Rate missing for MilkType: " + collectedMilk.getMilkType() + "date " + collectedMilk.getDate());
        }
    }

}
