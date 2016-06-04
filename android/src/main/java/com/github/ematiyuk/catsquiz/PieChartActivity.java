package com.github.ematiyuk.catsquiz;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class PieChartActivity extends Activity {
    public static final String EXTRA_CORRECT_ANSWERS_NUMBER = "com.github.ematiyuk.catsquiz.correct_answers_number";
    public static final String EXTRA_INCORRECT_ANSWERS_NUMBER = "com.github.ematiyuk.catsquiz.incorrect_answers_number";
    public static final String EXTRA_CHEATED_ANSWERS_BANK = "com.github.ematiyuk.catsquiz.cheated_answers_bank";

    private int mCorrectAnswersNumber = 0;
    private int mIncorrectAnswersNumber = 0;
    private int mCheatedAnswersNumber = 0;

    private PieChart pieChart;
    private Resources res;
    private String backgroundColorStr;
    private ArrayList<String> labels;
    private Typeface tf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piechart);

        mCorrectAnswersNumber = getIntent().getIntExtra(EXTRA_CORRECT_ANSWERS_NUMBER, 0);
        mIncorrectAnswersNumber = getIntent().getIntExtra(EXTRA_INCORRECT_ANSWERS_NUMBER, 0);
        mCheatedAnswersNumber = getCheatedAnswersNumber(
                getIntent().getBooleanArrayExtra(EXTRA_CHEATED_ANSWERS_BANK));

        pieChart = (PieChart) findViewById(R.id.chart);
        res = getResources();

        // "0 + color resource ID" makes it possible to get String value of the specified color
        backgroundColorStr = res.getString(0 + R.color.colorBackground);

        tf = Typeface.createFromAsset(getAssets(), "OpenSans-Semibold.ttf");

        setData(); // set data for PieChart

        pieChart.setDescription("");

        // disables drawing slice labels
        pieChart.setDrawSliceText(false);

        pieChart.setCenterTextSize(20f);
        setDefaultCenterText();

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
        legend.setFormSize(14f);
        legend.setXEntrySpace(20f);
        legend.setYEntrySpace(0f);
        legend.setYOffset(20f);
        legend.setTypeface(tf);

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                if (e == null)
                    return;
                int xIndex = e.getXIndex();

                pieChart.setCenterText(getFormattedCenterSpannableText(
                        new SpannableString(Math.round(e.getVal()) + "\n" + labels.get(xIndex)),
                        pieChart.getData().getColors()[xIndex]));
            }

            @Override
            public void onNothingSelected() {
                setDefaultCenterText();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_piechart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_share:
                // do something
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(RESULT_OK); // return result to the caller
    }

    private void setData() {
        final ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(mCorrectAnswersNumber, 0));
        entries.add(new Entry(mIncorrectAnswersNumber, 1));
        entries.add(new Entry(mCheatedAnswersNumber, 2));

        labels = new ArrayList<String>();
        labels.add(res.getString(R.string.correct_string));
        labels.add(res.getString(R.string.incorrect_string));
        labels.add(res.getString(R.string.cheated_string));

        final PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(2f); // set space between slices
        dataSet.setSelectionShift(4f); // set a shift when tapping a slice

        dataSet.setValueTextSize(20f);
        dataSet.setValueFormatter(new CustomValueFormatter());
        dataSet.setValueTypeface(tf);

        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(labels, dataSet);

        // enables drawing slice values
        data.setDrawValues(true);

        pieChart.setData(data);

        setValueTextColors();

        // undo all highlights
        pieChart.highlightValues(null);

        pieChart.invalidate();
    }

    /** Sets background or transparent color for the value text depending on answerNumber variable,
     *  f.i. if mCorrectAnswersNumber > 0 its value text color on the PieChart is set to transparent one */
    private void setValueTextColors() {
        int transparentColor = android.R.color.transparent;
        int backgroundColor = Color.parseColor(backgroundColorStr);
        List<Integer> colors = new ArrayList<Integer>();

        boolean correctANGreaterThanZero = mCorrectAnswersNumber > 0;
        boolean incorrectANGreaterThanZero = mIncorrectAnswersNumber > 0;
        boolean cheatedANGreaterThanZero = mCheatedAnswersNumber > 0;

        if (correctANGreaterThanZero && incorrectANGreaterThanZero && cheatedANGreaterThanZero) {
            colors.add(backgroundColor);
            colors.add(backgroundColor);
            colors.add(backgroundColor);
        } else if (correctANGreaterThanZero && incorrectANGreaterThanZero && !cheatedANGreaterThanZero) {
            colors.add(backgroundColor);
            colors.add(backgroundColor);
            colors.add(transparentColor);
        } else if (correctANGreaterThanZero && !incorrectANGreaterThanZero && cheatedANGreaterThanZero) {
            colors.add(backgroundColor);
            colors.add(transparentColor);
            colors.add(backgroundColor);
        } else if (correctANGreaterThanZero && !incorrectANGreaterThanZero && !cheatedANGreaterThanZero) {
            colors.add(backgroundColor);
            colors.add(transparentColor);
            colors.add(transparentColor);
        } else if (!correctANGreaterThanZero && incorrectANGreaterThanZero && cheatedANGreaterThanZero) {
            colors.add(transparentColor);
            colors.add(backgroundColor);
            colors.add(backgroundColor);
        } else if (!correctANGreaterThanZero && incorrectANGreaterThanZero && !cheatedANGreaterThanZero) {
            colors.add(transparentColor);
            colors.add(backgroundColor);
            colors.add(transparentColor);
        } else if (!correctANGreaterThanZero && !incorrectANGreaterThanZero && cheatedANGreaterThanZero) {
            colors.add(transparentColor);
            colors.add(transparentColor);
            colors.add(backgroundColor);
        }

        pieChart.getData().getDataSet().setValueTextColors(colors);
    }

    private SpannableString getFormattedCenterSpannableText(SpannableString text, int color) {
        text.setSpan(new CustomTypefaceSpan("", tf), 0, text.length(), 0);
        text.setSpan(new ForegroundColorSpan(color), 0, text.length(), 0);
        return text;
    }

    private void setDefaultCenterText() {
        pieChart.setCenterText(getFormattedCenterSpannableText(
                new SpannableString(res.getString(R.string.app_name) + "\n" +
                        res.getString(R.string.results_string)),
                R.color.colorText));
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
