/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pw.sherlock.utils;

import com.pw.sherlock.UI.Home;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

/**
 *
 * @author pwankhed
 */
public class CustomTabbedPaneUI_V2 extends BasicTabbedPaneUI {
	
//class CustomTabbedPaneUI extends MetalTabbedPaneUI {
//class CustomTabbedPaneUI extends BasicTabbedPaneUI {
	Rectangle xRect;

	@Override
	protected void installListeners() {
		super.installListeners();
		tabPane.addMouseListener(new MyMouseHandler());
	}

	@Override
	protected void paintTab(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect) {
		super.paintTab(g, tabPlacement, rects, tabIndex, iconRect, textRect);
		Font f = g.getFont();
		g.setFont(new Font("Courier", Font.BOLD, 10));
		FontMetrics fm = g.getFontMetrics(g.getFont());
		int charWidth = fm.charWidth('x');
		int maxAscent = fm.getMaxAscent();
		g.drawString("x", textRect.x + textRect.width - 3, textRect.y + textRect.height - 3);
		g.drawRect(textRect.x + textRect.width - 5,
				textRect.y + textRect.height - maxAscent, charWidth + 2, maxAscent - 1);
		xRect = new Rectangle(textRect.x + textRect.width - 5,
				textRect.y + textRect.height - maxAscent, charWidth + 2, maxAscent - 1);
		g.setFont(f);		
	}

	public class MyMouseHandler extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			//System.out.println(e);
			if (xRect.contains(e.getPoint())) {
				JTabbedPane tabPane = (JTabbedPane) e.getSource();
				if (tabPane.getTitleAt(tabForCoordinate(tabPane, e.getX(), e.getY())).equals(Home.master_agentName + " (Agent)    ")) {
					JOptionPane.showMessageDialog(null, "BW Agent monitor is primary tab hence cannot be closed.", "Waning", JOptionPane.WARNING_MESSAGE);
				} else {
					int dialogResult = JOptionPane.showConfirmDialog(null, "Closing this tab will reset the statistics/monitoring done so far, "
							+ "are you sure you want to close this tab?", "Warning", JOptionPane.YES_NO_CANCEL_OPTION);
					if (dialogResult == JOptionPane.YES_OPTION) {
						tabPane.remove(tabForCoordinate(tabPane, e.getX(), e.getY()));
					}
				}
			}
		}
	}
}