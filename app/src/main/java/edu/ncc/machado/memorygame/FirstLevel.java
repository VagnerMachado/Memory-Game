package edu.ncc.machado.memorygame;

/**
 * This is a Memory Game containing two levels and the code below defines the Level 1.
 * When the app starts, the player sees the User interface for Level 1, defined below,
 * with two rows containing five cards each and displaying a default image.
 * The user can click on a Image Button and then it displays an image an waits for the user to
 * click on another Image Button. After the second click is detected, the program analyzes
 * the pair of images and if it is a match, the cards stay flipped and a Toast informs the user that a
 * pair was found. If the Images in the Image Buttons do not match, the game pauses for two
 * seconds and then the Image Buttons are reset to the default image. When the user finds 5 pairs,
 * the Level 2 activity starts. A click on the Reset button located in the Action Bar will shuffle
 * the Images  and change all Images Buttons to display the default image, hence
 * restarting the game and all variables.
 *
 * ************  Vagner Machado - N00820127 - Nassau Community College - Fall 2016  ****************
 * *****************************   Professor Lori Postner - CSC 240  *******************************
 */

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import java.util.Arrays;
import java.util.Collections;

/**
 * MainActivity class - the class that contains the declarations of variables and methods used in the Memory Game App
 * implements: View.OnClickListener interface: to handle clicks on Image Buttons
 * extends: AppcompatActivity to support a custom Action Bar
 */
public class FirstLevel extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "DEBUGGING";

    // instance variables
    private ImageButton[] buttons; //an array of image buttons
    private boolean secondCard; // keeps track of which card is flipped
    private int card1; // corresponds to the position of card 1 in array
    private int card2; // corresponds to the position of card 2 in array
    private int image1; // image of card 1
    private int image2; // image of card 2
    private int pairs; // certain amount of pairs launches a new activity
    private int idButton; //the first button int R class
    private boolean delay; // keeps track of orientation change while Handler is pausing activity
    private Integer [] images = {R.drawable.haha, R.drawable.angry, R.drawable.blushing, R.drawable.plain, R.drawable.sad, //array of icons to be shuffled
                                 R.drawable.haha, R.drawable.angry, R.drawable.blushing, R.drawable.plain, R.drawable.sad};
    private int [] scrambled; //shuffled cards are saved here
    private int [] currentImage; // contans the current card displayed
    private boolean [] clickStatus; // conains the clickable status of each Image Button


    @Override
    /**
     * on create method - where the instance data assignments occur when program heads to onResumed
     * The array of cards in shuffled and saved in an array for use during the game
     * @param savedInstanceState - A Bundle with key-pair data
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_level);

        //makes the toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //places the icon on the Action Bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.memory2);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //keeps track of which card is flipped
        secondCard = false;

        //tracks the amount of pairs, 5 pairs start Level 2
        pairs = 0;

        //declares the size of arrays and first button id
        idButton = R.id.imageButton3;
        buttons = new ImageButton[10];
        clickStatus = new boolean[10];

        //instantiates buttons and tags them. tag is helpful to determine positions
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = (ImageButton) findViewById(idButton);
            buttons[i].setOnClickListener(this);
            buttons[i].setTag(i); //each button gets a tag
            clickStatus[i] = true; // makes all of them clickable, this is the bundle data
            idButton++;
            Log.d(TAG, "inside on Create" + idButton);
        }

        //images are shuffled and associated with another array
        //had to change from Integer to int to put in Bundle, i did not find a way to put an IntegerArray in outState Bundle
        Collections.shuffle(Arrays.asList(images));

        scrambled = new int[10]; //scrambled images
        currentImage = new int[10]; //to send to Bundle, some flipped and some are not yet flipped

        for(int i = 0; i < images.length; i++) {
            scrambled[i] = (images[i]); //assigns the scrambled images to an array to be used.
            currentImage[i] = R.drawable.defaultsmile; // default image for the current image array
        }
    }

    @Override
/**
 * onClick method - specifies what happens when the user taps on Image Buttons
 * 1- if it is the first card, it stays visible
 * 2- if it is the second card, both cards stay visible at least for two seconds.
 *     a- if they are a match, a toast informs that to the player and cards stay visible
 *     b- if they do not match, the cards with flip back to default image after two seconds
 * 3- Five pairs cause level 2 to get started.
 * @param v - the Image Button being clicked
 */
    public void onClick(View v) {
        int index = (int) v.getTag(); //gets the tag of the view clicked
        ((ImageButton) v).setImageResource(scrambled[index]); // uses tag to display an image to display

        //if the is the first card
        if (!secondCard) {
            secondCard = true; //next card will be the second
            card1 = (int) v.getTag(); //assigns tag to card 1
            image1 = scrambled[index]; //uses tag to assign the image to card 1
            currentImage[card1] = image1;   //puts image in the array for the bundle
            buttons[card1].setClickable(false);   //click on same icon prevention
            clickStatus[card1] = false;           // clickable reassignment array

            //else it is the second card
        } else {
            card2 = (int) v.getTag(); //gets the tag
            image2 = scrambled[index]; // assignment made to image2 variable
            currentImage[card2] = image2; //bundle data for image


            // ** When there are two cards flipped, the analysis is performed as shown below ** //

            //if they are the same adds a pair to game
            System.out.println("inside same image-- Image1:" + image1 + " image2: " + image2); //debugging

            if (image1 == image2) {
                Log.d(TAG, "inside same image-- Image1:" + image1 + " image2: " + image2); //debugging
                pairs++;                                //adds a pair
                buttons[card1].setClickable(false);     //makes image buuton 1 not clickable
                buttons[card2].setClickable(false);     //makes image button 2 not clickable
                clickStatus[card1] = false; //bundle
                clickStatus[card2] = false; //bundle

                //informs the player a pair was found, unless it is the last pair, in which case another toast informs the user is changing levels
                if (pairs != 5) {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.a_pair, Toast.LENGTH_SHORT);
                    toast.setGravity(0, 0, 1200);
                    toast.show();
                }

                //if the pair is not a match, delay and restore
            } else
            {
                clickStatus[card1] = true;             // updates clickable array

                //turns all of them not clickable for 2 seconds
                for (int i = 0; i < buttons.length; i++)
                    buttons[i].setClickable(false);

                delay = true; //deals with change of orientation during pause

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        //makes all but pair clickable
                        for (int i = 0; i < buttons.length; i++)
                            buttons[i].setClickable(clickStatus[i]);

                        //reset images
                        buttons[card1].setImageResource(R.drawable.defaultsmile); //restores default image
                        buttons[card2].setImageResource(R.drawable.defaultsmile); //restores default image
                        currentImage[card1] = R.drawable.defaultsmile; //bundle update
                        currentImage[card2] = R.drawable.defaultsmile; //bundle update
                        delay = false;

                    }
                }, 2000);
              /*  buttons[card1].setImageResource(R.drawable.defaultsmile);
                buttons[card2].setImageResource(R.drawable.defaultsmile);
                secondCard = false;*/
            }
            secondCard = false; //next card will be first card
        }

        //Five pairs laund level 2 activity
        if (pairs == 5) {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.upLevel, Toast.LENGTH_LONG);
            toast.setGravity(0, 0, 1200);
            toast.show();
            Intent intent = new Intent(this, SecondLevel.class);
            startActivity(intent);
        }
    }

    /**
     * onCreateOptionMenu method - enables the inflation of a custom Action Bar in the activity.
     * @param menu the menu to be used for the activity
     * @return - always true
     */
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //inflates the menu
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    /**
     * onOptionItemSelected method - deals with the clicks on Reset button in the action bar
     * Causes the level 1 to be restarted with all images shuffled.
     * @param item - the item clicked in the action bar
     * @return - true
     */
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Collections.shuffle(Arrays.asList(images)); //shuffles the  images
    for(int i = 0; i < buttons.length; i++){
        buttons[i].setImageResource(R.drawable.defaultsmile); //default image
        buttons[i].setClickable(true); //all are clickable
        scrambled[i] = (images[i]); // assings scrambled images
        currentImage[i] = R.drawable.defaultsmile; //bundle array is updated
        clickStatus[i] = true; //clickable bundle is updated
    }
        secondCard = false; //next card is first card
        pairs = 0; //pairs
     return true;
    }

    /**
     * onSaveInstanceState method - sends all instance data necessary to recreate
     * the game in other orientation. All gathered data is sent to a Bundle object.
     * @param outState - the bundle with the instance data saved
     */
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "in onSaveInstanceState");

        //sends into bundle
        outState.putBooleanArray("clickStatus", clickStatus); //click status of each image button
        outState.putIntArray("scrambled", scrambled); //the array with shuffled images
        outState.putIntArray("currentImage", currentImage); //the current images being displayed
        int [] miscInts = { card1, card2, image1, image2, pairs}; //instance variables
        outState.putIntArray("miscInts", miscInts); //instance variables
        outState.putBoolean("secondCard", secondCard); // tracks which card id flipped
        outState.putBoolean("delay", delay); // tracks if orientation change happens during delay

        //this has to be the last declaration
        super.onSaveInstanceState(outState);
    }

    /**
     * onRestoreInstanceState method - redefines the state of the current
     * memory game defined at onSaveInstanceState method. The parameter
     * bundle has the items used to restore the game
     *
     * @param inState - the Bundle objec received with the game variables.
     */
    public void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        clickStatus = inState.getBooleanArray("clickStatus"); //restores the bundle data
        scrambled = inState.getIntArray("scrambled"); //restores the array with shuffled images
        currentImage = inState.getIntArray("currentImage"); //links the current images
        int [] tempImages = inState.getIntArray("currentImage"); // used to reassign images
        int [] tempMisc = inState.getIntArray("miscInts"); //instance data reassignments below
        card1 = tempMisc[0];
        card2 = tempMisc[1];
        image1 = tempMisc[2];
        image2 = tempMisc[3];
        pairs = tempMisc[4];

        secondCard = inState.getBoolean("secondCard"); //instance data
        delay = inState.getBoolean("delay"); //sees if orientation changed during delay
        if (delay)
        {
            //if there was a delay, cards did not match. Flip them.
            currentImage[card1] = R.drawable.defaultsmile; //bundle update
            currentImage[card2] = R.drawable.defaultsmile; //bundle update
            buttons[card1].setImageResource(R.drawable.defaultsmile); //default image
            buttons[card2].setImageResource(R.drawable.defaultsmile); // default image
        }

        //reassigns the image and click status to each Image Button
        for(int i = 0; i < 10; i++) {
            buttons[i].setImageResource(tempImages[i]);
            buttons[i].setClickable(clickStatus[i]);
        }
    }

}
