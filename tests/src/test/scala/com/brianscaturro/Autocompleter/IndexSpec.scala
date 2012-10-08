package com.brianscaturro.Autocompleter

import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, FlatSpec}
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.selenium.WebBrowser
import org.openqa.selenium.{Keys, WebDriver}
import org.scalatest.time.{Seconds, Span}
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.interactions.Actions

abstract class IndexSpec extends FlatSpec with ShouldMatchers
  with WebBrowser
  with BeforeAndAfterEach
  with BeforeAndAfterAll {

  implicit val webDriver: WebDriver

  override def beforeEach(configMap: Map[String, Any]) {
    go to "localhost:8080"
  }

  override def afterAll(configMap: Map[String, Any]) {
    webDriver.quit
  }

  "The form" should "contain an input field named input" in {
    IndexPage.input.isDisplayed should be(true)
  }

  it should "contain a submit button" in {
    IndexPage.submit should be('defined)
  }

  "The page" should "contain an empty div called 'matches'" in {
    IndexPage.matches should be ('defined)
  }

  it should "contain text \"Matches:\"" in {
    IndexPage.wrapper.filter(_.text.contains("Matches:")) should be('defined)
  }

  "^" should "enable autocomplete when it is the the first thing input" in {
    IndexPage.pressCarrot
    implicitlyWait(Span(10, Seconds))
    IndexPage.isAuto should be(true)
  }

  it should "enable autocomplete following whitespace" in {
    IndexPage.input.value = "hello "
    IndexPage.pressCarrot
    implicitlyWait(Span(10, Seconds))
    IndexPage.isAuto should be(true)
  }

  it should "enable autocomplete following multiple whitespace chars" in {
    IndexPage.input.value = "goodbye                "
    IndexPage.pressCarrot
    implicitlyWait(Span(10, Seconds))
    IndexPage.isAuto should be(true)
  }

  it should "enable autocomplete following whitespace at the beginning" in {
    IndexPage.input.value = "     "
    IndexPage.pressCarrot
    implicitlyWait(Span(10, Seconds))
    IndexPage.isAuto should be(true)
  }

  "If one or more matches are returned, the 'Matches' div" should "be populated with the results" in {
    IndexPage.pressCarrot
    IndexPage.input.value = "z"
    implicitlyWait(Span(10, Seconds))
    IndexPage.matchItems.size should be(3)
  }

  "the edit field" should "be updated to include the best match" in {
    IndexPage.pressCarrot
    IndexPage.input.value = "z"
    implicitlyWait(Span(10, Seconds))
    IndexPage.input.value should be("zer")
  }

  it should "be updated to include the best match for second word in field" in {
    IndexPage.input.value = "first "
    IndexPage.pressCarrot
    IndexPage.input.underlying.sendKeys("a")
    implicitlyWait(Span(10, Seconds))
    IndexPage.input.value should be("first aborad")
  }

  it should "be updated to include the best match after several words" in {
    IndexPage.input.value = "first "
    IndexPage.pressCarrot
    IndexPage.input.underlying.sendKeys("a")
    IndexPage.input.underlying.sendKeys(Keys.TAB)
    implicitlyWait(Span(10, Seconds))
    IndexPage.input.underlying.sendKeys(" ");
    IndexPage.pressCarrot
    IndexPage.input.underlying.sendKeys("z")
    implicitlyWait(Span(10, Seconds))
    IndexPage.input.value should be("first aborad zer")
  }

  "If the user presses the backspace key while in autocomplete mode, the web page" should "re-display the matches that result from the shorter stem" in {
    IndexPage.pressCarrot
    IndexPage.input.value = "ze" //autocomplete to "zer"
    1.to(2).foreach(i => IndexPage.backspace)
    implicitlyWait(Span(10, Seconds))
    val paras = IndexPage.matchItems
    paras.exists(_.text == "zer") should be(true)
    paras.exists(_.text == "zooxanthella") should be(true)
    paras.exists(_.text == "zygomaxillary") should be(true)
  }

  /**
   * This wasn't covered in the spec, but this is the behavior
   * in more popular autocomplete tools that don't use a delimiter
   * like comma. I figured if this is the behavior I am implementing,
   * then I make an assertion
   */
  "autocomplete mode" should "not be enabled between words" in {
    IndexPage.input.value ="first second"
    (1 to 7).foreach(i => IndexPage.input.underlying.sendKeys(Keys.ARROW_LEFT))
    IndexPage.input.underlying.sendKeys(Keys.SPACE)
    IndexPage.input.underlying.sendKeys("^")
    IndexPage.input.value should be("first ^ second")
  }

  "submitting the form" should "not complete the word" in {
    IndexPage.pressCarrot
    IndexPage.input.value = "a"
    submit()
    implicitlyWait(Span(10, Seconds))
    IndexPage.input.value should be("a")
  }

  it should "leave text as is when auto mode is off" in {
    IndexPage.input.value = "hello."
    submit()
    implicitlyWait(Span(10, Seconds))
    IndexPage.input.value should be("hello.")
  }

  "The best match" should "be accepted from the edit field when the user presses TAB" in {
    IndexPage.pressCarrot
    IndexPage.input.value = "a"
    IndexPage.input.underlying.sendKeys(Keys.TAB)
    implicitlyWait(Span(10, Seconds))
    IndexPage.input.value should be("aborad")
  }

  it should "be accepted from the edit field when the user presses TAB on second word" in {
    IndexPage.input.value = "first "
    IndexPage.pressCarrot
    IndexPage.input.underlying.sendKeys("a")
    IndexPage.input.underlying.sendKeys(Keys.TAB)
    implicitlyWait(Span(10, Seconds))
    IndexPage.input.value should be("first aborad")
  }

  it should "be accepted from a clicked item in the matches list" in {
    IndexPage.pressCarrot
    IndexPage.input.value = "a"
    click on cssSelector("p[data-text='accommodatingly']")
    implicitlyWait(Span(10, Seconds))
    IndexPage.input.value should be("accommodatingly")
  }

  it should "be accepted from a clicked item in the matches list on second word" in {
    IndexPage.input.value = "first "
    IndexPage.pressCarrot
    IndexPage.input.underlying.sendKeys("a")
    click on cssSelector("p[data-text='accommodatingly']")
    implicitlyWait(Span(10, Seconds))
    IndexPage.input.value should be("first accommodatingly")
  }

  "autocomplete" should "exit when the user enters text that results in zero matches" in {
    IndexPage.pressCarrot
    IndexPage.input.value = "ze"
    IndexPage.input.underlying.sendKeys("e")
    implicitlyWait(Span(10, Seconds))
    IndexPage.isAuto should be(false)
  }

  it should "exit when the user hits tab" in {
    IndexPage.pressCarrot
    IndexPage.input.value = "ze"
    IndexPage.input.underlying.sendKeys(Keys.TAB)
    implicitlyWait(Span(10, Seconds))
    IndexPage.isAuto should be(false)
  }

  it should "exit when the user clicks one of the words displayed in the Matches div" in {
    IndexPage.pressCarrot
    IndexPage.input.value = "a"
    click on cssSelector("p[data-text='accommodatingly']")
    implicitlyWait(Span(10, Seconds))
    IndexPage.isAuto should be(false)
  }

  it should "exit when the user presses spacebar" in {
    IndexPage.pressCarrot
    IndexPage.input.value = "a"
    IndexPage.input.underlying.sendKeys(Keys.SPACE)
    implicitlyWait(Span(10, Seconds))
    IndexPage.isAuto should be(false)
  }

  it should "exit when the user clicks the Submit button" in {
    IndexPage.pressCarrot
    IndexPage.input.value = "a"
    click on IndexPage.submit.get.underlying
    implicitlyWait(Span(10, Seconds))
    IndexPage.isAuto should be(false)
  }

  it should "exit when the user backspaces past the first letter entered in autocomplete mode" in {
    IndexPage.input.value = "hello "
    IndexPage.pressCarrot
    IndexPage.input.underlying.sendKeys("ze")
    1.to(3).foreach(i => IndexPage.backspace)
    implicitlyWait(Span(10, Seconds))
    IndexPage.isAuto should be(false)
  }

}

