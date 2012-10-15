package com.brianscaturro.Autocompleter

import org.openqa.selenium.{Keys, WebDriver}
import org.scalatest.selenium.WebBrowser
import org.openqa.selenium.interactions.Actions

object IndexPage extends WebBrowser {
  val url = "localhost:8080"

  def input(implicit driver: WebDriver): TextField = {
    textField("input")
  }

  def isAuto(implicit driver: WebDriver): Boolean = {
    input.attribute("class").getOrElse("")
         .contains("auto")
  }

  def submit(implicit driver: WebDriver): Option[Element] = {
    find(cssSelector("input[type='submit']"))
  }

  def matches(implicit driver: WebDriver): Option[Element] = {
    find(cssSelector("div[id='matches']"))
  }

  def matchItems(implicit driver: WebDriver): Iterator[Element] = {
    findAll(cssSelector("#matches p"))
  }

  def wrapper(implicit driver: WebDriver): Option[Element] = {
    find(className("wrapper"))
  }

  def backspace(implicit driver: WebDriver): Unit = {
    input.underlying.sendKeys("\u0008")
  }

  def pressCarrot(implicit driver: WebDriver): Unit = {
    val builder = new Actions(driver)
    builder.click(input.underlying)
    applyCarrotKey(builder)
  }

  def applyCarrotKey(builder: Actions): Unit = {
    builder.keyDown(Keys.SHIFT)
    builder.sendKeys("6")
    builder.keyUp(Keys.SHIFT)
    builder.build().perform
  }
}
