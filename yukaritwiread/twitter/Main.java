package yukaritwiread.twitter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.StatusUpdate;

public class Main
{
private static Twitter twitter;
private static boolean nowComMode=true;
public static void main(String[] args) throws TwitterException
	{
System.out.println("このプログラムはApacheLicense2.0でライセンスされているライブラリtwitter4jを使用しています。ライセンス本文はLICENSE/twitter4j/LICENSE.txtを参照して下さい。");
System.out.println("このプログラムはMITライセンスでライセンスされているプログラムYukaCmdを使用しています。ライセンス本文はLICENSE/YukaCmdを参照して下さい。");
String userID=null;
if(args.length>0)
		{
userID=args[0];
		}
twitter = YukariTwiReadAuthorize.authorization(userID);
User user = twitter.verifyCredentials();
System.out.println("name:"+user.getName());
System.out.println("screenName:"+user.getScreenName());
System.out.println("follow:"+user.getFriendsCount());
System.out.println("follower:"+user.getFollowersCount());
String inputStr="fast";
YukariTimeLine ytl = new YukariTimeLine();
try
		{
Thread.sleep(1000);
		}
catch(Exception e){;}
BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
while(!inputStr.equals("q"))
		{
inputCommand(br,inputStr);
try
			{
nowComMode=true;
commandPrint();
inputStr=br.readLine();
			}
catch(Exception e)
			{
e.printStackTrace();
			}
nowComMode=false;
		}
ytl.end();
	}
public static Twitter getTwitter()
	{
return twitter;
	}
public static void inputCommand(BufferedReader br,String input)
	{
try
		{
switch(input)
			{
case "t":
TweetAction.tweet(br);
break;
case "re":
TweetAction.reply(br);
break;
default:
ShowTimeLine.show();
break;
			}
		}
catch(Exception e)
		{
e.printStackTrace();
		}
	}
public static void commandPrint()
	{
if(nowComMode)
		{
System.out.print("\ncommand(tでツイート、reでリプライ、それ以外でTLを見る、qを押す場合やめる):");
		}
	}
}
