package yukaritwiread.twitter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;

public class TweetAction
{
public static void tweet(BufferedReader br) throws TwitterException
	{
System.out.println("ツイートする(文末に;を入れること。改行直前に;を入れたい場合はそこだけ\\;と入力すること。)");
String str=readString(br);
if(str!=null)
		{
Main.getTwitter().updateStatus(str);
System.out.println("tweeted!");
		}
else
		{
System.out.println("文字数が長過ぎます！");
		}
	}
public static void reply(BufferedReader br) throws TwitterException
	{
System.out.println("リプライを送る。(文末に;を入れること。改行直前に;を入れたい場合はそこだけ\\;と入力すること。)");
System.out.println("送り先のIDを入れて下さい。(cでキャンセル)");
boolean conti=true;
while(conti)
		{
String idstr=null;
try
			{
idstr = br.readLine();
			}
catch(Exception e)
			{
idstr="c";
			}
if(idstr.equals("c"))
			{
conti=false;
			}
else
			{
String str=readString(br);
try
				{
if(str!=null)
					{
long id=Long.parseLong(idstr);
StatusUpdate sd=new StatusUpdate(str);
sd.setInReplyToStatusId(id);
Main.getTwitter().updateStatus(sd);
conti=false;
					}
				}
catch(Exception e)
				{;}
			}
		}
	}
private static String readString(BufferedReader br)
	{
StringBuffer sb = new StringBuffer("");
boolean conti=true;
try
		{
while(conti)
			{
sb.append(br.readLine());
conti=!((sb.length()>1)&&(sb.charAt(sb.length()-1)==';')&&(sb.charAt(sb.length()-2)!='\\'));
if(conti)
				{
if(sb.charAt(sb.length()-1)==';')
					{
sb.deleteCharAt(sb.length()-2);
					}
sb.append("\n");
				}
			}
if(sb.length()<=141)
			{
return sb.substring(0,sb.length()-1);
			}
		}
catch(Exception e)
		{;}
return null;
	}

}
