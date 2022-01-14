/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pw.sherlock.charts;

import com.pw.sherlock.BWCalls.AgentCalls;
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
public class AgentRecourceMon_IMPL implements ExampleChart_Interface<XYChart> {

//delete this soon	
//private XYChart nodeHeapChart;
	private XYChart agentMemChart;
	private XYChart agentCpuChart;
	public static String agentResourceInfoLbl_Source, agentResourceUpTimeLbl_Source;
	private final Font cursorFont = new Font("Verdana", Font.PLAIN, 12);
	private final Font chartTitleFont = new Font("Verdana", Font.BOLD, 13);
	//private final Font chartFont = new Font("Verdana", Font.BOLD, 13);
	//private final Font chartTitleFont = new Font("Verdana", Font.BOLD, 13);
	//private final Font chartTitleFont = new Font("Verdana", Font.BOLD, 13);
	
	

	//Initialize once, used in both mem and CPU initial loading
	AgentCurrHeapUsage_Util initialResourceDetails;

	public static final String Ser1_UsedMem = "Used Memory";
	public static final String Ser2_TotalMem = "Total Memory";
	public static final String Ser_CpuUsage = "CPU Usage (%)";

	private static final Logger agentMonitorLogger = Logger.getLogger(AgentRecourceMon_IMPL.class);

	//Global declaration of X and Y series datalist of node heap chart
	private List<Integer> usedMemData, totMemData, cpuUsageData;
	private List<Date> agentRunTime;

	public XYChart getAgentMemChart() {
		//AgentCurrHeapUsage_Util currMemObj = new AgentCurrHeapUsage_Util();
		initialResourceDetails = new AgentCurrHeapUsage_Util();

		usedMemData = initialResourceDetails.currUsedMem;
		totMemData = initialResourceDetails.currTotMem;
		agentRunTime = initialResourceDetails.currAgentRunTime;

		agentMemChart = new XYChartBuilder()
				.width(500)
				.height(400)
				.yAxisTitle("Usage in MBs")
				.xAxisTitle("Time")
				.theme(ChartTheme.Matlab)
				.title("Process Memory Monitor")
				.build();

		agentMemChart.getStyler().setChartTitleFont(new Font("Arial", Font.PLAIN, 13));
		
		agentMemChart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Area);
		agentMemChart.getStyler().setDatePattern("dd/MMM - HH:mm:ss");
		agentMemChart.getStyler().setYAxisDecimalPattern("#,### MB");
		agentMemChart.getStyler().setChartTitleFont(new Font("Arial", Font.PLAIN, 14));
				
			
		agentMemChart.getStyler().setLegendPosition(LegendPosition.OutsideS);
		agentMemChart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);
		agentMemChart.getStyler().setLegendFont(new Font("Arial", Font.PLAIN, 13));
		agentMemChart.getStyler().setLegendVisible(true);

		//agentMemChart.getStyler().setToolTipsEnabled(true);
		agentMemChart.getStyler().setCursorEnabled(true);
		agentMemChart.getStyler().setCursorBackgroundColor(Color.darkGray);
		agentMemChart.getStyler().setCursorColor(Color.DARK_GRAY);
		agentMemChart.getStyler().setCursorFontColor(Color.WHITE);
		agentMemChart.getStyler().setCursorFont(cursorFont);

		XYSeries usedMemSeries, totMemSeries;

		totMemSeries = agentMemChart.addSeries(Ser2_TotalMem, agentRunTime, totMemData);
		totMemSeries.setMarker(SeriesMarkers.NONE);
		
		//Original colour
		//totMemSeries.setFillColor(new Color(249, 220, 182, 180));
		totMemSeries.setFillColor(new Color(204, 227, 153, 180));
		totMemSeries.setLineWidth(2);
		//Original line color
		//totMemSeries.setLineColor(new Color(241, 154, 42));
		totMemSeries.setLineColor(new Color(76, 153, 0));
		totMemSeries.setSmooth(true);

		usedMemSeries = agentMemChart.addSeries(Ser1_UsedMem, agentRunTime, usedMemData);
		//System.out.println("Used Heap" + usedMemData + "\n");
		usedMemSeries.setMarker(SeriesMarkers.NONE);
		//Original used color
		//usedMemSeries.setFillColor(new Color(208, 241, 252, 180));
		usedMemSeries.setFillColor(new Color(254, 200, 153, 180));
		usedMemSeries.setLineWidth(2);
		//Original line color
		//usedMemSeries.setLineColor(new Color(32, 171, 217));
		usedMemSeries.setLineColor(new Color(204, 102, 51));
		usedMemSeries.setSmooth(true);
		
		//usedHeapMemSeries.setFillColor(new Color(254, 200, 153, 180));
		//usedHeapMemSeries.setLineWidth(3);
		//usedHeapMemSeries.setLineColor(new Color(204, 102, 51));

		agentMemChart.getStyler().setPlotBorderVisible(true);
		agentMemChart.getStyler().setYAxisMin(10.0);

		//configuring the grid lines formatting
		agentMemChart.getStyler().setPlotGridLinesColor(Color.GRAY);
		Stroke plotGridLinesStroke = new BasicStroke(0.4f);
		agentMemChart.getStyler().setPlotGridLinesStroke(plotGridLinesStroke);
		
		
		return agentMemChart;
	}

	public XYChart getAgentCpuChart() {
		//AgentCurrHeapUsage_Util initialResourceDetails = new AgentCurrHeapUsage_Util();
		cpuUsageData = initialResourceDetails.currCpuUsage;
		agentRunTime = initialResourceDetails.currAgentRunTime;

		agentCpuChart = new XYChartBuilder()
				.width(500)
				.height(400)
				.yAxisTitle("Usage in %")
				.xAxisTitle("Time")
				.theme(ChartTheme.Matlab)
				.title("Agent CPU Monitor")
				.build();

		agentCpuChart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Line);
		agentCpuChart.getStyler().setDatePattern("dd/MMM - HH:mm:ss");
		agentCpuChart.getStyler().setYAxisMax(100.0);
		agentCpuChart.getStyler().setYAxisMin(0.0);
		//agentCpuChart.getStyler().setYAxisDecimalPattern("## %");

		agentCpuChart.getStyler().setLegendPosition(LegendPosition.OutsideS);
		agentCpuChart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);
		agentCpuChart.getStyler().setLegendVisible(true);

		XYSeries usedCpuSeries;

		usedCpuSeries = agentCpuChart.addSeries(Ser_CpuUsage, agentRunTime, cpuUsageData);
		usedCpuSeries.setLineWidth(2);
		usedCpuSeries.setLineColor(new Color(241, 154, 42));
		usedCpuSeries.setMarker(SeriesMarkers.NONE);

		agentCpuChart.getStyler().setPlotBorderVisible(true);
		//configuring the grid lines formatting
		agentCpuChart.getStyler().setPlotGridLinesColor(Color.LIGHT_GRAY);
		//agentCpuChart.getStyler().setPlotBackgroundColor(Color.darkGray);
		Stroke plotGridLinesStroke = new BasicStroke(0.4f);
		agentCpuChart.getStyler().setPlotGridLinesStroke(plotGridLinesStroke);
		
		//agentCpuChart.getStyler().setToolTipsEnabled(false);
		agentCpuChart.getStyler().setCursorEnabled(true);
		agentCpuChart.getStyler().setCursorBackgroundColor(Color.darkGray);
		agentCpuChart.getStyler().setCursorColor(Color.DARK_GRAY);
		agentCpuChart.getStyler().setCursorFontColor(Color.WHITE);
		agentCpuChart.getStyler().setCursorFont(cursorFont);

		usedCpuSeries.setSmooth(true);
		return agentCpuChart;
	}

	@Override
	public XYChart getChart() {
		return null;
	}

	public void updateData() {
		//Global declaration of X and Y series datalist of node heap chart
		//private List<Integer> usedMemData, totMemData;

		// Get the current agent usage details
		AgentCurrHeapUsage_Util currAgentRecourceObj = new AgentCurrHeapUsage_Util();
		List<Integer> usedMemDataPoints_updated = currAgentRecourceObj.currUsedMem;
		List<Integer> totMemDataPoints_updated = currAgentRecourceObj.currTotMem;

		List<Integer> totCpuDataPoints_updated = currAgentRecourceObj.currCpuUsage;
		List<Date> agentRunningTime_updated = currAgentRecourceObj.currAgentRunTime;

		usedMemData.addAll(usedMemDataPoints_updated);
		totMemData.addAll(totMemDataPoints_updated);

		cpuUsageData.addAll(totCpuDataPoints_updated);
		agentRunTime.addAll(agentRunningTime_updated);

		//Updating Memory chart
		agentMemChart.updateXYSeries(Ser1_UsedMem, agentRunTime, usedMemData, null);
		agentMemChart.updateXYSeries(Ser2_TotalMem, agentRunTime, totMemData, null);

		//Updating CPU chart
		agentCpuChart.updateXYSeries(Ser_CpuUsage, agentRunTime, cpuUsageData, null);
	}
}

class AgentCurrHeapUsage_Util {

	AgentCalls agentCalls = new AgentCalls();

	int[] agentResourceOutput = agentCalls.getAgentRT_Info();

	List<Integer> currUsedMem, currTotMem, currCpuUsage;
	List<Date> currAgentRunTime = new ArrayList<>();

	AgentCurrHeapUsage_Util() {
		try {
			Date currDateTime = new Date();
			currUsedMem = new CopyOnWriteArrayList<Integer>();
			currTotMem = new CopyOnWriteArrayList<Integer>();

			currCpuUsage = new CopyOnWriteArrayList<Integer>();

			//System.out.println("currUsedHeap:" + agentResourceOutput[7] + "\n");
			currUsedMem.add(agentResourceOutput[7]);
			//System.out.println("currTotHeap:" + agentResourceOutput[2] + "\n");
			currTotMem.add(agentResourceOutput[2]);
			//System.out.println("currCpuUsage:" + agentResourceOutput[1] + "\n");
			currCpuUsage.add(agentResourceOutput[1]);
			currAgentRunTime.add(currDateTime);

			int seconds = (int) (agentResourceOutput[0] / 1000) % 60;
			int minutes = (int) ((agentResourceOutput[0] / (1000 * 60)) % 60);
			int hours = (int) ((agentResourceOutput[0] / (1000 * 60 * 60)) % 24);

			String formattedUpTime;
			if (hours <= 0) {
				formattedUpTime = "<html><b>Up since:</b> " + minutes + " min " + seconds + " sec</html>";
			} else {
				formattedUpTime = "<html><b>Up since:</b> " + hours + " Hrs " + minutes + " min " + seconds + " sec</html>";
			}

			AgentRecourceMon_IMPL.agentResourceInfoLbl_Source = "<html><b>BW Agent process ID:</b> " + agentResourceOutput[4]
					+ ". <b>Active thread count:</b> " + agentResourceOutput[3] + ". <b>CPU used:</b> " + agentResourceOutput[1]
					+ "%. <b>System Memory used:</b> " + agentResourceOutput[6] + "%</html>";

			AgentRecourceMon_IMPL.agentResourceUpTimeLbl_Source = formattedUpTime;

//				agentUsageDetails[0] = obj_agentJSONArray.getInt("upSince");
//				agentUsageDetails[1] = obj_agentJSONArray.getInt("percentCpuUsed");
//				agentUsageDetails[2] = obj_agentJSONArray.getInt("totalMemoryInBytes") / toMBs;
//				agentUsageDetails[3] = obj_agentJSONArray.getInt("activeThreadCount");
//				agentUsageDetails[4] = obj_agentJSONArray.getInt("systemProcessId");
//				agentUsageDetails[5] = obj_agentJSONArray.getInt("freeMemoryInBytes") / toMBs;
//				agentUsageDetails[6] = obj_agentJSONArray.getInt("percentMemoryUsed");
//				agentUsageDetails[7] = obj_agentJSONArray.getInt("usedMemoryInBytes") / toMBs;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
