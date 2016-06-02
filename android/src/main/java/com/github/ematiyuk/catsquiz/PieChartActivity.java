package com.github.ematiyuk.catsquiz;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class PieChartActivity extends Activity {
    public static final String EXTRA_CORRECT_ANSWERS_NUMBER = "com.github.ematiyuk.catsquiz.correct_answers_number";
    public static final String EXTRA_INCORRECT_ANSWERS_NUMBER = "com.github.ematiyuk.catsquiz.incorrect_answers_number";
    public static final String EXTRA_CHEATED_ANSWERS_BANK = "com.github.ematiyuk.catsquiz.cheated_answers_bank";

    private int mCorrectAnswersNumber = 0;
    private int mIncorrectAnswersNumber = 0;
    private int mCheatedAnswersNumber = 0;

    Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piechart);

        final PieChart pieChart = (PieChart) findViewById(R.id.chart);

        mCorrectAnswersNumber = getIntent().getIntExtra(EXTRA_CORRECT_ANSWERS_NUMBER, 0);
        mIncorrectAnswersNumber = getIntent().getIntExtra(EXTRA_INCORRECT_ANSWERS_NUMBER, 0);
        mCheatedAnswersNumber = getCheatedAnswersNumber(
                getIntent().getBooleanArrayExtra(EXTRA_CHEATED_ANSWERS_BANK));

        res = getResources();

        // "0 + color resource ID" makes it possible to get String value of the specified color
        String backgroundColorStr = res.getString(0 + R.color.colorBackground);

        final ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(mCorrectAnswersNumber, 0));
        entries.add(new Entry(mIncorrectAnswersNumber, 1));
        entries.add(new Entry(mCheatedAnswersNumber, 2));

        final ArrayList<String> labels = new ArrayList<String>();
        labels.add(res.getString(R.string.correct_string));
        labels.add(res.getString(R.string.incorrect_string));
        labels.add(res.getString(R.string.cheated_string));

        final PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(2f); // set space between slices
        dataSet.setSelectionShift(4f); // set a shift when tapping a slice

        dataSet.setValueTextSize(20f);
        dataSet.setValueTextColor(Color.parseColor(backgroundColorStr));
        dataSet.setValueFormatter(new CustomValueFormatter());

        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(labels, dataSet);

        // enables drawing slice values
        data.setDrawValues(true);

        pieChart.setDescription("");
        pieChart.setData(data);

        // disables drawing slice labels
        pieChart.setDrawSliceText(false);

        pieChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));
        pieChart.setCenterTextSize(20f);
        pieChart.setCenterText(generateCenterSpannableText());

        pieChart.setTransparentCircleRadius(55f);
        pieChart.setTransparentCircleColor(Color.parseColor(backgroundColorStr));
        pieChart.setTransparentCircleAlpha(100);

        pieChart.setHoleColor(Color.parseColor(backgroundColorStr));
        pieChart.setHoleRadius(50f);

        // disable rotation of the chart by touch
        pieChart.setRotationEnabled(false);

        pieChart.animateY(1000);

        Legend legend = pieChart.getLegend();
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        legend.setTextSize(12f);
        legend.setFormSize(12f);
        legend.setXEntrySpace(20f);
        legend.setYEntrySpace(0f);
        legend.setYOffset(20f);

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                if (e == null)
                    return;
                int xIndex = e.getXIndex();

                setDefaultCenterTextSize(pieChart,
                        Math.round(e.getVal()) + "\n" + labels.get(xIndex));
                pieChart.setCenterTextColor(pieChart.getData().getColors()[xIndex]);
            }

            @Override
            public void onNothingSelected() {
                pieChart.setCenterText(generateCenterSpannableText());
            }
        });
    }

    private SpannableString generateCenterSpannableText() {
        String textColorStr = res.getString(0 + R.color.colorText);
        SpannableString str = new SpannableString(
                res.getString(R.string.app_name) + "\n" +
                        res.getString(R.string.results_string));
        str.setSpan(new StyleSpan(Typeface.BOLD), 0, str.length(), 0);
        str.setSpan(new ForegroundColorSpan(Color.parseColor(textColorStr)), 0, str.length(), 0);
        return str;
    }

    private void setDefaultCenterTextSize(PieChart pieChart, CharSequence text) {
        pieChart.setCenterText(text);
        pieChart.setCenterTextSize(20f);
    }

    private int getCheatedAnswersNumber(boolean[] cheatedQuestionBank) {
        int cheatedQBankLength = cheatedQuestionBank.length;
        int count = 0;
        for (int i = 0; i < cheatedQBankLength; i++) {
            if (cheatedQuestionBank[i])
                count++;
        }
        return count;
    }
}
