/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pw.sherlock.charts;

import com.pw.sherlock.BWCalls.AppnodeCalls;
import java.awt.BasicStroke;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.awt.Color;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Date;
import org.apache.log4j.Logger;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.markers.SeriesMarkers;

//public class NodeMemMonitor implements ExampleChart<XYChart>, RealtimeExampleChart {
public class NodeMemMonitor implements ExampleChart_Interface<XYChart> {

	private XYChart nodeHeapChart;

	public static final String Ser1_UsedHeap = "Used Heap";
	public static final String Ser2_TotHeap = "Heap Size";
	private static final Logger homeLogger = Logger.getLogger(NodeMemMonitor.class);

	//Global declaration of X and Y series datalist of node heap chart
	private List<Integer> usedHeapMemData, totHeapMemData;
	private List<Date> nodeRunTime;

	@Override
	public XYChart getChart() {
		//private List<Integer> usedHeapMemData, totHeapMemData;
		NodeCurrHeapUsage_Util2 currMemObj = new NodeCurrHeapUsage_Util2();
		//Definition
		//private List<Integer> usedHeapMemData, totHeapMemData;
		//private List<Date> nodeRunTime;
		usedHeapMemData = currMemObj.currUsedHeap;
		totHeapMemData = currMemObj.currTotHeap;
		nodeRunTime = currMemObj.currNodeRunTime;

		nodeHeapChart = new XYChartBuilder()
				.width(500)
				.height(400)
				.yAxisTitle("Usage in MBs")
				.xAxisTitle("Time")
				.theme(ChartTheme.Matlab)
				.title("Heap memory monitor")
				.build();

		nodeHeapChart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Area);
		nodeHeapChart.getStyler().setDatePattern("dd/MMM - HH:mm:ss");
		//nodeHeapChart.getStyler().setDatePattern("DD/MM/YYYY - HH:mm:ss");

		//Set the legend
		nodeHeapChart.getStyler().setLegendPosition(LegendPosition.OutsideS);
		nodeHeapChart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);
		nodeHeapChart.getStyler().setLegendVisible(true);

		XYSeries usedHeapSeries, totHeapSeries;

		totHeapSeries = nodeHeapChart.addSeries(Ser2_TotHeap, nodeRunTime, totHeapMemData);
		//System.out.println("Heap size" + totHeapMemData + "\n");
		totHeapSeries.setMarker(SeriesMarkers.NONE);
		totHeapSeries.setFillColor(new Color(249, 220, 182, 180));
		totHeapSeries.setLineWidth(3);
		totHeapSeries.setLineColor(new Color(241, 154, 42));
		//totHeapSeries.setSmooth(true);

		usedHeapSeries = nodeHeapChart.addSeries(Ser1_UsedHeap, nodeRunTime, usedHeapMemData);
		//System.out.println("Used Heap" + usedHeapMemData + "\n");
		usedHeapSeries.setMarker(SeriesMarkers.NONE);
		usedHeapSeries.setFillColor(new Color(208, 241, 252, 180));
		usedHeapSeries.setLineWidth(3);
		usedHeapSeries.setLineColor(new Color(32, 171, 217));
		//usedHeapSeries.setSmooth(true);

		nodeHeapChart.getStyler().setPlotBorderVisible(true);
		nodeHeapChart.getStyler().setYAxisMin(10.0);

		//configuring the grid lines formatting
		nodeHeapChart.getStyler().setPlotGridLinesColor(Color.GRAY);
		Stroke plotGridLinesStroke = new BasicStroke(0.4f);
		nodeHeapChart.getStyler().setPlotGridLinesStroke(plotGridLinesStroke);
		nodeHeapChart.getStyler().setToolTipsEnabled(true);

		//xyChart.getStyler().setXAxisLabelRotation(45);
		return nodeHeapChart;

	}

	public void updateData() {
		//Global declaration of X and Y series datalist of node heap chart
		//private List<Integer> usedHeapMemData, totHeapMemData;

		// Get the current node memory usage details
		NodeCurrHeapUsage_Util2 currMemObj = new NodeCurrHeapUsage_Util2();
		List<Integer> usedHeapDataPoints_updated = currMemObj.currUsedHeap;
		List<Integer> totHeapDataPoints_updated = currMemObj.currTotHeap;
		List<Date> nodeRunningTime_updated = currMemObj.currNodeRunTime;

		usedHeapMemData.addAll(usedHeapDataPoints_updated);
		totHeapMemData.addAll(totHeapDataPoints_updated);
		nodeRunTime.addAll(nodeRunningTime_updated);

		//xyChart.updateXYSeries(Ser1_UsedHeap, null, usedHeapMemData, null);
		nodeHeapChart.updateXYSeries(Ser1_UsedHeap, nodeRunTime, usedHeapMemData, null);
		nodeHeapChart.updateXYSeries(Ser2_TotHeap, nodeRunTime, totHeapMemData, null);
	}
}

class NodeCurrHeapUsage_Util23 {

	AppnodeCalls nodeCalls = new AppnodeCalls();

	int[] nodeMemOutput = nodeCalls.getNodeRT_Info("Sherlock", "AS", "AN1");

	List<Integer> currUsedHeap, currTotHeap;
	List<Date> currNodeRunTime = new ArrayList<>();

	NodeCurrHeapUsage_Util23() {
		try {
			Date currDateTime = new Date();
			currUsedHeap = new CopyOnWriteArrayList<Integer>();
			currTotHeap = new CopyOnWriteArrayList<Integer>();

			//Get the total available heap
			//System.out.println(nodeMemOutput[0]);
			currUsedHeap.add(nodeMemOutput[2]);
			//Get the used heap details
			//System.out.println(nodeMemOutput[2]);
			currTotHeap.add(nodeMemOutput[0]);
			currNodeRunTime.add(currDateTime);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}