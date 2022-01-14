/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pw.sherlock.charts;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import org.apache.log4j.Logger;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.markers.SeriesMarkers;

public class MemMonitor implements ExampleChart_Interface<XYChart> {

	private XYChart xyChart;

	public static final String Ser1_UsedHeap = "Used Heap";
	public static final String Ser2_TotHeap = "Heap Size";
	private static final Logger homeLogger = Logger.getLogger(MemMonitor.class);

	private List<Integer> usedHeapMemData, totHeapMemData;
	private List<Date> nodeRunTime;

	public static void main(String[] args) {
		final MemMonitor memMonitor = new MemMonitor();
		memMonitor.go();
	}

	public void go() {
		final SwingWrapper<XYChart> swingWrapper = new SwingWrapper<XYChart>(getChart());
		swingWrapper.displayChart();

		//Get the memory details from BW Agent on timer tick and repaint the chart
		TimerTask updateMemChart = new TimerTask() {
			@Override
			public void run() {
				//Get the memory details from BW Agent 
				updateData();
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						//repaint chart
						swingWrapper.repaintChart();
						//homeLogger.warn("Just now...");
					}
				});
			}
		};

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(updateMemChart, 0, 2000);
	}

	@Override
	public XYChart getChart() {
		//private List<Integer> usedHeapMemData, totHeapMemData;

		//Random data generator
		NodeCurrHeapUsage_Util2 currMemObj = new NodeCurrHeapUsage_Util2();

		//Real heap data getter
		//NodeCurrHeapUsage_Util currMemObj = new NodeCurrHeapUsage_Util();
		//Definition
		//private List<Integer> usedHeapMemData, totHeapMemData;
		//private List<Date> nodeRunTime;
		usedHeapMemData = currMemObj.currUsedHeap;
		totHeapMemData = currMemObj.currTotHeap;
		nodeRunTime = currMemObj.currNodeRunTime;

		xyChart = new XYChartBuilder()
				.width(500)
				.height(400)
				.yAxisTitle("Usage in MBs")
				.xAxisTitle("Time")
				.theme(ChartTheme.Matlab)
				.title("Heap memory monitor")
				.build();

		xyChart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Area);
		xyChart.getStyler().setDatePattern("DD/MMM - HH:mm:ss");
		xyChart.getStyler().setLegendPosition(LegendPosition.InsideNE);

		XYSeries usedHeapSeries, totHeapSeries;

		usedHeapSeries = xyChart.addSeries(Ser1_UsedHeap, nodeRunTime, usedHeapMemData);
		usedHeapSeries.setMarker(SeriesMarkers.NONE);
		usedHeapSeries.setFillColor(new Color(249, 220, 182, 180));
		usedHeapSeries.setLineWidth(4);
		usedHeapSeries.setLineColor(new Color(241, 154, 42));

		totHeapSeries = xyChart.addSeries(Ser2_TotHeap, nodeRunTime, totHeapMemData);
		totHeapSeries.setMarker(SeriesMarkers.NONE);
		totHeapSeries.setFillColor(new Color(208, 241, 252, 180));
		//totHeapSeries.setFillColor(new Color(1f,0f,0f,.5f));
		totHeapSeries.setLineWidth(4);
		totHeapSeries.setLineColor(new Color(32, 171, 217));

//		xyChart.getStyler().setPlotGridVerticalLinesVisible(true);
//		xyChart.getStyler().setPlotGridHorizontalLinesVisible(true);
//		//xyChart.getStyler().setXAxisLabelRotation(45);
		return xyChart;
	}

	public void updateData() {
		//Random data generator
		NodeCurrHeapUsage_Util2 currMemObj = new NodeCurrHeapUsage_Util2();

		// Get the current node memory usage details
		//NodeCurrHeapUsage_Util currMemObj = new NodeCurrHeapUsage_Util();
		List<Integer> usedHeapDataPoints = currMemObj.currUsedHeap;
		//System.out.println(currMemObj.currUsedHeap);
		List<Integer> totHeapDataPoints = currMemObj.currTotHeap;
		//System.out.println(currMemObj.currTotHeap);
		List<Date> nodeRunningTime = currMemObj.currNodeRunTime;

		usedHeapMemData.addAll(usedHeapDataPoints);
		totHeapMemData.addAll(totHeapDataPoints);
		nodeRunTime.addAll(nodeRunningTime);

		//xyChart.updateXYSeries(Ser1_UsedHeap, null, usedHeapMemData, null);
		xyChart.updateXYSeries(Ser1_UsedHeap, nodeRunTime, usedHeapMemData, null);
		xyChart.updateXYSeries(Ser2_TotHeap, nodeRunTime, totHeapMemData, null);
	}
}

class NodeCurrHeapUsage_Util2 {

	//AppnodeCalls nodeCalls = new AppnodeCalls();
	//int[] nodeMemOutput = nodeCalls.getNodeRT_Info_Old("Sherlock", "AS", "AN1");
	Random ran = new Random();
	int usedHeap;

	List<Integer> currUsedHeap, currTotHeap;
	List<Date> currNodeRunTime = new ArrayList<>();

	NodeCurrHeapUsage_Util2() {
		try {
			Date currDateTime = new Date();
			currUsedHeap = new CopyOnWriteArrayList<Integer>();
			currTotHeap = new CopyOnWriteArrayList<Integer>();

			//This is my real implementation to get the total available heap
			//currUsedHeap.add(nodeMemOutput[0]);
			//This is temp impl
			usedHeap = ran.nextInt(100) + 5;
			//System.out.println(usedHeap);
			currUsedHeap.add(usedHeap);

			//Get the used heap details
			//currTotHeap.add(nodeMemOutput[2]);
			//This is temp impl
			//System.out.println(300);
			currTotHeap.add(usedHeap + 50);
			System.out.println(currDateTime.toString());
			currNodeRunTime.add(currDateTime);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
