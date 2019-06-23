package swap.irfanullah.com.swap.CustomComponents;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;


@SuppressLint("AppCompatCustomView")
public class SwapsSquareImageViewForComposing extends ImageView {
    public SwapsSquareImageViewForComposing(Context context) {
        super(context);
    }

    public SwapsSquareImageViewForComposing(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwapsSquareImageViewForComposing(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
       super.onMeasure(getMeasuredWidth(), getMeasuredWidth()/2);

//        int width = MeasureSpec.getSize(widthMeasureSpec);
//        int height = MeasureSpec.getSize(heightMeasureSpec);
//        int size;
//        if(MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY ^ MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
//            if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY)
//                size = width;
//            else
//                size = height-50;
//        }
//        else
//            size = Math.min(width, height);
//        setMeasuredDimension(size, size);
    }
}
