package yukaritwiread.twitter;

import java.io.File;
import java.util.ArrayList;
import java.net.URL;
import java.awt.image.BufferedImage;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.Graphics;
import java.awt.MediaTracker;
import java.awt.event.WindowEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.imageio.ImageIO;
import twitter4j.ResponseList;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.UserStreamAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.MediaEntity;


public class YukariTimeLine 
{
private TwitterStream ts;
private java.awt.DisplayMode dm = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
private YukariFrame pop = new YukariFrame(dm.getWidth()*3/4,dm.getHeight()*3/4,dm.getWidth()/3,dm.getHeight()/4); 
private UserStreamAdapter uad=new UserStreamAdapter()
	{
@Override 
public void onException(Exception e)
		{
e.printStackTrace();
		}
@Override
public void onStatus(Status st)
		{
try
			{
String str =removeURL(st.getUser().getName()+"。"+st.getText());
String fs = System.getProperty("file.separator");
Process p =new ProcessBuilder((new File(new File(System.getProperty("java.class.path")).getAbsolutePath()).getParent())+fs+"lib"+fs+"YukaCmd"+fs+"yukaCmd.sh",str).start();
pop.popUpMedia(st);
p.waitFor();
Thread.sleep(2000);
ShowTimeLine.show();
Main.commandPrint();
			}
catch(Exception e)
			{
e.printStackTrace();
			}
		}
	};
public YukariTimeLine() throws TwitterException
	{
try
		{
ts = new TwitterStreamFactory().getInstance();
ts.setOAuthAccessToken(Main.getTwitter().getOAuthAccessToken());
ts.addListener(uad);
ts.user();
		}
catch(Exception e)
		{
e.printStackTrace();
		}
	}
public void end()
	{
ts.clearListeners();
System.out.println("end");
System.exit(0);
	}
public static String removeURL(String str)
	{
String ret=str;
int startind=-1;
do
		{
if(ret.indexOf("http://")>=0)
			{
startind=ret.indexOf("http://");
			}
else if(ret.indexOf("https://")>=0)
			{
startind=ret.indexOf("https://");
			}
else
			{
startind=-1;
			}
if(startind>=0)
			{
int endind=startind;
while((endind<ret.length())&&(ret.charAt(endind)<128)&&(ret.charAt(endind)!=' ')&&(ret.charAt(endind)!='\n'))
				{
endind++;
				}
if(endind<0)
				{
endind=ret.length();
				}
ret=ret.substring(0,startind)+ret.substring(endind,ret.length());
			}
		}
while(startind>=0);

return ret;
	}
}
class YukariFrame extends Frame
{
private MediaPanel mp;
private Panel yp;
private BufferedImage yukari;
private ArrayList<MediaImage> bimg= new ArrayList<MediaImage>();
private YukariFrame(){;}
YukariFrame(int x,int y,int width,int height)
	{
addWindowListener(new java.awt.event.WindowAdapter()
		{
@Override
public void windowClosing(WindowEvent e)
			{
setVisible(false);
			}
		});
int mediaWidth=width*2/3;
try
		{
yukari=MediaImage.inFieldImage(ImageIO.read(this.getClass().getResource("yukari.png")),width-mediaWidth,height);
		}
catch(Exception e){;}
mp=new MediaPanel(bimg,width-mediaWidth,0,mediaWidth,height);
yp=new Panel(){
@Override
public void paint(Graphics g)
			{
g.drawImage(yukari,0,0,null);
			}
		};
yp.setBounds(0,0,mediaWidth-width,height);
add(mp);
add(yp);
setBounds(x,y,width,height);
	}
void popUpMedia(Status st)
	{
MediaEntity[] medias = st.getExtendedMediaEntities();
if(medias.length>0)
		{
while(bimg.size()>0)
			{
bimg.remove(0);
			}
int widthBase=mp.getWidth()/medias.length;
MediaTracker mt = new MediaTracker(mp);
for(int i=0;i<medias.length;i++)
			{
bimg.add(new MediaImage(medias[i],i*widthBase,0,widthBase,mp.getHeight()));
mt.addImage(bimg.get(i).getImage(),i);
			}
try
			{
mt.waitForAll();
			}
catch(Exception e){;}
setVisible(true);
mp.repaint();
YukariFrame YF=this;
Thread th = new Thread(){
YukariFrame yf=YF;
public void run()
				{
try
					{
sleep(40000);
yf.setVisible(false);
					}
catch(Exception e){;}
				}
			};
th.start();
		}
	}
}
class MediaPanel extends Panel
{
ArrayList<MediaImage> bimg;
private MediaPanel(){;}
MediaPanel(ArrayList<MediaImage> imgs,int x,int y,int width,int height)
	{
bimg=imgs;
setBounds(x,y,width,height);
addMouseListener(new MouseAdapter()
		{
@Override
public void mouseClicked(MouseEvent e)
			{
try
				{
java.awt.Desktop.getDesktop().browse(bimg.get(e.getX()*bimg.size()/getWidth()).getURL().toURI());
				}
catch(Exception ex){;}
			}
		});
	}
@Override
public void paint(Graphics g)
	{
for(int i=0;i<bimg.size();i++)
		{
g.drawImage(bimg.get(i).getImage(),bimg.get(i).getX(),bimg.get(i).getY(),null);
		}
	}
}
class MediaImage
{
private BufferedImage outimg;
private URL url;
private int X;
private int Y;
private MediaImage(){;}
MediaImage(MediaEntity media,int x,int y,int width,int height)
	{
X=x;
Y=y;
try
		{
url=new URL(media.getMediaURL());
if(media.getType().equals("photo"))
			{
try
				{
outimg=MediaImage.inFieldImage(ImageIO.read(url),width,height);
				}
catch(Exception e)
				{
outimg=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
Graphics g = outimg.getGraphics();
g.setColor(java.awt.Color.black);
g.drawString(url.getPath(),0,height/2);
				}
			}
else
			{
outimg=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
Graphics g =outimg.getGraphics();
g.setColor(java.awt.Color.black);
g.drawString(url.getPath(),0,height/2);
			}
		}
catch(Exception e){;}
	}
int getX()
	{
return X;
	}
int getY()
	{
return Y;
	}
BufferedImage getImage()
	{
return outimg;
	}
URL getURL()
	{
return url;
	}
static BufferedImage inFieldImage(BufferedImage bi,int width,int height)
	{
boolean baseWidth=height>bi.getHeight()*width/bi.getWidth();
if(baseWidth)
			{
/*
横基準倍率
*/
return getNewSize(bi,width,bi.getHeight()*width/bi.getWidth());
			}
else
			{
/*横基準倍率*/
return getNewSize(bi,bi.getWidth()*height/bi.getHeight(),height);
			}
	}
private static BufferedImage getNewSize(BufferedImage bi,int width,int height)
	{
BufferedImage ret = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
double imgpernewimgwidth = (double)bi.getWidth()/(double)ret.getWidth();
double imgpernewimgheight = (double)bi.getHeight()/(double)ret.getHeight();
int bix=0;
int biy=0;

for(int x=0;width>x;x++)
				{
for(int y=0;height>y;y++)
					{
bix=(int)((double)x*imgpernewimgwidth);
if(bix < 0)
						{
bix=0;
						}
else if(bix >= bi.getWidth())
						{
bix=bi.getWidth()-1;	}
biy=(int)((double)y*imgpernewimgheight);
if(biy<0)
						{
biy=0;
						}
else if(biy>=bi.getHeight())
						{
biy=bi.getHeight()-1;
						}
ret.setRGB(x,y,bi.getRGB(bix,biy));
					}
				}
return ret;
	}

}
