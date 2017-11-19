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

    int antWidth;
    int antHeight;
    int antDeadWidth;
    int antDeadHeight;
    private SparseArray<Bitmap> bitmapsAlive = new SparseArray<>();
    private SparseArray<Bitmap> bitmapsDead = new SparseArray<>();
    private SparseArray<AntRect> ants = new SparseArray<>();
    private AntListener antListener;

    public BoardView(Context context) {
        super(context);
        init(context);
    }

    public BoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        final Resources res = context.getResources();
        antWidth = res.getDimensionPixelSize(R.dimen.ant_width);
        antHeight = res.getDimensionPixelSize(R.dimen.ant_height);
        antDeadWidth = res.getDimensionPixelSize(R.dimen.ant_dead_width);
        antDeadHeight = res.getDimensionPixelSize(R.dimen.ant_ded_height);
    }

    public void clear() {
        ants.clear();
        bitmapsAlive.clear();
        bitmapsDead.clear();
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
            bitmap = ant.alive ? bitmapsAlive.get(ant.speciesId) : bitmapsDead.get(ant.speciesId);
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
        rect.alive = ant.isAlive();
        ants.put(rect.id, rect);

        Bitmap bitmap = bitmapsAlive.get(speciesId);
        if (bitmap == null) {
            final Resources res = getResources();

            Bitmap antNormal = BitmapFactory.decodeResource(res, R.drawable.ant_normal);
            antNormal = Bitmap.createScaledBitmap(antNormal, antWidth, antHeight, false);
            bitmap = tintImage(antNormal, species.getTint());
            bitmapsAlive.put(speciesId, bitmap);

            Bitmap antSmashed = BitmapFactory.decodeResource(res, R.drawable.ant_squashed);
            antSmashed = Bitmap.createScaledBitmap(antSmashed, antDeadWidth, antDeadHeight, false);
            bitmap = tintImage(antSmashed, species.getTint());
            bitmapsDead.put(speciesId, bitmap);
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

    public void smashAnt(Ant ant) {
        AntRect rect = ants.get(ant.getId());
        if (rect != null) {
            rect.alive = false;
            rect.offset((antWidth - antDeadWidth) / 2, (antHeight - antDeadHeight) / 2);
            postInvalidate();
        }
    }
}
