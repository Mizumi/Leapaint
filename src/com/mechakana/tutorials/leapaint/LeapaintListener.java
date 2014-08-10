//File: LeapaintListener.java
//Project: Leapaint
//Date: June 23rd, 2014
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

import com.leapmotion.leap.*;

//Class: LeapaintListener//////////////////////////////////////////////////////////
public class LeapaintListener extends Listener
{    
//Public///////////////////////////////////////////////////////////////////////

	//Leapaint instance.
	public Leapaint paint;
	
	//Frame.
	public Frame frame;
	
	//Leap interaction box.
	private InteractionBox normalizedBox;
	
	//Constructor//////////////////////////////////////////////////////////////
	public LeapaintListener(Leapaint newPaint)
	{
		//Assign the Leapaint instance.
		paint = newPaint;
	}
	
	//Member Function: onInit//////////////////////////////////////////////////
    //
	//Called when the LeapaintListener class is first initialized.
	//
	public void onInit(Controller controller) 
    {
        System.out.println("Initialized");
    }

	//Member Function: onConnect///////////////////////////////////////////////
	//
	//Called when the LeapaintListener class is connected to a Leap Motion Controller.
	//
    public void onConnect(Controller controller) 
    {
        System.out.println("Connected");
    }

    //Member Function: onDisconnect////////////////////////////////////////////
    //
    //Called when the LeapaintListener class is disconnected from a Leap Motion Controller.
    //
    public void onDisconnect(Controller controller) 
    {
        System.out.println("Disconnected");
    }

    //Member Function: onExit//////////////////////////////////////////////////
    //
    //Called when the LeapaintListener class is destroyed.
    //
    public void onExit(Controller controller) 
    {
        System.out.println("Exited");
    }

    //Member Function: onFrame/////////////////////////////////////////////////
    //
    //Called when a new frame is received from the Leap Motion Controller.
    //
    public void onFrame(Controller controller) 
    {
        //Get the most recent frame.
        frame = controller.frame();
        
        //Detect if fingers are present.
        if (!frame.fingers().isEmpty())
        {
        	//Retrieve the front-most finger.
        	Finger frontMost = frame.fingers().frontmost();
        	
        	//Set up its position.
        	Vector position = new Vector(-1, -1, -1);
        	
        	//Retrieve an interaction box so we can normalize the Leap's coordinates to match screen size.
        	normalizedBox = frame.interactionBox();
        	
        	//Retrieve normalized finger coordinates.
        	position.setX(normalizedBox.normalizePoint(frontMost.tipPosition()).getX());
        	position.setY(normalizedBox.normalizePoint(frontMost.tipPosition()).getY());
        	position.setZ(normalizedBox.normalizePoint(frontMost.tipPosition()).getZ());
        	
        	//Scale coordinates to the resolution of the painter window.
        	position.setX(position.getX() * paint.getBounds().width);
			position.setY(position.getY() * paint.getBounds().height);
			
			//Flip Y axis so that up is actually up, and not down.
			position.setY(position.getY() * -1);
			position.setY(position.getY() + paint.getBounds().height);
			
			//Pass the X/Y coordinates to the painter.
			paint.prevX = paint.x;
			paint.prevY = paint.y;
			paint.x = (int) position.getX();
			paint.y = (int) position.getY();
			paint.z = position.getZ();
			
			//Tell the painter to update.
			paint.paintPanel.repaint();
			
			//Check if the user is hovering over any buttons.
			if (paint.button1.getBigBounds().contains((int) position.getX(), (int) position.getY()))
				paint.button1.expand();
			
			else paint.button1.canExpand = false;

			if (paint.button2.getBigBounds().contains((int) position.getX(), (int) position.getY()))
				paint.button2.expand();
			
			else paint.button2.canExpand = false;
			
			if (paint.button3.getBigBounds().contains((int) position.getX(), (int) position.getY()))
				paint.button3.expand();
			
			else paint.button3.canExpand = false;
			
			if (paint.button4.getBigBounds().contains((int) position.getX(), (int) position.getY())) 
				paint.button4.expand();
			
			else paint.button4.canExpand = false;
        }
    }
}