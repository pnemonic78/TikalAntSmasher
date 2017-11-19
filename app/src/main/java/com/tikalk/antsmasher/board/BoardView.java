package com.tikalk.antsmasher.board;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import com.tikalk.antsmasher.R;
import com.tikalk.antsmasher.model.Ant;
import com.tikalk.antsmasher.model.AntSpecies;
import com.tikalk.antsmasher.model.Game;
import com.tikalk.antsmasher.model.Team;

import static com.tikalk.graphics.ImageUtils.tintImage;

/**
 * Board view with wood background and ants walking on top.
 */

public class BoardView extends View {

    private SparseArray<Bitmap> bitmaps = new SparseArray<>();
    private SparseArray<AntRect> ants = new SparseArray<>();
    private Thread thread;

    public BoardView(Context context) {
        super(context);
    }

    public BoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void start(Game game) {
        final float width = getWidth();
        final float height = getHeight();

        ants.clear();

        Resources res = getResources();
        final int antWidth = 100;
        final int antHeight = 100;
        Bitmap antNormal = BitmapFactory.decodeResource(res, R.drawable.ant_normal);
        antNormal = Bitmap.createScaledBitmap(antNormal, antWidth, antHeight, false);
        Bitmap bitmap;
        AntSpecies species;
        int speciesId;
        AntRect rect;
        float x, y;
        float left;
        float top;
        float right;
        float bottom;

        for (Team team : game.getTeams()) {
            species = team.getAntSpecies();
            speciesId = species.getId();
            bitmap = tintImage(antNormal, species.getTint());
            bitmaps.put(speciesId, bitmap);

            for (Ant ant : species.getAllAnts()) {
                rect = new AntRect();
                rect.id = ant.getId();
                rect.speciesId = speciesId;
                x = ant.getLocation().x * width;
                y = ant.getLocation().y * height;
                left = x - (antWidth / 2);
                top = y - (antHeight / 2);
                right = left + antWidth;
                bottom = top + antHeight;
                rect.set(left, top, right, bottom);
                ants.put(rect.id, rect);
            }
        }

        thread = new Thread() {
            @Override
            public void run() {
                //TODO move ants.
            }
        };
        thread.start();
    }

    public void stop() {
        if (thread != null) {
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paintLine.setStyle(Paint.Style.STROKE);
        final float width = getWidth();
        final float width10 = width / 10f;
        final float height = getHeight();
        final float height10 = height / 10f;
        float x, y;
        paintLine.setColor(Color.BLUE);
        for (int i = 0; i <= 10; i++) {
            y = height10 * i;
            canvas.drawLine(0, y, width, y, paintLine);
        }
        paintLine.setColor(Color.MAGENTA);
        for (int i = 0; i <= 10; i++) {
            x = width10 * i;
            canvas.drawLine(x, 0, x, height, paintLine);
        }

        final int size = ants.size();
        AntRect ant;
        Bitmap bitmap;
        for (int i = 0; i < size; i++) {
            ant = ants.valueAt(i);
            bitmap = bitmaps.get(ant.speciesId);
            canvas.rotate(ant.angle);
            canvas.drawBitmap(bitmap, ant.left, ant.top, null);
            canvas.rotate(-ant.angle);
        }
    }

    private final Paint paintLine = new Paint();
}
