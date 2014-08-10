//File: LeapButton.java
//Project: Leapaint
//Date: June 21, 2014
//
//Author: Brandon Sanders <brandon@mechakana.com>
//
///////////////////////////////////////////////////////////////////////////////
//Copyright (c) 2014 Brandon Sanders <brandon@mechakana.com>
/*
This software is provided 'as-is', without any express or implied
warranty. In no event will the authors be held liable for any damages
arising from the use of this software.

Permission is granted to anyone to use this software for any purpose,
including commercial applications, and to alter it and redistribute it
freely, subject to the following restrictions:

    1. The origin of this software must not be misrepresented; you must not
    claim that you wrote the original software. If you use this software
    in a product, an acknowledgment in the product documentation would be
    appreciated but is not required.

    2. Altered source versions must be plainly marked as such, and must not be
    misrepresented as being the original software.

    3. This notice may not be removed or altered from any source
    distribution.
*/
///////////////////////////////////////////////////////////////////////////////
//
package com.mechakana.tutorials.leapaint;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JButton;

//Class: LeapButton////////////////////////////////////////////////////////////
/**
 * Creates a JButton with added support for reacting to touch input via a Leap
 * Motion device.
 * 
 * This implementation must be triggered manually via a Listener class or similar
 * by calling expand().
 */
@SuppressWarnings("serial")
public class LeapButton extends JButton
{
//Private//////////////////////////////////////////////////////////////////////
	
	//Expanding state of the button.
	private boolean expanding = false;
	
	//Original button size.
	private int originalSizeX, originalSizeY;
	
	//Button expansion multiplier; defaults to 1.5 times as large.
	private double expansionMultiplier;
	
//Public///////////////////////////////////////////////////////////////////////
	
	//Allow expansion?
	public boolean canExpand = false;
	
	//Constructor//////////////////////////////////////////////////////////////
	LeapButton(String label, double expansionMultiplier)
	{
		//Call super.
		super(label);
		
		//Assign values.
		this.expansionMultiplier = expansionMultiplier;
	}
	
	//Member Function: getBigBounds////////////////////////////////////////////
	/**
	 * Returns an exaggerated set of rectangular coordinates for this button,
	 * making it easier to trigger.
	 */
	public Rectangle getBigBounds()
	{
		//Retrieve original bounds.
		Rectangle rect = getBounds();
		
		//Increase height and width of the button.
		rect.width = rect.width + 30;
		rect.height = rect.height + 30;
		
		//Reposition the button so that its central coordinates remain the same.
		rect.x = rect.x - 15;
		rect.y = rect.y - 15;
		
		return rect;
	}
	
	//Member Function: expand//////////////////////////////////////////////////
	/**
	 * Causes this button to begin expanding, triggering its callback once it
	 * reaches full size.
	 * 
	 * This example starts an anonymous thread in order to prevent stalling the
	 * main execution thread while the button expands; there's probably
	 * a very fancy way to do this synchronously, but this is more than
	 * sufficient for an example!
	 */
	public void expand()
	{
		//Don't do anything if this button is currently in the process of expanding.
		if (!expanding)
		{
			//Begin expanding.
			canExpand = true;
			expanding = true;
			
			//Create an anonymous inner thread, so as not to stop the main execution thread.
			(new Thread() 
			{
				public void run() 
				{
					//Change the button's color to green.
					Color originalColor = getBackground();
					setBackground(Color.green); 
					
					//Initialize size.
					originalSizeX = getPreferredSize().width;
					originalSizeY = getPreferredSize().height;
					
					//Calculate target sizes.
					int targetSizeX = (int) (originalSizeX * expansionMultiplier);
					int targetSizeY = (int) (originalSizeY * expansionMultiplier);
					
					//Calculate the amount to increase button size by in each loop.
					int stepX = (targetSizeX - originalSizeX) / 10;
					int stepY = (targetSizeY - originalSizeY) / 10;
					
					while (canExpand && getPreferredSize().width < targetSizeX)
					{
						//Adjust button size.
						setPreferredSize(new Dimension(getPreferredSize().width + stepX, getPreferredSize().height + stepY));
						
						//Repaint.
						revalidate();
						
						//Wait a moment.
						try { Thread.sleep(75); }
						catch (Exception e) { }
					}
					
					//Trigger action if current size meets or exceeds the target.
					if (getPreferredSize().width >= targetSizeX) doClick();
					
					//Otherwise, revalidate the button to make sure everything renders (normally doClick handles this).
					else revalidate();
					
					//Reset the size of the button to its original dimensions.
					setPreferredSize(new Dimension(originalSizeX, originalSizeY));
					
					//Revalidate the button again to re-render it.
					revalidate();
					
					//No longer expanding.
					expanding = false;
					
					//Restore original button color.
					setBackground(originalColor); 
				}
			}).start();
		}
	}
}
