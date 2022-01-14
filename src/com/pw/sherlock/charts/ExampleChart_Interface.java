/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pw.sherlock.charts;

import org.knowm.xchart.internal.chartpart.Chart;
/**
 *
 * @author pwankhed
 */
public interface ExampleChart_Interface <C extends Chart<?, ?>> {

  C getChart();
}
