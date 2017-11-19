package com.tikalk.antsmasher.board;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import com.tikalk.antsmasher.R;
import com.tikalk.antsmasher.model.Ant;
import com.tikalk.antsmasher.model.AntSpecies;

import static com.tikalk.graphics.ImageUtils.tintImage;

/**
 * Board view with wood background and ants walking on top.
 */

public class BoardView extends View {

    interface AntListener {
        void onAntTouch(@Nullable Integer antId);
    }

    private SparseArray<Bitmap> bitmaps = new SparseArray<>();
    private SparseArray<AntRect> ants = new SparseArray<>();
    private AntListener antListener;

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

    public void clear() {
        ants.clear();
        bitmaps.clear();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final int size = ants.size();
        AntRect ant;
        Bitmap bitmap;
        float angle;
        float px, py;
        for (int i = 0; i < size; i++) {
            ant = ants.valueAt(i);
            bitmap = bitmaps.get(ant.speciesId);
            angle = ant.angle;
            px = ant.x();
            py = ant.y();
            canvas.rotate(angle, px, py);
            canvas.drawBitmap(bitmap, ant.left, ant.top, null);
            canvas.rotate(-angle, px, py);
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

    public void moveTo(Ant ant) {
        AntRect rect = ants.get(ant.getId());
        if (rect != null) {
            float dxPercent = ant.getLocation().x;
            float dyPercent = ant.getLocation().y;
            rect.moveTo(dxPercent * getWidth(), dyPercent * getHeight());
        }
    }

    public void setAntListener(AntListener antListener) {
        this.antListener = antListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                final AntListener listener = antListener;
                if (listener != null) {
                    final float x = event.getX();
                    final float y = event.getY();
                    final int size = ants.size();
                    AntRect ant;
                    for (int i = 0; i < size; i++) {
                        ant = ants.valueAt(i);
                        if (ant.isHit(x, y)) {
                            listener.onAntTouch(ant.id);
                            return true;
                        }
                    }
                    listener.onAntTouch(null);
                    return true;
                }
                break;
        }

        return super.onTouchEvent(event);
    }
}
