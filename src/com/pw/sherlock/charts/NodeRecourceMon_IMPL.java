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
import java.awt.Font;
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
public class NodeRecourceMon_IMPL implements ExampleChart_Interface<XYChart> {

//delete this soon	
//private XYChart nodeHeapChart;
	private XYChart nodeHeapMemChart, nodeNativeMemChart, nodeCpuChart, nodeThreadsChart;
	public static String nodeResourceInfoLbl_Source, nodeResourceUpTime_Source;
	private final Font cursorFont = new Font("Verdana", Font.PLAIN, 12);

	//Initialize once, used in both mem and CPU initial loading
	NodeCurrResourceUsage_Util initialResourceDetails;

	public static final String ser1_UsedHeap = "Used Heap";
	public static final String ser2_TotHeap = "Heap Size";

	public static final String ser1_UsedNativeMem = "Used Native Memory";
	public static final String ser2_TotNativeMem = "Total Process Memory";

	public static final String ser_CpuUsage = "CPU Usage (%)";
	public static final String ser_threads = "Threads";

	private static final Logger nodeMonitorLogger = Logger.getLogger(NodeRecourceMon_IMPL.class);

	//Global declaration of X and Y series datalist of node heap dialChart
	private List<Integer> usedHeapMemData, totHeapMemData, usedNativeMemData, totNativeMemData, cpuUsageData, nodeThreadData;
	private List<Date> nodeRunTime;

	public XYChart getNodeHeapMemChart(String domainName, String appspaceName, String appnodeName) {
		//AgentCurrHeapUsage_Util currMemObj = new AgentCurrHeapUsage_Util();
		initialResourceDetails = new NodeCurrResourceUsage_Util(domainName, appspaceName, appnodeName);

		usedHeapMemData = initialResourceDetails.currUsedHeap;
		totHeapMemData = initialResourceDetails.currTotHeap;
		nodeRunTime = initialResourceDetails.currNodeRunTime;

		nodeHeapMemChart = new XYChartBuilder()
				.width(500)
				.height(450)
				.yAxisTitle("Usage in MBs")
				.xAxisTitle("Time")
				.theme(ChartTheme.Matlab)
				.title("Heap Memory Monitor")
				.build();

		nodeHeapMemChart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Area);
		nodeHeapMemChart.getStyler().setDatePattern("dd/MMM - HH:mm:ss");
		nodeHeapMemChart.getStyler().setYAxisDecimalPattern("#,### MB");

		nodeHeapMemChart.getStyler().setLegendPosition(LegendPosition.OutsideS);
		nodeHeapMemChart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);
		nodeHeapMemChart.getStyler().setLegendVisible(true);
		//nodeHeapMemChart.getStyler().setChartTitleFont(new Font("Calibri", Font.BOLD, 14 ));

		XYSeries totHeapMemSeries, usedHeapMemSeries;

		totHeapMemSeries = nodeHeapMemChart.addSeries(ser2_TotHeap, nodeRunTime, totHeapMemData);
		totHeapMemSeries.setMarker(SeriesMarkers.NONE);
		//Below are value that always worked
		//totHeapMemSeries.setFillColor(new Color(249, 220, 182, 180));
		////Green for free Mem
		totHeapMemSeries.setFillColor(new Color(204, 227, 153, 180));
		totHeapMemSeries.setLineWidth(2);
		totHeapMemSeries.setLineColor(new Color(76, 153, 0));
		totHeapMemSeries.setSmooth(true);

		usedHeapMemSeries = nodeHeapMemChart.addSeries(ser1_UsedHeap, nodeRunTime, usedHeapMemData);
		//System.out.println("Used Heap" + usedHeapMemData + "\n");
		usedHeapMemSeries.setMarker(SeriesMarkers.NONE);
		//Below are value that always worked
		//usedHeapMemSeries.setFillColor(new Color(208, 241, 252, 180));
		////Pink for used
		usedHeapMemSeries.setFillColor(new Color(254, 200, 153, 180));
		usedHeapMemSeries.setLineWidth(2);
		usedHeapMemSeries.setLineColor(new Color(204, 102, 51));
		//32, 171, 217
		usedHeapMemSeries.setSmooth(true);

		nodeHeapMemChart.getStyler().setPlotBorderVisible(true);
		nodeHeapMemChart.getStyler().setYAxisMin(10.0);

		//configuring the grid lines formatting
		nodeHeapMemChart.getStyler().setPlotGridLinesColor(Color.GRAY);
		Stroke plotGridLinesStroke = new BasicStroke(0.4f);
		nodeHeapMemChart.getStyler().setPlotGridLinesStroke(plotGridLinesStroke);
		//nodeHeapMemChart.getStyler().setToolTipsEnabled(true);

		nodeHeapMemChart.getStyler().setCursorEnabled(true);
		nodeHeapMemChart.getStyler().setCursorBackgroundColor(Color.darkGray);
		nodeHeapMemChart.getStyler().setCursorColor(Color.DARK_GRAY);
		nodeHeapMemChart.getStyler().setCursorFontColor(Color.WHITE);
		nodeHeapMemChart.getStyler().setCursorFont(cursorFont);

		return nodeHeapMemChart;
	}

	public XYChart getNodeNativeMemChart(String domainName, String appspaceName, String appnodeName) {
		//AgentCurrHeapUsage_Util currMemObj = new AgentCurrHeapUsage_Util();
		initialResourceDetails = new NodeCurrResourceUsage_Util(domainName, appspaceName, appnodeName);

		//usedNativeMemData, totNativeMemData,
		//currUsedNativeMem,currTotNativeMem
		usedNativeMemData = initialResourceDetails.currUsedNativeMem;
		totNativeMemData = initialResourceDetails.currTotNativeMem;
		nodeRunTime = initialResourceDetails.currNodeRunTime;

		nodeNativeMemChart = new XYChartBuilder()
				.width(500)
				.height(450)
				.yAxisTitle("Usage in MBs")
				.xAxisTitle("Time")
				.theme(ChartTheme.Matlab)
				.title("Native Memory Monitor")
				.build();

		nodeNativeMemChart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Area);
		nodeNativeMemChart.getStyler().setDatePattern("dd/MMM - HH:mm:ss");
		nodeNativeMemChart.getStyler().setYAxisDecimalPattern("#,### MB");

		nodeNativeMemChart.getStyler().setLegendPosition(LegendPosition.OutsideS);
		nodeNativeMemChart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);
		nodeNativeMemChart.getStyler().setLegendVisible(true);

		XYSeries totNativeMemSeries, usedNativeMemSeries;

		totNativeMemSeries = nodeNativeMemChart.addSeries(ser2_TotNativeMem, nodeRunTime, totNativeMemData);
		totNativeMemSeries.setMarker(SeriesMarkers.NONE);
		totNativeMemSeries.setFillColor(new Color(204, 227, 153, 180));
		totNativeMemSeries.setLineWidth(2);
		totNativeMemSeries.setLineColor(new Color(76, 153, 0));
		totNativeMemSeries.setSmooth(true);

		usedNativeMemSeries = nodeNativeMemChart.addSeries(ser1_UsedNativeMem, nodeRunTime, usedNativeMemData);
		//System.out.println("Used Heap" + usedHeapMemData + "\n");
		usedNativeMemSeries.setMarker(SeriesMarkers.NONE);
		usedNativeMemSeries.setFillColor(new Color(254, 200, 153, 180));
		usedNativeMemSeries.setLineWidth(2);
		usedNativeMemSeries.setLineColor(new Color(204, 102, 51));
		usedNativeMemSeries.setSmooth(true);

		nodeNativeMemChart.getStyler().setPlotBorderVisible(true);
		nodeNativeMemChart.getStyler().setYAxisMin(10.0);

		//configuring the grid lines formatting
		nodeNativeMemChart.getStyler().setPlotGridLinesColor(Color.GRAY);
		Stroke plotGridLinesStroke = new BasicStroke(0.4f);
		nodeNativeMemChart.getStyler().setPlotGridLinesStroke(plotGridLinesStroke);
		//nodeNativeMemChart.getStyler().setToolTipsEnabled(true);

		nodeNativeMemChart.getStyler().setCursorEnabled(true);
		nodeNativeMemChart.getStyler().setCursorBackgroundColor(Color.darkGray);
		nodeNativeMemChart.getStyler().setCursorColor(Color.DARK_GRAY);
		nodeNativeMemChart.getStyler().setCursorFontColor(Color.WHITE);
		nodeNativeMemChart.getStyler().setCursorFont(cursorFont);

		return nodeNativeMemChart;
	}

	public XYChart getNodeCpuChart(String domainName, String appspaceName, String appnodeName) {
		//AgentCurrHeapUsage_Util initialResourceDetails = new AgentCurrHeapUsage_Util();
		cpuUsageData = initialResourceDetails.currCpuUsage;
		nodeRunTime = initialResourceDetails.currNodeRunTime;

		nodeCpuChart = new XYChartBuilder()
				.width(500)
				.height(450)
				.yAxisTitle("Usage in %")
				.xAxisTitle("Time")
				.theme(ChartTheme.Matlab)
				.title("CPU Monitor")
				.build();

		nodeCpuChart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Line);
		nodeCpuChart.getStyler().setDatePattern("dd/MMM - HH:mm:ss");
		nodeCpuChart.getStyler().setYAxisMax(100.0);
		nodeCpuChart.getStyler().setYAxisMin(0.0);
		//nodeCpuChart.getStyler().sety
		//nodeCpuChart.getStyler().setYAxisDecimalPattern(" %");

		nodeCpuChart.getStyler().setLegendPosition(LegendPosition.OutsideS);
		nodeCpuChart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);
		nodeCpuChart.getStyler().setLegendVisible(true);

		XYSeries usedCpuSeries;

		usedCpuSeries = nodeCpuChart.addSeries(ser_CpuUsage, nodeRunTime, cpuUsageData);
		usedCpuSeries.setLineWidth(2);
		usedCpuSeries.setLineColor(new Color(241, 154, 42));
		usedCpuSeries.setMarker(SeriesMarkers.NONE);

		nodeCpuChart.getStyler().setPlotBorderVisible(true);
		nodeCpuChart.getStyler().setPlotGridLinesColor(Color.LIGHT_GRAY);
		Stroke plotGridLinesStroke = new BasicStroke(0.4f);
		nodeCpuChart.getStyler().setPlotGridLinesStroke(plotGridLinesStroke);

		//nodeCpuChart.getStyler().setToolTipsEnabled(false);
		nodeCpuChart.getStyler().setCursorEnabled(true);
		nodeCpuChart.getStyler().setCursorBackgroundColor(Color.darkGray);
		nodeCpuChart.getStyler().setCursorColor(Color.DARK_GRAY);
		nodeCpuChart.getStyler().setCursorFontColor(Color.WHITE);
		nodeCpuChart.getStyler().setCursorFont(cursorFont);

		usedCpuSeries.setSmooth(true);
		return nodeCpuChart;
	}

	public XYChart getNodeThreadChart(String domainName, String appspaceName, String appnodeName) {
		//AgentCurrHeapUsage_Util initialResourceDetails = new AgentCurrHeapUsage_Util();
		nodeThreadData = initialResourceDetails.currThreads;
		nodeRunTime = initialResourceDetails.currNodeRunTime;

		nodeThreadsChart = new XYChartBuilder()
				.width(500)
				.height(450)
				.yAxisTitle("Number of threads")
				.xAxisTitle("Time")
				.theme(ChartTheme.Matlab)
				.title("Thread Monitor")
				.build();

		nodeThreadsChart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Line);
		nodeThreadsChart.getStyler().setDatePattern("dd/MMM - HH:mm:ss");
		//nodeThreadsChart.getStyler().setYAxisMax(100.0);
		//nodeThreadsChart.getStyler().setYAxisMin(0.0);

		nodeThreadsChart.getStyler().setLegendPosition(LegendPosition.OutsideS);
		nodeThreadsChart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);
		nodeThreadsChart.getStyler().setLegendVisible(true);

		XYSeries usedThreadsSeries;

		usedThreadsSeries = nodeThreadsChart.addSeries(ser_threads, nodeRunTime, nodeThreadData);
		usedThreadsSeries.setLineWidth(2);
		usedThreadsSeries.setLineColor(new Color(241, 154, 42));
		usedThreadsSeries.setMarker(SeriesMarkers.NONE);

		nodeThreadsChart.getStyler().setPlotBorderVisible(true);
		nodeThreadsChart.getStyler().setPlotGridLinesColor(Color.LIGHT_GRAY);
		Stroke plotGridLinesStroke = new BasicStroke(0.4f);
		nodeThreadsChart.getStyler().setPlotGridLinesStroke(plotGridLinesStroke);

		//nodeThreadsChart.getStyler().setToolTipsEnabled(false);
		nodeThreadsChart.getStyler().setCursorEnabled(true);
		nodeThreadsChart.getStyler().setCursorBackgroundColor(Color.darkGray);
		nodeThreadsChart.getStyler().setCursorColor(Color.DARK_GRAY);
		nodeThreadsChart.getStyler().setCursorFontColor(Color.WHITE);
		nodeThreadsChart.getStyler().setCursorFont(cursorFont);

		usedThreadsSeries.setSmooth(true);
		return nodeThreadsChart;
	}

	@Override
	public XYChart getChart() {
		return null;
	}

	public void updateData(String domainName, String appspaceName, String appnodeName) {
		//Global declaration of X and Y series datalist of node heap dialChart
		//private List<Integer> usedHeapMemData, totHeapMemData, usedNativeMemData, totNativeMemData, cpuUsageData, nodeThreadData;

		// Get the current agent usage details
		//initialResourceDetails = new NodeCurrResourceUsage_Util(domainName, appspaceName, appnodeName);
		NodeCurrResourceUsage_Util currNodeRecourceObj = new NodeCurrResourceUsage_Util(domainName, appspaceName, appnodeName);
		List<Integer> usedHeapMemDataPoints_updated = currNodeRecourceObj.currUsedHeap;
		List<Integer> totHeapMemDataPoints_updated = currNodeRecourceObj.currTotHeap;

		List<Integer> usedNativeMemDataPoints_updated = currNodeRecourceObj.currUsedNativeMem;
		List<Integer> totNativeMemDataPoints_updated = currNodeRecourceObj.currTotNativeMem;

		List<Integer> totCpuDataPoints_updated = currNodeRecourceObj.currCpuUsage;
		List<Integer> totThreadDataPoints_updated = currNodeRecourceObj.currThreads;

		List<Date> nodeRunningTime_updated = currNodeRecourceObj.currNodeRunTime;

		usedHeapMemData.addAll(usedHeapMemDataPoints_updated);
		totHeapMemData.addAll(totHeapMemDataPoints_updated);

		usedNativeMemData.addAll(usedNativeMemDataPoints_updated);
		totNativeMemData.addAll(totNativeMemDataPoints_updated);

		cpuUsageData.addAll(totCpuDataPoints_updated);
		nodeThreadData.addAll(totThreadDataPoints_updated);
		nodeRunTime.addAll(nodeRunningTime_updated);

		//Updating heap Memory
		nodeHeapMemChart.updateXYSeries(ser1_UsedHeap, nodeRunTime, usedHeapMemData, null);
		nodeHeapMemChart.updateXYSeries(ser2_TotHeap, nodeRunTime, totHeapMemData, null);

		//Updating Native Memory
		nodeNativeMemChart.updateXYSeries(ser1_UsedNativeMem, nodeRunTime, usedNativeMemData, null);
		nodeNativeMemChart.updateXYSeries(ser2_TotNativeMem, nodeRunTime, totNativeMemData, null);

		//Updating CPU
		nodeCpuChart.updateXYSeries(ser_CpuUsage, nodeRunTime, cpuUsageData, null);
		
		//Updating threads chart
		nodeThreadsChart.updateXYSeries(ser_threads, nodeRunTime, nodeThreadData, null);
	}
}

class NodeCurrResourceUsage_Util {

	AppnodeCalls nodeCalls = new AppnodeCalls();

	List<Integer> currUsedHeap, currTotHeap, currUsedNativeMem, currTotNativeMem, currCpuUsage, currThreads;
	List<Date> currNodeRunTime = new ArrayList<>();

	NodeCurrResourceUsage_Util(String domainName, String appspaceName, String appnodeName) {
		try {
			Date currDateTime = new Date();
			currUsedHeap = new CopyOnWriteArrayList<>();
			currTotHeap = new CopyOnWriteArrayList<>();
			currUsedNativeMem = new CopyOnWriteArrayList<>();
			currTotNativeMem = new CopyOnWriteArrayList<>();
			currCpuUsage = new CopyOnWriteArrayList<>();
			currThreads = new CopyOnWriteArrayList<>();

			int[] nodeResourceOutput = nodeCalls.getNodeRT_Info(domainName, appspaceName, appnodeName);

			//System.out.println("currUsedHeap:" + agentResourceOutput[7] + "\n");
			currUsedHeap.add(nodeResourceOutput[8]);
			currUsedNativeMem.add(nodeResourceOutput[10]);
			//System.out.println("currTotHeap:" + agentResourceOutput[2] + "\n");
			currTotHeap.add(nodeResourceOutput[11]);
			currTotNativeMem.add(nodeResourceOutput[1]);
			//System.out.println("currCpuUsage:" + agentResourceOutput[1] + "\n");
			currCpuUsage.add(nodeResourceOutput[4]);
			currThreads.add(nodeResourceOutput[0]);
			currNodeRunTime.add(currDateTime);

			int seconds = (int) (nodeResourceOutput[2] / 1000) % 60;
			int minutes = (int) ((nodeResourceOutput[2] / (1000 * 60)) % 60);
			int hours = (int) ((nodeResourceOutput[2] / (1000 * 60 * 60)) % 24);

			String formattedUpTime;
			if (hours <= 0) {
				formattedUpTime = "<html><b>Up since:</b> " + minutes + " min " + seconds + " sec</html>";
			} else {
				formattedUpTime = "<html><b>Up since:</b> " + hours + " Hrs " + minutes + " min " + seconds + " sec</html>";
			}

			NodeRecourceMon_IMPL.nodeResourceInfoLbl_Source = "<html><b>BW Appnode process ID:</b> " + nodeResourceOutput[5]
					+ ". <b>Active thread count:</b> " + nodeResourceOutput[0] + ". <b>CPU used:</b> " + nodeResourceOutput[4]
					+ "%. <b>System Memory used:</b> " + nodeResourceOutput[3] + "%</html>";
			NodeRecourceMon_IMPL.nodeResourceUpTime_Source = formattedUpTime;
//				JSONObject obj_agentJSONArray = new JSONObject(formattedJsonResp);
//				nodeUsageDetails[0] = obj_agentJSONArray.getInt("activeThreadCount");
//				nodeUsageDetails[1] = (int) (obj_agentJSONArray.getLong("totalMemoryInBytes")/ toMBs);
//				nodeUsageDetails[2] = obj_agentJSONArray.getInt("upSince");
//				nodeUsageDetails[3] = obj_agentJSONArray.getInt("percentMemoryUsed");
//				nodeUsageDetails[4] = obj_agentJSONArray.getInt("percentCpuUsed");
//				nodeUsageDetails[5] = obj_agentJSONArray.getInt("systemProcessId");
//				nodeUsageDetails[6] = (int) (obj_agentJSONArray.getLong("freeMemoryInBytes")/ toMBs);
//				nodeUsageDetails[7] = (int) (obj_agentJSONArray.getLong("freeHeapMemoryInBytes") / toMBs);
//				nodeUsageDetails[8] = (int) (obj_agentJSONArray.getLong("usedHeapMemoryInBytes") / toMBs);
//				nodeUsageDetails[9] = (int) (obj_agentJSONArray.getLong("maxHeapMemoryInBytes") / toMBs);
//				nodeUsageDetails[10] = (int) (obj_agentJSONArray.getLong("usedMemoryInBytes") / toMBs);
//				nodeUsageDetails[11] = (int) (obj_agentJSONArray.getLong("totalHeapMemoryInBytes") / toMBs);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
