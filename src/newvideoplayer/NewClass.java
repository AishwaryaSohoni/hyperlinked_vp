   public void VideoProcessing(String readPath,int startFrame,Boolean addToQueue) {

        try {
            
                    
                    if(fps!=null)
                    {
                        fps.stop();
                    }
			primaryVideo = new File(readPath);
			is = new FileInputStream(primaryVideo);
                        sF=startFrame;
                        if(addToQueue)
                        {
                            Video videoObj=new Video(); 
                            videoObj.videoName=readPath;
                            videoObj.startFrame=startFrame;
                            videoQueue.add(videoObj);
                            videoCount++;
                         }
                        is.skip((long)(3*height*width*startFrame));
		} catch (FileNotFoundException e) {
			System.out.println("File not found!!");
		}
                catch(IOException e){
                    System.out.println("IO Exception!!");
                }
                
		final long len = primaryVideo.length();
		//System.out.println("file.len :" + len);
		
		int numRead = 0;

		labelArray = new JLabel[(int) (primaryVideo.length() / (3 * height * width))];// length of video
																						// total
																						// number
																						// of
																						// frames
		RED_Count = labelArray.length-sF;// RED_COUNT keeps track of frame being
										// displayed
		imageArray = new BufferedImage[(int) (primaryVideo.length() / (3 * height * width))];
		
		LabelIndex = labelArray.length;
		try {
            

            ActionListener actionPerformed;
           
            actionPerformed = new ActionListener() {
                    int numRead = 0;
                    
                    byte[] bytes = new byte[(int) len];

                    // @Override
                    public void actionPerformed(ActionEvent e) {
                            if (RED_Count > 0) {
                                    currentFrame = 720 - RED_Count;
                                    try {
                                            if ((numRead = is
                                                            .read(bytes, 0, 3 * height * width)) >= 0) {
                                                                                       int ind = 0;
                                                    img = new BufferedImage(width, height,
                                                                    BufferedImage.TYPE_INT_RGB);
                                                    for (int y = 0; y < height; y++) {
                                                            for (int x = 0; x < width; x++) {
                                                                    byte a = 0;
                                                                    byte r = bytes[ind];
                                                                    byte g = bytes[ind + height * width];
                                                                    byte b = bytes[ind + height * width * 2];
                                                                    int pix = 0xff000000
                                                                                    | ((r & 0xff) << 16)
                                                                                    | ((g & 0xff) << 8)
                                                                                    | (b & 0xff);
                                                                    img.setRGB(x, y, pix);
                                                                    ind++;
                                                            }
                                                    }

                                                    for (int p = 0; p < metaSet.size(); p++) {

                                                          
                                                            int nextKf=metaSet.get(p).nextKf;
                                                            int prevKf=metaSet.get(p).prevKf;
                                                            calcIncrement(metaSet.get(p).kf.get(nextKf).frameNum,p);
                                                            if (metaSet.get(p).kf.get(prevKf).frameNum <= currentFrame
                                                                            && currentFrame <= metaSet.get(p).kf
                                                                                            .get(nextKf).frameNum) {
                                                           
                                                                    
                                                                    interpolateArea(currentFrame, p);
                                                            } else {
                                                                    DisplayLabel.setCursor(Cursor
                                                                                    .getDefaultCursor());
                                                            }
                                                            
                                                            
                                                            
                                                    }
                                                    //Change the next and current keyframe index
                                                    for(int p=0;p<metaSet.size();p++)
                                                    {
                                                        int nextKf=metaSet.get(p).nextKf;
                                                        if(currentFrame==metaSet.get(p).kf.get(nextKf).frameNum)
                                                        {
                                                            
                                                            if(metaSet.get(p).nextKf<metaSet.get(p).numOfKeyFrames-1)
                                                            {
                                                                metaSet.get(p).nextKf++;
                                                            }
                                                            metaSet.get(p).prevKf++;
                                                        }
                                                    }

                                                    DisplayLabel.setIcon(new ImageIcon(img));
                                                    RED_Count--;
                                            }
                                    } catch (IOException ex) {
                                            Logger.getLogger(playerForm.class.getName()).log(
                                                            Level.SEVERE, null, ex);
                                    }
                            } else {
                                    // all frames displayed,now loop the video
                                    for(int p=0;p<metaSet.size();p++)
                                    {
                                        metaSet.get(p).prevKf=0;
                                        metaSet.get(p).nextKf=1;
                                    }RED_Count = labelArray.length-sF;
                                    try {
                                            is = new FileInputStream(primaryVideo);
                                    try {
                                        is.skip((long)(3*height*width*sF));
                                    } catch (IOException ex) {
                                        Logger.getLogger(playerForm.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    } catch (FileNotFoundException ex) {
                                            Logger.getLogger(playerForm.class.getName()).log(
                                                            Level.SEVERE, null, ex);
                                    }
                            }
                    }
            };
            ps=new PlaySound(readPath, startFrame);
                fps = new Timer(DELAY, actionPerformed);
			fps.start();
                ps.start();
		} catch (Exception e) {
		}
	}

	public void interpolateArea(int FrameNumber, int p) {

		int currTopLeftX = 0;
		int currTopLeftY = 0;
		int currBotRightX = 0;
		int currBotRightY = 0;
                int nextKf=metaSet.get(p).nextKf;
                int prevKf=metaSet.get(p).prevKf;

		currTopLeftX = (int)((FrameNumber - metaSet.get(p).kf.get(prevKf).frameNum) * topLeftXInc)
				+ metaSet.get(p).kf.get(prevKf).topLeftX;
		currTopLeftY = (int)((FrameNumber - metaSet.get(p).kf.get(prevKf).frameNum) * topLeftYInc)
				+ metaSet.get(p).kf.get(prevKf).topLeftY;
		currBotRightX = (int)((FrameNumber - metaSet.get(p).kf.get(prevKf).frameNum) * botRightXInc)
				+ metaSet.get(p).kf.get(prevKf).bottomRightX;
		currBotRightY = (int)((FrameNumber - metaSet.get(p).kf.get(prevKf).frameNum) * botRightYInc)
				+ metaSet.get(p).kf.get(prevKf).bottomRightY;

                                
                Graphics g=getGraphics();
                g.setColor(Color.red);
                
                g.drawRect(currTopLeftX, currTopLeftY,Math.abs(currBotRightX-currTopLeftX) ,Math.abs(currBotRightY-currTopLeftY));

		
		Point mousePoint = MouseInfo.getPointerInfo().getLocation();
                SwingUtilities.convertPointFromScreen(mousePoint,DisplayLabel);
		if (currTopLeftX <= (int)(mousePoint.getX())
				&& (int)(mousePoint.getX())<= currBotRightX) {
			if (currTopLeftY <=(int)(mousePoint.getY())
					&& (int)(mousePoint.getY())<= currBotRightY)
                        {
                            DisplayLabel.setCursor(Cursor
						.getPredefinedCursor(Cursor.HAND_CURSOR));
                            
                        }
		} else
			DisplayLabel.setCursor(Cursor.getDefaultCursor());
	}

public void calcIncrement(int endFrame, int p) {

                float frameDiff=endFrame - metaSet.get(p).kf.get(metaSet.get(p).prevKf).frameNum;
		if ((frameDiff) != 0) {
                    
			topLeftXInc = (metaSet.get(p).kf.get(metaSet.get(p).nextKf).topLeftX - metaSet
					.get(p).kf.get(metaSet.get(p).prevKf).topLeftX)
					/ (frameDiff);
			topLeftYInc = (metaSet.get(p).kf.get(metaSet.get(p).nextKf).topLeftY - metaSet
					.get(p).kf.get(metaSet.get(p).prevKf).topLeftY)
					/ (frameDiff);

			botRightXInc = (metaSet.get(p).kf.get(metaSet.get(p).nextKf).bottomRightX - metaSet
					.get(p).kf.get(metaSet.get(p).prevKf).bottomRightX)
					/ (frameDiff);

			botRightYInc = (metaSet.get(p).kf.get(metaSet.get(p).nextKf).bottomRightY - metaSet
					.get(p).kf.get(metaSet.get(p).prevKf).bottomRightY)
					/ (frameDiff);
		}
	}
