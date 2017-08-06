package yukaritwiread.twitter;

import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.InputStreamReader;

import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.PropertyConfiguration;
import twitter4j.conf.ConfigurationBuilder;

public class YukariTwiReadAuthorize
{
public static final String consumerKey="FlrIAfNsbsme2pDaBesdBrAIG";
public static final String consumerSecret="hLPQjdLeFzYcrkw2w9ag67kppSfGpYxyrRjBNKEZbiCoyVkIOF";
private static String authorizeKeyBase = System.getProperty("user.home")+File.separator+".yukaritwireadrc";
private static File authorizeDir=new File(authorizeKeyBase);
private static final File defaultID=new File(authorizeDir.getAbsolutePath()+File.separator+"defaultID");

public static Twitter authorization() throws TwitterException
	{
return authorization("");
	}
public static Twitter authorization(String userID) throws TwitterException
	{
System.setProperty("twitter4j.oauth.consumerKey",consumerKey);
System.setProperty("twitter4j.oauth.consumerSecret",consumerSecret);
Twitter ret = new TwitterFactory().getInstance();
if(!authorizeDir.exists())
		{
authorizeDir.mkdirs();
		}
File authorizeKey = accessTokenFromName(userID);
if(authorizeKey.exists())
		{
ret.setOAuthAccessToken(readAccessToken(authorizeKey));
		}
else
		{
AccessToken act= getAccessToken();
ret.setOAuthAccessToken(act);
String name=ret.verifyCredentials().getScreenName();
writeAccessToken(name,act);
if(!defaultID.exists())
			{
writeDefaultName(name);
			}
		}
return ret;
	}
private static File accessTokenFromName(String name)
	{
if((name==null) || (name.length()<=0))
		{
String notFoundName=readDefaultName();
if(notFoundName!=null)
			{
name="."+notFoundName;
			}
else
			{
name="notFound";
			}
		}
else
		{
name="."+name;
		}
return new File(authorizeDir.getAbsolutePath()+File.separator+name);
	}
private static void writeDefaultName(String userID)
	{
try(BufferedWriter bw = new BufferedWriter(new FileWriter(defaultID)))
		{
bw.write(userID);
		}
catch (Exception e)
		{
e.printStackTrace();
		}
	}
private static String readDefaultName()
	{
try
		{
return (new BufferedReader(new FileReader(defaultID))).readLine();
		}
catch (Exception e)
		{;}
return null;
	}
private static AccessToken readAccessToken(File authorizeKey)
	{
try(BufferedReader br = new BufferedReader(new FileReader(authorizeKey)))
			{
String accessToken=br.readLine();
String accessTokenSecret=br.readLine();
return new AccessToken(accessToken,accessTokenSecret);
			}
catch(Exception e)
			{;}
return null;
	}
private static void writeAccessToken(String name,AccessToken act)
	{
File authorizeKey=accessTokenFromName(name);
try(BufferedWriter bw = new BufferedWriter(new FileWriter(authorizeKey)))
		{
bw.write(act.getToken());
bw.newLine();
bw.write(act.getTokenSecret());
		}
catch(Exception e)
		{
e.printStackTrace();
System.exit(1);
		}
	}
private static AccessToken getAccessToken() throws TwitterException
	{
Twitter twitter = TwitterFactory.getSingleton();
RequestToken requestToken= twitter.getOAuthRequestToken();
AccessToken act = null;
BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
while(act == null)
		{
System.out.println("以下のURLにアクセスし,そこで取得したPINコードを入力して下さい");
System.out.println(requestToken.getAuthorizationURL());
System.out.print("PIN:");
try
			{
String pin = br.readLine();
if(pin.length()>0)
				{
act=twitter.getOAuthAccessToken(requestToken,pin);
				}
else
				{
act=twitter.getOAuthAccessToken();
				}
			}
catch(TwitterException e)
			{
System.out.println("Error("+e.getStatusCode()+"):認証に失敗しました。");
System.exit(0);
			}
catch(Exception e)
			{
System.exit(0);
			}
		}
return act;
	}
}
