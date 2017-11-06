package mas_hcorporation.com.apptaq;

import android.widget.BaseAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.*;
import android.widget.ImageView;

import java.util.Arrays;
/**
 * Created by mash on 02/11/17.
 */

public class TaquinAdapter extends BaseAdapter {
    private Context mContext;

    private Bitmap[] chunks;

    private Bitmap[] shuffled;

    public TaquinAdapter(Context mContext, Bitmap picture, int gridWidth) {

        this.mContext=mContext;
        chunks = new Bitmap[gridWidth*gridWidth];
        for (int i=1; i<chunks.length ; i++) {
            //chunks[i]=Bitmap.createBitmap(..)
        }
        chunks[0] = Bitmap.createBitmap( picture.getWidth()/gridWidth,
                picture.getHeight()/gridWidth,
                Bitmap.Config.ALPHA_8);
    }

    public void shuffle() {
        shuffled = Arrays.copyOf(chunks, chunks.length);
        //on doit faire une boucle pour permutter les morceaux un nombre aléatoire de fois
    }

    @Override
    public int getCount() {
        return chunks.length;
    }

    @Override
    public Object getItem(int i) {
        return chunks[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView;
        if (view == null) {
            //if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            imageView = (ImageView) view;
        }

        imageView.setImageBitmap(shuffled[i]);
        return imageView;
    }



}
