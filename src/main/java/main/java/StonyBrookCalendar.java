package main.java;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class StonyBrookCalendar {
	
	static Scanner userInput = new Scanner(System.in);
	static WebDriver driver = new HtmlUnitDriver(true);
	static List<StonyBrookClass> schedule = new ArrayList<>();
	static JavascriptExecutor je = (JavascriptExecutor) driver;
	
	
    private static final String APPLICATION_NAME = "Stony Brook Schedule to Google Calendar";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static final String LOGIN_FILE_PATH = "/login.json";

    private static boolean login() throws Exception {
    	InputStream login = StonyBrookCalendar.class.getResourceAsStream(LOGIN_FILE_PATH);
    	
    	LoginInfo loginInfo = LoginInfo.load(JSON_FACTORY,new InputStreamReader(login));
    	String username = loginInfo.netid;
    	String password = loginInfo.password;
    	System.out.println(username);
    	System.out.println("PW: " + password);
		String loginPage = "https://psns.cc.stonybrook.edu/psp/csprods/EMPLOYEE/CAMP/h/?tab=DEFAULT&cmd=login&";
		driver.get(loginPage);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		WebElement user_we = driver.findElement(By.xpath("//*[@id=\"userid\"]"));
		WebElement password_we = driver.findElement(By.xpath("//*[@id=\"pwd\"]"));
		System.out.println(user_we.toString());
		System.out.println(password_we.toString());
		user_we.click();
		user_we.clear();
		user_we.sendKeys(username);
		password_we.click();
		password_we.clear();
		password_we.sendKeys(password);
		System.out.println("Input Sent");
		je.executeScript("submitAction(document.login);");
		if (driver
				.getCurrentUrl() == "https://psns.cc.stonybrook.edu/psp/csprods/EMPLOYEE/CAMP/h/?tab=DEFAULT&cmd=login&errorCode=105&languageCd=ENG")
			return false; // login failed
		return true;
	}

	public static void generateSchedule() {
		driver.get(
				"https://psns.cc.stonybrook.edu/psc/csprods/EMPLOYEE/CAMP/c/SA_LEARNER_SERVICES.SSR_SSENRL_SCHD_W.GBL?PORTALPARAM_PTCNAV=HC_SSR_SSENRL_SCHD_W_GBL&EOPP.SCNode=CAMP&EOPP.SCPortal=EMPLOYEE&EOPP.SCName=ADMN_SOLAR_SYSTEM&EOPP.SCLabel=Enrollment&EOPP.SCFName=HCCC_ENROLLMENT&EOPP.SCSecondary=true&EOPP.SCPTcname=PT_PTPP_SCFNAV_BASEPAGE_SCR&FolderPath=PORTAL_ROOT_OBJECT.CO_EMPLOYEE_SELF_SERVICE.SU_STUDENT_FOLDER.HCCC_ENROLLMENT.HC_SSR_SSENRL_SCHD_W_GBL&IsFolder=false&PortalActualURL=https%3a%2f%2fpsns.cc.stonybrook.edu%2fpsc%2fcsprods%2fEMPLOYEE%2fCAMP%2fc%2fSA_LEARNER_SERVICES.SSR_SSENRL_SCHD_W.GBL&PortalContentURL=https%3a%2f%2fpsns.cc.stonybrook.edu%2fpsc%2fcsprods%2fEMPLOYEE%2fCAMP%2fc%2fSA_LEARNER_SERVICES.SSR_SSENRL_SCHD_W.GBL&PortalContentProvider=CAMP&PortalCRefLabel=My%20Weekly%20Schedule&PortalRegistryName=EMPLOYEE&PortalServletURI=https%3a%2f%2fpsns.cc.stonybrook.edu%2fpsp%2fcsprods%2f&PortalURI=https%3a%2f%2fpsns.cc.stonybrook.edu%2fpsc%2fcsprods%2f&PortalHostNode=CAMP&NoCrumbs=yes&PortalKeyStruct=yes");
		WebElement table = driver.findElement(By.xpath("//*[@id=\"WEEKLY_SCHED_HTMLAREA\"]"));
		List<WebElement> rows = table.findElements(By.tagName("tr"));
		WebElement week = driver.findElement(By.xpath("/html/body/form/div[5]/table/tbody/tr/td/div/table/tbody/tr[8]/td[2]/div/table/tbody/tr/td/table/tbody/tr[3]/td[3]/div/table/tbody/tr[1]/td/table/tbody/tr/td/table/tbody/tr/td"));
		
		String weekOf = week.getText().split(" ")[2];
		StonyBrookClass.weekStart = weekOf;
		StonyBrookClass.setEndString();
		System.out.println("+=========================="+StonyBrookClass.GetendString());
		
		
		
		System.out.println(weekOf);
		// Week of 1/28/2019 - 2/3/2019
		
		
		
		for (int i = 0; i < 16; i++) {
			List<WebElement> cellsInRow = rows.get(i).findElements(By.cssSelector("*"));
			cellsInRow.removeIf(f -> f.getAttribute("class") == null || !f.getAttribute("class").equals("SSSTEXTWEEKLY")
					&& !f.getAttribute("class").equals("PSLEVEL3GRID"));
			System.out.println(cellsInRow);
			int count = 0;
			for (WebElement e : cellsInRow) {
				if (e.getAttribute("class").equals("SSSTEXTWEEKLY")) {
					String text[] = e.getText().split("\n");
					schedule.add(new StonyBrookClass(text[0], text[1], text[2], text[3], count));
				}
				count++;
			}
		}
	}
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = StonyBrookCalendar.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static void main(String... args) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
    	
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar calendar = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

		try {
			if (login())
				System.out.println("Login Succesful");
			else {
				System.err.println("Login Failed Incorrect Credentials");
				return;
			}
		} catch (Exception e) {
			System.err.println("Login Failed");
			e.printStackTrace();
			return;
		}
		System.out.println(driver.getCurrentUrl());
		generateSchedule();
		System.out.println(" + ");
		System.out.println(StonyBrookClass.weekStart);
		for (StonyBrookClass s : schedule) {
			
			
			
			Event e = new Event() 
					.setDescription(s.getDescription())
					.setLocation(s.getLocation())
					.setSummary(s.getClassAndSection())
					.setStart(s.getStart())
					.setEnd(s.getEnd())
					.setRecurrence((Arrays.asList("RRULE:FREQ=WEEKLY;UNTIL="+StonyBrookClass.GetendString()+"T23"+"0000Z")));
			
			e = calendar.events().insert("primary", e).execute();
			
		}
		
    }
    /*
    public static List<String> getRecurrence() {
    	String lastweek = findLastWeek();
    	// Week of 1/28/2019 - 2/3/2019
    	//20110701T170000Z
    	String[] weekArray = lastweek.split(" ");
    	String weekString = weekArray[2];
    	weekArray = weekString.split("/");
    	String month = Integer.valueOf(weekArray[0]) < 10 ? "0"+weekArray[0] : weekArray[0];
    	String year = weekArray[2];
    	String day = Integer.valueOf(weekArray[1]) < 10 ? "0"+weekArray[1] : weekArray[0];
    	String until = year+month+day;
    	return Arrays.asList("RRULE:FREQ=WEEKLY;UNTIL="+until);
    }
        
       
    
    public static String findLastWeek() {
    	try {
    	while(driver.findElement(By.className("SSSTEXTWEEKLY")) != null) {
    		driver.findElement(By.id("DERIVED_CLASS_S_SSR_NEXT_WEEK")).click();
    	}
    	} catch(Exception e) {}
    	return driver.findElement(By.className("SSSGROUPBOXGREEN")).getText();
    }
    */
    
    
    
    
    
}