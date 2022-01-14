/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pw.sherlock.utils;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

/**
 *
 * @author pwankhed
 * This class provides the smooth tab UI corners for tabs that will be added under agent and node monitor JTabbedPane.
 */

public class CustomTabbedPaneUI_V1 extends BasicTabbedPaneUI {

	@Override
	protected void paintTab(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect) {
		super.paintTab(g, tabPlacement, rects, tabIndex, iconRect, textRect);
		Font f = g.getFont();
		g.setFont(new Font("Courier", Font.BOLD, 10));
		FontMetrics fm = g.getFontMetrics(g.getFont());
		g.setFont(f);		
	}
}