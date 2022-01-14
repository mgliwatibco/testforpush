/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homework;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author pwankhed
 */
public class OOMThreadIssue {
	
	public static void main(String args[])
	{
		System.out.println("Test started...");
		long i =0;
	while (true) {
		i++;
		System.out.println("Current iteration:" + i);
  new Thread(new Runnable() {
	  @Override
	  public void run() {
		  try {
			  TimeUnit.HOURS.sleep(1);
		  } catch (InterruptedException e) {
			  e.printStackTrace();
		  } }
  }).start();
}}
}
