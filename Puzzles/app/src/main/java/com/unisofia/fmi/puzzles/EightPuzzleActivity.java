package com.unisofia.fmi.puzzles;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EightPuzzleActivity extends Activity {

	private final String[] ORDERED_PUZZLE = { "1", "2", "3", "4", "5", "6", "7", "8", " " };
	private List<Button> buttons;
	private List<String> orderedNumbers;
	private List<String> piecesLocation;
	private int emptyPosition;

    private UpdateTimerRunnable updateTimerThread;
    private Handler customHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		buttons = new ArrayList<Button>();
		orderedNumbers = getOrderedNumbers();
		piecesLocation = getRandomNumbers();
		emptyPosition = piecesLocation.indexOf(" ");
		
//		notRandomInitialization();

        RelativeLayout rl = new RelativeLayout(this);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

		final GridLayout gl = new GridLayout(this);
        gl.setId(R.id.gridlayout_eightpuzzle);
		gl.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		gl.setColumnCount(3);
		gl.setRowCount(3);
        rl.addView(gl);

        final TextView timeTextView = new TextView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        timeTextView.setPadding(2, 10, 2, 0);
        params.addRule(RelativeLayout.BELOW, gl.getId());

        customHandler = new Handler();
        updateTimerThread = new UpdateTimerRunnable(SystemClock.uptimeMillis(),customHandler, timeTextView);

		for (String i : piecesLocation) {
			final Button button = new Button(this);
			button.setTag(i);
			button.setPadding(5, 5, 5, 5);

			button.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			button.setText(String.valueOf(i));
			button.setTextSize(25);

			buttons.add(button);

			button.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View view, MotionEvent motionEvent) {
					if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
						Button emptyButton = (Button) gl
								.getChildAt(emptyPosition);
						Button btn = (Button) view;
						CharSequence btnText = btn.getText();

						if (!btnText.equals(" ")) {
							int btnPosition = piecesLocation.indexOf(btnText);

							int x = emptyPosition % 3;
							int y = emptyPosition / 3;
							boolean rowCondition = (x == 0 && btnPosition == emptyPosition + 1)
									|| (x == 2 && btnPosition == emptyPosition - 1)
									|| (x == 1 && (btnPosition == emptyPosition + 1 || btnPosition == emptyPosition - 1));

							boolean columnCondition = (y == 0 && btnPosition == emptyPosition + 3)
									|| (y == 2 && btnPosition == emptyPosition - 3)
									|| (y == 1 && (btnPosition == emptyPosition + 3 || btnPosition == emptyPosition - 3));
							if (rowCondition || columnCondition) {
								btn.setText(" ");
								emptyButton.setText(btnText);
								piecesLocation.set(btnPosition, " ");
								piecesLocation.set(emptyPosition, btnText.toString());

								emptyPosition = btnPosition;
								if (victory()) {
                                    updateTimerThread.active = false;
									AlertDialog.Builder alertDialogBuilder = getAlertDiologBuilder();
									AlertDialog alertDialog = alertDialogBuilder.create();
									alertDialog.show();
								}
							}
						}

						return true;
					} else {
						return false;
					}
				}
			});

			gl.addView(button);
		}
        rl.addView(timeTextView, params);
        setContentView(rl, rlp);
        customHandler.postDelayed(updateTimerThread, 1000);
	}

	private boolean victory() {
		return piecesLocation.equals(orderedNumbers);
	}

	private List<String> getOrderedNumbers() {
		return Arrays.asList(ORDERED_PUZZLE);
	}

	private List<String> getRandomNumbers() {
		int[] array = new int[ORDERED_PUZZLE.length];
		for (int i = 0; i < array.length; i++) {
			array[i] = i;
		}
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < array.length; i++) {
			result.add(orderedNumbers.get(i));
		}
		Collections.shuffle(result);
		return result;

	}
	
	private AlertDialog.Builder getAlertDiologBuilder() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);
			// set title
			alertDialogBuilder.setTitle("Congratulations. You win!");
			// set dialog message
			alertDialogBuilder
				.setMessage("Do you want to play again?")
				.setCancelable(false)
				.setNegativeButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						changeButtonsText();
                        updateTimerThread.active = true;
                        updateTimerThread.startTime = SystemClock.uptimeMillis();
                        customHandler.postDelayed(updateTimerThread, 1000);
					}
				  })
				.setPositiveButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						EightPuzzleActivity.this.finish();
						
					}
				});
			return alertDialogBuilder;
	}
	
	private void changeButtonsText() {
		piecesLocation = getRandomNumbers();
		emptyPosition = piecesLocation.indexOf(" ");
		for (int i = 0; i < piecesLocation.size(); i++) {
			buttons.get(i).setText(piecesLocation.get(i));
			
		}
	}

//	used only for testing
	private void notRandomInitialization() {
		String[] temp = { "1", "2", "3", "4", "5", "6", "7", " ", "8" };
		piecesLocation = Arrays.asList(temp);
		emptyPosition = 7;
	}
	

}
