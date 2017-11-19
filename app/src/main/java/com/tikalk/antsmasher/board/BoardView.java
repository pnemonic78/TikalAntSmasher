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

import static com.tikalk.graphics.ImageUtils.tintImage;

/**
 * Board view with wood background and ants walking on top.
 */

public class BoardView extends View {

    private SparseArray<Bitmap> bitmaps = new SparseArray<>();
    private SparseArray<AntRect> ants = new SparseArray<>();
    private Thread thread;
    private final Paint paintLine = new Paint();

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

    public void start() {
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
        ants.clear();
        bitmaps.clear();
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

    public void addAnt(Ant ant) {
        final float width = getWidth();
        final float height = getHeight();

        final Resources res = getResources();
        final int antWidth = res.getDimensionPixelSize(R.dimen.ant_width);
        final int antHeight = res.getDimensionPixelSize(R.dimen.ant_height);

        final AntSpecies species = ant.getSpecies();
        final int speciesId = species.getId();

        float x = ant.getLocation().x * width;
        float y = ant.getLocation().y * height;
        float left = x - (antWidth / 2);
        float top = y - (antHeight / 2);
        float right = left + antWidth;
        float bottom = top + antHeight;
        final AntRect rect = new AntRect();
        rect.id = ant.getId();
        rect.speciesId = speciesId;
        rect.set(left, top, right, bottom);
        ants.put(rect.id, rect);

        Bitmap bitmap = bitmaps.get(speciesId);
        if (bitmap == null) {
            Bitmap antNormal = BitmapFactory.decodeResource(res, R.drawable.ant_normal);
            antNormal = Bitmap.createScaledBitmap(antNormal, antWidth, antHeight, false);
            bitmap = tintImage(antNormal, species.getTint());
            bitmaps.put(speciesId, bitmap);
        }
    }
}
