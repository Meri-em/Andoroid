package com.unisofia.fmi.puzzles;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.SystemClock;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class DuckPuzzleActivity extends Activity {
	
	private final int PIECES_COUNT = 16;
    private List<ImageView> imageViews;
    private List<Drawable> drawablesOrdered;
    private int[] piecesLocation;

    private UpdateTimerRunnable updateTimerThread;
    private Handler customHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        drawablesOrdered = obtainDrawables();
        imageViews = new ArrayList<ImageView>();

        RelativeLayout rl = new RelativeLayout(this);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        GridLayout gl = new GridLayout(this);
        gl.setId(R.id.gridlayout_duckpuzzle);
        gl.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        gl.setColumnCount(4);
        gl.setRowCount(4);
        rl.addView(gl);

        final TextView timeTextView = new TextView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        timeTextView.setPadding(2, 10, 2, 0);
        params.addRule(RelativeLayout.BELOW, gl.getId());


        piecesLocation = getRandomPositions();

        customHandler = new Handler();
        updateTimerThread = new UpdateTimerRunnable(SystemClock.uptimeMillis(),customHandler, timeTextView);

        for(int i: piecesLocation) {
            final ImageView imageView = new ImageView(this);

            imageView.setAdjustViewBounds(true);
            imageView.setPadding(2, 2, 2, 2);
            imageView.setLayoutParams(new LayoutParams(200, 224));
            imageView.setImageDrawable(drawablesOrdered.get(i));
            imageViews.add(imageView);

            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                        view.startDrag(null, shadowBuilder, view, 0);
                        return true;
                    } else {
                        return false;
                    }
                }
            });

            imageView.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    if (event.getAction() == DragEvent.ACTION_DROP) {
                        final ImageView draggedView = (ImageView) event.getLocalState();

                        Drawable draggedViewDrawable = draggedView.getDrawable();
                        draggedView.setImageDrawable(imageView.getDrawable());
                        imageView.setImageDrawable(draggedViewDrawable);

                        if (victory()) {
                            updateTimerThread.active = false;
                            AlertDialog.Builder alertDialogBuilder = getAlertDialogBuilder();
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                    }
                    return true;
                }
            });
            gl.addView(imageView);
        }
        rl.addView(timeTextView, params);
        setContentView(rl, rlp);
        customHandler.postDelayed(updateTimerThread, 1000);
    }


        private boolean victory() {
            return drawablesOrdered.equals(getDrawablesFromImageViews());
        }

    public List<Drawable> getDrawablesFromImageViews(){
        List<Drawable> drawables = new ArrayList<Drawable>();
        for (ImageView imageView: imageViews){
            drawables.add(imageView.getDrawable());
        }
        return drawables;

    }

    public List<Drawable> obtainDrawables(){
        TypedArray ta = getResources().obtainTypedArray(R.array.pictures);
        List<Drawable> drawables = new ArrayList<Drawable>();
        for (int i = 0; i < ta.length(); i++) {
            drawables.add(ta.getDrawable(i));
        }
        ta.recycle();
        return drawables;
    }
    
    public int[] getRandomPositions() {
    	Integer[] ordered = new Integer[PIECES_COUNT];
    	for (int i = 0; i < ordered.length; i++) {
    		ordered[i] = i;
    	}
    	List<Integer> temp = Arrays.asList(ordered);
    	Collections.shuffle(temp);
    	int[] result = new int[PIECES_COUNT];
    	for (int i = 0; i < result.length; i++) {
    		result[i] = temp.get(i);
    	}
    	return result;
    }
    
    private AlertDialog.Builder getAlertDialogBuilder() {
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
						piecesLocation = getRandomPositions();
						for (int i =0; i < PIECES_COUNT; i++) {
							imageViews.get(piecesLocation[i]).setImageDrawable(drawablesOrdered.get(i));
						}

                        updateTimerThread.active = true;
                        updateTimerThread.startTime = SystemClock.uptimeMillis();
                        customHandler.postDelayed(updateTimerThread, 1000);

					}
				  })
				.setPositiveButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						DuckPuzzleActivity.this.finish();
					}
				});
			return alertDialogBuilder;
	}
}
