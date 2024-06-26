package com.moeabdel.deckofcards;



import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.moeabdel.deckofcards.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import android.os.Handler;



public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;
    private EditText you;
    private EditText dealer;
    private TextView RoundDecider;
    private Button goToNextRound;
    private ConstraintLayout constraintLayout;
    private ArrayList<String> cardsImagesUrls = new ArrayList<>();
    private ArrayList<String> userCardsValues = new ArrayList<>();
    private ArrayList<String> dealersCardValues = new ArrayList<>();
    private ArrayList<String> dealersCardImages = new ArrayList<>();
    private Button getANewDeck;
    private TextView totalValueTextView;
    private TextView dealersMove;
    private Picasso picasso;
    private RequestQueue requestQueue;
    private TextView cardsAtStake;
    private ImageView cardsAtStakeImage;

    private TextView cardsValue;
    private Button startButton;
    private HorizontalScrollView horiScroll;
    private String deckOfCardUrlApi = "https://deckofcardsapi.com/api/deck/";
    private String faceDownCard = "https://deckofcardsapi.com/static/img/back.png";
    private String deckId;
    private boolean userCardOneDisplayed = false;
    private boolean userCardTwoDisplayed = false;
    private LinearLayout linearLayout;
    private HorizontalScrollView horiScroll2;
    private LinearLayout linearLayout2;
    private boolean userBusted = false;
    private boolean userStayed = false;
    private boolean userHitBlackjack = false;
    private boolean userHasAce = false;
    private boolean hitNewDeck = false;
    private String remaining;
    private int startingEditTextValueYou = 0;
    private int startingEditTextValueDealer = 0;
    private Button hitButton;
    private Button stayButton;
    private boolean dealerHitBlackjack = false;
    private boolean dealerStays = false;
    private boolean isTied = false;
    private boolean enabledNextRoundButton = false;
    private int allCardsAtStake;
     private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
       // userCardOne = binding.userCardOne;
       // userCardTwo = binding.userCardTwo;
        //dealerCardOne = binding.dealerCardOne;
        //dealerCardTwo = binding.dealerCardTwo;
        cardsValue = binding.cardsValue;
        dealersMove = binding.dealersMove;
        startButton = binding.startButton;
        linearLayout = binding.linearlayout;
        totalValueTextView = binding.totalValueTExtView;
        horiScroll2 = binding.horiScroll2;
        linearLayout2 = binding.linearlayout2;
        goToNextRound = binding.goToNextRoundButton;
        you = binding.editTextYou;
        dealer = binding.editTextDealer;
        hitButton = binding.hitButton;
        stayButton = binding.stayButton;
        getANewDeck = binding.getANewDeckButton;
        RoundDecider = binding.RoundDecider;
        cardsAtStake = binding.cardsAtStake;
        cardsAtStakeImage = binding.CardsAtStakeImage;
        //horiScroll = binding.HoriScroll;
       // constraintLayout = binding.ConLayout;
        requestQueue = Volley.newRequestQueue(this);
        picasso = Picasso.get();
        you.setText("" + startingEditTextValueYou);
        dealer.setText("" + startingEditTextValueDealer);


    }

    public void startTheGame(View v) {
        hitButton.setEnabled(true);
        stayButton.setEnabled(true);
        startButton.setVisibility(View.GONE);
        goToNextRound.setVisibility(View.VISIBLE);
        Uri.Builder builder = Uri.parse(deckOfCardUrlApi).buildUpon();

        builder.appendPath("new");
        builder.appendPath("shuffle");
        builder.appendQueryParameter("deck_count", "1");
        String url = builder.build().toString();

        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String deckId = parseStartJson(response);
                        doDrawACardDownload(deckId);

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "No More API Calls For 24 Hours", Toast.LENGTH_SHORT).show();
                        // Handle the error if needed
                    }


                }
        ) {


        };
        requestQueue.add(getRequest);

    }

    private void getAFullNewDeck(){
        hitButton.setEnabled(true);
        stayButton.setEnabled(true);
        linearLayout.removeAllViews();
        linearLayout2.removeAllViews();
        userBusted = false;
        userStayed = false;
        userHitBlackjack = false;
        dealerHitBlackjack = false;
        dealerStays = false;
        enabledNextRoundButton = false;
        userCardsValues.clear();
        cardsImagesUrls.clear();
        dealersCardImages.clear();
        dealersCardValues.clear();
        totalValueTextView.setText("");
        RoundDecider.setText(" ");
        cardsValue.setText("");
        dealersMove.setText("");
        Uri.Builder builder = Uri.parse(deckOfCardUrlApi).buildUpon();

        builder.appendPath("new");
        builder.appendPath("shuffle");
        builder.appendQueryParameter("deck_count", "1");
        String url = builder.build().toString();

        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String deckId = parseStartJson(response);
                        doDrawACardDownload(deckId);



                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "No More API Calls For 24 Hours", Toast.LENGTH_SHORT).show();
                        // Handle the error if needed
                    }


                }
        ) {


        };
        requestQueue.add(getRequest);

    }

    private void updateYouValue(int numbone, int numbtwo){
        //startingEditTextValueYou++;
        String temp = you.getText().toString();
        int firstValue = Integer.parseInt(temp);
        int total = (numbone + numbtwo) + firstValue;
        if(isTied){
            total = (numbone + numbtwo) + (firstValue + allCardsAtStake);
            isTied = false;
            cardsAtStake.setVisibility(View.INVISIBLE);
            cardsAtStakeImage.setVisibility(View.INVISIBLE);
        }
        you.setText("" + total);
    }
    private void updateDealerValue(int numbOne, int numbTwo){
        //startingEditTextValueDealer++;
        String temp = dealer.getText().toString();
        int firstValue = Integer.parseInt(temp);
        int total = (numbOne + numbTwo) + firstValue;
        if(isTied){
            total = (numbOne + numbTwo) + (firstValue + allCardsAtStake);
            isTied = false;
            cardsAtStake.setVisibility(View.INVISIBLE);
            cardsAtStakeImage.setVisibility(View.INVISIBLE);
        }
        dealer.setText("" + total);
    }

    private String parseStartJson(String s) {
        //String deckId = "";
        try {
            JSONObject jsonObject = new JSONObject(s);
              deckId = jsonObject.getString("deck_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return deckId;
    }

    private void doDrawACardDownload(String s){
        Uri.Builder urlBuilder = Uri.parse(deckOfCardUrlApi).buildUpon();
        urlBuilder.appendPath(s);
        urlBuilder.appendPath("draw");
        urlBuilder.appendQueryParameter("count", "4");

        String url = urlBuilder.build().toString();
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseDrawACardJson(response);
                        updateLinearLayout();

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "No More API Calls For 24 Hours", Toast.LENGTH_SHORT).show();
                        // Handle the error if needed
                    }


                }
        ) {


        };
        requestQueue.add(getRequest);
    }

    private void parseDrawACardJson(String s){
        //String allCardsImages = "";
        //String cardsValue = "";
        try {
            JSONObject jsonObject = new JSONObject(s);
            remaining = jsonObject.optString("remaining");
            JSONArray cardsObject = jsonObject.getJSONArray("cards");
            for (int i = 0; i < cardsObject.length(); i++) {
                if (i == 0 || i == 1) {
                    JSONObject jsonObject1 = cardsObject.getJSONObject(i);
                    String cardImage = jsonObject1.getString("image");
                    //allCardsImages = allCardsImages + "," + cardImage;
                    dealersCardImages.add(cardImage);
                    String value = jsonObject1.getString("value");
                    if (value.equals("KING") || value.equals("QUEEN") || value.equals("JACK")) {
                        dealersCardValues.add("10");
                    } else if (value.equals("ACE")) {
                        dealersCardValues.add("11");
                    } else {
                        dealersCardValues.add(value);
                    }
                } else if (i == 2 || i == 3) {
                    JSONObject jsonObject1 = cardsObject.getJSONObject(i);
                    String cardImage = jsonObject1.getString("image");
                    //allCardsImages = allCardsImages + "," + cardImage;
                    cardsImagesUrls.add(cardImage);
                    String value = jsonObject1.getString("value");
                    if (value.equals("KING") || value.equals("QUEEN") || value.equals("JACK")) {
                        userCardsValues.add("10");
                    } else if (value.equals("ACE")) {
                        userCardsValues.add("11");
                    } else {
                        userCardsValues.add(value);
                    }
                }

            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        //userCardTwo.setVisibility(View.VISIBLE);
       // userCardOne.setVisibility(View.VISIBLE);

        //picasso.load(faceDownCard).into(userCardTwo);
       // picasso.load(faceDownCard).into(userCardOne);
        //picasso.load(faceDownCard).into(dealerCardOne);
        //picasso.load(faceDownCard).into(dealerCardTwo);
        //dealerMove();

    }
    private void updateLinearLayout(){
        if(!cardsImagesUrls.isEmpty()) {
            String newCardImage = cardsImagesUrls.get(0);

            // Create a new ImageView for the new card
            ImageView newCard = new ImageView(this);
            picasso.load(newCardImage).into(newCard);
            String newCardImage2 = cardsImagesUrls.get(1);

            ImageView newCard2 = new ImageView(this);
            picasso.load(newCardImage2).into(newCard2);
            //horiScroll.addView(linearLayout);
            //horiScroll.addView(newCard2);
            linearLayout.addView(newCard);
            linearLayout.addView(newCard2);
            String dealersCardsUrl = dealersCardImages.get(0);
            ImageView dealersCardOne = new ImageView(this);
            picasso.load(dealersCardsUrl).into(dealersCardOne);
            ImageView dealersCardTwo = new ImageView(this);
            picasso.load(faceDownCard).into(dealersCardTwo);
            linearLayout2.addView(dealersCardOne);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Code to execute after the delay
                    linearLayout2.addView(dealersCardTwo);
                }
            }, 500); // 3000 milliseconds = 3 seconds

           // linearLayout2.addView(dealersCardTwo);
            getCardsTotalValue();
        }

    }
    public void getCardsTotalValue() {


        if (userCardsValues.size() <= 2) {
           // String cardOneValue = userCardsValues.get(0);
           // String cardTwoValue = userCardsValues.get(1);
            //int cardOne = Integer.parseInt(cardOneValue);
            //int cardTwo = Integer.parseInt(cardTwoValue);
            final String cardOneValue = userCardsValues.get(0);
            final String cardTwoValue = userCardsValues.get(1);
            final int[] cardValues = {Integer.parseInt(cardOneValue), Integer.parseInt(cardTwoValue)};

            if (cardValues[0] == 11 || cardValues[1] == 11) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.alert_dialog_layout, null);
                ImageView firstCurrentCard = view.findViewById(R.id.userCardOneImage);
                String cardImage = cardsImagesUrls.get(0);
                picasso.load(cardImage).into(firstCurrentCard);
                ImageView secondCurrentCard = view.findViewById(R.id.userCardTwoImage);
                String cardImage2 = cardsImagesUrls.get(1);
                picasso.load(cardImage2).into(secondCurrentCard);

                builder.setView(view);
                builder.setTitle("You Have An Ace");
                builder.setMessage("Do You Want To Set Its Value To One or Eleven");
                builder.setCancelable(false);
                //builder.setIcon(R.drawable.logo);
                builder.setPositiveButton("One", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (cardValues[0] == 11) {
                            cardValues[0] = 1;
                        } else if (cardValues[1] == 11) {
                            cardValues[1] = 1;
                        }
                        updateTotalValue(cardValues[0], cardValues[1]);
                    }
                });
                builder.setNegativeButton("Eleven", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(cardValues[0] + cardValues[1] ==21) {
                                    int value = 0;
                                    for (String dealerCards : dealersCardValues) {
                                        int temp = Integer.parseInt(dealerCards);
                                        value = value + temp;
                                    }
                                    if (value != 21) {
                                        dealersMove.setText("You Hit Blackjack");
                                        displayDealersCards();
                                        updateYouValue(userCardsValues.size(), dealersCardValues.size());
                                        hitButton.setEnabled(false);
                                        stayButton.setEnabled(false);
                                        enabledNextRoundButton = true;
                                    }
                                    else if( value == 21){
                                        dealersMove.setText("Both You And The Dealer Hit Blackjack");
                                        displayDealersCards();
                                        hitButton.setEnabled(false);
                                        stayButton.setEnabled(false);
                                        enabledNextRoundButton = true;
                                    }
                                }
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }


            else if(cardValues[0] + cardValues[1] > 21){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("You Have Double Aces");
                builder.setMessage("Set both aces as one or one as eleven and the other as one?");
                builder.setCancelable(false);
                builder.setPositiveButton("Set Both As One", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    cardValues[0] = 1;
                    cardValues[1] = 1;
                    updateTotalValue(cardValues[0], cardValues[1]);
                    }
                });
                builder.setNegativeButton("Set One As Eleven, One As One", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cardValues[0] = 1;
                        cardValues[1] = 11;
                        updateTotalValue(cardValues[0], cardValues[1]);
                    }
                });
            }


            int totalValue = cardValues[0] + cardValues[1];
            String totalValueString = String.valueOf(totalValue);
            cardsValue.setText( totalValueString);
            cardsValue.setVisibility(View.VISIBLE);
            totalValueTextView.setVisibility(View.VISIBLE);
            totalValueTextView.setText("Total Value:");
            Log.d(TAG, "remaining: " + remaining.toString());
            //dealerMove();

        }
        else{
            String cardValue = userCardsValues.get(userCardsValues.size() -1);
            int cardValueInt = Integer.parseInt(cardValue);
            String currentValue = cardsValue.getText().toString();
            int currentValueInt = Integer.parseInt(currentValue);
            int newValue = cardValueInt + currentValueInt;
            if(newValue>21){
                totalValueTextView.setText("You Busted: ");
                RoundDecider.setText("You Lost");
                displayDealersCards();
                userBusted = true;
                //dealerMove();
                updateDealerValue(userCardsValues.size(), dealersCardValues.size());
                //dealersMove.setText("Dealer Won");
                enabledNextRoundButton = true;
            }
            else if(newValue == 21){
                userHitBlackjack = true;
            }
            String newValueString = String.valueOf(newValue);
            cardsValue.setText(newValueString);
            //dealerMove();

        }
        //dealerMove();
    }


    private void updateTotalValue(int newCardOneValue, int newCardTwoValue) {
        String newCardOneValueString = String.valueOf(newCardOneValue);
        String newCardTwoValueString = String.valueOf(newCardTwoValue);
        userCardsValues.clear();
        userCardsValues.add(newCardOneValueString);
        userCardsValues.add(newCardTwoValueString);
        int totalValue = newCardOneValue + newCardTwoValue;
        String totalValueString = String.valueOf(totalValue);
        cardsValue.setText(totalValueString);
    }




    public void goToNextRound(View v) {
        if (!enabledNextRoundButton) {
            // Do Nothing Here
        } else {
            if (hitNewDeck) {
                getAFullNewDeck();
                hitNewDeck = false;
            } else if (!hitNewDeck) {
                hitButton.setEnabled(true);
                stayButton.setEnabled(true);
                int remainingCards = Integer.parseInt(remaining);
                if (remainingCards >= 4) {
                    if (!cardsImagesUrls.isEmpty() && !userCardsValues.isEmpty() &&
                            !dealersCardValues.isEmpty() && !dealersCardImages.isEmpty()) {
                        cardsImagesUrls.clear();
                        userCardsValues.clear();
                        dealersCardImages.clear();
                        dealersCardValues.clear();
                        linearLayout.removeAllViews();
                        linearLayout2.removeAllViews();
                        totalValueTextView.setText("");
                        cardsValue.setText("");
                        dealersMove.setText("");
                        userBusted = false;
                        userStayed = false;
                        userHitBlackjack = false;
                        dealerHitBlackjack = false;
                        dealerStays = false;
                        RoundDecider.setText(" ");
                        enabledNextRoundButton = false;
                    }
                    Uri.Builder urlBuilder = Uri.parse(deckOfCardUrlApi).buildUpon();
                    urlBuilder.appendPath(deckId);
                    urlBuilder.appendPath("draw");
                    urlBuilder.appendQueryParameter("count", "4");

                    String url = urlBuilder.build().toString();
                    StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    parseDrawACardJson(response);
                                    updateLinearLayout();

                                }
                            },
                            new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(MainActivity.this, "No More API Calls For 24 Hours", Toast.LENGTH_SHORT).show();
                                    // Handle the error if needed
                                }


                            }
                    ) {


                    };
                    requestQueue.add(getRequest);
                } else {
                    // Toast.makeText(this, "There Arent Enough Cards In The Deck To Continue Playing", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("No Cards Remaining In This Deck");
                    builder.setMessage("Use A New Deck Or Close The Application\nSelecting A New Deck Will Discard The Current Cards Held");
                    builder.setCancelable(false);
                    builder.setPositiveButton("New Deck", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getAFullNewDeck();
                        }
                    });
                    builder.setNegativeButton("Close App", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //finish();
                            onDestroy();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        }
    }

    private void dealerMove() {
        if (!remaining.equals("0")) {
            if (dealersCardValues.size() <= 2) {
                final String cardOneValue = dealersCardValues.get(0);
                final String cardTwoValue = dealersCardValues.get(1);
                final int[] cardValues = {Integer.parseInt(cardOneValue), Integer.parseInt(cardTwoValue)};
                if (cardValues[0] + cardValues[1] > 21) {
                    cardValues[0] = 1;
                    dealersCardValues.clear();
                    String changedCardValue = String.valueOf(cardValues[0]);
                    String nonChangedCardValue = String.valueOf(cardValues[1]);
                    dealersCardValues.add(changedCardValue);
                    dealersCardValues.add(nonChangedCardValue);
                } else if (cardValues[0] + cardValues[1] == 21) {
                    dealerHitBlackjack = true;
                    dealersMove.setText("Dealer Has Blackjack");
                    // updateDealerValue(userCardsValues.size(), dealersCardValues.size());
                    displayDealersNewCard();
                    hitButton.setEnabled(false);
                    stayButton.setEnabled(false);
                    enabledNextRoundButton = true;
                }
            }
            int totalDealersValue = 0;
            if (!remaining.equals("0")) {
                for (String cardsValue : dealersCardValues) {
                    int cardsValueInt = Integer.parseInt(cardsValue);
                    totalDealersValue = totalDealersValue + cardsValueInt;

                }
                if (totalDealersValue <= 16) {
                    dealersMove.setText("Dealer Decides To Hit");
                    displayDealersNewCard();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Code to execute after the delay
                            hitDealerDownload(deckId);
                        }
                    }, 1500); // 3000 milliseconds = 3 seconds
                }

                //hitDealerDownload(deckId);
            }
            if (totalDealersValue > 16) {
                if (!dealerHitBlackjack) {
                    dealersMove.setText("Dealer Decides To Stay With  " + totalDealersValue);
                    displayDealersCards();
                    stayWinnerDetermine();
                } else if (dealerHitBlackjack) {
                    dealersMove.setText("Dealer Hit Blackjack");
                    displayDealersCards();
                    updateDealerValue(userCardsValues.size(), dealersCardValues.size());
                    RoundDecider.setText("You Lost");
                    enabledNextRoundButton = true;
                }

            }
        } else if (remaining.equals("0")) {
            Log.d(TAG, "Inside else if when dealer wants to hit with zero cards");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Cards Remaining In This Deck");
            builder.setMessage("Dealer attempted to hit with no cards left; hand outcome will be determined by existing cards");
            builder.setCancelable(false);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    displayDealersCards();
                    winnerBeforeNewDeck();
                    enabledNextRoundButton = true;
                    //getAFullNewDeck();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
            //Log.d(TAG, "totalDealersValue: " + totalDealersValue);

            //Log.d(TAG, "totalDealersValue: " + totalDealersValue);
            // String dealersCardOne = userCardsValues.get(0);
            //String dealersCardTwo = userCardsValues.get(1);
            //  int dealersCardOneInt = Integer.parseInt(dealersCardOne);
            // int dealersCardTwoInt = Integer.parseInt(dealersCardTwo);
            // int cardsSum = dealersCardOneInt + dealersCardTwoInt;
            // if(totalDealersValue >= 17){
            //    dealersMove.setText("Dealer Decides To Stand");
            // }
            //if (totalDealersValue < 17) {
              //  dealersMove.setText("Dealer Decides To Hit");
              //  if (userBusted) {
                  //  dealersMove.setText("Dealer Won");
                  //  displayDealersCards();
                  //  updateDealerValue();
               // } else {
                   // hitDealerDownload(deckId);
              //  }
           // } else if (totalDealersValue >= 17 && totalDealersValue < 21) {
               // dealersMove.setText("Dealer Decides To Stay");
               // if (userStayed) {
                 //   displayDealersCards();
                  //  stayWinnerDetermine();
               // }
               // if (userBusted) {
                    //Log.d(TAG, "dealers arrayList size: " + dealersCardValues.size());
                   // dealersMove.setText("Dealer Won");
                   // updateDealerValue();

                   // displayDealersCards();
               // }

          //  } else if (totalDealersValue == 21) {
              //  dealerHitBlackjack = true;
             //   if (userHitBlackjack) {
                 //   dealersMove.setText("You And The Dealer Both Hit Blackjack");
                  //  displayDealersCards();
                   // updateDealerValue();
                  //  updateYouValue();
               // } else {
                  //  dealersMove.setText("Dealer Hit BlackJack");
                  //  if(!userBusted) {
                    //    displayDealersCards();
                     //   updateDealerValue();
                   // }
                   // else {
                        // Don't Increment Dealer's Score
                   // }
                   // }
              //  }
           // } else if (totalDealersValue > 21) {
               // if (userBusted) {
                 //   dealersMove.setText("Dealer Also Busted\nIts A Tie");
               // }
               // else if(!userBusted) {
                 //   dealersMove.setText("Dealer Has Busted\nYou Won");
                  //  updateYouValue();
               // }
               // displayDealersCards();





        private void stayWinnerDetermine() {
            int userNum = 0;
            int dealerNum = 0;
                for (String userValues : userCardsValues) {
                    int user = Integer.parseInt(userValues);
                    userNum = userNum + user;
                }
                for (String dealerValues : dealersCardValues) {
                    int dealer = Integer.parseInt(dealerValues);
                    dealerNum = dealerNum + dealer;
                }
                if (userNum > dealerNum) {
                    RoundDecider.setText("You Won");
                    updateYouValue(userCardsValues.size(), dealersCardValues.size());
                    enabledNextRoundButton = true;
                } else if (dealerNum > userNum) {
                    RoundDecider.setText("Dealer Won");
                    updateDealerValue(userCardsValues.size(), dealersCardValues.size());
                    enabledNextRoundButton = true;
                } else {
                    RoundDecider.setText("Its A Tie");
                    enabledNextRoundButton = true;
                    if (isTied) {
                        tiedStateMoreThanOnce();
                    } else {
                        isTied = true;
                        tiedState();
                    }
                }
            }

            public void tiedStateMoreThanOnce() {
                if (isTied) {
                    int dealersCards = dealersCardValues.size();
                    int userCards = userCardsValues.size();
                    allCardsAtStake = allCardsAtStake + (dealersCards + userCards);
                    cardsAtStake.setText(allCardsAtStake + " Cards At Stake");
                    cardsAtStake.setVisibility(View.VISIBLE);
                    cardsAtStakeImage.setVisibility(View.VISIBLE);

                }

            }

            public void tiedState(){
                int dealersCards = dealersCardValues.size();
                int usersCards = userCardsValues.size();
                allCardsAtStake = dealersCards + usersCards;
                cardsAtStake.setText(allCardsAtStake + " Cards At Stake");
                cardsAtStake.setVisibility(View.VISIBLE);
                cardsAtStakeImage.setVisibility(View.VISIBLE);
            }

            
    public void hitPlayer(View v) {
        if (!remaining.equals("0")) {
            //dealerMove();
            String totalCardsValue = cardsValue.getText().toString();
            int totalCardsValueInt = Integer.parseInt(totalCardsValue);
            if (totalCardsValueInt < 21) {
                hitDownload(deckId);
            } else if (totalCardsValueInt == 21) {
                Toast.makeText(this, "You have 21", Toast.LENGTH_SHORT).show();
            } else if (totalCardsValueInt > 21) {
                Toast.makeText(this, "You Busted", Toast.LENGTH_SHORT).show();
                //displayDealersCards();

            }
            //dealerMove();
        }
        else if(remaining.equals("0")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No Cards Remaining In This Deck");
            builder.setMessage("This Hand's Winner Will Be Calculated Based On The Cards On The Table\nWould You Like To Continue With A New Deck After?");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    displayDealersCards();
                    winnerBeforeNewDeck();
                    enabledNextRoundButton = true;
                    //getAFullNewDeck();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }
    }

        public void stayPlayer(View v){
        totalValueTextView.setText("Stayed with: ");
        userStayed = true;
        dealerMove();
        }

        private void displayDealersCards() {
            hitButton.setEnabled(false);
            stayButton.setEnabled(false);
            linearLayout2.removeAllViews();
            for (String cardsImage : dealersCardImages) {
                ImageView imageView = new ImageView(this);
                picasso.load(cardsImage).into(imageView);
                linearLayout2.addView(imageView);
            }
        }


    private void hitDownload(String s){
        Uri.Builder urlBuilder = Uri.parse(deckOfCardUrlApi).buildUpon();
        urlBuilder.appendPath(s);
        urlBuilder.appendPath("draw");
        urlBuilder.appendQueryParameter("count", "1");

        String url = urlBuilder.build().toString();
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseDrawASingleCardJson(response);
                        displayNewCard();
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "No More API Calls For 24 Hours", Toast.LENGTH_SHORT).show();
                        // Handle the error if needed
                    }


                }
        ) {


        };
        requestQueue.add(getRequest);

    }
    private void parseDrawASingleCardJson(String s){
        try {
            JSONObject jsonObject = new JSONObject(s);
            remaining = jsonObject.optString("remaining");
            JSONArray cardsObject = jsonObject.getJSONArray("cards");
            for(int i = 0; i < cardsObject.length(); i++) {
                JSONObject jsonObject1 = cardsObject.getJSONObject(i);
                String cardImage = jsonObject1.getString("image");
                //allCardsImages = allCardsImages + "," + cardImage;
                cardsImagesUrls.add(cardImage);
                String value = jsonObject1.getString("value");
                if (value.equals("KING") || value.equals("QUEEN") || value.equals("JACK")) {
                    userCardsValues.add("10");
                } else if(value.equals("ACE")) {
                    userCardsValues.add("11");
                }
                else{
                    userCardsValues.add(value);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //dealerMove();

    }

    private void hitDealerDownload(String s){
        Uri.Builder urlBuilder = Uri.parse(deckOfCardUrlApi).buildUpon();
        urlBuilder.appendPath(s);
        urlBuilder.appendPath("draw");
        urlBuilder.appendQueryParameter("count", "1");

        String url = urlBuilder.build().toString();
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //parseDrawASingleCardJson(response);
                        Log.d("API_RESPONSE", "Response: " + response);
                        //displayDealersNewCard();
                        parseDrawASingleCardDealerJson(response);

                       // displayDealersCards();
                        //stayWinnerDetermine();
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "No More API Calls For 24 Hours", Toast.LENGTH_SHORT).show();
                        // Handle the error if needed
                    }


                }
        ) {


        };
        requestQueue.add(getRequest);

    }
    private void parseDrawASingleCardDealerJson(String s){
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray cardsObject = jsonObject.getJSONArray("cards");
            for(int i = 0; i < cardsObject.length(); i++) {
                JSONObject jsonObject1 = cardsObject.getJSONObject(i);
                String cardImage = jsonObject1.getString("image");
                //allCardsImages = allCardsImages + "," + cardImage;
                dealersCardImages.add(cardImage);
                String value = jsonObject1.getString("value");
                if (value.equals("KING") || value.equals("QUEEN") || value.equals("JACK")) {
                    dealersCardValues.add("10");
                } else if(value.equals("ACE")) {
                    dealersCardValues.add("11");
                }
                else{
                    dealersCardValues.add(value);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        int checkValue = 0;
        for(String value: dealersCardValues){
            int valueInt = Integer.parseInt(value);
            checkValue += valueInt;
        }
        if( checkValue == 21 ) {
            if (dealersCardValues.size() == 2) {
                dealersMove.setText("Dealer Hit Blackjack");
                RoundDecider.setText("You Lost");
                displayDealersCards();
                updateDealerValue(userCardsValues.size(), dealersCardValues.size());
                dealerHitBlackjack = true;
                enabledNextRoundButton = true;
            } else {
                dealersMove.setText("Dealer Has 21");
                RoundDecider.setText("You Lost");
                displayDealersCards();
                updateDealerValue(userCardsValues.size(), dealersCardValues.size());
                enabledNextRoundButton = true;
            }
        }
        else if (checkValue > 21) {
            if (dealersCardValues.contains("11")) {
                dealersCardValues.remove("11");
                dealersCardValues.add("1");
                dealerMove();
            } else {
                dealersMove.setText("Dealer Has Busted With " + checkValue);

                RoundDecider.setText("You Won");
                updateYouValue(userCardsValues.size(), dealersCardValues.size());
                displayDealersCards();
                enabledNextRoundButton = true;
            }
        }
        else if ( checkValue < 21 && checkValue > 16){
            dealersMove.setText("Dealer Decides to Stay With " + checkValue);
            dealerMove();
        }
        else if ( checkValue <= 16){
            dealersMove.setText("Dealer Decides To Hit Again");
            dealerMove();
            //hitDealerDownload(deckId);
        }
        //dealerMove();

    }

    private void winnerBeforeNewDeck(){
        hitButton.setEnabled(false);
        stayButton.setEnabled(false);
        int usersValuesInt = Integer.parseInt(cardsValue.getText().toString());
        int dealersValuesInt = 0;
        for(String dealersValues: dealersCardValues){
            dealersValuesInt = dealersValuesInt + Integer.parseInt(dealersValues);
        }
        if(usersValuesInt <= 21 && dealersValuesInt <= 21){
            if(usersValuesInt > dealersValuesInt){
                updateYouValue(userCardsValues.size(), dealersCardValues.size());
                RoundDecider.setText("You Won");
                dealersMove.setText("Dealer Has " + dealersValuesInt);
            }
            else if(dealersValuesInt > usersValuesInt){
                updateDealerValue(userCardsValues.size(), dealersCardValues.size());
                RoundDecider.setText("Dealer Won");
                dealersMove.setText("Dealer Has " + dealersValuesInt);
            }
        }
        hitNewDeck = true;

    }

    private void displayUserAceCard(int[] newCardArrayInt, String newCardImage){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.horizontalview_alert_dialog, null);
        LinearLayout alertDialogLinearLayout = view.findViewById(R.id.alertDialogLinearLayout);
        for(String cardsImages: cardsImagesUrls){
            ImageView imageView = new ImageView(this);
            picasso.load(cardsImages).into(imageView);
            alertDialogLinearLayout.addView(imageView);
        }
        builder.setView(view);
        builder.setTitle("You Have An Ace");
        builder.setMessage("Do You Want To Set Its Value To One or Eleven");
        builder.setCancelable(false);
        //builder.setIcon(R.drawable.logo);
        builder.setPositiveButton("One", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                newCardArrayInt[newCardArrayInt.length - 1] = 1;
                String newCardToAdd = String.valueOf(newCardArrayInt[newCardArrayInt.length -1]);
                userCardsValues.remove(userCardsValues.size() - 1);
                userCardsValues.add(newCardToAdd);
                getCardsTotalValue();
            }
        });
        builder.setNegativeButton("Eleven", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newCardArrayInt[newCardArrayInt.length - 1] = 11;
                String newCardToAdd = String.valueOf(newCardArrayInt[newCardArrayInt.length -1]);
                userCardsValues.remove(userCardsValues.size() - 1);
                userCardsValues.add(newCardToAdd);
                getCardsTotalValue();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        ImageView newCard = new ImageView(this);
        picasso.load(newCardImage).into(newCard);

        linearLayout.addView(newCard);

    }


    private void displayNewCard() {
        String newCardImage = cardsImagesUrls.get(cardsImagesUrls.size() - 1);
        String newCardValue = userCardsValues.get(userCardsValues.size() - 1);
        final int[] newCardArrayInt = {Integer.parseInt(newCardValue)};
        if (newCardValue.equals("11")) {
            displayUserAceCard(newCardArrayInt, newCardImage);
        } else {

            // Create a new ImageView for the new card
            ImageView newCard = new ImageView(this);
            picasso.load(newCardImage).into(newCard);

            linearLayout.addView(newCard);
            getCardsTotalValue();
            //newCard.setVisibility(View.VISIBLE);
        }
    }
    private void displayDealersNewCard(){

        ImageView newCard = new ImageView(this);
        picasso.load(faceDownCard).into(newCard);
        linearLayout2.addView(newCard);
        //dealerMove();
    }


}

