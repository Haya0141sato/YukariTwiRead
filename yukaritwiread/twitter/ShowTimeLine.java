package yukaritwiread.twitter;

import java.util.concurrent.ArrayBlockingQueue;
import twitter4j.ResponseList;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.TwitterException;

public class ShowTimeLine
{
public static final int TIMELINE_LENGTH=300;
private static final long Cyclelimit = 15000;
private static boolean waiting=false;
private static long calledTime=System.currentTimeMillis()-Cyclelimit;

private ArrayBlockingQueue<Status> tl;
public ShowTimeLine()
	{
tl=new ArrayBlockingQueue<Status>(TIMELINE_LENGTH);
readTimeLine();
addTimeLine();
try
		{
show();
		}
catch(Exception e)
		{
e.printStackTrace();
		}
	}
private void readTimeLine()
	{
//tlに今までのTLの中身を読み込む
	}
private void addTimeLine()
	{
//今回のログインで新しく取得したTLをtlに追加する。
	}
public static void show() throws TwitterException
	{
if(!waiting)
		{
waiting=true;
try
			{
while(System.currentTimeMillis()<(calledTime+Cyclelimit))
				{
Thread.sleep(3000);
				}
calledTime=System.currentTimeMillis();
			}
catch(Exception e){;}
ResponseList<Status> st = Main.getTwitter().timelines().getHomeTimeline();
for(int i=st.size()-1;i>=0;i--)
			{
System.out.print(st.get(i).getUser().getName()+":「 "+st.get(i).getText());
System.out.println(" 」\t"+st.get(i).getId());
			}
System.out.println("");
waiting=false;
		}
/*
for(Status st:tl)
		{
System.out.print(st.getUser().getName()+":「"+st.getText());
System.out.println("」\tID:"+st.getId());
		}
*/
	}
public void addStatusToTimeLine(Status st)
	{
if(st!=null)
		{
tl.add(st);
		}
	}
}
