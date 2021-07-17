package net.codetojoy.fortyfives.steps;

import static net.codetojoy.Constants.*;
import net.codetojoy.utils.*;
import net.codetojoy.fortyfives.*;

import io.cucumber.java.en.*;

import java.time.*;
import java.util.*;

import static org.junit.Assert.*;

public class FortyFivesStepDefinitions {
    private String trump;
    private String leading;
    private String cards;
    private String actual;

    private final Lists lists = new Lists();
    private final Strings strings = new Strings();

    private static boolean didPing = false;
    private static PingRemote pingRemote = new PingRemote(SCHEME, FORTY_FIVES_HOST, FORTY_FIVES_PING_PATH);

    @Given("trump: {string} leading: {string} cards: {string}")
    public void givenInitialInput(String trump,
                                  String leading,
                                  String cardsStr) {
        // TODO: find a better home for pinging:
        if (!didPing) {
            if (!pingRemote.ping()) {
               throw new IllegalStateException("server not running");
            } 
            didPing = true;
        }
        this.trump = trump;
        this.leading = leading;
        this.cards = cardsStr;
    }

    @And("I shuffle")
    public void iShuffle() {
        cards = strings.shuffleCards(cards);
    }

    @When("I sort cards by rank")
    public void iSelectCard() {
        var apiClient = new ApiClient(SCHEME, FORTY_FIVES_HOST, RANK_PATH);
        actual = apiClient.rankCards(trump, leading, cards);
    }

    @Then("cards should be {string}")
    public void cardRankShouldBe(String expected) {
        assertEquals((String) expected, (String) actual);
    }
}
