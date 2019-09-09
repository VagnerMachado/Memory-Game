package edu.ncc.machado.memorygame;

/**
 * This is a Memory Game containing two levels and the code below defines the Level 2.
 * Level 2 automatically starts when the player finishes Level 1. The Level 2, defined below
 * contains 16 Image Buttons initially displaying a default image. Just like in Level 1,
 * the user can click on a Image Button and then it displays an image an waits for the user to
 * click on another Image Button. After the second click is detected, the program analyzes
 * the pair of images and if it is a match, the cards stay flipped and a Toast informs the user that a
 * pair was found. If the Images in the Image Buttons do not match, the game pauses for two
 * seconds and then the Image Buttons are reset to the default image. A click on the Reset button
 * located in the Action Bar will cause the Level 1 to launch, with a new set set of shuffled cards,
 * default displayed images and initialized variables. Otherwise, if the user finds all 8 pairs, a
 * final toast informs the player the game has ended.
 *
 * ************  Vagner Machado - N00820127 - Nassau Community College - Fall 2016  ****************
 * *****************************   Professor Lori Postner - CSC 240  *******************************
 */

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import java.util.Arrays;
import java.util.Collections;

public class SecondLevel extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "DEBUGGING";

    //instance variables
    private ImageButton[] buttons; //an array of image buttons
    private boolean secondCard; // keeps track of which card is flipped
    private int card1; // corresponds to the position of card 1 in array
    private int card2; // corresponds to the position of card 2 in array
    private int image1; // image of card 1
    private int image2; // image of card 2
    private int pairs; // certain amount of pairs launches a new activity
    private boolean delay; // keeps track of orientation change while Handler is pausing activity
    //array of icons to be shuffled
    private Integer [] images = {R.drawable.haha, R.drawable.angry, R.drawable.blushing, R.drawable.plain, R.drawable.sad, R.drawable.smile, R.drawable.boo, R.drawable.cat,
                                 R.drawable.haha, R.drawable.angry, R.drawable.blushing, R.drawable.plain, R.drawable.sad, R.drawable.smile, R.drawable.boo, R.drawable.cat};
    private int [] scrambled; //shuffled cards are saved here
    private int [] currentImage; // contains the current card displayed
    private boolean [] clickStatus; // contains the clickable status of each Image Button



    @Override
    /**
     * on create method - where the instance data assignments occur when program heads to onResumed
     * The array of cards in shuffled and saved in an array for use during the game
     * @param savedInstanceState - A Bundle with key-pair data
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_level);

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

        //tracks the amount of pairs, 8 pairs displays a final toast
        pairs = 0;

        ////declares the size of arrays and first button id
        int idButton = R.id.imageButton3;
        buttons = new ImageButton[16];
        clickStatus = new boolean[16];

        //instantiates buttons and tags them. tag is helpful to determine positions
        for (int i = 0; i < buttons.length; i++)
        {
            Log.d(TAG, "insideAssign " + i + " id: " + idButton);
            buttons[i] = (ImageButton) findViewById(idButton);
            buttons[i].setOnClickListener(this);
            buttons[i].setTag(i); //each button gets a tag
            clickStatus[i] = true; // bundle data
            idButton++;
            if(i == 9) // deals with the non sequential ids, took me a while to find this :(
                idButton++;
            Log.d(TAG, "inside on Create" + idButton);
        }

        //images are shuffled and associated with another array
        //had to change from Integer to int to put in Bundle, i did not find a way to put an IntegerArray in outState Bundle
        Collections.shuffle(Arrays.asList(images));

        scrambled = new int[16]; //scrambled images
        currentImage = new int[16]; //to send to Bundle, some flipped and some are default

        for(int i = 0; i < images.length; i++) {
            scrambled[i] = (images[i]); //assigns scrambled images to an array
            currentImage[i] = R.drawable.defaultsmile; //default image for the current image
        }
    }

    @Override
    /**
     * onClick method - specifies what happens when the user taps on Image Buttons
     * 1- if it is the first card, it stays visible
     * 2- if it is the second card, both cards stay visible at least for two seconds.
     *     a- if they are a match, a toast informs that to the player and cards stay visible
     *     b- if they do not match, the cards with flip back to default image after two seconds
     * 3- Eight pairs causes a Toast to displaya final message
     * @param v - the Image Button being clicked
     */
    public void onClick(View v) {
        int index = (int) v.getTag(); //gets the tag of clicked view
        ((ImageButton) v).setImageResource(scrambled[index]); //uses the tag to display an image

        //if the is the first card
        if (!secondCard) {
            secondCard = true; //next card will be the second
            card1 = (int) v.getTag(); //assigns tag to card 1
            image1 = scrambled[index]; //uses the tag to assign an image to card 1
            currentImage[card1] = image1; //puts image in the array for the bundle
            buttons[card1].setClickable(false);   //click on same icon prevention plan
            clickStatus[card1] = false;             // clickable reassignment array

            //else it is the second card
        } else {
            card2 = (int) v.getTag(); // gets the tag for card 2
            image2 = scrambled[index]; //assignment made to image 2 variable
            currentImage[card2] = image2; // bundle data for image

            // ** When there are two cards flipped, the analysis is performed as shown below ** //

            //if they are the same adds a pair
            System.out.println("inside same image-- Image1:" + image1 + " image2: " + image2);
            if (image1 == image2) {
                Log.d(TAG, "analyze same image-- Image1:" + image1 + " image2: " + image2);
                pairs++;                            // adds a pair
                buttons[card1].setClickable(false); //makes image button 1 not clickable
                buttons[card2].setClickable(false); // amkes image button 2 not clickable
                clickStatus[card1] = false; // bundle status
                clickStatus[card2] = false; //bundle status
                if (pairs != 8) { //does not show this Toast for last pair, shows a final Toast instead
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.a_pair, Toast.LENGTH_SHORT);
                    toast.setGravity(0, 0, 1200);
                    toast.show(); //displays a Toast to inform a match was found
                }
            } else //if cards are not the same, delay and restore
            {
                clickStatus[card1] = true;             // restores first card clickable status
                //turns them not clickable for 2 seconds
                for (int i = 0; i < buttons.length; i++)
                    buttons[i].setClickable(false);
                delay = true; //boolean is useful if screen is rotated while on delay

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        //makes all but pair clickable
                        for (int i = 0; i < buttons.length; i++)
                            buttons[i].setClickable(clickStatus[i]);

                        //reset images to default and update the image array for the bundle
                        buttons[card1].setImageResource(R.drawable.defaultsmile);
                        buttons[card2].setImageResource(R.drawable.defaultsmile);
                        currentImage[card1] = R.drawable.defaultsmile; //bundle update
                        currentImage[card2] = R.drawable.defaultsmile; //bundle update
                        delay = false;
                    }
                }, 2000);
            }
            secondCard = false;
        }

        // When all pairs are found, a final Toast is shown
        if (pairs == 8) {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.done, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 1200);
            toast.show();
        }
    }

    /**
     * onCreateOptionMenu method - enables the inflation of an Action Bar in the activity.
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
     * onOptionsItemMenu method - defines what happens when the used clicks in the Action Bar Buttons.
     * Since we have only one button, Reset, a click there causes the Level 1 to come to foreground.
     * @param item - the item in the Actio Bar
     * @return - always true when clicked
     */
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //defines the intent and start the activity (Level 1)
        Intent intent = new Intent(this, FirstLevel.class);
        startActivity(intent);
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
        outState.putBooleanArray("clickStatus", clickStatus);   //array with the click status of each Image Button
        outState.putIntArray("scrambled", scrambled);           //the array with shuffled images
        outState.putIntArray("currentImage", currentImage);     //array with the current displayed images(some default, some pairs
        int [] miscInts = { card1, card2, image1, image2, pairs};// array with other instance data
        outState.putIntArray("miscInts", miscInts);             //the array containing the instance data above
        outState.putBoolean("secondCard", secondCard);          //tracks if the first card was flipped when screen turned
        outState.putBoolean("delay", delay);

        //this has to be the last declaration
        super.onSaveInstanceState(outState);
    }

    public void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);

        //unbundles and reassigning the instance data
        clickStatus = inState.getBooleanArray("clickStatus"); //reassigns the bundle data
        scrambled = inState.getIntArray("scrambled");         //reassigns the shuffled image array
        currentImage = inState.getIntArray("currentImage");   //reassigns the bundle array with current images displayed
        int [] tempImages = inState.getIntArray("currentImage");//gets the current images in an array to reassing to image buttons
        int [] tempMisc = inState.getIntArray("miscInts");      //gets an array with other instance data and reassigns values below
        card1 = tempMisc[0];
        card2 = tempMisc[1];
        image1 = tempMisc[2];
        image2 = tempMisc[3];
        pairs = tempMisc[4];
        secondCard = inState.getBoolean("secondCard"); //reassigns if th first card was flipped when screen turned
        delay = inState.getBoolean("delay");            //reassigns the delay, will be true if screen turned while Handler paused was active

        //if screen is flipped while delay
        if (delay)
        {
            currentImage[card1] = R.drawable.defaultsmile; //bundle update
            currentImage[card2] = R.drawable.defaultsmile; //bundle update
            buttons[card1].setImageResource(R.drawable.defaultsmile); //image displayed
            buttons[card2].setImageResource(R.drawable.defaultsmile);// image displayed
        }
        //restores the images and the click status
        for(int i = 0; i < 16; i++) {
            buttons[i].setImageResource(tempImages[i]);
            buttons[i].setClickable(clickStatus[i]);
        }
    }
}
