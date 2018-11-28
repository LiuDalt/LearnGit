package com.example.administrator.spannable;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.ClickableSpan;
import android.text.style.DrawableMarginSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.IconMarginSpan;
import android.text.style.ImageSpan;
import android.text.style.MaskFilterSpan;
import android.text.style.QuoteSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.MainActivity;
import com.example.administrator.myapplication.R;

public class SpannableActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spannable);

        testSpannable();
    }

    private void testSpannable() {
        TextView textView = findViewById(R.id.spannable_tv);
        SpannableStringBuilder spannableString = new SpannableStringBuilder();
        spannableString.append("ABCDEFGHIJABCDEFGHIJABCDEFGHIJABCDEFGHIJABCDEFGHIJABCDEFGHIJABCDEFGHIJABCDEFGHIJABCDEFGHIJABCDEFGHIJ");
        spannableString.append("ABCDEFGHIJABCDEFGHIJABCDEFGHIJABCDEFGHIJABCDEFGHIJABCDEFGHIJABCDEFGHIJABCDEFGHIJABCDEFGHIJABCDEFGHIJ");
        //设置前景色
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.GREEN);
        spannableString.setSpan(foregroundColorSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        //设置背景色
        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.GRAY);
        spannableString.setSpan(backgroundColorSpan, 5, 10, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        //设置字体大小
        AbsoluteSizeSpan absoluteSizeSpanBig = new AbsoluteSizeSpan(24, true);
        spannableString.setSpan(absoluteSizeSpanBig, 10, 15, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        AbsoluteSizeSpan absoluteSizeSpanSmall = new AbsoluteSizeSpan(10, true);
        spannableString.setSpan(absoluteSizeSpanSmall, 15, 20, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        //设置粗体 斜体 粗斜体
        StyleSpan styleSpanBold = new StyleSpan(Typeface.BOLD);//粗体
        spannableString.setSpan(styleSpanBold, 20, 25, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        StyleSpan styleSpanItlaic = new StyleSpan(Typeface.ITALIC);//斜体
        spannableString.setSpan(styleSpanItlaic, 25, 30, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        StyleSpan styleSpanBoldItlaic = new StyleSpan(Typeface.BOLD_ITALIC);//粗斜体
        spannableString.setSpan(styleSpanBoldItlaic, 30, 35, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        //设置删除线
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        spannableString.setSpan(strikethroughSpan, 35, 40, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        //设置下划线
        UnderlineSpan underlineSpan = new UnderlineSpan();
        spannableString.setSpan(underlineSpan, 40, 45, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        //插入图片, 30-35的字符用图片代替
        ImageSpan imageSpan = new ImageSpan(this, R.drawable.beauty);
        spannableString.setSpan(imageSpan, 45, 50, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        //设置点击事件
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Toast.makeText(SpannableActivity.this, "我被点击了", Toast.LENGTH_LONG).show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.BLUE);//设置默认颜色
                ds.setUnderlineText(false);//设置下划线显示与否
                ds.clearShadowLayer();//清除阴影
            }
        };
        spannableString.setSpan(clickableSpan, 50, 55, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        //设置链接文本
        URLSpan urlSpan = new URLSpan("http://baidu.com");
        spannableString.setSpan(urlSpan, 55, 60, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        //字体样式文本
        TypefaceSpan typefaceSpan = new TypefaceSpan("cursive");
        spannableString.setSpan(typefaceSpan, 60, 65, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        //字体外观文本
        TextAppearanceSpan textAppearanceSpan = new TextAppearanceSpan("casual", Typeface.BOLD_ITALIC, 40, null, null);
        spannableString.setSpan(textAppearanceSpan, 65, 70, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

//        功能：文字设置为上标，数学公式中用到,比如表示6555的13次方，13就可以用它来展示
        SuperscriptSpan superscriptSpan = new SuperscriptSpan();
        spannableString.setSpan(superscriptSpan, 71, 72, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        //文字设置为下标，化学式中用到。比如表示氧气的化学式中2可用它来表示
        SubscriptSpan subscriptSpan = new SubscriptSpan();
        spannableString.setSpan(subscriptSpan, 74, 75, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

//        文字横向缩放,参数为倍数
        ScaleXSpan scaleXSpan = new ScaleXSpan(3);
        spannableString.setSpan(scaleXSpan, 75, 80, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        //相对原始字体大小文本，比如原始字体大小为9,参数为2，则相对字体大小为18
        RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(2);
        spannableString.setSpan(relativeSizeSpan, 80, 85, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        //设置文字左侧显示引用样式（一条竖线）。
        QuoteSpan quoteSpan = new QuoteSpan(Color.GREEN);
        spannableString.setSpan(quoteSpan, 85, 90, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        //设置文字模糊效果和浮雕效果
        //模糊效果
        MaskFilterSpan maskFilterSpanBlurMaskFilter = new MaskFilterSpan(new BlurMaskFilter(3, BlurMaskFilter.Blur.OUTER));
        spannableString.setSpan(maskFilterSpanBlurMaskFilter, 90, 95, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //浮雕效果
        MaskFilterSpan maskFilterSpanEmos = new MaskFilterSpan(new EmbossMaskFilter(new float[]{1,1,1}, 0.1f, 5, 5));
        spannableString.setSpan(maskFilterSpanEmos, 95, 100, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

//        功能：文本插入图片+Margin
        IconMarginSpan iconMarginSpan = new IconMarginSpan(BitmapFactory.decodeResource(getResources(), R.drawable.beauty), 60);
        spannableString.setSpan(iconMarginSpan, 100, 105, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        //文本插入图片+Margin。
        DrawableMarginSpan drawableMarginSpan = new DrawableMarginSpan(getResources().getDrawable(R.drawable.beauty), 60);
        spannableString.setSpan(drawableMarginSpan, 105, 110, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

//        类似于HTML中的<li>标签的圆点效果。
        BulletSpan bulletSpan = new BulletSpan(30, Color.GREEN);
        spannableString.setSpan(bulletSpan, 110, 115, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);


        //设置文字的对其方式：ALIGN_CENTER ALIGN_LEFT ALIGN_RIGHT ALIGN_NORMAL ALIGN_POSITIVE
        AlignmentSpan.Standard standard = new AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE);
        spannableString.setSpan(standard, 115, 120, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

//        LeadingMarginSpan.Standard
//        设置文本缩进。
//        LeadingMarginSpan.Standard(int first, int rest)
//        first：首行的 margin left 偏移量。
//        rest：其他行的 margin left 偏移量。


        // 在单击或者跳转链接时凡是有要执行的动作，都必须设置MovementMethod对象
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        // 设置点击后的颜色，这里涉及到ClickableSpan的点击背景
        textView.setHighlightColor(getResources().getColor(android.R.color.transparent));

        textView.setText(spannableString);
    }
}
