package com.solr;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class WebPageContentFetcher {
    public static void main(String[] args) {
    	// Setup ChromeDriver with WebDriverManager
        WebDriverManager.chromedriver().setup();

        // Set Chrome to run in headless mode
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");          // Runs Chrome in headless mode
        options.addArguments("--disable-gpu");       // Recommended for Windows OS
        options.addArguments("--window-size=1920,1080"); // Optional, but helps with rendering

        // Initialize the WebDriver with options
        WebDriver driver = new ChromeDriver(options);

        try {
            // Open the website
            String url = "https://www.benefits.gov";
            driver.get(url);

         // Get the body element
            WebElement body = driver.findElement(By.tagName("body"));

            // Extract plain text (no HTML tags)
            String textContent = body.getText();

            // Print the content
            System.out.println(textContent);
        } finally {
            // Close the browser
            driver.quit();
        }
    }
}
